package commands;

import java.util.List;

import driver.*;
import exceptions.*;
import structures.*;

/**
 * The Class DisplayFile handles the moveing of a file to another dir.
 */
public class MoveFile implements Command {

  // MySession is used to access the files by finding them through the
  // root or current directory
  private MySession s;

  private FileTypes sourceCopy;

  public FileTypes getSourceCopy() {
    return sourceCopy;
  }

  public Directory getSourceParent() {
    return sourceParent;
  }

  private Directory sourceParent;

  public MoveFile(MySession session) {
    s = session;
  }

  /**
   * Returns the manual for the mv command.
   *
   * @return the manual for the mv command
   */
  @Override
  public void man() {
    s.setOutput("MV(1)\t\t\t\tUser Commands\t\t\t\tMV(1)\n"
        + "\nNAME\n\t\tmv - Move file from source to destination. \n" + "\n"
        + "SYNOPSIS\n" + "\t\t" + "\n\t\t"
        + "mv FILE1 [FILE2]\n\nDESCRIPTION\n\t\t"
        + "Move the file from the source path to the \n"
        + "destination paths if it is valid.");
  }

  /**
   * Process arguments passed for mv command and determine whether the command
   * was entered correctly or not.
   *
   * @param args Arguments parsed from command
   * @return The contents of the files given.
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 3) {
      s.setError("mv usage: mv SRCPATH [DESTPATH] ...");
    } else {
      exec(args.subList(1, args.size()));
      // return output from function call
    }
  }

  // TODO: 10/07/16 Need to check what happens if the user tries to move the
  // root. Also have some sort of collaboration with cp and mv
  // mv a b and a and b are both files then b is replaced with a

  /**
   * Once this is finished delete the other one
   */
  public void exec(List<String> args) {
    try {
      FileTypes src =
          FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(0));

      FileTypes dest;
      String newName = "";
      try {
        // Determine if the destination path exists
        dest =
            FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
      } catch (InvalidDirectoryPathException e) {
        // If the dest path doesn't exist, maybe the command is asking
        // for a rename so check if the parent exists
        dest = FilePathInterpreter.interpretMakePath(s.getCurrentDir(),
            args.get(1));

        String[] names = args.get(1).split("/");
        newName = names[names.length - 1];
      }

      // If src is parent or equivalent to destination, InvalidAddition
      if (FileTypes.isInvalidAddition(src, dest))
        throw new InvalidAdditionException();

      // Make a backup of the source object
      sourceCopy = FileTypes.deepCopy(src);

      Directory parent = src.getParent();
      // backup of the source parent
      sourceParent = parent;

      if (dest instanceof File) {
        if (src instanceof File) {
          // If src and destination lead to file, then replace the file in
          // the destination with the file in the src
          Directory dParent = dest.getParent();
          parent.remove(src.getName());
          src.setName(dest.getName());
          dParent.addReplace(src);
        } else {
          // moving directory to a file case
          s.addError(
              "Invalid destination path. Can not move " + "source directory.");
        }
      } else {
        Directory dDest = (Directory) dest;
        // Rename case
        if (!newName.equals("")) {
          parent.remove(src.getName());
          src.setName(newName);
          dDest.add(src);
        } else {
          // determine whether or not to move
          boolean move = false;
          // move if dest doesn't contain same name as src
          if (dDest.nameExists(src.getName()) == -1) {
            move = true;
          } else {
            // move if dest contains same name as src but both the
            // src and dest child are directories or files. If both
            // are directories, only move if dest child directory
            // is empty
            FileTypes dChild = dDest.getChild(src.getName());
            if ((src instanceof Directory && dChild instanceof Directory
                && ((Directory) dChild).size() == 0)
                || (src instanceof File && dChild instanceof File))
              move = true;
          }

          // add or replace the file in dest
          if (move) {
            parent.remove(src.getName());
            dDest.addReplace(src);
          } else
            s.addError("Unable to move. Type mismatch between "
                + "source file and file being replaced or the "
                + "file being replaced is not empty.");
        }
      }
    } catch (InvalidDirectoryPathException e) {
      s.addError(e.getMessage());
    } catch (InvalidAdditionException invalidAddition) {
      s.addError(invalidAddition.getMessage());
    } catch (MissingNameException e) {
      s.addError(e.getMessage());
    } catch (InvalidNameException invalidName) {
      s.addError(invalidName.getMessage());
    } catch (NameExistsException e) {
      s.addError(e.getMessage());
    }
  }
}
