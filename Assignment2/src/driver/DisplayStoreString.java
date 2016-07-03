package driver;

import java.util.ArrayList;
import java.util.List;

import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes.InvalidName;


/**
 * The Class DisplayStoreString can either display a string in the JShell or
 * store it in a new or existing file.
 */
public class DisplayStoreString implements Command {

  /** Use the session to get the current and root directory */
  private MySession s;
  private Output out;

  /**
   * Create new DisplayStoreString instance, to be able to run echo command
   * 
   * @param session Current JShell session attributes
   * @return DisplayStoreString instance
   */
  public DisplayStoreString(MySession session) {
    s = session;
    out = new Output();
  }

  /**
   * Return man page for echo command.
   * 
   * @return man page for echo command
   */
  @Override
  public String man() {
    return "ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n"
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
        + "by double quotation\n\t\tmarks.";
  }

  /**
   * Error check format of command entered and if correct format entered, then
   * go and try to execute command. Otherwise return error messages.
   * 
   * @param args arguments to process for echo
   * @return error message or string entered/null if stored in file
   */
  @Override
  public Output interpret(List<String> args) {
    if (args.size() != 4 && args.size() != 2) {
      return out.withStdError("echo usage: STRING [>[>] [OUTFILE]");
    } else {
      // Check that the string is surrounded by quotes
      if (args.get(1).startsWith("\"") && args.get(1).endsWith("\"")) {
        if (args.get(1).length() == 2) { // Set to null if string is empty
          args.set(1, null);
        } else { // Remove quotes
          args.set(1, args.get(1).substring(1, args.get(1).length() - 1));
        }
      } else {
        return out.withStdError(
            "ERROR: STRING must be surrounded by double quotations");
      }
      if (args.size() == 2) { // return the string if no file is given
        return out.withStdOutput(
            args.get(1) == null ? "" : (String) args.get(1), false);
      } else {
        List<String> retArgs = new ArrayList<String>();
        retArgs.add(args.get(1));
        retArgs.add(args.get(3));
        if (args.get(2).equals(">>")) { // Append to file
          retArgs.add("false");
          return exec(retArgs);
        } else {
          if (args.get(2).equals(">")) { // Write to file
            retArgs.add("true");
            return exec(retArgs);
          } else {
            return out.withStdError("echo usage: STRING [>[>] [OUTFILE]");
          }
        }
      }
    }
  }

  @Override
  public Output exec(List<String> args) {
    // Redirect the string to a file
    out.redirect(s, Boolean.valueOf(args.get(2)), args.get(1), args.get(0));
    return out;
  }

}
