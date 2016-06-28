package driver;

import java.util.ArrayList;
import java.util.List;

import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes.InvalidName;


/**
 * The Class DisplayStoreString can either display a string in the JShell or
 * store it in a new or existing file.
 */
public class DisplayStoreString implements Command {

  /** Use the session to get the current and root directory */
  private MySession s;

  public DisplayStoreString(MySession session) {
    s = session;
  }

  @Override
  public String man() {
    return "ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n"
        + "NAME\n\t\techo - prints a string to standard output,"
        + " or saves a string to a text\n\t\tfile\n\nSYNOPSIS"
        + "\n\t\techo STRING [>[>] OUTFILE]\n\nDESCRIPTION\n\t\t"
        + "Saves the STRING to the OUTFILE if it's provided, "
        + "otherwise it prints\n\t\tthe STRING to the standard "
        + "output.\n\n\t\tIf the OUTFILE is an existing file in"
        + " the directory, it will overwrite\n\t\tthe contents"
        + " of the OUTFILE with the string, otherwise it will"
        + " create\n\t\ta new file with name OUTFILE .\n\n\t\t"
        + "STRING should be a string of characters surrounded "
        + "by double quotation\n\t\tmarks.";
  }



  @Override
  public String interpret(List<String> args) {
    if (args.size() != 4 && args.size() != 2) {
      return "echo usage: STRING [>[>] [OUTFILE]";
    } else {
      // Check that the string is surrounded by quotes
      if (args.get(1).startsWith("\"") && args.get(1).endsWith("\"")) {
        if (args.get(1).length() == 2) { // Set to null if string is empty
          args.set(1, null);
        } else { // Remove quotes
          args.set(1, args.get(1).substring(1, args.get(1).length() - 1));
        }
      } else {
        return "ERROR: STRING must be surrounded by double quotations";
      }
      if (args.size() == 2) { // return the string if no file is given
        return args.get(1) == null ? "" : (String) args.get(1);
      } else {
        List<String> retArgs = new ArrayList<String>();
        retArgs.add(args.get(1));
        retArgs.add(args.get(3));
        if (args.get(2).equals(">>")) { // Append to file
          retArgs.add("false");
          return exec(retArgs);
        } else {
          if (args.get(2).equals(">")) { // Write to file
            retArgs.add("true");
            return exec(retArgs);
          } else {
            return "echo usage: STRING [>[>] [OUTFILE]";
          }
        }
      }
    }
  }

  @Override
  public String exec(List<String> args) {
    String retVal = "";
    File outputFile = null;
    // Check if the file exists in the directory
    try {
      outputFile = (File) FilePathInterpreter.interpretPath(s.getCurrentDir(),
          (String) args.get(1));
    }
    // If the file doesn't exist create it
    catch (InvalidDirectoryPathException e) {
      try {
        outputFile = createFileFromPath(args.get(1));
      } catch (InvalidDirectoryPathException e1) {
        return "ERROR: The directory of the file does not exist";
      } catch (NameExistsException e1) {
        return "ERROR: There is already a subdirectory with the same name";
      } catch (InvalidAddition e1) {
      } catch (InvalidName e1) {
        return "ERROR: That's an invalid file name";
      }
    } catch (ClassCastException e) {
      return "ERROR: There is already a subdirectory with the same name";
    }
    // Write to the file, overwrite or append as given
    if (Boolean.valueOf(args.get(2)))
      outputFile.setContent(args.get(0) == null ? "" : (String) args.get(0));
    else
      outputFile.appendContent(args.get(0) == null ? "" : (String) args.get(0));
    return retVal;
  }

  /**
   * Creates the file from the given path, and throws an exception otherwise.
   *
   * @param path the path to the file
   * @return the file
   * @throws InvalidName if the file has an invalid character
   * @throws InvalidDirectoryPathException if parent directory doesn't exist
   * @throws NameExistsException there already is a file/directory with the same
   *         name
   * @throws InvalidAddition adding a file to the same file
   */
  public File createFileFromPath(String path) throws InvalidName,
      InvalidDirectoryPathException, NameExistsException, InvalidAddition {
    File outputFile = new File("");
    // Check if the file is going to be in the current directory
    // or a different directory
    if (((String) path).contains("/")) {
      // If it's a different directory get that directory and add the file
      // to it
      outputFile = new File(
          ((String) path).substring(((String) path).lastIndexOf("/") + 1));
      Directory fileDir =
          (Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(),
              path.substring(0, path.lastIndexOf("/")));
      fileDir.add(outputFile);
    } else {
      // If it's the same directory
      outputFile = new File((String) path);
      s.getCurrentDir().add(outputFile);
    }
    return outputFile;
  }
}
