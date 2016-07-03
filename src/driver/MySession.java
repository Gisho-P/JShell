package driver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The Class MySession maintains various attributes of a shell's current
 * session. These attributes include location information, history and a map
 * between classes and commands. These attributes can be accessed and changed by
 * users through various commands.
 * 
 * @author Adnan Bhuiyan
 * @author Girrshotan Pushparajah
 * @author John Song
 * @author Dhrumil Patel
 */
public class MySession {

  /**
   * Storage for commands entered by users.
   */
  protected List<String> commandHistory;
  /**
   * Current directory location.
   */
  private Directory currentDir;
  /**
   * Location of the root directory.
   */
  private Directory rootDir;
  /**
   * Table mapping commands to classes to invoke in JShell.
   */
  public Hashtable<String, String> commandToClass =
      new Hashtable<String, String>();

  /**
   * Create a new MySession object with default attributes for root and current
   * directory. Furthermore, the map between commands and classes, as well as
   * the command storage, are initialized.
   * 
   * @return New MySession object
   */
  public MySession() {
    // initialize command storage
    commandHistory = new ArrayList<String>();

    // map commands to classes in src
    commandToClass.put("man", "DisplayManual");
    commandToClass.put("history", "DisplayHistory");
    commandToClass.put("echo", "DisplayStoreString");
    commandToClass.put("cat", "DisplayFile");
    commandToClass.put("popd", "PopDirectory");
    commandToClass.put("pushd", "PushDirectory");
    commandToClass.put("pwd", "DisplayPath");
    commandToClass.put("ls", "ListDirectoryContents");
    commandToClass.put("cd", "ChangeDirectory");
    commandToClass.put("mkdir", "MakeDirectory");
    commandToClass.put("exit", "ExitProgram");
    commandToClass.put("curl", "RetrieveUrlFile");

    // try to initialize root and current directory (to root)
    try {
      rootDir = new Directory("");
    } catch (FileTypes.InvalidName invalidName) {
      invalidName.printStackTrace();
    }
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
   * Given a number n less then the number of commands in history prints the
   * last n commands to stdout
   *
   * @param numberOfCommands the number of commands to be printed from history
   * @return The history of commands, the amount of entries listed based on
   *         parameter
   */
  public Output getCommandHistory(int numberOfCommands) {
    Output out = new Output();
    int historySize = commandHistory.size();
    // If a number greater then the number of commands in history is given
    // print all commands
    if (numberOfCommands > historySize)
      numberOfCommands = historySize;
    if (numberOfCommands < 0) { // can't print negative commands
      return out.withStdError("history usage: history [NUMBER >= 0]\n");
    } else { // format output and return
      for (int cmdNumber = historySize - numberOfCommands
          + 1; cmdNumber <= historySize; cmdNumber++) {
        out.addStdOutput(cmdNumber + ". " +
          commandHistory.get(cmdNumber - 1));
        if (cmdNumber != historySize) {
          out.addStdOutput("\n");
        }
      }
      return out;
    }
  }

  /**
   * Prints the command history to stdout.
   * 
   * @return List of commands entered by user, ordered chronologically.
   */
  public Output getCommandHistory() {
    return getCommandHistory(commandHistory.size()); // call
                                                       // printCommandHistory
                                                       // with the max number as
                                                       // a parameter
  }

  /**
   * Clear list of user commands saved in session.
   */
  public void clearCommands() {
    commandHistory.clear();
  }

  /**
   * Get the root directory of the session.
   * 
   * @return root directory
   */
  public Directory getRootDir() {
    return rootDir;
  }

  /**
   * Get the location user is currently in.
   * 
   * @return current location in session
   */
  public Directory getCurrentDir() {
    return currentDir;
  }

  /**
   * Change the current to a new specified directory.
   * 
   * @param cDir new directory to set as current
   */
  public void setCurrentDir(Directory cDir) {
    currentDir = cDir;
  }
}
