package driver;

import java.util.List;

/**
 * The Class DisplayHistory displays the history of commands entered in the
 * shell. It can display all of the commands entered or a chosen last amount
 * of commands can be specified.
 */
public class DisplayHistory implements Command {

  private MySession s;

  public DisplayHistory(MySession session) {
    s = session;
  }

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

  @Override
  public String interpret(List<String> args) {
    if (args.size() > 2) {
      return "history usage: history [number]";
    } else {
      return exec(args);
    }
  }

  @Override
  public String exec(List<String> args) {
    if (args.size() == 2) {
      try {
        int arg = Integer.parseInt(args.get(1));
        return s.printCommandHistory(arg);
      } catch (NumberFormatException n) {
        return "history usage: history [number (INTEGER >= 0)]";
      }
    } else {
      return s.printCommandHistory();
    }
  }

}
