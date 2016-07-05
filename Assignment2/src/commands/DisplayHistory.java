package commands;

import java.util.List;

import driver.MySession;
import structures.Output;

/**
 * The Class DisplayHistory displays the history of commands entered in the
 * shell. It can display all of the commands entered or a chosen last amount of
 * commands can be specified.
 * 
 * @author Adnan Bhuiyan
 * @author Girrshotan Pushparajah
 */
public class DisplayHistory implements Command {

  /**
   * Current session attributes of the shell
   */
  private MySession s;
  private Output out;

  /**
   * Create a new DisplayHistory instance, to be able to run the history
   * command.
   * 
   * @param session Current shell's session attributes
   * @return DisplayHistory instance
   */
  public DisplayHistory(MySession session) {
    s = session; // store current session is instance variable
    out = new Output();
  }

  /**
   * Return man page for history command.
   * 
   * @return manual for History command
   */
  @Override
  public String man() {
    return "HISTORY(1)\t\t\t\tUser Commands\t\t\t\t"
        + "HISTORY(1)\n\nNAME\n\t\thistory - prints out the recent "
        + "commands entered in the shell\n\nSYNOPSIS\n\t\t"
        + "history [NUMBER]\n\nDESCRIPTION\n\t\tPrints out "
        + "the commands entered in the shell from the first "
        + "one entered\n\t\tto the last.\n\n\t\tIf NUMBER is"
        + " specified, will print only the last NUMBER "
        + "amount of\n\t\tcommands that were entered.";
  }

  /**
   * Process arguments passed for history command and determine whether the
   * command was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return Error message/directory to go to
   */
  @Override
  public Output interpret(List<String> args) {
    if (args.size() > 2) { // too many args, error
      return out.withStdError("history usage: history [number]");
    } else { // process args
      return exec(args);
    }
  }

  /**
   * Return history of user commands or error message.
   * 
   * @param args Valid arguments parsed from command
   * @return History or error message
   */
  @Override
  public Output exec(List<String> args) {
    if (args.size() == 2) { // a potential number has been entered
      try { // check if a number has been added
        int arg = Integer.parseInt(args.get(1));
        return s.getCommandHistory(arg);
      } catch (NumberFormatException n) { // number hasn't been, error
        return out.withStdError(
            "history usage: history [number (INTEGER >= 0)]");
      }
    } else { // print all user commands histpry
      return s.getCommandHistory();
    }
  }

}
