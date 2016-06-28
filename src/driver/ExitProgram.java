package driver;

import java.util.List;

/**
 * This class represents the manual for the exit command, the exit command
 * execution is done immediately in the shell.
 * 
 * @author Adnan Bhuiyan
 * @see List
 */
public class ExitProgram implements Command {

  /**
   * Returns a ExitProgram object that represents the exit command. This class
   * is only used for exit's manual.
   * 
   * @param session the current Shell session's attributes
   */
  public ExitProgram(MySession session) {}

  /**
   * Returns the manual for the exit command.
   * 
   * @return the manual for the exit command
   */
  @Override
  public String man() {
    return "EXIT(1)\t\t\t\tUser Commands\t\t\t\tEXIT(1)\n\n"
        + "NAME\n\t\texit - terminates the shell\n\nSYNOPSIS\n\t\t"
        + "exit\n\nDESCRIPTION\n\t\tTerminates the users"
        + " session with the shell which removes their ability"
        + "\n\t\tto process any more commands.";
  }

  /**
   * @deprecated
   */
  @Override
  public String exec(List<String> args) {
    return null;
  }

  /**
   * @deprecated
   */
  @Override
  public String interpret(List<String> args) {
    return null;
  }

}
