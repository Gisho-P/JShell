package driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

/**
 * The Class ListDirectoryContents can list the children of a given list of
 * directories.
 */
public class ListDirectoryContents implements Command {

  /**
   * The session is used to get the current directory when searching for paths
   */
  private MySession s;

  /**
   * Return ListDirectoryContents instance, to be able to run ls command.
   * 
   * @param session Current JShell's session attributes
   * @return ListDirectoryContents instance
   */
  public ListDirectoryContents(MySession session) {
    s = session;
  }

  /**
   * Return ls man page.
   * 
   * @return manual for ls command
   */
  @Override
  public String man() {
    return "LS(1)\t\t\t\tUser Commands\t\t\t\tLS(1)\n\nNAME\n\t"
        + "\tls - prints out all of the contents of one or many "
        + "files/directories\n\nSYNOPSIS\n\t\tls [PATH ...]\n\n"
        + "DESCRIPTION\n\t\tPrints out the contents of files/"
        + "directories.\n\n\t\tIf PATH is not specified, prints "
        + "out the contents of the current\n\t\tdirectory by "
        + "default.\n\n\t\tIf PATH is specified, prints out the "
        + "contents of the files/directories\n\t\tfor each PATH given";
  }

  /**
   * Return list of contents in directory specified.
   * 
   * @param args Arguments to process for ls command
   * @return output message (directory contents) or error message
   */
  @Override
  public String interpret(List<String> args) {
    return exec(args);
  }

  /**
   * Lists the name of the given file/directory followed by the contents if it
   * is a directory.
   *
   * @param paths the paths of directories/files to be listed
   * @return a list of the contents of each of the given paths
   */
  private String execMult(List<String> paths) {
    String retVal = "";
    String currentDirectory = "";
    ArrayList<String> childNames = new ArrayList<String>();
    ArrayList<String> directoryAndContents = new ArrayList<String>();
    // Get children if path is a directory, or return path if it's a file
    for (String i : paths) {
      try { // Assume it's a directory and get children
        childNames.addAll(((Directory) FilePathInterpreter
            .interpretPath(s.getCurrentDir(), i)).getChildNames());
        currentDirectory = i + ":";
      } catch (InvalidDirectoryPathException e) {
        retVal += ("No such directory or file with path " + i + "\n");
      } catch (ClassCastException e) { // Indicates that path points to a file
        currentDirectory = i;
      }
      if (currentDirectory != "") { // Sort children alphabetically and store
        Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
        for (String childName : childNames) {
          currentDirectory += " " + childName;
        }
        directoryAndContents.add(currentDirectory);
        childNames.clear();
        currentDirectory = "";
      }
    } // Sort directory and contents alphabetically and add to return
    Collections.sort(directoryAndContents, String.CASE_INSENSITIVE_ORDER);
    for (String dirCon : directoryAndContents) {
      retVal += dirCon + "\n";
    }
    return retVal.substring(0, retVal.length() - 1);
  }

  /**
   * Returns contents of director(y/ies) in a neatly formatted string.
   * 
   * @param args arguments to be run with ls command
   * @return error message or directory contents
   */
  @Override
  public String exec(List<String> args) {
    // If there are paths specified get the output form function call
    if (args.size() > 1) {
      return execMult(args.subList(1, args.size()));
    }
    // Otherwise, get the contents of the current directory and return it
    else {
      String retVal = "";
      ArrayList<String> childNames = s.getCurrentDir().getChildNames();
      Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
      for (String childName : childNames) {
        retVal += childName + " ";
      }
      return retVal.trim(); // remove last blank space
    }
  }

}
