package gitlet;

import static gitlet.Constants.INVALID_ARG;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Mingqian Yu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    private static void validateArgs(int actual, int expected, String cmd) {
        if (actual != expected) {
            throw new RuntimeException(INVALID_ARG + cmd);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                validateArgs(args.length, 1, args[0]);
                Repository repository = new Repository();
                repository.init();
                repository.save();
            }
            case "add" -> {
                validateArgs(args.length, 2, args[0]);
                Repository repository = new Repository();
                repository.add(args[1]);
                repository.save();
            }
            case "commit" -> {
                validateArgs(args.length, 2, args[0]);
                Repository repository = new Repository();
                repository.commit(args[1]);
                repository.save();
            }
            case "log" -> {
                validateArgs(args.length, 1, args[0]);
                Repository repository = new Repository();
                repository.log();
                repository.save();
            }
            case "checkout" -> {
                Repository repository = new Repository();
                switch (args.length) {
                    case 2 -> repository.checkout(true, null, null, args[1]);
                    case 3 -> repository.checkout(false, null, args[2], null);
                    case 4 -> repository.checkout(false, args[1], args[3], null);
                    default -> throw new RuntimeException(INVALID_ARG + "checkout");
                }
                repository.save();
            }
        }
    }
}
