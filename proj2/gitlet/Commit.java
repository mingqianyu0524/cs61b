package gitlet;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import static gitlet.Utils.*;
import static gitlet.Constants.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
    public static Commit read(String fileName) {
        try {
            File f = join(COMMITS_DIR, fileName);
            return readObject(f, Commit.class);
        } catch (Exception e) {
            return null;
        }
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
     * Get the file blob by looking up the file name in the commit tree
     *
     * @return SHA1 hash of the file blob
     */
    public String getBlob(String filename) {
        return this.tree.get(sha1(filename));
    }

    public Map<String, String> getTree() {
        return tree;
    }


}
