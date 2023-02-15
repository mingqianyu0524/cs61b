package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static gitlet.Constants.*;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *
 *  @author Mingqian Yu
 */
public class Repository implements Serializable, Dumpable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /* Fields */

    Staging staging; // Staging area

    String currentBranch = "master"; // Name of the current branch

    Commit head; // Pointer to the HEAD commit of the current branch


    /* Constructor */

    public Repository() {
        // If the repository doesn't exist under .gitlet/ create an empty repository
        // Otherwise load the repository
        File file = Utils.join(GITLET_DIR, "Repository");
        if (file.exists()) {
            Repository repo = load();
            this.staging = repo.staging;
            this.currentBranch = repo.currentBranch;
            this.head = repo.head;
        }
    }

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit:
     * a commit that contains no files and has the commit message initial commit (just like that, with no punctuation).
     *
     * It will have a single branch: master, which initially points to this initial commit,
     * and master will be the current branch. The timestamp for this initial commit will be 00:00:00 UTC,
     * Thursday, 1 January 1970 in whatever format you choose for dates (this is called “The (Unix) Epoch”,
     * represented internally by the time 0.)
     *
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content,
     * it follows that all repositories will automatically share this commit (they will all have the same UID) and all
     * commits in all repositories will trace back to it.
     */
    public void init() {
        // Check if the version-control system already exists
        Path path = Paths.get(".gitlet");
        if (Files.exists(path)) {
            throw Utils.error(GITLET_EXISTS_ERR);
        }

        // Initialize file directories for GitLet repository and set staging area
        Repository.createDirectories();
        this.staging = new Staging();

        // Create the initial commit and store it in the commits folder
        Commit commit = new Commit();
        commit.save();

        // Create the master branch and assign the HEAD pointer
        this.head = commit;
        File branch = Utils.join(BRANCHES_DIR, currentBranch);
        writeContents(branch, Utils.sha1((Object) serialize(commit)));
        this.currentBranch = "master";

        // Save this repository object under .gitlet/
        this.save();
    }

    /**
     *
     * Lazy loading and caching: Let’s say you store the state of which files have been gitlet added to your repo
     * in your file system.
     *
     * Lazy loading: The first time you want that list of files when you run your Java program, you need to load
     * it from disk.
     *
     * Caching: The second time you need that list of files in the same run of the Java program,
     * don’t load it from disk again, but use the same list as you loaded before. If you need to,
     * you can then add multiple files to that list object in your Java program.
     *
     * Writing back: When your Java program is finished, at the very end, since you had loaded that list of files and
     * may have modified it, write it back to your file system.
     *
     * Adds a copy of the file as it currently exists to the staging area.
     *
     * For this reason, adding a file is also called staging the file for addition.
     *
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet.
     *
     * If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     *
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     *
     * @param filename Name of the file to be added to the staging area
     */
    public void add(String filename) {

        File file = join(CWD, filename);
        // The initial version of gitlet only allows adding plain file, maybe in later version it will support dir too
        if (!file.isFile()) {
            throw error(FILE_NOT_EXIST_ERR);
        }
        String contents = Utils.readContentsAsString(file);

        // store the blob (do NOT use the file name but sha1 hash of the content to avoid duplicates)
        File blob = join(BLOBS_DIR, Utils.sha1(contents));
        Utils.writeContents(blob, contents);

        // Compare the file to the one in the current head commit
        Commit head = this.head;
        String headBlobName = head.getBlobName(filename);
        if (headBlobName != null) {
            String headBlobContent = Utils.readContentsAsString(join(BLOBS_DIR, headBlobName));
            // Compare blob with headBlob, if they are equal, then do not add the file to the staging area
            // and remove it from untracked files if it's there.
            if (contents.equals(headBlobContent)) {
                staging.getStagedForRemoval().remove(filename);
                return;
            }
        }

        // Otherwise, add the file to the staging area and save
        staging.getStagedForAddition().put(filename, Utils.sha1(contents));
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area, so they can be restored at a later time,
     * creating a new commit. The commit is said to be tracking the saved files.
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     *
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time
     * of commit, in which case the commit will now include the version of the file that was staged instead of the
     * version it got from its parent. A commit will save and start tracking any files that were staged for addition
     * but weren’t tracked by its parent.
     *
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged
     * for removal by the rm command (below).
     *
     * The bottom line: By default a commit has the same file contents as its parent.
     * Files staged for addition and removal are the updates to the commit.
     * Of course, the date (and likely the message) will also different from the parent.
     *
     * Some additional points about commit:
     *
     * The staging area is cleared after a commit.
     *
     * The commit command never adds, changes, or removes files in the working directory
     * (other than those in the .gitlet directory). The rm command will remove such files,
     * as well as staging them for removal, so that they will be untracked after a commit.
     *
     * Any changes made to files after staging for addition or removal are ignored by the commit command,
     * which only modifies the contents of the .gitlet directory. For example, if you remove a tracked file
     * using the Unix rm command (rather than Gitlet’s command of the same name), it has no effect on the next commit,
     * which will still contain the (now deleted) version of the file.
     *
     * After the commit command, the new commit is added as a new node in the commit tree.
     *
     * The commit just made becomes the “current commit”, and the head pointer now points to it.
     * The previous head commit is this commit’s parent commit.
     *
     * Each commit should contain the date and time it was made.
     *
     * Each commit has a log message associated with it that describes the changes to the files in the commit.
     * This is specified by the user. The entire message should take up only one entry in the array args that is
     * passed to main. To include multiword messages, you’ll have to surround them in quotes.
     *
     * Each commit is identified by its SHA-1 id, which must include the file (blob) references of its files,
     * parent reference, log message, and commit time.
     *
     * @param message
     */
    public void commit(String message) {
        // The commit message must not be empty
        if (message == null || message.isEmpty()) {
            throw error(COMMIT_MSG_MISSING_ERR);
        }

        // Read from the file system the HEAD commit object and the staging area
        Commit head = this.head;

        // Clone the HEAD commit, modify its message and timestamp according to user input
        // Most importantly, set the parent of this new commit to the original HEAD commit
        Commit commit = new Commit(message, head.getTree(), sha1((Object) serialize(head)), currentBranch);

        // The staging area must not be empty
        if (staging.getStagedForAddition().isEmpty() && staging.getStagedForRemoval().isEmpty()) {
            throw error(STAGING_AREA_EMPTY_ERR);
        }

        // Add the files staged for addition from parent
        for (String filename : staging.getStagedForAddition().keySet()) {
            Map<String, String> tree = commit.getTree();
            if (tree.containsKey(filename)) {
                String blobName = staging.getStagedForAddition().get(filename);
                tree.put(filename, blobName);
            } else {
                File file = Utils.join(CWD, filename);
                tree.put(filename, Utils.sha1(Utils.readContentsAsString(file)));
            }
        }

        // Remove the files staged for removal from parent
        for (String filename : staging.getStagedForRemoval()) {
            Map<String, String> tree = commit.getTree();
            tree.remove(filename);
        }

        // Update the HEAD + branch pointer to the new commit
        this.head = commit;
        File branch = Utils.join(BRANCHES_DIR, currentBranch);
        readContentsAsString(branch);
        writeContents(branch, Utils.sha1((Object) serialize(commit)));

        // Store any new or modified commit object into the file system and clean up the staging area
        commit.save();
        staging.getStagedForAddition().clear();
        staging.getStagedForRemoval().clear();
        this.save();
    }

    /**
     * Remove the file from the repository
     * @param filename
     */
    public void rm(String filename) {
        Map<String, String> stagedForAddition = staging.getStagedForAddition();
        Set<String> stagedForRemoval = staging.getStagedForRemoval();
        Map<String, String> tree = head.getTree();

        // If the file is neither staged nor tracked by the head commit, print the error message
        if (!stagedForAddition.containsKey(filename) && !tree.containsKey(filename)) {
            throw error(RM_ERR);
        }

        // Unstage the file if it is currently staged for addition.
        stagedForAddition.remove(filename);

        // If the file is tracked in the current commit, stage it for removal.
        if (tree.containsKey(filename)) {
            stagedForRemoval.add(filename);
            // Remove the file from the working directory if the user has not already done so
            // (do not remove it unless it is tracked in the current commit).
            File file = Utils.join(CWD, filename);
            file.delete();
        }
    }

    /**
     * Descriptions:
     *
     * `java gitlet.Main checkout -- [file name]`
     *
     * Takes the version of the file as it exists in the head commit and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     *
     * `java gitlet.Main checkout [commit id] -- [file name]`
     *
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     *
     * `java gitlet.Main checkout [branch name]`
     *
     * Takes all files in the commit at the head of the given branch, and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     * Also, at the end of this command, the given branch will now be considered the current branch (HEAD).
     *
     * Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).
     *
     */

    public void checkout( boolean isBranch, String commitID, String filename, String branch) {

        // Checkout branch
        if (isBranch) {
            if (branch.equals(currentBranch)) {
                throw error(CHECKOUT_CURRENT_BRANCH);
            }

            // Retrieve all files from the checkout branch and store them under CWD
            try {
                commitID = Utils.readContentsAsString(Utils.join(BRANCHES_DIR, branch));
            } catch (IllegalArgumentException e) {
                throw error(BRANCH_NOT_EXIST);
            }

            // Checkout commit
            Commit commit = Commit.read(commitID);
            checkoutCommit(commit);

            // Reset the current branch, HEAD pointer and staging area
            currentBranch = branch;
            head = commit;
            staging = new Staging();

            return;
        }

        // Checkout a single file
        String blob = null;

        if (commitID == null) {
            blob = head.getBlobName(filename);
        } else {
            Commit commit = Commit.read(commitID);
            blob = commit.getBlobName(filename);
        }
        if (blob == null) {
            throw Utils.error(FILE_NOT_IN_COMMIT); // do not move the exception handling to getBlobName()
        }

        File bf = Utils.join(BLOBS_DIR, blob);
        String c = Utils.readContentsAsString(bf);
        File f = Utils.join(CWD, filename);
        writeContents(f, c);

    }

    // TODO: Per project description, the log() function will need to support "Merge", we are not there yet
    public void log() {
        Commit ptr = head;
        while (ptr != null) {
            String filename = Utils.sha1((Object) serialize(ptr));

            System.out.println("===");
            System.out.printf("commit %s%n", filename);
            System.out.printf("Date: %s%n", ptr.getTimeStamp());
            System.out.println(ptr.getMessage());
            System.out.println();

            if (ptr.getParent() == null) {
                break;
            }
            ptr = Commit.read(ptr.getParent());
        }
    }

    /**
     * Description: Like log, except displays information about all commits ever made.
     * The order of the commits does not matter.
     */
    public void globalLog() {
        for (String commitID : Objects.requireNonNull(plainFilenamesIn(COMMITS_DIR))) {
            Commit commit = Commit.read(commitID);
            System.out.println("===");
            System.out.printf("commit %s%n", commitID);
            System.out.printf("Date: %s%n", commit.getTimeStamp());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    /**
     * Description: Prints out the ids of all commits that have the given commit message, one per line.
     * If there are multiple such commits, it prints the ids out on separate lines.
     * The commit message is a single operand; to indicate a multiword message,
     * put the operand in quotation marks, as for the commit command below.
     *
     * @param given given message
     */
    public void find(String given) {
        boolean res = false;
        for (String commitID : Objects.requireNonNull(plainFilenamesIn(COMMITS_DIR))) {
            Commit commit = Commit.read(commitID);
            String msg = commit.getMessage();
            if (given.equals(msg)) {
                res = true;
                System.out.println(commitID);
            }
        }
        if (!res) {
            throw Utils.error("Found no commit with that message.");
        }
    }

    /**
     * Description: Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal.
     * An example of the exact format it should follow is as follows.
     */
    public void status() {
        System.out.println("=== Branches ===");
        List<String> branches = Utils.plainFilenamesIn(BRANCHES_DIR);
        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");

        List<String> stagedFiles = new ArrayList<>(staging.getStagedForAddition().keySet());
        Collections.sort(stagedFiles);

        for (String stagedFile : stagedFiles) {
            System.out.println(stagedFile);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String removedFile : staging.getStagedForRemoval()) {
            System.out.println(removedFile);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        // TODO
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String untrackedFile : this.getUntracked()) {
            System.out.println(untrackedFile);
        }
        System.out.println();
    }

    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch, your code should be running with a default branch called “master”.
     *
     * @param branchName branch name from user input
     */
    public void branch(String branchName) {
        for (String n : Objects.requireNonNull(plainFilenamesIn(BRANCHES_DIR))) {
            if (n.equals(branchName)) {
                throw error("A branch with that name already exists.");
            }
        }
        // The new branch points to the current branch's HEAD commit
        File f = new File(BRANCHES_DIR, branchName);
        File branch = Utils.join(BRANCHES_DIR, currentBranch);
        String commitID = readContentsAsString(branch);
        writeContents(f, commitID);
    }

    /**
     * Deletes the branch with the given name. This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were created under the branch, or anything like that.
     *
     * @param given branch name from user input
     */
    public void rmBranch(String given) {
        if (currentBranch.equals(given)) {
            throw error("Cannot remove the current branch.");
        }
        String branchName = "";
        for (String n : Objects.requireNonNull(plainFilenamesIn(BRANCHES_DIR))) {
            if (n.equals(given)) {
                branchName = given;
                break;
            }
        }
        if (branchName.isEmpty()) throw error("A branch with that name does not exist.");
        // Delete branch
        Utils.join(BRANCHES_DIR, branchName).delete();
    }

    /**
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     *
     * The [commit id] may be abbreviated as for checkout. The staging area is cleared.
     * The command is essentially checkout of an arbitrary commit that also changes the current branch head.
     *
     * @param given
     */

    // TODO: Debug 24, do we keep commit ${1} after resetting to ${2}?

    public void reset(String given) {
        List<String> untracked = this.getUntracked();
        if (!untracked.isEmpty()) {
            throw error(UNTRACKED_ERR);
        }
        Commit commit = Commit.read(given);
        String branch = commit.getBranch();
        checkoutCommit(commit);
        this.staging = new Staging();
        this.head = commit;
        // Set the branch HEAD as well
        Utils.writeContents(Utils.join(BRANCHES_DIR, branch), given);
    }

    public void save() {
        Utils.writeObject(Utils.join(GITLET_DIR, "Repository"), this);
    }

    /**
     * Load the Repository object from .gitlet/ folder
     * TODO: move this method to main?
     * @return
     */
    public static Repository load() {
        return Utils.readObject(Utils.join(GITLET_DIR, "Repository"), Repository.class);
    }

    /**
     * Create directories for initial repository
     */
    private static void createDirectories() {
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        BLOBS_DIR.mkdir();
    }

    /**
     * Clean the current working directory (except .gitlet/)
     */
    public void cleanDirectory() {
        File dir = Utils.join(CWD);
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            if (!file.getName().equals(".gitlet")) {
                file.delete();
            }
        }
    }

    // untracked = CWD - staged - (CWD & tracked)
    private List<String> getUntracked() {
        List<String> cwd = plainFilenamesIn(CWD);
        List<String> staged = staging.getStagedFiles();
        List<String> tracked = head.getTrackedFiles();
        ArrayList<String> untracked = new ArrayList<>(plainFilenamesIn(CWD));

        for (String s : staged) {
            untracked.remove(s);
        }
        for (String s : tracked) {
            untracked.remove(s);
        }

        return untracked;
    }

    /**
     * Helper method to check out files in a given commit. (Used both in checkout and reset)
     * @param commit
     */
    private void checkoutCommit(Commit commit) {
        // Make sure there aren't untracked files first
        List<String> untracked = this.getUntracked();
        if (!untracked.isEmpty()) {
            throw error(UNTRACKED_ERR);
        }

        // Delete all files under the current working directory
        cleanDirectory();

        // Checkout tracked files in the given commit
        for (String fn : commit.getTree().keySet()) {
            // Read the blobs in the commit tree, and write to the files in CWD
            String blob = commit.getBlobName(fn);
            String c = Utils.readContentsAsString(Utils.join(BLOBS_DIR, blob));
            writeContents(Utils.join(CWD, fn), c);
        }
    }

    @Override
    public void dump() {
        System.out.println("=============Dumping Repository=============");
        System.out.printf("current branch name: %s%n", currentBranch);
        System.out.printf("commit id: %s%n", Utils.sha1((Object) Utils.serialize(head)));
        System.out.printf("commit msg: %s%n", head.getMessage());
        System.out.printf("commit timestamp: %s%n", head.getTimeStamp());
        System.out.printf("commit parent: %s%n", head.getParent());
        System.out.println("staged for addition");
        for (Map.Entry<String, String> entry : staging.getStagedForAddition().entrySet()) {
            System.out.printf("key: %s, value: %s%n", entry.getKey(), entry.getValue());
        }
    }
}
