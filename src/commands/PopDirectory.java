package commands;

import java.util.ArrayList;
import java.util.List;

import driver.MySession;

/**
 * This class represents the command popd, which pops the most recently saved
 * directory on the directory stack and goes to that location.
 * 
 * @author Adnan Bhuiyan
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
  public void man() {
    s.setOutput("POPD(1)\t\t\t\tUser Commands\t\t\t\tPOPD(1)\n\n"
        + "NAME\n\t\tpopd - removes the last directory pushed,"
        + " and changes the current\n\t\tdirectory to that one"
        + "\n\nSYNOPSIS\n\t\tpopd\n\nDESCRIPTION\n\t\tRemoves "
        + "the last directory that was pushed to the directory "
        + "stack,\n\t\tand changes the current working "
        + "directory to this directory.");
  }

  /**
   * Process arguments passed for popd command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return Error message/directory to go to
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 1) { // too many args entered, return an error message
      s.setError("popd usage: popd");
    } else { // process args and complete actions for command
      exec(args);
    }
  }

  /**
   * Return last directory stored in directory stack or error message.
   * 
   * @param args Valid arguments parsed from command
   * @return Directory for shell to go to
   */
  @Override
  public void exec(List<String> args) {
    List<Object> res = s.retrieveDirectory(); // pop last saved directory from stack

    if ((boolean) res.get(1) == false) { // if stack is empty, return error
      s.setError((String) res.get(0));
    } else {
      ChangeDirectory cd = new ChangeDirectory(s);
      List<String> r = new ArrayList<String>();
      r.addAll(args);
      r.add((String) (res.get(0)));
      cd.interpret(r);
    }
  }

}
