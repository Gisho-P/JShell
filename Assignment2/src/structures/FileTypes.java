package structures;

import java.util.regex.Pattern;

import exceptions.*;

/**
 * This abstract class FileTypes provides the skeleton for all files and
 * directories created in this file system. The skeleton methods allow for a
 * user to get and set the name of a file and check if it's a valid name.
 */
public abstract class FileTypes {

  /**
   * Stores the parent directory of the current FileType
   */
  private Directory parent;

  /**
   * Name of file/directory created
   */
  private String name;

  /**
   * Create an object with superclass FileTypes with a name.
   *
   * @param name Name of the FileType object
   * @return new FileTypes object, subclass specifies which type
   * @throws InvalidNameException when invalid name is given
   */
  public FileTypes(String name) throws InvalidNameException {
    if (isValid(name)) // valid name, assign it to name property
      this.name = name;
    else // throw exception for invalid name
      throw new InvalidNameException(name);
  }

  /**
   * Get the parent Directory of current directory.
   *
   * @return The parent Directory of the current directory
   */
  public Directory getParent() {
    return parent;
  }

  /**
   * Set the parent of the current FileType object.
   *
   * @param dir The directory to make the parent of this object
   * @throws InvalidSetParentException Thrown when a user tries to set the
   *         parent directory of this FileType object but the parent directory
   *         doesn't already contain this FileType object
   */
  public void setParent(Directory dir) throws InvalidSetParentException {
    // Only setParent if the given directory already contains this object
    // as a child. This is a safety measure to ensure the child points to the
    // correct parent. This means the child needs to be added first before
    // adding a parent

    if (dir != null) {
      boolean childExists = false;
      for (FileTypes ft : dir.getChildren()) {
        if (ft == this) {
          this.parent = dir;
          childExists = true;
          break;
        }
      }

      if (!childExists)
        throw new InvalidSetParentException();
    } else
      this.parent = null;
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
   * @throws InvalidNameException when invalid name is given
   * @throws NameExistsException Thrown when the new already exists in the
   *         parent directory
   */
  public void setName(String name)
      throws InvalidNameException, NameExistsException {
    if (isValid(name)) // check for name validity
    {
      if (this.getParent() == null)
        this.name = name;
      else if (this.getParent().nameExists(name) == -1)
        this.name = name;
      else
        throw new NameExistsException(name);
    } else
      throw new InvalidNameException(name);
  }

  /**
   * Determine if the src object is a parent or equivalent object to dest
   * 
   * @param src The fileType object to add
   * @param dest The fileType object to add to
   * @return Whether or not the src is a parent or equivalent object to dest
   */
  public static boolean isInvalidAddition(FileTypes src, FileTypes dest) {
    boolean isInvalid = false;
    // If src is the same as one of the parent objects then the addition is
    // invalid
    while (dest != null) {
      if (src == dest) {
        isInvalid = true;
        break;
      }
      dest = dest.getParent();
    }
    return isInvalid;
  }

  /**
   * Create a deep copy of the fileType object and all of its contents
   *
   * @param ft FileType to be copied
   * @return The copied fileType object
   */
  public static FileTypes deepCopy(FileTypes ft) {
    if (ft instanceof File)
      return File.copy((File) ft);
    else {
      Directory newDir = null;
      // This should always work since the original directory couldn't have
      // been created with an invalidName
      try {
        newDir = new Directory(ft.getName());
      } catch (InvalidNameException invalidName) {
        invalidName.printStackTrace();
      }

      for (FileTypes fileType : ((Directory) ft).getChildren()) {
        // Depending on the type of the File, add a copy of it ot the
        // new directory
        try {
          newDir.add(deepCopy(fileType));
        } catch (NameExistsException e) {
          e.printStackTrace();
        } catch (InvalidAdditionException invalidAddition) {
          invalidAddition.printStackTrace();
        }
      }
      return newDir;
    }
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
      // If the name doesn't contain all alphanumeric character return
      // false
    } else {
      Pattern p = Pattern.compile("[^a-zA-Z0-9._-]");
      return !p.matcher(name).find();
    }
  }
}
