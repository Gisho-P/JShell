package commands;

import java.util.List;

import driver.MySession;

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

  /**
   * Create DisplayManual instance to run man command.
   * 
   * @param session Current JShell's session attributes
   * @return DisplayManual instance
   */
  public DisplayManual(MySession session) {
    s = session;
  }

  /**
   * Return man page for man command.
   * 
   * @return man page for man command
   */
  @Override
  public void man() {
    s.setOutput("MAN(1)\t\t\t\tUser Commands\t\t\t\tMAN(1)\n\nNAME"
        + "\n\t\tman - prints the documentation for a specified"
        + " command\n\nSYNOPSIS\n\t\tman CMD\n\nDESCRIPTION\n\t\t"
        + "Prints out the documentation for the CMD which "
        + "contains information\n\t\ton how to use the command");
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
  public void interpret(List<String> args) {
    if (args.size() != 2) {
      s.setError("man usage: man CMD");
    } else {
      exec(args);
    }
  }

  /**
   * Try to retrieve man page for given command in args and return results.
   * 
   * @param args command to get man page for
   * @return man page for command or error
   */
  @Override
  public void exec(List<String> args) {
    // check for !# and get #'th command
    if (args.get(1).charAt(0) == '!' && args.get(1).length() != 1) {
      try {
        int a = Integer.parseInt(args.get(1).substring(1)); // convert # to int
        List<Object> command = s.getHistoricalCommand(a); // get #'th command

        if ((boolean) command.get(1)) {
          // get #'th command's command
          String[] newCmd = ((String) command.get(0)).trim()
              .replaceAll("[\\s]+", " ").split(" ");
          // if there's a ! then man !, otherwise man cmd (potentially error)
          args.set(1, newCmd[0].charAt(0) == '!' ? "!" : newCmd[0]);
        } else { // couldn't get #'th command, store error and leave
          s.addError((String) command.get(0));
          return;
        }
      } catch (NumberFormatException e) { // supposed # wasn't a #
        s.addError("ERROR: Invalid number entered for !");
        return;
      }
    }
    if (s.commandToClass.containsKey((String) args.get(1))) {
      Command c = s.commandToClass.get((String) args.get(1)); // get cmd
      c.man(); // call the cmd's man function
    } else {
      s.addError("ERROR: Command does not exist.");
    }
  }
}
