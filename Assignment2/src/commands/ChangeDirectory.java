package commands;

import java.util.List;

import exceptions.InvalidDirectoryPathException;
import structures.Directory;
import driver.FilePathInterpreter;
import structures.FileTypes;
import driver.MySession;

/**
 * ChangeDirectory class replicates the cd command in a shell. It allows
 * traversal through a File System
 */
public class ChangeDirectory implements Command {

  /**
   * Uses session to change and retrieve current node
   */
  private MySession s;

  /**
   * Create
   * 
   * @param session
   */
  public ChangeDirectory(MySession session) {
    s = session;
  }

  /**
   * Returns the man of the given command ChangeDirectory.
   * 
   * @returns The manual of the command ChangeDirectory.
   */
  @Override
  public void man() {
    s.setOutput("CD(1)\t\t\t\tUser Commands\t\t\t\tCD(1)\n\nNAME\n\t"
        + "\tcd - changes the working directory of the shell"
        + "\n\nSYNOPSIS\n\t\tcd DIR\n\nDESCRIPTION\n\t\t"
        + "Changes the current directory of the shell to the one"
        + " specified by DIR.\n\n\t\tThe DIR may be relative to "
        + "the current directory or the full path.\n\n\t\t"
        + ".. indicates the parent directory, and . indicates "
        + "the current directory\n\t\twhen specifying the DIR.");
  }

  /**
   * Interprets the arguments and parses the of the command ChangeDirectory,
   * Will call exec and execute the command with the arguments if they are
   * valid.
   * 
   * @param args The list of arguments for ChangeDirectory to use.
   * @returns The output string of the command.
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 2) {
      s.setError("cd usage: cd DIR"); // error, print usage
    } else { // return output from function call
      exec(args);
    }
  }

  /**
   * Interprets and executes the command ChangeDirectory based on the arguments
   * given.
   * 
   * @param args The arguments for the command to take in.
   * @return The output string of the command.
   */
  public void exec(List<String> args) {
    try {
      // Get the node at the end of the path if it exists and set the
      // current directory to the node
      FileTypes dest =
          FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
      if (dest instanceof Directory) {
        s.setCurrentDir((Directory) dest);
      } else
        s.addError(dest.getName() + " is not a directory.");
    } catch (InvalidDirectoryPathException e) {
      s.addError("No such dir as " + args.get(1));
    }
  }

}
