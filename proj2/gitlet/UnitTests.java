package gitlet;
import org.junit.Test;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import static gitlet.Constants.*;
import static gitlet.Utils.*;
import static org.junit.Assert.*;


public class UnitTests {

    // Initial commit SHA1 hash, may change if the Commit class gets modified
    public static final String INITIAL_COMMIT = "b3a82f44130594618a373770e45b91e9bfbc3472";
    // Unix epoch time in designated datetime format
    public static final String UNIX_EPOCH = "00:00:00 UTC, Thu, Jan 01 1970";

    public void init() {
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

    @Test
    public void testDuplicateGitLet() {
        Repository repo = new Repository();
        try {
            repo.init();
        } catch (Exception e) {
            assertEquals(e.getMessage(), GITLET_EXISTS_ERR);
        }
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
    public void testWriteContents() {
        File input = new File("input.txt");
        byte[] inputContents = Utils.readContents(input);
        File output = new File("output.txt");
        Utils.writeContents(output, inputContents);

        byte[] outputContents = Utils.readContents(output);
        for (int i = 0; i < inputContents.length; i++) {
            assertEquals(inputContents[i], outputContents[i]);
        }

    }

    @Test
    public void testAdd() {
        init();

        // Definition here
        String filename = "input.txt";
        String firstContent = "created";
        String secondContent = "edited";
        Utils.writeContents(new File(filename), firstContent);

        // Add the input.txt file with "test" content to the staging area
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
        deleteDirectory(Utils.join(CWD, ".gitlet"));
    }

    @Test
    public void testCommit() {
        init();

        // Definition here
        String firstFilename = "firstFile.txt";
        String secondFilename = "secondFile.txt";
        String msg1 = "create first file";
        String msg2 = "add second file and modified first file";
        Utils.writeContents(new File(firstFilename), "created");

        // add a new file in the home directory and commit (commit id 1)
        Repository repo = new Repository();
        repo.add(firstFilename);
        Staging staging = repo.staging;
        assertTrue(staging.getStagedForAddition().containsKey(firstFilename));

        repo.commit(msg1);

        File f = Utils.join(BRANCHES_DIR, repo.currentBranch);
        String commitID1 = getCommitID(f);

        // add a second new file, also modify the file in the last commit, then commit (commit id 2)
        File f1 = Utils.join(CWD, firstFilename);
        File f2 = Utils.join(CWD, secondFilename);
        writeContents(f1, "edited");
        writeContents(f2, "random contents");
        repo.add(secondFilename);
        assertTrue(staging.getStagedForAddition().containsKey(secondFilename));

        repo.commit(msg2);
        String commitID2 = getCommitID(f);

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
        // clean up
        deleteDirectory(Utils.join(CWD, ".gitlet"));
    }

    /**
     * Helper function to delete directory recursively
     * @param directoryToBeDeleted
     * @return
     */
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private Commit getCommitFromID (String commitID) {
        File file = Utils.join(COMMITS_DIR, commitID);
        return Utils.readObject(file, Commit.class);
    }

    private String getCommitID(File f) {
        Branch branch = Branch.load(f);
        Commit branchHEAD = branch.getHead();
        return Utils.sha1((Object) serialize(branchHEAD));
    }
}
