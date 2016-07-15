package commands;

import java.util.List;

import driver.*;

/**
 * The Class DisplayStoreString can display a string in the JShell
 */
public class DisplayString implements Command {

  /** Use the session to get the current and root directory */
  private MySession s;

  /**
   * Create new DisplayStoreString instance, to be able to run echo command
   * 
   * @param session Current JShell session attributes
   * @return DisplayStoreString instance
   */
  public DisplayString(MySession session) {
    s = session;
  }

  /**
   * Return man page for echo command.
   * 
   * @return man page for echo command
   */
  @Override
  public void man() {
    s.setOutput("ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n"
        + "NAME\n\t\techo - prints a string to standard output,"
        + " or saves a string to a text\n\t\tfile\n\nSYNOPSIS"
        + "\n\t\techo STRING [>[>] OUTFILE]\n\nDESCRIPTION\n\t\t"
        + "Saves the STRING to the OUTFILE if it's provided, "
        + "otherwise it prints\n\t\tthe STRING to the standard "
        + "output.\n\n\t\tIf the OUTFILE is an existing file in"
        + " the directory, it will overwrite\n\t\tthe contents"
        + " of the OUTFILE with the string, otherwise it will"
        + " create\n\t\ta new file with name OUTFILE .\n\n\t\t"
        + "STRING should be a string of characters surrounded "
        + "by double quotation\n\t\tmarks.");
  }

  /**
   * Error check format of command entered and if correct format entered, then
   * go and try to execute command. Otherwise return error messages.
   * 
   * @param args arguments to process for echo
   * @return error message or string entered/null if stored in file
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 2) {
      s.addError("echo usage: echo STRING (In quotes)");
    } else {
      // Check that the string is surrounded by quotes
      if (!args.get(1).startsWith("\"") || !args.get(1).endsWith("\"")
          || args.get(1).length() < 2) {
        s.addError("ERROR: STRING must be surrounded by double quotations");
      } else {
        exec(args);
      }
    }
  }

  /* 
   * Returns the given string without the quotes
   */
  @Override
  public void exec(List<String> args) {
    String text = args.get(1);
    s.addOutput(text.length() == 2 ? "" : text.substring(1, text.length() - 1));
  }
}
