package gitlet;
import org.junit.Test;

import java.util.List;

import static gitlet.Constants.*;
import static gitlet.Utils.plainFilenamesIn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class UnitTests {

    // Initial commit SHA1 hash, may change if the Commit class gets modified
    public static final String INITIAL_COMMIT = "cba6a485adc182869ca5ccb5552a8f82dae8eb7f";
    // Unix epoch time in designated datetime format
    public static final String UNIX_EPOCH = "00:00:00 UTC, Thu, Jan 01 1970";

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
    }
}
