package driver;

import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

/**
 * The Class DisplayFile handles displaying the contents of one or more files.
 */
public class DisplayFile implements Command {

  // MySession is used to access the files by finding them through the
  // root or current directory
  private MySession s;
  private Output out;

  public DisplayFile(MySession session) {
    s = session;
    out = new Output();
  }

  /**
   * Returns the manual for the cat command.
   * 
   * @return the manual for the cat command
   */
  @Override
  public String man() {
    return "CAT(1)\t\t\t\tUser Commands\t\t\t\tCAT(1)\n"
        + "\nNAME\n\t\tcat - displays the contents of one or "
        + "more files on the standard output\n\nSYNOPSIS\n\t\t"
        + "cat FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t"
        + "Concatenates one or more files to the standard output"
        + ".\n\t\tCan take any amount of files greater then one as a"
        + " parameter.";
  }

  /**
   * Process arguments passed for cat command and determine whether the command
   * was entered correctly or not.
   * 
   * @param args Arguments parsed from command
   * @return The contents of the files given.
   */
  @Override
  public Output interpret(List<String> args) {
    if (args.size() < 2) {
      return out.withStdError("cat usage: cat FILE [FILE2] ...");
    } else {
      return exec(args.subList(1, args.size()));
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
  public Output exec(List<String> args) {
    Boolean firstFile = true;

    // Iterate through each path and get the file contents
    for (String i : args) {
      try {
        File currentFile =
            ((File) FilePathInterpreter.interpretPath(s.getCurrentDir(), i));
        // print three line breaks in between files
        if (!firstFile)
           out.addStdOutput("\n\n\n");
        // Add the content to the return
        out.addStdOutput(currentFile.getContent());
        if (firstFile)
          firstFile = false;
      } catch (InvalidDirectoryPathException e) {
        out.addStdError("No such dir as " + i + "\n");
      } catch (ClassCastException e) {
        out.addStdError("Unable to cat dir " + i + "\n");
      }
    }
    return out;
  }

}
