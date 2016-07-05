package commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import driver.MySession;
import structures.Output;

/**
 * The Class DisplayManual takes a command name and returns the documentation
 * manual for the given command.
 * 
 * @author Adnan Bhuiyan
 * @author Girrshotan Pushparajah
 */
public class DisplayManual implements Command {

  /**
   * Current session attributes of shell.
   */
  private MySession s;
  private Output out;

  /**
   * Create DisplayManual instance to run man command.
   * 
   * @param session Current JShell's session attributes
   * @return DisplayManual instance
   */
  public DisplayManual(MySession session) {
    s = session;
    out = new Output();
  }

  /**
   * Return man page for man command.
   * 
   * @return man page for man command
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
   * Check the formatting of the command entered, and make sure it matches up
   * with the correct format. If so, return man page. Otherwise, return an error
   * message.
   * 
   * @param args Arguments to process with man command
   * @return error message or man page
   */
  @Override
  public Output interpret(List<String> args) {
    if (args.size() != 2) {
      return out.withStdError("man usage: man CMD");
    } else {
      return exec(args);
    }
  }

  /**
   * Try to retrieve man page for given command in args and return results.
   * 
   * @param args command to get man page for
   * @return man page for command or error
   */
  @Override
  public Output exec(List<String> args) {
    try {
      Class<?> c =
          Class.forName("commands." + s.commandToClass.get((String) args.get(1)));
      Object t = c.getConstructor(MySession.class).newInstance(s);
      Method m = c.getMethod("man");
      return out.withStdOutput((String) m.invoke(t, (Object[]) null), true);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      return out.withStdError("ERROR: Command does not exist.");
    }
  }

}
