package exceptions;

/**
 * The exception class InvalidNameException is invoked when a user tries to give
 * a name for a FileTypes object that is invalid when comparing it with
 * restrictions for characters that can be used.
 */
public class InvalidNameException extends Exception {
  /**
   * Serial version ID needed when creating exceptions.
   */
  private static final long serialVersionUID = 59L;

  /**
   * Return a new InvalidNameException exception with specified message.
   *
   * @param name The invalid name
   * @return InvalidNameException exception
   */
  public InvalidNameException(String name) {
    super("Name " + name + " contains invalid characters");
  }
}
