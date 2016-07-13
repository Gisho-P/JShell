package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import structures.Directory;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.MySession;

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
  public void man() {
    s.addOutput("LS(1)\t\t\t\tUser Commands\t\t\t\tLS(1)\n\nNAME\n\t"
        + "\tls - prints out all of the contents of one or many "
        + "files/directories\n\nSYNOPSIS\n\t\tls [-R] [PATH ...]\n\n"
        + "DESCRIPTION\n\t\tPrints out the contents of files/"
        + "directories.\n\n\t\tIf PATH is not specified, prints "
        + "out the contents of the current\n\t\tdirectory by "
        + "default.\n\n\t\tIf PATH is specified, prints out the "
        + "contents of the files/directories\n\t\tfor each PATH given");
  }

  /**
   * Return list of contents in directory specified.
   * 
   * @param args Arguments to process for ls command
   * @return output message (directory contents) or error message
   */
  @Override
  public void interpret(List<String> args) {
    exec(args);
  }

  /**
   * Lists the name of the given file/directory followed by the contents if it
   * is a directory.
   *
   * @param paths the paths of directories/files to be listed
   * @return a list of the contents of each of the given paths
   */
  private void execMult(List<String> paths, Boolean recursiveDirectories) {
    Collections.sort(paths, String.CASE_INSENSITIVE_ORDER);
    // Get children if path is a directory, or return path if it's a file
    for (String i : paths) {
      try { // Assume it's a directory and get children
        Directory currentDir = ((Directory) FilePathInterpreter
            .interpretPath(s.getCurrentDir(), i));
        s.addOutput(getDirectoryContents((Directory) currentDir,
            currentDir, i, recursiveDirectories));
      } catch (InvalidDirectoryPathException e) {
        s.addError("No such directory or file with path " + i + "\n");
      } catch (ClassCastException e) { // Indicates that path points to a file
        s.addOutput(i + "\n");
      }
    }
  }

  private String getDirectoryContents(Directory currentDir, Directory root,
      String path, Boolean r) {
    String curDirContents = currentDir == root ? path + ":"
        : (path.equals("/") ? "" : path) + currentDir.getPath(root)
            + ":";
    ArrayList<String> childNames = currentDir.getChildNames();
    Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
    for (String childName : childNames) {
      curDirContents += " " + childName;
    }
    if (r) {
      ArrayList<Directory> subDirectories = currentDir.getChildDirs();
      Collections.sort(subDirectories, new Comparator<Directory>() {
        @Override
        public int compare(Directory d1, Directory d2) {
          return d1.getName().compareTo(d2.getName()); // Ascending
        }
      });
      for (Directory subDir : subDirectories) {
        curDirContents += "\n" + getDirectoryContents(subDir, root, path, r);
      }
    }
    return curDirContents;
  }


  /**
   * Returns contents of director(y/ies) in a neatly formatted string.
   * 
   * @param args arguments to be run with ls command
   * @return error message or directory contents
   */
  @Override
  public void exec(List<String> args) {
    if (args.size() == 2 && args.get(1).equals("-R")) {
      if(s.getCurrentDir().equals(s.getRootDir()))
        execMult(Arrays.asList("/"), args.get(1).equals("-R"));
      else
        s.addOutput(getDirectoryContents(s.getCurrentDir(),
            s.getCurrentDir(), s.getCurrentDir().getName(),
            args.get(1).equals("-R")));
    }
    // If there are paths specified get the output form function call
    if (args.size() > 1) {
      if (args.get(1).equals("-R"))
        execMult(args.subList(2, args.size()), true);
      else
        execMult(args.subList(1, args.size()), false);
    }
    // Otherwise, get the contents of the current directory and return it
    else {
      s.addOutput(getDirectoryContents(s.getCurrentDir(),
          s.getCurrentDir(), "", false).replaceFirst(":", "").trim());
    }
  }

}
