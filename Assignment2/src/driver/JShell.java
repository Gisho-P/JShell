// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: songjin8
// UT Student #: 1001545571
// Author: Jin Song
//
// Student2:
// UTORID user_name: pushpa10
// UT Student #: 1001624628
// Author: Girrshotan(Gisho) Pushparajah
//
// Student3:
// UTORID user_name: pateld59
// UT Student #: 1001517815
// Author: Dhrumil Patel
//
// Student4:
// UTORID user_name: bhuiya16
// UT Student #: 1001280026
// Author: Adnan Bhuiyan
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package driver;

import java.util.*;

import commands.Command;
import structures.*;

/**
 * This class provides the JShell that allows the user to enter a command,
 * processes it and then executes it. It will display any output or error
 * messages based on the validity of the command.
 * 
 * @author Adnan Bhuiyan
 * @author Girrshotan Pushparajah
 * @author John Song
 * @author Dhrumil Patel
 */
public class JShell {

  /**
   * Try to execute command entered by the user.
   * 
   * @param args arguments called with command
   * @param session current shell's session attributes
   */
  private static void callFunction(List<String> args, MySession session) {
    if (session.commandToClass.containsKey(args.get(0))) {
      Command c = session.commandToClass.get(args.get(0));
      c.interpret(args);
    } else {
      session.setError("ERROR: Invalid Command.");
    }
  }

  /**
   * Take the user's input and parse it, then pass it to command class to
   * execute the command. Return any output or error messages.
   * 
   * @param cmd user's input into shell
   * @param session session current shell's session attributes
   */
  public static void commandProcessor(String cmd, MySession session) {
    List<String> cmdArgs = new ArrayList<String>(); // Store the output here

    // Splitting the cmd
    cmd = cmd.trim();
    if (cmd.isEmpty() ? false : cmd.charAt(0) == '!') {
      cmdArgs.add(cmd.substring(0, 1));
      cmd = cmd.substring(1);
    }
    if (cmd.contains("\"")) {
      // If cmd contains a string separate it while keeping the STRING
      // as one argument
      String beforeQuotes = cmd.substring(0, cmd.indexOf("\"")).trim();
      String afterQuotes = cmd.substring(cmd.lastIndexOf("\"") + 1).trim();
      beforeQuotes = beforeQuotes.replaceAll("[\\s]+", " ");
      afterQuotes = afterQuotes.replaceAll("[\\s]+", " ");
      // save args before quoted string, string, and args after
      cmdArgs.addAll(Arrays.asList(beforeQuotes.split(" ")));
      cmdArgs.add(cmd.substring(cmd.indexOf("\""), cmd.lastIndexOf("\"") + 1));
      if (!afterQuotes.isEmpty()) {
        cmdArgs.addAll(Arrays.asList(afterQuotes.split(" ")));
      }
    } else { // split string and save as args
      cmd = cmd.replaceAll("[\\s]+", " ");
      cmdArgs = Arrays.asList(cmd.split(" "));
    }

    processForRedirection(session, cmdArgs); // check for redirection
  }

  /**
   * Take processed command arguments and check if user wants to redirect output
   * to a file and execute command accordingly.
   * 
   * @param s Current shell's session attributes
   * @param args command arguments entered from shell
   */
  private static void processForRedirection(MySession s, List<String> args) {
    int argSize = args.size();
    if (argSize >= 3) { // a valid argument will potentially have direction
                        // when there are 3 or more arguments
      boolean containsRedirect = args.get(argSize - 2).equals(">")
          || args.get(argSize - 2).equals(">>");
      if (containsRedirect) { // redirection token was entered
        // process non redirection args, then process output
        callFunction(args.subList(0, argSize - 2), s);
        redirectOutput(args.get(argSize - 2), args.get(argSize - 1), s);
        return;
      }
    }
    callFunction(args, s); // no redirection, process args straight up
  }

  /**
   * Redirect output of command into file specified. Output will either be
   * appended to the file or write (potentially overwrite) the file.
   * 
   * @param type type of redirection (append/write)
   * @param file file that output will be stored
   * @param s current shell's session attributes
   */
  private static void redirectOutput(String type, String file, MySession s) {
    if (!s.getOutput().isEmpty()) { // only redirect output if output exists
      s.redirectOutput(file, type);
    }
  }

  /**
   * Main method for JShell. Accept input from user until exit is entered. Take
   * the user input and parse it into arguments. Take those arguments and try to
   * execute command, return errors or any output returned.
   */
  public static void main(String[] args) {
    String lastCommand = "";
    MySession session = new MySession(new Output()); // new session attributes
                                                     // created
    Scanner input = new Scanner(System.in); // accept input from user

    // Continually accept commands until the command exit is entered
    while (session.getRunStatus()) {
      System.out.print(session.getCurrentDir().getEntirePath() + "$: ");
      lastCommand = input.nextLine();
      session.saveCommand(lastCommand); // Save the command to history
      commandProcessor(lastCommand, session);
      if (session.getRunStatus()) {
        String ret = session.returnBuffer();
        if (!ret.isEmpty()) {
          System.out.println(ret.trim()); // Printing the output
        }
      }
      session.clearBuffer(); // clear output, error buffers
    }
    input.close();
  }

}
