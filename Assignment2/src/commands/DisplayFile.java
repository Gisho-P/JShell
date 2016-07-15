package commands;

import java.util.List;

import structures.File;
import driver.*;
import exceptions.InvalidDirectoryPathException;

/**
 * The Class DisplayFile handles displaying the contents of one or more files.
 */
public class DisplayFile implements Command {

  // MySession is used to access the files by finding them through the
  // root or current directory
  private MySession s;

  public DisplayFile(MySession session) {
    s = session;
  }

  /**
   * Returns the manual for the cat command.
   * 
   * @return the manual for the cat command
   */
  @Override
  public void man() {
    s.setOutput("CAT(1)\t\t\t\tUser Commands\t\t\t\tCAT(1)\n"
        + "\nNAME\n\t\tcat - displays the contents of one or "
        + "more files on the standard output\n\nSYNOPSIS\n\t\t"
        + "cat FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t"
        + "Concatenates one or more files to the standard output"
        + ".\n\t\tCan take any amount of files greater then one as a"
        + " parameter.");
  }

  /**
   * Process arguments passed for cat command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return The contents of the files given.
   */
  @Override
  public void interpret(List<String> args) {
    if (args.size() < 2) {
      s.setError("cat usage: cat FILE [FILE2] ...");
    } else {
      exec(args.subList(1, args.size()));
      // return output from function call
    }
  }

  /**
   * Returns the contents of the with files listed in the args.
   * 
   * @param args Valid arguments parsed from command
   * @return The contents of the files given.
   */
  @Override
  public void exec(List<String> args) {
    Boolean firstFile = true;

    // Iterate through each path and get the file contents
    for (String i : args) {
      try {
        File currentFile =
            ((File) FilePathInterpreter.interpretPath(s.getCurrentDir(), i));
        // print three line breaks in between files
        if (!firstFile)
          s.addOutput("\n\n");
        // Add the content to the return
        s.addOutput(currentFile.getContent());
        if (firstFile)
          firstFile = false;
      } catch (InvalidDirectoryPathException e) {
        s.addError("No such file at " + i);
      } catch (ClassCastException e) {
        s.addError("Unable to cat dir " + i);
      }
    }
  }

}
