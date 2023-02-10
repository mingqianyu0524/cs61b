package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gitlet.Constants.STAGING_DIR;

/**
 * Staging area of the GitLet repository
 */
public class Staging implements Serializable {

    /**
     * Fields
     */

    // Important: map the actual filename to the file blob name
    private Map<String, String> stagedForAddition = new HashMap<>();
    private Set<String> stagedForRemoval = new HashSet<>();

    /**
     * Constructor
     */

    public Staging() {}

    /* After changes were applied to the staging area, upload the staging object locally */
    public Staging(Map<String, String> trackedFiles, Set<String> untrackedFiles) {
        this.stagedForAddition = trackedFiles;
        this.stagedForRemoval = untrackedFiles;
    }

    /**
     * Methods
     */

    public Map<String, String> getStagedForAddition() {
        return this.stagedForAddition;
    }

    public Set<String> getStagedForRemoval() {
        return this.stagedForRemoval;
    }
}
