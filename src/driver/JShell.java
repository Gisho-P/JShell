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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import structures.*;

/**
 * This class provides the JShell that allows the user to enter a command,
 * processes it and then executes it. It will display any output or error
 * messages based on the validity of the command.
 * 
 * @author Adnan Bhuiyan
 * @author Girrshotan Pushparajah
 * @author John Song
 */
public class JShell {

  /**
   * Returns output/error message from Shell based on command entered.
   * 
   * @param args arguments called with command
   * @param session current shell's session attributes
   * @return output/error message of command class
   */
  private static Output callFunction(List<String> args, MySession session) {
    Output out = new Output();
    try { // call command class w/ arguments and execute functions
      Class<?> c =
          Class.forName("commands." + session.commandToClass.get(args.get(0)));
      Object t = c.getConstructor(MySession.class).newInstance(session);
      Method m = c.getMethod("interpret", List.class);
      out = (Output) m.invoke(t, args);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      out.addStdError("ERROR: Invalid Command."); // if execution failed, give error
    }

    return out;
  }

  /**
   * Take the user's input and parse it, then pass it to command class to
   * execute the command. Return any output or error messages.
   * 
   * @param cmd user's input into shell
   * @param session session current shell's session attributes
   * @return output/error message of command class
   */
  public static String commandProcessor(String cmd, MySession session) {
    // Store the output here
    List<String> cmdArgs = new ArrayList<String>();

    // Splitting the cmd
    cmd = cmd.trim();
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
    Output out = new Output();
    if(cmdArgs.size() < 3 ? false : (cmdArgs.get(cmdArgs.size() - 2).equals(">")
        || cmdArgs.get(cmdArgs.size() - 2).equals(">>"))){
      out = callFunction(cmdArgs.subList(0, cmdArgs.size() - 2), session);
      if(!out.getStdOutput().isEmpty())
        return reDirectOutput(cmdArgs, session, out);
    } else
      out = callFunction(cmdArgs, session);
    return out.getAllOutput();
  }
  
  public static String reDirectOutput(List<String> cmdArgs, MySession session,
      Output out){
    if(cmdArgs.get(cmdArgs.size() - 2).equals(">"))
      out.redirect(session, true, cmdArgs.get(cmdArgs.size() - 1),
          out.getStdOutput());
    else{
      if(cmdArgs.get(cmdArgs.size() - 2).equals(">>"))
        out.redirect(session, false, cmdArgs.get(cmdArgs.size() - 1),
            out.getStdOutput());
      else
        out.addStdError("redirection usage: cmd [args] [>][>] outFile ");
    }
      return out.getStdError();
  }

  /**
   * Main method for JShell. Accept input from user until exit is entered. Take
   * the user input and parse it into arguments. Take those arguments and try to
   * execute command, return errors or any output returned.
   */
  public static void main(String[] args) {
    String lastCommand = "";
    MySession session = new MySession(); // new session attributes created
    Scanner input = new Scanner(System.in); // accept input from user

    // Continually accept commands until the command exit is entered
    // exit can precede or follow any amount of white spaces and can have
    // anything after exit and a white space
    while (lastCommand.matches("[\\s]*exit[\\s]*") == false) {
      System.out.print(session.getCurrentDir().getEntirePath() + "$: ");
      lastCommand = input.nextLine();
      // Save the command to history
      session.saveCommand(lastCommand);
      if (lastCommand.matches("[\\s]*exit[\\s]*") == false) {
        // Printing the output
        String ret = commandProcessor(lastCommand, session);
        if (ret != null && ret != "") {
          System.out.println(ret.trim());
        }
      }
    }

    input.close();
  }

}
