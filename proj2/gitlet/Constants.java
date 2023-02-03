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

    public static final String GITLET_EXISTS_ERR = "A Gitlet version-control system already exists in the current directory.";
}
