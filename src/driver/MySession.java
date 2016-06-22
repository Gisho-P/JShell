package driver;

import java.util.ArrayList;
import java.util.Hashtable;
//import java.util.Iterator;
import java.util.List;

/**
 * The Class MySession stores, and outputs a history of commands.
 */
public class MySession {

    protected static List<String> commandHistory;
    private static Hashtable<String, String> manCmd =
            new Hashtable<String, String>();
    private Directory currentDir;
    private Directory rootDir;


    public MySession() {
        commandHistory = new ArrayList<String>();
        manCmd.put("cat", "CAT(1)\t\t\t\tUser Commands\t\t\t\tCAT(1)\n" +
                "\nNAME\n\t\tcat - displays the contents of one or " +
                "more files on the standard output\n\nSYNOPSIS\n\t\t" +
                "cat FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t" +
                "Concatenates one or more files to the standard output" +
                ".\n\t\tCan take any amount of files greater then one as a" +
                " parameter.");
        manCmd.put("cd", "CD(1)\t\t\t\tUser Commands\t\t\t\tCD(1)\n\nNAME\n\t"
                + "\tcd - changes the working directory of the shell" +
                "\n\nSYNOPSIS\n\t\tcd DIR\n\nDESCRIPTION\n\t\t" +
                "Changes the current directory of the shell to the one" +
                " specified by DIR.\n\n\t\tThe DIR may be relative to " +
                "the current directory or the full path.\n\n\t\t" +
                ".. indicates the parent directory, and . indicates " +
                "the current directory\n\t\twhen specifying the DIR.");
        manCmd.put("echo", "ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n" +
                "NAME\n\t\techo - prints a string to standard output," +
                " or saves a string to a text\n\t\tfile\n\nSYNOPSIS" +
                "\n\t\techo STRING [>[>] OUTFILE]\n\nDESCRIPTION\n\t\t" +
                "Saves the STRING to the OUTFILE if it's provided, " +
                "otherwise it prints\n\t\tthe STRING to the standard " +
                "output.\n\n\t\tIf the OUTFILE is an existing file in" +
                " the directory, it will overwrite\n\t\tthe contents" +
                " of the OUTFILE with the string, otherwise it will" +
                " create\n\t\ta new file with name OUTFILE .\n\n\t\t" +
                "STRING should be a string of characters surrounded " +
                "by double quotation\n\t\tmarks.");
        manCmd.put("exit", "EXIT(1)\t\t\t\tUser Commands\t\t\t\tEXIT(1)\n\n"
        		+ "NAME\n\t\texit - terminates the shell\n\nSYNOPSIS\n\t\t" +
                "exit\n\nDESCRIPTION\n\t\tTerminates the users" +
                " session with the shell which removes their ability" +
                "\n\t\tto process any more commands.");
        manCmd.put("history", "HISTORY(1)\t\t\t\tUser Commands\t\t\t\t"
        		+ "HISTORY(1)\n\nNAME\n\t\thistory - prints out the recent " +
                "commands entered in the shell\n\nSYNOPSIS\n\t\t" +
                "history [NUMBER]\n\nDESCRIPTION\n\t\tPrints out " +
                "the commands entered in the shell from the first " +
                "one entered\n\t\tto the last.\n\n\t\tIf NUMBER is" +
                " specified, will print only the last NUMBER " +
                "amount of\n\t\tcommands that were entered.");
        manCmd.put("man", "MAN(1)\t\t\t\tUser Commands\t\t\t\tMAN(1)\n\nNAME"
        		+ "\n\t\tman - prints the documentation for a specified" +
                " command\n\nSYNOPSIS\n\t\tman CMD\n\nDESCRIPTION\n\t\t" +
                "Prints out the documentation for the CMD which " +
                "contains information\n\t\ton how to use the command");
        manCmd.put("ls", "LS(1)\t\t\t\tUser Commands\t\t\t\tLS(1)\n\nNAME\n\t"
        		+ "\tls - prints out all of the contents of one or many " +
                "files/directories\n\nSYNOPSIS\n\t\tls [PATH ...]\n\n" +
                "DESCRIPTION\n\t\tPrints out the contents of files/" +
                "directories.\n\n\t\tIf PATH is not specified, prints " +
                "out the contents of the current\n\t\tdirectory by " +
                "default.\n\n\t\tIf PATH is specified, prints out the " +
                "contents of the files/directories\n\t\tfor each PATH " +
                "given");
        manCmd.put("mkdir", "MKDIR(1)\t\t\t\tUser Commands\t\t\t\tMKDIR(1)\n\n"
        		+ "NAME\n\t\tmkdir - creates directory(s)\n\nSYNOPSIS" +
                "\n\t\tmkdir DIR ...\n\nDESCRIPTION\n\t\tCreates " +
                "directories which may be in the current directory " +
                "or a full\n\t\tpath relative to the root/current " +
                "directory.");
        manCmd.put("popd", "POPD(1)\t\t\t\tUser Commands\t\t\t\tPOPD(1)\n\n" +
                "NAME\n\t\tpopd - removes the last directory pushed," +
                " and changes the current\n\t\tdirectory to that one" +
                "\n\nSYNOPSIS\n\t\tpopd\n\nDESCRIPTION\n\t\tRemoves " +
                "the last directory that was pushed to the directory " +
                "stack,\n\t\tand changes the current working " +
                "directory to this directory");
        manCmd.put("pushd", "PUSHD(1)\t\t\t\tUser Commands\t\t\t\tPUSHD(1)\n\n"
        		+ "NAME\n\t\tpushd - saves the current working " +
                "directory to the stack and changes\n\t\tnew working directory"
                + " to DIR\n\nSYNOPSIS\n\t\tpushd DIR\n\n" +
                "DESCRIPTION\n\t\tSaves the current working " +
                "directory to the top of the directory\n\t\tstack. Changes"
                + " working directory to path specified in DIR.\n\t\t"
                + "Directory stack follows stack behaviour (LIFO).");
        manCmd.put("pwd", "PWD(1)\t\t\t\tUser Commands\t\t\t\tPWD(1)\n\nNAME" +
                "\n\t\tpwd - prints the current directory\n\n" +
                "SYNOPSIS\n\t\tpwd\n\nDESCRIPTION\n\t\tPrints the" +
                " current working directories full path to standard " +
                "output");

        rootDir = new Directory("");
        currentDir = rootDir;
    }

    /**
     * Saves the command to the command history.
     *
     * @param command the command to be added to history
     */
    public void saveCommand(String command) {
    	commandHistory.add(command);
    }

    /**
     * Given a number n less then the number of commands in history
     * prints the last n commands to stdout
     *
     * @param numberOfCommands the number of commands to be printed from history
     */
    public static String printCommandHistory(int numberOfCommands) {
        int historySize = commandHistory.size();
        if (numberOfCommands < 0) {
            return "history usage: history [NUMBER >= 0]";
        } else {
            String output = "";
            for (int cmdNumber = historySize - numberOfCommands + 1;
                 cmdNumber <= historySize; cmdNumber++) {
                output = output + cmdNumber + ". " +
                        commandHistory.get(cmdNumber - 1) + System.lineSeparator();
            }
            return output;
        }
    }

    /**
     * Prints the command history to stdout.
     */
    public static String printCommandHistory() {
        return printCommandHistory(commandHistory.size());
    }

    public static void clearCommands() {
        commandHistory.clear();
    }

    public static String manPages(String command) {
        return manCmd.containsKey(command) ? manCmd.get(command) :
                "error: invalid command";
    }

    public Directory getRootDir() {
        return rootDir;
    }

    public Directory getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(Directory currentDir) {
        this.currentDir = currentDir;
    }
}
