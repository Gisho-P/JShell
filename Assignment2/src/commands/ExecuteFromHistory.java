package commands;

import java.util.*;

import driver.*;

/**
 * The ExecuteFromHistory class represents the ! command which allows a user to
 * retrieve a call from their history based on the number they enter.
 */
public class ExecuteFromHistory implements Command {

  /**
   * Shell's current attributes
   */
  private MySession s;

  /**
   * Initialize a new ExecuteFromHistory object to be able to execute the !
   * command.
   * 
   * @param session Shell's current attributes
   * @return ExecuteFromHistory object
   */
  public ExecuteFromHistory(MySession session) {
    s = session;
  }

  // TODO: DO THE DAMN BUILD XML FILE GOD
  /**
   * Retrieve the manual page for the ! command and store it to the output
   * buffer.
   */
  @Override
  public void man() {
    s.setOutput("!(1)\t\t\t\tUser Commands\t\t\t\t!(1)"
        + "\n\nNAME\n\t\t! - allows user to execute command "
        + "from history, based\n\t\ton user specified number"
        + "\n\nSYNOPSIS\n\t\t!NUMBER > 0 && <= # of commands run"
        + "\n\nDESCRIPTION\n\t\tThis command allows a user to execute"
        + "a command that they\n\t\thad once run.\n\t\tIn order to do "
        + "so, the user needs to specify which\n\t\tcommand from "
        + "history they\n\t\twant to with a number.\n\t\tE.G. !3 - "
        + "executes the third command typed by a user.");
  }

  /**
   * Check that the correct number of arguments have been entered for this
   * command; a superficial check.
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 2) {
      s.addError("! usage: !NUMBER > 0");
    } else {
      exec(args);
    }
  }

  @Override
  /**
   * Gets the command to execute from history, if possible, executes it and
   * stores the output/error to the appropriate buffer. If not possible, error
   * message is stored to error buffer.
   */
  public void exec(List<String> args) {
    List<Object> set = new ArrayList<Object>();

    try { // try to parse # entered
      int i = Integer.parseInt(args.get(1));
      set = s.getHistoricalCommand(i); // get #'th cmd from history
    } catch (NumberFormatException e) { // number isn't a number; error
      s.addError("! ERROR: Enter a number");
    }

    if (set.size() != 0) { // if a command has been gotten from history
      if ((boolean) set.get(1)) { // store error or execute command
        JShell.commandProcessor((String) set.get(0), s);
      } else {
        s.addError((String) set.get(0));
      }
    }
  }
}
