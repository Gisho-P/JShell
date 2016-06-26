package driver;

import java.util.ArrayList;
import java.util.Iterator;

public class FilePathInterpreter {

  public static FileTypes interpretPath(Directory init, String path)
      throws InvalidDirectoryPathException {
    System.out.println(path + "AFTER");
    return interpretPathRecursive(init, path);
  };

  private static FileTypes interpretPathRecursive(Directory init,
      String currPath) throws InvalidDirectoryPathException {
    if (!currPath.equals("")) {
      String[] splitPath = currPath.split("/");

      if (currPath.charAt(0) == '/') {
        // Directory parent = init.getParent();
        while (init.getParent() != null) {
          init = init.getParent();
        }
        return interpretPathRecursive(init,
            currPath.substring(1, currPath.length()));
      }

      if (splitPath[0].equals("..")) {
        if (init.getParent() != null) {
          if (splitPath.length > 1) {
            return interpretPathRecursive(init.getParent(),
                currPath.substring(3, currPath.length()));
          } else {
            return interpretPathRecursive(init.getParent(),
                currPath.substring(2, currPath.length()));
          }
        } else {
          if (splitPath.length > 1) {
            return interpretPathRecursive(init,
                currPath.substring(3, currPath.length()));
          } else {
            return interpretPathRecursive(init,
                currPath.substring(2, currPath.length()));
          }
        }
      }

      if (splitPath[0].equals(".")) {
        if (splitPath.length > 1) {
          return interpretPathRecursive(init,
              currPath.substring(2, currPath.length()));
        } else {
          return interpretPathRecursive(init,
              currPath.substring(1, currPath.length()));
        }
      }


      // Checking sub dirs
      ArrayList<Directory> subDirs = init.getChildDirs();
      Iterator<Directory> dirIterator = subDirs.iterator();

      while (dirIterator.hasNext()) {
        Directory next = dirIterator.next();
        if (next.getName().equals(splitPath[0])) {

          int length = splitPath[0].length();
          // Go into the new dir
          // Taking care of the '/' in case there are other sub dirs

          // System.out.println(splitPath.length);
          if (splitPath.length == 1)
            return interpretPathRecursive(next,
                currPath.substring(length, currPath.length()));
          else
            return interpretPathRecursive(next,
                currPath.substring(length + 1, currPath.length()));
        }
      }

      // If we're at the end looking for a file
      if (splitPath.length == 1) {
        ArrayList<File> files = init.getChildFiles();
        Iterator<File> filesIterator = files.iterator();

        // Else it's a file we're looking for
        while (filesIterator.hasNext()) {
          File next = filesIterator.next();

          if (next.getName().equals(splitPath[0])) {
            return next;
          }
        }
      }

      // Else it doesn't exist and throw an expcetion
      throw new InvalidDirectoryPathException(
          "There are no files or directories with name " + currPath);
    }
    return init;
  }

  public static FileTypes interpretMakePath(Directory init, String path)
      throws InvalidDirectoryPathException {
    if (path != "") {

      System.out.println(path + "HI");

      String[] splitPath = path.split("/");
      int last = splitPath[splitPath.length - 1].length();

      if (splitPath[0].equals(".")) {

    	  
      // Either /t1/t2.. or /t1/t2/ or /
      } else if (splitPath[0].equals("..") || path.equals("/")) {
        return interpretPath(init, "/");
      }

      // if our path has more than 1 subpath e.g. dir/dir1/dir3
      if (splitPath.length > 2) {
        return interpretPath(init, path.substring(0, path.length() - last - 1));
      }
      // 1 dir like /test
      return interpretPath(init, path.substring(0, path.length() - last));
      // if our path has exactly than 1 subpath e.g. dir/dir1/dir3
    }
    return init.getParent();
  };

  public static class InvalidDirectoryPathException extends Exception {
    private static final long serialVersionUID = 59L;

    public InvalidDirectoryPathException(String message) {
      super(message);
    }
  }
}
