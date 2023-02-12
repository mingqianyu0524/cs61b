package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Mingqian Yu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                // TODO: handle the `init` command
                Repository repository = new Repository();
                repository.init();
                repository.save();
            }
            case "add" -> {
                // TODO: handle the `add [filename]` command
                Repository repository = new Repository();
                repository.add(args[1]);
                repository.save();
            }
            // TODO: FILL THE REST IN
            case "commit" -> {
                Repository repository = new Repository();
                repository.commit(args[1]);
                repository.save();
            }
            case "log" -> {
                Repository repository = new Repository();
                repository.log();
                repository.save();
            }
        }
    }
}
