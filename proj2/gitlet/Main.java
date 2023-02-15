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

        // Handle commands with various number of args
        if (expected == 0) {
            if (!b) {
                throw Utils.error(INCORRECT_OPERANDS);
            }
            return;
        }

        // Handle commands with fixed number of args
        if (args.length != expected || !b) {
            throw Utils.error(INCORRECT_OPERANDS);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        // TODO: If a user inputs a command that requires being in an initialized Gitlet working directory
        //  (i.e., one containing a .gitlet subdirectory), but is not in such a directory,
        //  print the message Not in an initialized Gitlet directory.

        Repository repository = new Repository();

        switch (firstArg) {
            case "init" -> {
                validateArgs(args, 1, firstArg);
                try {
                    repository.init();
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "add" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                repository = new Repository(); // TODO: Add load() to Repository and returns an existing repository
                try {
                    repository.add(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "commit" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9\\s*]*");
                repository = new Repository();
                try {
                    repository.commit(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "log" -> {
                validateArgs(args, 1, firstArg);
                repository = new Repository();
                repository.log();
            }
            case "global-log" -> {
                validateArgs(args, 1, firstArg);
                repository = new Repository();
                repository.globalLog();
            }
            case "find" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9\\s*]*");
                repository = new Repository();
                try {
                    repository.find(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "status" -> {
                validateArgs(args, 1, firstArg);
                repository = new Repository();
                repository.status();
            }
            case "rm" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                repository = new Repository();
                try {
                    repository.rm(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "checkout" -> {
                repository = new Repository();
                try {validateArgs(args, 0, firstArg + "\\s*[A-Fa-f0-9]*\\s*[--]*\\s*[-_.A-Za-z0-9\\s]+");}
                catch (GitletException e) {
                    Utils.message(e.getMessage());
                    return;
                }
                if (args.length == 2) {
                    try {
                        repository.checkout(true, null, null, args[1]);
                    } catch (GitletException e) {
                        Utils.message(e.getMessage());
                    }
                }
                if (args.length == 3) {
                    try {
                        repository.checkout(false, null, args[2], null);
                    } catch (GitletException e) {
                        Utils.message(e.getMessage());
                    }
                }
                if (args.length == 4) {
                    try {
                        repository.checkout(false, args[1], args[3], null);
                    } catch (GitletException e) {
                        Utils.message(e.getMessage());
                    }
                }
            }
            case "branch" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                repository = new Repository();
                try {
                    repository.branch(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "rm-branch" -> {
                validateArgs(args, 2, firstArg + "[-_.A-Za-z0-9]+");
                repository = new Repository();
                try {
                    repository.rmBranch(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            case "reset" -> {
                validateArgs(args, 2, firstArg + "\\s*[A-Fa-f0-9]*");
                repository = new Repository();
                try {
                    repository.reset(args[1]);
                } catch (GitletException e) {
                    Utils.message(e.getMessage());
                }
            }
            default -> Utils.message("No command with that name exists.");
        }
        repository.save();
    }
}
