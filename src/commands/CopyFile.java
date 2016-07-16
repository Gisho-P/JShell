package commands;

import java.util.List;

import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.NameExistsException;
import structures.Directory;

/**
 * The Class DisplayFile handles the copying of a file to another dir and
 * renaming the copied file.
 */
public class CopyFile implements Command {

  // MySession is used to access the files by finding them through the
  // root or current directory
  private MySession s;

  /**
   * Create a CopyFile object
   * @param session
     */
  public CopyFile(MySession session) {
    s = session;
  }

  /**
   * Returns the manual for the cp command.
   *
   * @return the manual for the cp command
   */
  @Override
  public void man() {
    s.setOutput("cp(1)\t\t\t\tUser Commands\t\t\t\tcp(1)\n"
            + "\nNAME\n\t\tcp - Copy file from source to destination. \n" + "\n"
            + "SYNOPSIS\n" + "\t\t" + "\n\t\t"
            + "cp FILE1 [FILE2]\n\nDESCRIPTION\n\t\t"
            + "Copy the file from the source path to the \n"
            + "destination paths if it is valid.");
  }

  /**
   * Process arguments passed for cp command and determine whether the command
   * was entered correctly or not.
   *
   * @param args Arguments parsed from command
   * @return The contents of the files given.
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() != 3) {
      s.setError("cp usage: cp Src Dest");
    } else {
      exec(args.subList(1, args.size()));
      // return output from function call
    }
  }


  /**
   * Copies the file from one directory to another
   *
   * @param args Valid arguments parsed from command
   * @return The contents of the files given.
   */
  public void exec(List<String> args) {
    //Use move command to move file
    MoveFile mv = new MoveFile(s);
    mv.exec(args);
    if (s.getError().equals("")) {
      try {
        //get backup copy and add it to original location
        mv.getSourceParent().add(mv.getSourceCopy());
        if (mv.isCurrentDirSame()) {
          //used to reset current directory after performing mv
          s.setCurrentDir((Directory) mv.getSourceCopy());
        }
      } catch (NameExistsException e) {
        s.addError("Unable to copy. File already exists in directory.");
      } catch (InvalidAdditionException e) {
        s.addError(e.getMessage());
      } catch (ClassCastException e) {
      }
    }


  }
}
