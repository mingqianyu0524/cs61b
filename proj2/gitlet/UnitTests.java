package gitlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static gitlet.Constants.*;
import static gitlet.Utils.*;
import static org.junit.Assert.*;


public class UnitTests {

    // Initial commit SHA1 hash, remember to update if the Commit class gets modified
    public static final String INITIAL_COMMIT = "b3a82f44130594618a373770e45b91e9bfbc3472";
    // Unix epoch time in designated datetime format
    public static final String UNIX_EPOCH = "00:00:00 UTC, Thu, Jan 01 1970";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init() {
        ifExistsThenDelete();
        Repository repo = new Repository();
        repo.init();
        System.out.println("starting...");
    }

    @After
    public void cleanup() {
        System.out.println("cleaning up...");
        deleteDirectory(Utils.join(CWD, ".gitlet"));
    }

    /**
     * Helper function to init the repository, serves as a test case as well
     */
    @Test
    public void initTest() {
        // Create a GitLet Repository
        Repository repo = new Repository();
        try {
            repo.init();
        } catch (Exception e) {
            assertEquals(e.getMessage(), GITLET_EXISTS_ERR);
        }
        // Deserialize initial commit
        Commit commit = null;
        List<String> filenames = plainFilenamesIn(COMMITS_DIR);
        assert filenames != null;
        for (String filename : filenames) {
            if (INITIAL_COMMIT.equals(filename)) {
                commit = Commit.read(INITIAL_COMMIT);
            }
        }
        // Check if the message and timestamp are correctly assigned
        assert commit != null;
        assertEquals(commit.getMessage(), INITIAL_COMMIT_MSG);
        assertEquals(commit.getTimeStamp(), UNIX_EPOCH);
    }

    /* Test init with repository already created */
    @Test
    public void testInitFailure() {
        // add exception rules
        exceptionRule.expect(GitletException.class);
        exceptionRule.expectMessage(GITLET_EXISTS_ERR);

        // init while repository already exists
        Repository repo = new Repository();
        repo.init();
        deleteDirectory(GITLET_DIR);
    }
    
    @Test
    public void testTreeIterator() {
        TreeNode<Integer> tree = new TreeNode<>(1);
        TreeNode<Integer> subtree = null;
        for (int i = 2; i < 5; i++) subtree = tree.addChild(i);
        for (int j = 5; j < 8; j++) subtree.addChild(j);
        
        boolean found = false;
        for (TreeNode<Integer> treeNode : tree) {
            if (treeNode.data == 7) found = true;
        }
        assertTrue(found);

        found = false;
        for (TreeNode<Integer> treeNode : tree) {
            if (treeNode.data == 8) found = true;
        }
        assertFalse(found);
    }

    @Test
    public void testAdd() {
        // Define constants here
        final String filename = "input.txt";
        final String firstContent = "created";
        final String secondContent = "edited";

        // Add the input.txt file with "test" content to the staging area
        Utils.writeContents(new File(filename), firstContent);
        Repository repo = new Repository();
        repo.add(filename);
        String blobName = repo.staging.getStagedForAddition().get(filename);
        String content = Utils.readContentsAsString(Utils.join(BLOBS_DIR, blobName));
        System.out.printf("initial content: %s\n", content);
        assertEquals(firstContent, content);
        assertEquals(content, Utils.readContentsAsString(new File(filename)));

        // Edit the input.txt file with "edited" content in the staging area
        File file = new File(filename);
        Utils.writeContents(file, "edited");
        repo.add(filename);
        String editedBlobName = repo.staging.getStagedForAddition().get(filename);
        String editedContent = Utils.readContentsAsString(Utils.join(BLOBS_DIR, editedBlobName));
        System.out.printf("edited content: %s\n", editedContent);
        assertEquals(secondContent, editedContent);
        assertEquals(editedContent, Utils.readContentsAsString(new File(filename)));

        // clean up
        deleteFiles(filename);
    }

    /* Test adding non-existing file */
    @Test
    public void testAddFailure() {
        // add exception rules
        exceptionRule.expect(GitletException.class);
        exceptionRule.expectMessage(FILE_NOT_EXIST_ERR);

        // add a file that doesn't exist under the current working directory
        final String filename = "file_not_exist";
        Repository repo = new Repository();
        repo.add(filename);
    }

    @Test
    public void testCommit() {
        // Define constants here
        final String firstFilename = "firstFile.txt";
        final String secondFilename = "secondFile.txt";
        final String msg1 = "create first file";
        final String msg2 = "add second file and modified first file";

        // add a new file in the home directory and commit (commit id 1)
        Utils.writeContents(new File(firstFilename), "created");
        Repository repo = new Repository();
        repo.add(firstFilename);
        repo.dump(); // expect print first file in staging area
        Staging staging = repo.staging;
        assertTrue(staging.getStagedForAddition().containsKey(firstFilename));

        repo.commit(msg1);
        repo.dump(); // expect print empty staging area
        String commitID1 = getCommitID(repo.currentBranch);

        // add a second new file, also modify the file in the last commit, then commit (commit id 2)
        File f1 = Utils.join(CWD, firstFilename);
        File f2 = Utils.join(CWD, secondFilename);
        writeContents(f1, "edited");
        writeContents(f2, "random contents");
        repo.add(secondFilename);
        repo.dump(); // expect print second file in staging area
        assertTrue(staging.getStagedForAddition().containsKey(secondFilename));

        repo.commit(msg2);
        String commitID2 = getCommitID(repo.currentBranch);
        repo.dump(); // expect print empty staging area

        // get the latest commit pointed by HEAD and verify its id == commit id 2
        assertEquals(Utils.sha1((Object) serialize(repo.head)), commitID2);
        // go back to its parent and verify that its id == commit id 1
        String parentID = repo.head.getParent();
        assertEquals(parentID, commitID1);
        // verify the tree, messages and file content are correct in both commit 1 and 2
        Commit commit1 = getCommitFromID(commitID1);
        Commit commit2 = getCommitFromID(commitID2);
        assertEquals(commit1.getMessage(), msg1);
        assertEquals(commit2.getMessage(), msg2);
        // clean up files
        deleteFiles(firstFilename, secondFilename);
    }

    /* Test commit with empty message */
    @Test
    public void testCommitFailure1() {
        final String filename = "test.txt";

        // add exception rules
        exceptionRule.expect(GitletException.class);
        exceptionRule.expectMessage(COMMIT_MSG_MISSING_ERR);

        // add a file into the staging area
        File f = new File(filename);
        writeContents(f, "test");
        Repository repo = new Repository();
        repo.add(filename);
        try {
            repo.commit(null);
        } catch (Exception e) {
            deleteFiles(filename);
            throw e;
        }
    }

    /* Test commit with empty staging area (for addition / tracked files) */
    @Test
    public void testCommitFailure2() {
        // add exception rules
        exceptionRule.expect(GitletException.class);
        exceptionRule.expectMessage(STAGING_AREA_EMPTY_ERR);

        // add a file into the staging area
        Repository repo = new Repository();
        repo.commit("second commit");
    }

    @Test
    public void testLog() {
        // Define constants here
        final String firstFilename = "firstFile.txt";
        final String secondFilename = "secondFile.txt";
        final String msg1 = "create first file";
        final String msg2 = "add second file and modified first file";

        // add a new file in the home directory and commit (commit id 1)
        Utils.writeContents(new File(firstFilename), "created");
        Repository repo = new Repository();
        repo.add(firstFilename);
        Staging staging = repo.staging;

        repo.commit(msg1);

        // add a second new file, also modify the file in the last commit, then commit (commit id 2)
        File f1 = Utils.join(CWD, firstFilename);
        File f2 = Utils.join(CWD, secondFilename);
        writeContents(f1, "edited");
        writeContents(f2, "random contents");
        repo.add(secondFilename);

        repo.commit(msg2);

        // test log
        repo.log();

        // clean up files
        deleteFiles(firstFilename, secondFilename);
    }

    /* Helper functions */

    private void deleteFiles(String... filenames) {
        for (String filename : filenames) {
            Utils.join(CWD, filename).delete();
        }
    }

    /**
     * Helper function to delete directory recursively
     * @param directoryToBeDeleted
     * @return
     */
    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private boolean ifExistsThenDelete() {
        Path path = Paths.get(".gitlet");
        if (Files.exists(path)) {
            return deleteDirectory(Utils.join(CWD, ".gitlet"));
        }
        return true;
    }

    /**
     * Helper function to get commit object from commit ID
     * @param commitID
     * @return Commit object given by the UID
     */
    private Commit getCommitFromID (String commitID) {
        File file = Utils.join(COMMITS_DIR, commitID);
        return Utils.readObject(file, Commit.class);
    }

    /**
     * Helper to get commit ID from the current branch file
     * @param currentBranch: current branch name
     * @return commitID
     */
    private String getCommitID(String currentBranch) {
        File f = Utils.join(BRANCHES_DIR, currentBranch);
        Branch branch = Branch.load(f);
        // branch.dump(); // for debug use
        Commit branchHEAD = branch.getHead();
        return Utils.sha1((Object) serialize(branchHEAD));
    }
}
