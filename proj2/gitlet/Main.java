package gitlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static gitlet.Constants.INCORRECT_OPERANDS;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Mingqian Yu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    private static void validateArgs(String[] args, int expected, String pattern) {
        StringBuilder composed = new StringBuilder();

        for (String arg : args) {
            composed.append(arg);
        }

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(composed.toString());
        boolean b = m.matches();

        if (args.length != expected) {
            throw Utils.error(INCORRECT_OPERANDS);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        // TODO: exception handling

        // TODO: If a user inputs a command that requires being in an initialized Gitlet working directory
        //  (i.e., one containing a .gitlet subdirectory), but is not in such a directory,
        //  print the message Not in an initialized Gitlet directory.

        switch (firstArg) {
            case "init" -> {
                validateArgs(args, 1, firstArg);
                Repository repository = new Repository();
                repository.init();
                repository.save();
            }
            case "add" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                Repository repository = new Repository();
                repository.add(args[1]);
                repository.save();
            }
            case "commit" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                Repository repository = new Repository();
                repository.commit(args[1]);
                repository.save();
            }
            case "log" -> {
                validateArgs(args, 1, firstArg);
                Repository repository = new Repository();
                repository.log();
                repository.save();
            }
            case "status" -> {
                validateArgs(args, 1, firstArg);
                Repository repository = new Repository();
                repository.status();
                repository.save();
            }
            case "checkout" -> {
                Repository repository = new Repository();
                switch (args.length) {
                    // TODO: match input formats
                    case 2 -> repository.checkout(true, null, null, args[1]);
                    case 3 -> repository.checkout(false, null, args[2], null);
                    case 4 -> repository.checkout(false, args[1], args[3], null);
                    default -> throw Utils.error(INCORRECT_OPERANDS);
                }
                repository.save();
            }
            default -> {
                System.out.println("No command with that name exists.");
            }
        }
    }
}
