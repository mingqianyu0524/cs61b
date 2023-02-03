package gitlet;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import static gitlet.Utils.*;
import static gitlet.Constants.*;
import java.util.Date;
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
     * Snapshot of the current working directory at the time of this commit
     * Key = file name SHA1
     * Value = file blob SHA1
     */
    private final Map<String, String> snapshot;
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
        this.snapshot = null;
        this.parent = null;
    }

    public Commit(String message, Map<String, String> snapshot, String parent) {
        this.message = message;
        setTimestamp(false);
        this.snapshot = snapshot;
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
        File f = join(COMMITS_DIR, fileName);
        return readObject(f, Commit.class);
    }

    /**
     * Set commit date, if the commit is the initial one, set it to the epoch time;
     * Otherwise, set it to the current date.
     */
    public void setTimestamp(boolean init) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss zzz, E, MMM dd yyyy");
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
}
