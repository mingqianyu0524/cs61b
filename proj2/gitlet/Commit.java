package gitlet;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import static gitlet.Utils.*;
import static gitlet.Constants.*;

import java.util.*;

/** Represents a gitlet commit object.
 *  The commit object is Java serializable for the goal of persistence.
 *  Each commit can have
 *
 *  @author mingqian yu
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    @Serial
    private static final long serialVersionUID = -4625650758405441611L;
    /** The message of this Commit. */
    private final String message;
    /** The timestamp of this Commit. */
    private String timestamp;
    /**
     * Tree of the current working directory at the time of this commit
     * Under the assumption that the depth of the tree is 1, it's reasonable to use a map data structure
     * to represent the tree.
     *
     * Key = file name
     * Value = file blob SHA1
     */
    private final Map<String, String> tree;
    /**
     * Parent commit SHA1
     */
    private final String parent;

    /**
     * Create initial commit object
     */
    public Commit() {
        this.message = INITIAL_COMMIT_MSG;
        setTimestamp(true);
        this.tree = new HashMap<>();
        this.parent = null;
    }

    public Commit(String message, Map<String, String> tree, String parent) {
        this.message = message;
        setTimestamp(false);
        this.tree = tree;
        this.parent = parent;
    }

    /** Serialize the commit and store into repository, return the SHA-1 of the commit object */
    public void save() {
        String fileName = sha1((Object) serialize(this));
        File f = join(COMMITS_DIR, fileName);
        writeObject(f, this);
    }

    /** Deserialize the commit from the repository, and return the Commit object */
    public static Commit read(String commitID) {
        String filename = expandCommitID(commitID);

        File f = join(COMMITS_DIR, filename);
        if (!f.isFile()) {
            throw Utils.error("No commit with that id exists.");
        }
        return readObject(f, Commit.class);
    }

    /** Expand the commit id to full 40-byte long sha1 id */
    private static String expandCommitID(String shortened) {
        // If the commit id is null OR exceeds 40 bytes, exit
        if (shortened == null || shortened.length() > 40) {
            throw error("Invalid filename");
        }

        // Match the provided commit id with the file names under the directory
        // The project description specified a more efficient way of organizing the commit directory structure
        // in order to speed up the lookup process.
        List<String> commitIDs = Utils.plainFilenamesIn(Utils.join(COMMITS_DIR));
        assert commitIDs != null;
        String filename = "";
        for (String cid : commitIDs) {
            if (cid.indexOf(shortened) == 0) {
                filename = cid;
                break;
            }
        }
        return filename;
    }

    /**
     * Set commit date, if the commit is the initial one, set it to the epoch time;
     * Otherwise, set it to the current date.
     */
    public void setTimestamp(boolean init) {
        DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = init? new Date(0L) : new Date();
        this.timestamp = df.format(date);
    }

    /* Get a list of files tracked by the current commit */
    public List<String> getTrackedFiles() {
        return new LinkedList<>(this.tree.keySet());
    }

    public String getTimeStamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String getParent() {
        return this.parent;
    }

    /**
     * Get the file blob name in this commit tree by providing the filename
     * @param filename
     * @return String blob name if the file exists in the commit tree, null if it doesn't exist
     */
    public String getBlobName(String filename) {
        return this.tree.get(filename);
    }

    public Map<String, String> getTree() {
        return tree;
    }


}
