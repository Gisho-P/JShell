package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * The Class DisplayManual takes a command name and returns the documentation
 * manual for the given command.
 */
public class DisplayManual implements Command {

  /**
   * Holds current JShell attributes
   */
  private MySession s;

  /**
   * Create a DisplayManual instance
   * @param session
     */
  public DisplayManual(MySession session) {
    s = session;
  }

  /**
   * Returns the manual for the man command.
   * 
   * @return the manual for the man command
   */
  @Override
  public String man() {
    return "MAN(1)\t\t\t\tUser Commands\t\t\t\tMAN(1)\n\nNAME"
        + "\n\t\tman - prints the documentation for a specified"
        + " command\n\nSYNOPSIS\n\t\tman CMD\n\nDESCRIPTION\n\t\t"
        + "Prints out the documentation for the CMD which "
        + "contains information\n\t\ton how to use the command";
  }

  /**
   * Process arguments passed for man command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return The manual of the given command.
   */
  @Override
  public String interpret(List<String> args) {
    if (args.size() != 2) {
      return "man usage: man CMD";
    } else {
      return exec(args);
    }
  }

  /**
   * Returns the manual of the command given.
   * 
   * @param args Valid arguments parsed from command
   * @return The manual of the command given.
   */
  @Override
  public String exec(List<String> args) {
    try {
      Class<?> c =
          Class.forName("driver." + s.commandToClass.get((String) args.get(1)));
      Object t = c.getConstructor(MySession.class).newInstance(s);
      Method m = c.getMethod("man");
      return (String) m.invoke(t, (Object[]) null);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      return "ERROR: Command does not exist.";
    }
  }

}
