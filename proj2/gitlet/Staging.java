package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

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
    public Staging(Map<String, String> stagedForAddition, Set<String> stagedForRemoval) {
        this.stagedForAddition = stagedForAddition;
        this.stagedForRemoval = stagedForRemoval;
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

    /* Get all staged files, including staged for removal */
    public List<String> getStagedFiles() {
        List<String> res = new LinkedList<>(this.stagedForAddition.keySet());
        res.addAll(this.stagedForRemoval);
        return res;
    }
}
