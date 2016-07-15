package commands;

import java.util.List;

import driver.MySession;

/**
 * This class represents the manual for the exit command, the exit command
 * execution is done immediately in the shell.
 * 
 * @author Adnan Bhuiyan
 */
public class ExitProgram implements Command {

  /**
   * Current JShell attributes
   */
  private MySession s;

  /**
   * Returns a ExitProgram object that represents the exit command. This class
   * is only used for exit's manual.
   * 
   * @param session the current Shell session's attributes
   */
  public ExitProgram(MySession session) {
    s = session;
  }

  /**
   * Sets the output as the manual for the exit command.
   */
  @Override
  public void man() {
    s.setOutput("EXIT(1)\t\t\t\tUser Commands\t\t\t\tEXIT(1)\n\n"
        + "NAME\n\t\texit - terminates the shell\n\nSYNOPSIS\n\t\t"
        + "exit\n\nDESCRIPTION\n\t\tTerminates the users"
        + " session with the shell which removes their ability"
        + "\n\t\tto process any more commands.");
  }

  /**
   * Turn off the shell or display an error message
   */
  @Override
  public void exec(List<String> args) {
    if (args.size() == 1) {
      s.turnOffShell();
    } else {
      s.setError("exit usage: exit");
    }
  }

  /**
   * No arguments to process, check validity in exec() method
   */
  @Override
  public void interpret(List<String> args) {
    exec(args);
  }

}
