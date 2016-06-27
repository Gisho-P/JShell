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

  private MySession s;

  public ListDirectoryContents(MySession session) {
    s = session;
  }

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
    ArrayList<String> childNames = new ArrayList<String>();
    String fileName = "";
    ArrayList<String> directoryAndContents = new ArrayList<String>();
    // Iterate through each path and get the dir names in each directory or
    // the file name if it's a file
    for (String i : paths) {
      // First we assume the path points to a directory and get the
      // directory then add their children to the list
      try {
        fileName = "";
        childNames.addAll(((Directory) FilePathInterpreter
            .interpretPath(s.getCurrentDir(), i)).getChildNames());
        fileName = i + ":";
        // Sort the list of children directories/files alphabetically
        Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
        retVal += fileName;
        for (String childName : childNames) {
          retVal += childName + " ";
        }
        retVal += "\n";
        childNames.clear();
      } catch (InvalidDirectoryPathException e) {
        retVal += ("No such directory as " + i + "\n");
      } catch (ClassCastException e) {
        // If it wasn't a directory then we assume it's a file and
        // get the file name
        try {
          fileName =
              (((File) FilePathInterpreter.interpretPath(s.getCurrentDir(), i))
                  .getName());
          // If it doesn't throw exception it means it exists, overwrite with
          // path
          fileName = i;
          retVal += fileName + "\n";
        } catch (InvalidDirectoryPathException e1) {
          retVal += "No such directory or file as " + i + " \n";
        }
      }
      // Sort the list of children directories/files alphabetically
      Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
      String currentDirectory = fileName;
      for (String childName : childNames) {
        currentDirectory += " " + childName;
      }
      directoryAndContents.add(currentDirectory);
      childNames.clear();
    }
    Collections.sort(directoryAndContents, String.CASE_INSENSITIVE_ORDER);
    for (String dirCon : directoryAndContents) {
	retVal += dirCon + "\n";
      }
    return retVal.substring(0, retVal.length() - 1);
  }

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
