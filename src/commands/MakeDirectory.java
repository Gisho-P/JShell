package commands;

import java.util.List;

import exceptions.InvalidAdditionException;
import exceptions.InvalidDirectoryPathException;
import exceptions.InvalidNameException;
import exceptions.NameExistsException;
import structures.Directory;
import driver.FilePathInterpreter;
import structures.FileTypes;
import driver.MySession;

public class MakeDirectory implements Command {

  private MySession s;

  public MakeDirectory(MySession session) {
    s = session;
  }

  /**
   * Returns the man of the given command MakeDirectory.
   * 
   * @returns The manual of the command MakeDirectory.
   */
  @Override
  public void man() {
    s.setOutput("MKDIR(1)\t\t\t\tUser Commands\t\t\t\tMKDIR(1)\n\n"
        + "NAME\n\t\tmkdir - creates directory(s)\n\nSYNOPSIS"
        + "\n\t\tmkdir DIR ...\n\nDESCRIPTION\n\t\tCreates "
        + "directories which may be in the current directory "
        + "or a full\n\t\tpath relative to the root/current " + "directory.");
  }

  /**
   * Interprets the arguments and parses the of the command MakeDirectory, Will
   * call exec and execute the command with the arguments if they are valid.
   * 
   * @param args The list of arguments for MakeDirectory to use.
   * @returns The output string of the command.
   */
  @Override
  public void interpret(List<String> cmdArgs) {
    if (cmdArgs.size() == 1) {
      s.addError("mkdir usage: mkdir DIR [DIR2] ...");
    } else {// return output from function call
      exec(cmdArgs.subList(1, cmdArgs.size()));
    }
  }

  /**
   * Interprets and executes the command MakeDirectory based on the arguments
   * given.
   * 
   * @param args The arguments for the command to take in.
   * @return The output string of the command.
   */
  @Override
  public void exec(List<String> directory) {
    FileTypes parentDir;
    int slashIndex;
    String splitPath[];
    for (String i : directory) {
      try {
        if ((slashIndex = i.indexOf("/")) == -1) {
          s.getCurrentDir().add(new Directory(i));
        } else {
          splitPath = i.split("/");
          if (splitPath.length != 0) {
            parentDir =
                FilePathInterpreter.interpretMakePath(s.getCurrentDir(), i);
            ((Directory) parentDir)
                .add(new Directory(splitPath[splitPath.length - 1]));
          } else
            s.addError(
                "mkdir: cannot create a directory without a name");
        }
      } catch (NameExistsException e) {
        s.addError(
            "mkdir: cannot create directory '" + i + "': File exists");
      } catch (InvalidAdditionException invalidAddition) {
        invalidAddition.printStackTrace();
      } catch (InvalidDirectoryPathException e) {
        s.addError(
            "mkdir: cannot create directory '" + i + "': Invalid Path");
      } catch (InvalidNameException invalidName) {
        s.addError("mkdir: cannot create directory with name '" + i
            + "'. It is invalid.");
      }
    }
  }

}
