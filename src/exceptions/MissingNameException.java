package exceptions;

/**
 * MissingNameException is used when a user tries to remove a file or directory
 * that doesn't exist.
 *
 * @author Dhrumil Patel
 */
public class MissingNameException extends Exception {
  /**
   * Serial ID needed when creating exceptions.
   */
  private static final long serialVersionUID = 59L;

  /**
   * Constructor to create a new MissingNameException exception.
   *
   * @param name Name that is missing
   * @return MissingNameException
   */
  public MissingNameException(String name) {
    super(name + " does not exist in the current directory");
  }
}
