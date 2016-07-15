package structures;

import java.util.*;

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
  private List<String> directories = new ArrayList<String>();

  /**
   * Remove the last directory stored in the directory stack or an error message
   * if the stack is empty.
   * 
   * @return directory to change to
   */
  public List<Object> popd() {
    String newDir = "";
    boolean success = false;

    if (directories.size() == 0) { // can't pop from empty stack
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
  public void pushd(String currPath) {
    directories.add(currPath);
  }

  /**
   * Clear directory stack.
   */
  public void clear() {
    directories.clear();
  }
}
