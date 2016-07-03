package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This class represents the command pushd, which saves the current directory to
 * the directory stack and goes to the location specified in the argument.
 * 
 * @author Adnan Bhuiyan
 */
public class PushDirectory implements Command {

  /**
   * Holder of current JShell's session attributes
   */
  private MySession s;
  private Output out;

  /**
   * Returns a PushDirectory object that represents the pushd command. Now that
   * the command has been created, functions available to this command class can
   * be done. Store current session in class to be used when needed.
   * 
   * @param session the current Shell session's attributes
   * @return the push directory command class
   */
  public PushDirectory(MySession session) {
    s = session; // store current session
    out = new Output();
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
   */
  @Override
  public Output interpret(List<String> args) {
    if (args.size() != 2) { // incorrect # of args, error message
      return out.withStdError("pushd usage: pushd DIR");
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
   */
  @Override
  public Output exec(List<String> args) {
    String currDir = s.getCurrentDir().getEntirePath();
    try { // Call interpret method for cd command to change directory
      Class<?> c = Class.forName("driver.ChangeDirectory");
      Object t = c.getConstructor(MySession.class).newInstance(s);
      Method m = c.getMethod("interpret", List.class);
      out = (Output) m.invoke(t, args);
      if (out.getStdOutput() == "") {
        DirStack.pushd(currDir);
      }
      return out;
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace(); // print stack trace if failed, diagnose error
    }
    return out;
  }

}
