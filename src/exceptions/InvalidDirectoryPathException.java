package exceptions;

/**
 * InvalidDirectoryPathException is thrown when a filepath that doesn't
 * exist in the filesystem is provided
 */
public class InvalidDirectoryPathException extends Exception {
  /**
   * Serial version ID needed when creating exceptions.
   */
  private static final long serialVersionUID = 59L;

  /**
   * Return an new InvalidDirectroyPathException
   * @param currPath The path that doesn't exist
     */
  public InvalidDirectoryPathException(String currPath) {
    super("There are no files or directories with name " + currPath);
  }
}
