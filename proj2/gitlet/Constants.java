package gitlet;

import java.io.File;

import static gitlet.Utils.join;

/**
 * Stores constants for ease of use
 */
public class Constants {
    public static final String INITIAL_COMMIT_MSG = "initial commit";

    /** Files and directories */

    public static final String CWD = System.getProperty("user.dir");

    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGING_DIR = join(GITLET_DIR, "staging");

    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");

    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");

    /** Error messages */

    public static final String INVALID_ARG = "Invalid number of arguments for command ";

    public static final String GITLET_EXISTS_ERR =
            "A Gitlet version-control system already exists in the current directory.";

    public static final String FILE_NOT_EXIST_ERR =
            "File does not exist.";

    public static final String COMMIT_MSG_MISSING_ERR = "Please enter a commit message.";

    public static final String STAGING_AREA_EMPTY_ERR = "No changes added to the commit.";

    public static final String RM_ERR = "No reason to remove the file.";

    public static final String COMMIT_NOT_EXIST_ERR = "No commit with that id exists.";

    public static final String FILE_NOT_IN_COMMIT = "File does not exist in that commit.";

    public static final String CHECKOUT_CURRENT_BRANCH = "No need to checkout the current branch.";

    public static final String BRANCH_NOT_EXIST = "No such branch exists.";

    public static final String UNTRACKED_ERR = "There is an untracked file in the way; " +
            "delete it, or add and commit it first.";
}
