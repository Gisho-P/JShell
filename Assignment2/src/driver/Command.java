package driver;

import java.util.List;

/**
 * This interface provides the shared behaviour for classes that represent each
 * command able to be used in the JShell. Each command essentially has a manual,
 * a correct format that is processed and the actual method of completing the
 * command.
 * 
 * @author Adnan Bhuiyan
 * @see List
 */
public interface Command {

  /**
   * Return the manual for a command entered by a user or an error message if
   * the command does not exist.
   * 
   * @return The manual of a specified command
   */
  public String man();

  /**
   * Error check the command entered and continue processing if it's valid.
   * 
   * @return An error message or displayed amount from a command
   */
  public String interpret(List<String> args);

  /**
   * Complete the function of the command and return any necessary output/error
   * message.
   * 
   * @param args Arguments used by function to complete command
   */
  public String exec(List<String> args);
}
