package driver;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the directory stack where directories can be pushed and
 * popped from (using pushd and popd respectively). These two operations exist
 * in the class.
 * 
 * @author Adnan Bhuiyan
 */
public class DirStack {

  /**
   * Data type to hold directory stack
   */
  static List<String> directories = new ArrayList<String>();

  /**
   * Remove the last directory stored in the directory stack or an error message
   * if the stack is empty.
   * 
   * @return directory to change to
   */
  public static List<Object> popd() {
    String newDir = "";
    boolean success = false;
    if (directories.size() == 0) {
      newDir = "ERROR: Empty stack, nothing to pop";
    } else {
      newDir = directories.remove(directories.size() - 1);
      success = true;
    }

    List<Object> popCheck = new ArrayList<Object>();
    popCheck.add(newDir); // store new directory/error message
    popCheck.add(success); // store success status

    return popCheck;
  }

  /**
   * Store current directory in directory stack.
   * 
   * @param currPath current directory to store in stack
   */
  public static void pushd(String currPath) {
    directories.add(currPath);
  }

  /**
   * Clear directory stack.
   */
  public static void clear() {
    directories.clear();
  }
}
