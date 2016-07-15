package exceptions;

/**
 * NameExistsException is used when a file or directory with the same name
 * already exists in the current directory.
 *
 * @author Dhrumil Patel
 */
public class NameExistsException extends Exception {
  /**
   * Serial ID needed when creating exceptions.
   */
  private static final long serialVersionUID = 59L;

  /**
   * Constructor to create a new NameExistsException exception.
   *
   * @param name Message to display when throwing exception
   * @return NameExistsException
   */
  public NameExistsException(String name) {
    super(name + " name is already in use in the current directory.");
  }
}
