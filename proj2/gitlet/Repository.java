package gitlet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gitlet.Constants.*;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Mingqian Yu
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /* TODO: fill in the rest of this class. */

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit:
     * a commit that contains no files and has the commit message initial commit (just like that, with no punctuation).
     *
     * It will have a single branch: master, which initially points to this initial commit,
     * and master will be the current branch. The timestamp for this initial commit will be 00:00:00 UTC,
     * Thursday, 1 January 1970 in whatever format you choose for dates (this is called “The (Unix) Epoch”,
     * represented internally by the time 0.)
     *
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content,
     * it follows that all repositories will automatically share this commit (they will all have the same UID) and all
     * commits in all repositories will trace back to it.
     */
    public void init() {
        // Check if the version-control system already exists
        Path path = Paths.get(".gitlet");
        if (Files.exists(path)) {
            throw error(GITLET_EXISTS_ERR);
        }

        // TODO Create the master branch and maybe the HEAD pointer too?

        // Initialize file directories for GitLet repository
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        STAGING_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        BLOBS_DIR.mkdir();
        // Create the initial commit and store it in the file system
        new Commit().save();
    }

    /**
     * Adds a copy of the file as it currently exists to the staging area (see the description of the commit command).
     * For this reason, adding a file is also called staging the file for addition.
     *
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet. If the current working version of the file is identical to
     * the version in the current commit, do not stage it to be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     *
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     *
     * @param filename Name of the file to be added to the staging area
     */
    public void add(String filename) {
        // TODO create 'index' file under .gitlet/ for staging area persistence when 'add' is called

    }

    public void commit() {
        // Read from the file system the HEAD commit object and the staging area
        // Clone the HEAD commit
        // Modify its message and timestamp according to user input
        // Use the staging area in order to modify the files tracked by the new commit
        // Store any new or modified commit object into the file system and clean up the staging area
    }
}
