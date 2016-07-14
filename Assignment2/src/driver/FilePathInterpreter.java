package driver;

import java.util.ArrayList;
import java.util.Iterator;

import structures.*;

/**
 * The FilePathInterpreter takes a path and processes it from the current
 * directory until the end of the path is reached and returns a FileTypes object
 * in which operations specified by the user command can be done.
 */
public class FilePathInterpreter {

  /**
   * Finds the location and returns the existing dir with the input path.
   *
   * @param init The init dir to move from.
   * @param path The path relative to the init dir.
   * @return The dir at that valid path.
   * @throws InvalidDirectoryPathException
   */
  public static FileTypes interpretPath(Directory init, String path)
      throws InvalidDirectoryPathException {
    if (!path.equals("")) {

      // Checks to see if our first is "/" i.e. is it back to root?
      if (path.charAt(0) == '/') {
        while (init.getParent() != null) {
          init = init.getParent();
        }
        return interpretPathRecursive(init, path.substring(1, path.length()));
      }

      return interpretPathRecursive(init, path);
    }
    return init;
  }

  /**
   * Recursively goes through a file path and finds the dir of file at that
   * path.
   *
   * @param init The current dir the path is relative to.
   * @param currPath The current path of the dir/file.
   * @return The dir or file at that path.
   * @throws InvalidDirectoryPathException
   */
  private static FileTypes interpretPathRecursive(Directory init,
      String currPath) throws InvalidDirectoryPathException {
    // Checks to see whether the current path is nothing if it is we found
    currPath = removeLeadingSlashes(currPath);
    if (!currPath.equals("")) {
      String[] splitPath = currPath.split("/");
      // Same case as before except with ..
      if (splitPath[0].equals("..")) {
        return interpretDoubleDots(init, currPath, splitPath);
      }
      // Just return itself
      if (splitPath[0].equals(".")) {
        return interpretSingleDot(init, currPath, splitPath);
      }
      int index = init.nameExists(splitPath[0]);
      // If it doesn't exist
      if (index == -1) {
        throw new InvalidDirectoryPathException(currPath);
      }
      if (init.getChildren().get(index) instanceof Directory) {
        return getSubDirFromDir(init, currPath, splitPath);
      }
      // Else it's a file
      return getFileFromDir(init, currPath, splitPath);
    }
    return init;
  }

  /**
   * Returns the file from a subdir.
   *
   * @param init The dir the file is found at.
   * @param currPath The current path of the file.
   * @param splitPath An array split from currPath with the '/' delimiter;
   * @return A file with the given path relative to init;
   * @throws InvalidDirectoryPathException
   */
  private static FileTypes getFileFromDir(Directory init, String currPath,
      String[] splitPath) throws InvalidDirectoryPathException {
    ArrayList<File> files = init.getChildFiles();
    Iterator<File> filesIterator = files.iterator();

    // Else it's a file we're looking for
    while (filesIterator.hasNext()) {
      File next = filesIterator.next();

      if (!(currPath.lastIndexOf('/') == currPath.length() - 1)) {
        if (next.getName().equals(splitPath[0])) {
          return next;
        }
      }
    }

    // Else it doesn't exist and throw an exception
    throw new InvalidDirectoryPathException(currPath);
  }

  /**
   * Returns a subdir within another dir and calls interpret path recursively.
   *
   * @param init The dir the subdir is found at.
   * @param currPath The current path of the subdir.
   * @param splitPath An array split from currPath with the '/' delimiter;
   * @return A file with the given path relative to init;
   * @throws InvalidDirectoryPathException
   */
  private static FileTypes getSubDirFromDir(Directory init, String currPath,
                                            String[] splitPath) throws InvalidDirectoryPathException {
      // Checking sub dirs
      ArrayList<Directory> subDirs = init.getChildDirs();
      Iterator<Directory> dirIterator = subDirs.iterator();

      // Looping over to see if what we're looking for is a dir
      while (dirIterator.hasNext()) {
          Directory next = dirIterator.next();
          if (next.getName().equals(splitPath[0])) {

              int length = splitPath[0].length();
              // Go into the new dir
              // Taking care of the '/' in case there are other sub dirs

              if (splitPath.length == 1)
                  return interpretPathRecursive(next,
                          currPath.substring(length, currPath.length()));
              else
                  return interpretPathRecursive(next,
                          currPath.substring(length + 1, currPath.length()));
          }
      }

      throw new InvalidDirectoryPathException(currPath);
  }
  /**
   * Parses the current single dot from currPath and calls interpretPath again.
   *
   * @param init The dir to interpret the path from.
   * @param currPath The current path of the file.
   * @param splitPath An array split from currPath with the '/' delimiter;
   * @return A file with the given path relative to init;
   * @throws InvalidDirectoryPathException
   */
  private static FileTypes interpretSingleDot(Directory init, String currPath,
      String[] splitPath) throws InvalidDirectoryPathException {
    // Checking if we have './moredirs/evenmore' or just '.'
    if (splitPath.length > 1) {
      return interpretPathRecursive(init,
          currPath.substring(2, currPath.length()));
    } else {
      return interpretPathRecursive(init,
          currPath.substring(1, currPath.length()));
    }
  }

  /**
   * Parses the double dots from currPath and calls interpretPath again.
   *
   * @param init The dir to interpret the path from.
   * @param currPath The current path of the file.
   * @param splitPath An array split from currPath with the '/' delimiter;
   * @return A file with the given path relative to init;
   * @throws InvalidDirectoryPathException
   */
  private static FileTypes interpretDoubleDots(Directory init, String currPath,
      String[] splitPath) throws InvalidDirectoryPathException {
    // Checking to see if we're at the current root of not
    if (init.getParent() != null) {
      // Checking if we have '../moredirs/evenmore' or just '..'
      if (splitPath.length > 1) {
        return interpretPathRecursive(init.getParent(),
            currPath.substring(3, currPath.length()));
      } else {
        return interpretPathRecursive(init.getParent(),
            currPath.substring(2, currPath.length()));
      }
    } else {
      // Checking if we have '../moredirs/evenmore' or just '..'
      if (splitPath.length > 1) {
        return interpretPathRecursive(init,
            currPath.substring(3, currPath.length()));
      } else {
        return interpretPathRecursive(init,
            currPath.substring(2, currPath.length()));
      }
    }
  }

  /**
   * Removes the leading slashes in a string path entered.
   *
   * @param path path specified by user
   * @return path without any leading slashes
   */
  private static String removeLeadingSlashes(String path) {
    int slashes = 0;

    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(i) != '/') {
        break;
      }
      slashes++;
    }
    return path.substring(slashes, path.length());
  }

  /**
   * Removes the ending slashes in a string path entered.
   *
   * @param path user path given
   * @return processed path with no end slashes
   */
  private static String removeEndingSlashes(String path) {
    int slashes = 0;

    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(path.length() - i - 1) != '/') {
        break;
      }
      slashes++;
    }
    return path.substring(0, path.length() - slashes);
  }

  /**
   * @param init The initial dir in which the intrepret path was called in
   * @param path The path of the File/Dir TO BE MADE, the last position of the
   *        path is to be made and does not exist
   * @return The DIR of the path before the file
   * @throws InvalidDirectoryPathException
   */
  public static FileTypes interpretMakePath(Directory init, String path)
      throws InvalidDirectoryPathException {
    path = removeEndingSlashes(path);

    if (path != "") {
      String[] splitPath = path.split("/");

      int last = splitPath[splitPath.length - 1].length();

      // if our path has more than 1 subpath e.g. dir/dir1/dir3
      if (splitPath.length > 2) {
        return interpretPath(init, path.substring(0, path.length() - last - 1));
      }
      // 1 dir like /test
      return interpretPath(init, path.substring(0, path.length() - last));
      // if our path has exactly than 1 subpath e.g. dir/dir1/dir3
    }
    return init.getParent();
  }

  /**
   * InvalidDirectoryPathException is thrown when a filepath that doesn't
   * exist in the filesystem is provided
   */
  public static class InvalidDirectoryPathException extends Exception {
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
}
