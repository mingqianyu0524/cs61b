package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Constants.BRANCHES_DIR;

/**
 *
 * Creates a new branch with the given name, and points it at the current head commit.
 * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
 *
 * This command does NOT immediately switch to the newly created branch (just as in real Git).
 * Before you ever call branch, your code should be running with a default branch called “master”.
 *
 */
public class Branch implements Serializable, Dumpable {

    /* Name of the branch */
    private String name;
    /* Points to the tip commit of the current branch */
    private Commit head;

    private static final long serialVersionUID = 1281955272180489634L;

    public Branch(String name, Commit head) {
        this.name = name;
        this.head = head;
    }

    /**
     * Create a new branch (but not switch to it immediately)
     * @throws IOException
     */
    public void create() throws IOException {
        Branch branch = new Branch(this.name, this.head);
        File file = Utils.join(BRANCHES_DIR, this.name);
        file.createNewFile();
        save(file, branch);
    }

    /**
     * Save the branch (Serialize) to the file system
     * @param file
     * @param branch
     */
    public static void save(File file, Branch branch) {
        Utils.writeObject(file, branch);
    }

    /**
     * Load a branch object (Deserialize) from file system
     * @param branch
     * @return branch object
     */
    public static Branch load(File branch) {
        return Utils.readObject(branch, Branch.class);
    }

    /**
     * Get the branch head
     * @return the head commit of the branch
     */
    public Commit getBranchHead() {
        return this.head;
    }

    public void setHead(Commit head) {
        this.head = head;
    }

    public Commit getHead() {
        return head;
    }

    @Override
    public void dump() {
        System.out.println("=============Dumping Branch=============");
        System.out.printf("branch name: %s%n", name);
        System.out.printf("commit id: %s%n", Utils.sha1((Object) Utils.serialize(head)));
        System.out.printf("commit msg: %s%n", head.getMessage());
        System.out.printf("commit timestamp: %s%n", head.getTimeStamp());
        System.out.printf("commit parent: %s%n", head.getParent());
    }
}
