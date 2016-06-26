package driver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The Class MySession stores, and outputs a history of commands.
 */
public class MySession {

  protected List<String> commandHistory;
  private Directory currentDir;
  private Directory rootDir;
  public Hashtable<String, String> commandToClass =
      new Hashtable<String, String>();

  public MySession() {
    commandHistory = new ArrayList<String>();
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
   */
  public String printCommandHistory(int numberOfCommands) {
    int historySize = commandHistory.size();
    if (numberOfCommands < 0) {
      return "history usage: history [NUMBER >= 0]\n";
    } else {
      String output = "";
      for (int cmdNumber = historySize - numberOfCommands
          + 1; cmdNumber <= historySize; cmdNumber++) {
        output = output + cmdNumber + ". " + commandHistory.get(cmdNumber - 1);
        if (cmdNumber != historySize) {
          output = output + "\n";
        }
      }
      return output;
    }
  }

  /**
   * Prints the command history to stdout.
   */
  public String printCommandHistory() {
    return printCommandHistory(commandHistory.size());
  }

  public void clearCommands() {
    commandHistory.clear();
  }

  public Directory getRootDir() {
    return rootDir;
  }

  public Directory getCurrentDir() {
    return currentDir;
  }

  public void setCurrentDir(Directory cDir) {
    currentDir = cDir;
  }
}