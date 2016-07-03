package driver;

import java.util.List;

public class DisplayPath implements Command {

  private MySession s;
  private Output out;

  public DisplayPath(MySession session) {
    s = session;
    out = new Output();
  }

  /**
   * Returns the manual for the pwd command.
   * 
   * @return the manual for the pwd command
   */
  @Override
  public String man() {
    return "PWD(1)\t\t\t\tUser Commands\t\t\t\tPWD(1)\n\nNAME"
        + "\n\t\tpwd - prints the current directory\n\n"
        + "SYNOPSIS\n\t\tpwd\n\nDESCRIPTION\n\t\tPrints the"
        + " current working directories full path to standard " + "output";
  }

  /**
   * Process arguments passed for pwd command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return The path of the given file.
   */
  @Override
  public Output interpret(List<String> args) {
    return exec(args);
  }

  /**
   * Returns the path of the given file.
   * 
   * @param args Valid arguments parsed from command
   * @return The path of the given file.
   */
  @Override
  public Output exec(List<String> args) {
    if (args.size() != 1) {
      return out.withStdError("pwd usage: pwd");
    } else {
      return out.withStdOutput(s.getCurrentDir().getEntirePath(), false);
    }
  }

}
