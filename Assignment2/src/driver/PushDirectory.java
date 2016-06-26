package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This class represents the command pushd, which saves the current directory to
 * the directory stack and goes to the location specified in the argument.
 * 
 * @author Adnan Bhuiyan
 * @see List
 * @see reflect#Method
 * @see reflect#InvocationTargetException
 * @see Command
 */
public class PushDirectory implements Command {

  /**
   * Holder of current JShell's session attributes
   */
  private MySession s;

  /**
   * Returns a PushDirectory object that represents the pushd command. Now that
   * the command has been created, functions available to this command class can
   * be done. Store current session in class to be used when needed.
   * 
   * @param session the current Shell session's attributes
   * @return the push directory command class
   * @see MySession
   */
  public PushDirectory(MySession session) {
    s = session; // store current session
  }

  /**
   * Returns the manual for the pushd command.
   * 
   * @return the manual for the pushd command
   */
  @Override
  public String man() {
    return "PUSHD(1)\t\t\t\tUser Commands\t\t\t\tPUSHD(1)\n\n"
        + "NAME\n\t\tpushd - saves the current working "
        + "directory to the stack and changes\n\t\tnew working directory"
        + " to DIR\n\nSYNOPSIS\n\t\tpushd DIR\n\n"
        + "DESCRIPTION\n\t\tSaves the current working "
        + "directory to the top of the directory\n\t\tstack. Changes"
        + " working directory to path specified in DIR.\n\t\t"
        + "Directory stack follows stack behaviour (LIFO).";
  }

  /**
   * Process arguments passed for pushd command and determine whether the
   * command was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return Error message/null
   * @see List
   */
  @Override
  public String interpret(List<String> args) {
    if (args.size() != 2) { // incorrect # of args, error message
      return "pushd usage: pushd DIR";
    } else {
      return exec(args); // process args, complete actions for command
    }
  }

  /**
   * Store current directory to directory stack and return null or return an
   * error message if issue occurs.
   * 
   * @param args Valid arguments parsed from command
   * @return Error message (or null if successful)
   * @see DirStack
   * @see Class
   * @see Method
   * @see List
   * @see ClassNotFoundException
   * @see InstantiationException
   * @see IllegalAccessException
   * @see NoSuchMethodException
   * @see SecurityException
   * @see IllegalArgumentException
   */
  @Override
  public String exec(List<String> args) {
    DirStack.pushd(s.getCurrentDir().getEntirePath()); // push directory to
                                                       // stack
    try { // Call interpret method for cd command to change directory
      Class<?> c = Class.forName("driver.ChangeDirectory");
      Object t = c.getConstructor(MySession.class).newInstance(s);
      Method m = c.getMethod("interpret", List.class);
      return (String) m.invoke(t, args);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

}