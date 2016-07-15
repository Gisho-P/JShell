package driver;

import java.util.*;

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
   */
  private boolean status;

  /**
   * Session's directory stack
   */
  private DirStack ds = new DirStack();

  /**
   * JShell's output/error buffer
   */
  private Output o;

  /**
   * Set up a new MySession object with the default attributes for the current
   * running of the Shell. This includes setting up the output buffer, command
   * storage, initial run status and root directory. Also it should be mentioned
   * that the map between the commands and their corresponding classes is
   * created.
   * 
   * @param o Output/Error buffer
   * @return New MySession object
   */
  public MySession(Output o) {
    this.o = o; // initialize shell output buffer
    status = true; // set shell status
    commandHistory = new ArrayList<String>(); // initialize command storage

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
   * Change the Shell's running status to false (exit)
   */
  public void turnOffShell() {
    status = false;
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
   * Return either a list of commands entered (length specified by parameter) or
   * error message.
   *
   * @param numberOfCommands the number of commands to be printed from history
   * @return The amount of entries in the history of commands or error message,
   *         along with success status
   */
  public List<Object> getCommandHistory(int numberOfCommands) {
    int historySize = commandHistory.size();
    List<Object> a = new ArrayList<Object>();

    // print all commands if # of commands > command history size
    if (numberOfCommands > historySize)
      numberOfCommands = historySize;

    if (numberOfCommands < 0) { // can't print negative commands, error message
      a.add("history usage: history [NUMBER >= 0]");
    } else { // format output (cmd history)
      String ret = "";
      for (int cmdNumber = historySize - numberOfCommands
          + 1; cmdNumber <= historySize; cmdNumber++) {
        ret += cmdNumber + ". " + commandHistory.get(cmdNumber - 1);
        if (cmdNumber != historySize) {
          ret += "\n";
        }
      }
      a.add(ret);
    }

    a.add(numberOfCommands < 0 ? false : true); // store success status
    return a;
  }

  /**
   * Retrieve all commands from the history of commands entered and return them.
   * 
   * @return The history of commands or error message, along with success status
   */
  public List<Object> getCommandHistory() {
    return getCommandHistory(commandHistory.size()); // call
                                                     // printCommandHistory
                                                     // with the max number as
                                                     // a parameter
  }

  /**
   * Based on parameter i, get i'th command entered from history or error
   * message if i is out of bounds
   * 
   * @param i command to get
   * @return command or error message, success status
   */
  public List<Object> getHistoricalCommand(int i) {
    List<Object> a = new ArrayList<Object>();
    boolean bound = (i >= 1 && i <= commandHistory.size());
    a.add(bound ? commandHistory.get(i - 1)
        : "ERROR: Number entered is out of bounds.");
    a.add(bound);
    return a;
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
   * Get the running status of JShell
   * 
   * @return Running status of JShell
   */
  public boolean getRunStatus() {
    return status;
  }

  /**
   * Store path entered into directory stack
   */
  public void storeDirectory(String path) {
    ds.pushd(path);
  }

  /**
   * Retrieve last stored directory from directory stack or error message if
   * there is nothing stored or path stored was incorrect.
   * 
   * @return Path stored or error message, error status
   */
  public List<Object> retrieveDirectory() {
    return ds.popd();
  }

  /**
   * Clear directory stack.
   */
  public void clearDirectoryStack() {
    ds.clear();
  }

  /**
   * Return contents of the error buffer.
   */
  public String getError() {
    return o.getStdError();
  }

  /**
   * Store err into error buffer.
   * 
   * @param err Error message to store
   */
  public void setError(String err) {
    o.setStdError(err);
  }

  /**
   * Append err to the error buffer.
   * 
   * @param err Error message to store
   */
  public void addError(String err) {
    o.addStdError(err);
  }

  /**
   * Return the contents of the output buffer.
   */
  public String getOutput() {
    return o.getStdOutput();
  }

  /**
   * Set the contents of the output buffer to out.
   * 
   * @param out Output to store into buffer
   */
  public void setOutput(String out) {
    o.setStdOutput(out);
  }

  /**
   * Append out to the contents of the output buffer.
   * 
   * @param out Output to add to the buffer
   */
  public void addOutput(String out) {
    o.addStdOutput(out);
  }

  /**
   * Redirect (Append/Write) output buffer into file specified.
   * 
   * @param file Path to file where contents will be stored
   * @param type Redirection type (Append/ Write)
   */
  public void redirectOutput(String file, String type) {
    o.redirect(file, type, this.getCurrentDir(), this.getRootDir());
  }

  /**
   * Reset file system to the state it was when first initialized.
   */
  public void clearFileSystem() {
    try {
      rootDir = new Directory("");
      currentDir = rootDir;
    } catch (InvalidNameException e) {
    }
  }
    
  /**
   * Return the output and error buffers.
   */
  public String returnBuffer() {
    return o.getAllOutput();
  }

  /**
   * Clear the output and error buffers.
   */
  public void clearBuffer() {
    o.clear();
  }
}
