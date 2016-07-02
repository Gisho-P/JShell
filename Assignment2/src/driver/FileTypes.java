package driver;

import java.util.regex.Pattern;

/**
 * This abstract class FileTypes provides the skeleton for all files and
 * directories created in this file system. The skeleton methods allow for a
 * user to get and set the name of a file and check if it's a valid name.
 * 
 * @author Dhrumil Patel
 */
public abstract class FileTypes {

  /**
   * Name of file/directory created
   */
  private String name;

  /**
   * Create an object with superclass FileTypes with a name.
   *
   * @param name Name of the FileType object
   * @return new FileTypes object, subclass specifies which type
   * @throws InvalidName when invalid name is given
   */
  public FileTypes(String name) throws InvalidName {
    if (isValid(name)) // valid name, assign it to name property
      this.name = name;
    else // throw exception for invalid name
      throw new InvalidName("Name contains invalid characters");
  }

  /**
   * Get the name of a file/directory.
   *
   * @return Name of the FileTypes object
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the FileTypes object.
   *
   * @param name The new name to set
   * @throws InvalidName when invalid name is given
   */
  public void setName(String name) throws InvalidName {
    if (isValid(name)) // check for name validity
      this.name = name;
    else
      throw new InvalidName("Name " + name + " contains invalid characters");
  }

  /**
   * Determine if the given name is a valid name for Files and Directories.
   *
   * @param name Potential name of file
   * @return whether or not file name is valid
   */
  public boolean isValid(String name) {
    // .. and . are invalid names
    if (name.equals("..") || name.equals(".")) {
      return false;
      // If the name doesn't contain all alphanumeric character return false
    } else {
      Pattern p = Pattern.compile("[^a-zA-Z0-9.]");
      return !p.matcher(name).find();
    }
  }

  /**
   * The exception class InvalidName is invoked when a user tries to give a name
   * for a FileTypes object that is invalid when comparing it with restrictions
   * for characters that can be used.
   * 
   * @author Dhrumil Patel
   * 
   */
  public class InvalidName extends Exception {
    /**
     * Serial version ID needed when creating exceptions.
     */
    private static final long serialVersionUID = 59L;

    /**
     * Return a new InvalidName exception with specified message.
     * 
     * @param message Message to display when exception is thrown
     * @return InvalidName exception
     */
    public InvalidName(String message) {
      super(message);
    }
  }


}
