package commands;

import java.util.List;

import driver.MySession;

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
  }

  /**
   * Sets the output to the manual for the pushd command.
   */
  @Override
  public void man() {
    s.setOutput("PUSHD(1)\t\t\t\tUser Commands\t\t\t\tPUSHD(1)\n\n"
        + "NAME\n\t\tpushd - saves the current working "
        + "directory to the stack and changes\n\t\tnew working directory"
        + " to DIR\n\nSYNOPSIS\n\t\tpushd DIR\n\n"
        + "DESCRIPTION\n\t\tSaves the current working "
        + "directory to the top of the directory\n\t\tstack. Changes"
        + " working directory to path specified in DIR.\n\t\t"
        + "Directory stack follows stack behaviour (LIFO).");
  }

  /**
   * Process arguments passed for pushd command and determine whether the
   * command was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 2) { // incorrect # of args, error message
      s.setError("pushd usage: pushd DIR");
    } else {
      exec(args); // process args, complete actions for command
    }
  }

  /**
   * Store current directory to directory stack and return null or return an
   * error message if issue occurs.
   * 
   * @param args Valid arguments parsed from command
   */
  @Override
  public void exec(List<String> args) {
    String currDir = s.getCurrentDir().getEntirePath();
    ChangeDirectory cd = new ChangeDirectory(s);
    cd.interpret(args);
    if (s.getError().isEmpty()) {
      s.storeDirectory(currDir);
    }
  }

}
