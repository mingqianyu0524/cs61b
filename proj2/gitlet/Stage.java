package gitlet;

import java.io.Serializable;
import java.util.Map;

/**
 * Staging area of the GitLet repository
 */
public class Stage implements Serializable {

    // TODO Map file name hash to the file blob hash
    private Map<String, String> stagingForAddition;

    public Stage() {
        // TODO when 'add' is called for the first time, create staging and blobs folders
    }


}
