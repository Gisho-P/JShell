package driver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import commands.*;
import structures.*;
import exceptions.InvalidNameException;

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
  public Hashtable<String, Command> commandToClass =
      new Hashtable<String, Command>();
  
  /**
   * Running status of Shell
   * */
  private boolean status;
  
  /**
   * Session's directory stack
   * */
  private DirStack ds = new DirStack();

  /**
   * JShell's output/error buffer
   * */
  private Output o;
  
  /**
   * Create a new MySession object with default attributes for root and current
   * directory. Furthermore, the map between commands and classes, as well as
   * the command storage, are initialized.
   * 
   * @return New MySession object
   */
  public MySession(Output o) {
    this.o = o; // initialize shell output buffer
    // set shell status
    status = true;
    // initialize command storage
    commandHistory = new ArrayList<String>();

    // map commands to classes in src
    commandToClass.put("man", new DisplayManual(this));
    commandToClass.put("history", new DisplayHistory(this));
    commandToClass.put("echo", new DisplayString(this));
    commandToClass.put("cat", new DisplayFile(this));
    commandToClass.put("popd", new PopDirectory(this));
    commandToClass.put("pushd", new PushDirectory(this));
    commandToClass.put("pwd", new DisplayPath(this));
    commandToClass.put("ls", new ListDirectoryContents(this));
    commandToClass.put("cd", new ChangeDirectory(this));
    commandToClass.put("mkdir", new MakeDirectory(this));
    commandToClass.put("exit", new ExitProgram(this));
    commandToClass.put("curl", new RetrieveUrlFile(this));
    commandToClass.put("grep", new DisplayWithSubstring(this));
    commandToClass.put("cp", new CopyFile(this));
    commandToClass.put("mv", new MoveFile(this));
    commandToClass.put("!", new ExecuteFromHistory(this));


    // try to initialize root and current directory (to root)
   rootDir = Directory.createFileSystem();
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
  public List<Object> getCommandHistory(int numberOfCommands) {
    int historySize = commandHistory.size();
    List<Object> a = new ArrayList<Object>();
    // If a number greater then the number of commands in history is given
    // print all commands
    if (numberOfCommands > historySize)
      numberOfCommands = historySize;
    if (numberOfCommands < 0) { // can't print negative commands
      a.add("history usage: history [NUMBER >= 0]");
      a.add(false);
      return a;
    } else { // format output and return
      String ret = "";
      for (int cmdNumber = historySize - numberOfCommands + 1;
          cmdNumber <= historySize; cmdNumber++) {
        ret += cmdNumber + ". " + commandHistory.get(cmdNumber - 1);
        if (cmdNumber != historySize) {
          ret += "\n";
        }
      }
      a.add(ret);
      a.add(true);
      return a;
    }
  }

  /**
   * Prints the command history to stdout.
   * 
   * @return List of commands entered by user, ordered chronologically.
   */
  public List<Object> getCommandHistory() {
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
  
  /**
   * Change the Shell's running status to false (exit)
   * */
  public void turnOffShell() {
    status = false;
  }
  
  /**
   * Get the running status of JShell
   * 
   * @return Running status of JShell
   * */
  public boolean getRunStatus() {
    return status;
  }
  
  /***/
  public void storeDirectory(String path) {
    ds.pushd(path);
  }
  
  /***/
  public List<Object> retrieveDirectory() {
    return ds.popd();
  }
  
  /***/
  public void clearDirectoryStack() {
	  ds.clear();
  }

  public List<Object> getHistoricalCommand(int i) {
    List<Object> a = new ArrayList<Object>();
    boolean bound = (i >= 1 && i <= commandHistory.size());
    a.add(bound ? commandHistory.get(i-1) : 
      "ERROR: Number entered is out of bounds.");
    a.add(bound);
    return a;
  }
  
  public String returnBuffer() {
    return o.getAllOutput();
  }
  
  public void clearBuffer() {
    o.clear();
  }

  public String getError() {
    return o.getStdError();
  }
  
  public void setError(String err) {
    o.setStdError(err);
  }
  
  public void addError(String err) {
    o.addStdError(err);
  }
  
  public String getOutput() {
    return o.getStdOutput();
  }
  
  public void setOutput(String out) {
    o.setStdOutput(out);
  }
  
  public void addOutput(String out) {
    o.addStdOutput(out);
  }
  
  public void redirectOutput(String file, String type) {
    o.redirect(file, type, this.getCurrentDir(), this.getRootDir());
  }
  
  public void clearFileSystem(){
    try {
      rootDir = new Directory("");
    } catch (InvalidNameException e) {
    }
  }
}
