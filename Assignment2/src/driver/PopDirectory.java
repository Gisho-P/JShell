package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the command popd, which pops the most recently saved
 * directory on the directory stack and goes to that location.
 * 
 * @author Adnan Bhuiyan
 * @see List
 * @see reflect#Method
 * @see reflect#InvocationTargetException
 * @see Command
 */
public class PopDirectory implements Command {

  /**
   * Holder of current JShell's session attributes
   */
  private MySession s;

  /**
   * Returns a PopDirectory object that represents the popd command. Now that
   * the command has been created, functions available to this command class can
   * be done. Store current session in class to be used when needed.
   * 
   * @param session the current Shell session's attributes
   * @return the pop directory command class
   * @see MySession
   */
  public PopDirectory(MySession session) {
    s = session; // store current session
  }

  /**
   * Returns the manual for the popd command.
   * 
   * @return the manual for the popd command
   */
  @Override
  public String man() {
    return "POPD(1)\t\t\t\tUser Commands\t\t\t\tPOPD(1)\n\n"
        + "NAME\n\t\tpopd - removes the last directory pushed,"
        + " and changes the current\n\t\tdirectory to that one"
        + "\n\nSYNOPSIS\n\t\tpopd\n\nDESCRIPTION\n\t\tRemoves "
        + "the last directory that was pushed to the directory "
        + "stack,\n\t\tand changes the current working "
        + "directory to this directory.";
  }

  /**
   * Process arguments passed for popd command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return Error message/directory to go to
   * @see List
   */
  @Override
  public String interpret(List<String> args) {
    if (args.size() != 1) { // too many args entered, return an error message
      return "popd usage: popd";
    } else { // process args and complete actions for command
      return exec(args);
    }
  }

  /**
   * Return last directory stored in directory stack or error message.
   * 
   * @param args Valid arguments parsed from command
   * @return Directory for shell to go to
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
    List<Object> res = DirStack.popd(); // pop last saved directory from stack

    if ((boolean) res.get(1) == false) { // if stack is empty, return error
      return (String) res.get(0);
    } else { // use reflection to change directory to popped directory
      try {
        Class<?> c = Class.forName("driver.ChangeDirectory");
        Object t = c.getConstructor(MySession.class).newInstance(s);
        Method m = c.getMethod("interpret", List.class);
        List<String> r = new ArrayList<String>();
        r.addAll(args);
        r.add((String) res.get(0));
        return (String) m.invoke(t, r);
      } catch (ClassNotFoundException | InstantiationException
          | IllegalAccessException | NoSuchMethodException | SecurityException
          | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

}
