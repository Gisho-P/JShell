package driver;

import java.util.List;

import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes.InvalidName;


public class DisplayStoreString implements Command {

	@Override
	public String man() {
		return "ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n" +
               "NAME\n\t\techo - prints a string to standard output," +
               " or saves a string to a text\n\t\tfile\n\nSYNOPSIS" +
               "\n\t\techo STRING [>[>] OUTFILE]\n\nDESCRIPTION\n\t\t" +
               "Saves the STRING to the OUTFILE if it's provided, " +
               "otherwise it prints\n\t\tthe STRING to the standard " +
               "output.\n\n\t\tIf the OUTFILE is an existing file in" +
               " the directory, it will overwrite\n\t\tthe contents" +
               " of the OUTFILE with the string, otherwise it will" +
               " create\n\t\ta new file with name OUTFILE .\n\n\t\t" +
               "STRING should be a string of characters surrounded " +
               "by double quotation\n\t\tmarks.";
	}

	private String exec_helper(String text, String outfile, boolean overwrite) {
	  String retVal = "";
      File outputFile;
      try {
        outputFile = new File("newFile");

      // Check if the file exists in the directory
      try {
          outputFile = (File) FilePathInterpreter.interpretPath(MySession.getCurrentDir(), outfile);
      }
      // If the file doesn't exist create it
      catch (InvalidDirectoryPathException e) {
          try {
              // Check if the file is going to be in the current directory
              // or a different directory
              if (outfile.contains("/")) {
                  // If it's a different directory get that directory and add the file to it
                  outputFile = new File(outfile.substring(outfile.lastIndexOf("/") + 1));
                  Directory fileDir = (Directory) FilePathInterpreter.
                          interpretPath(MySession.getCurrentDir(), outfile.substring(0, outfile.lastIndexOf("/")));
                  fileDir.add(outputFile);
              } else {
                  // If it's the same directory
                  outputFile = new File(outfile);
                  MySession.getCurrentDir().add(outputFile);
              }

          } catch (InvalidDirectoryPathException e1) {
              return "ERROR: The directory of the file does not exist";
          } catch (NameExistsException e1) {
              return "ERROR: There is already a subdirectory with the same name";
          } catch (InvalidAddition e1) {
          }
      } catch (ClassCastException e) {
          return "ERROR: There is already a subdirectory with the same name";
      }
      } catch (InvalidName e2) {
        return "ERROR: That's an invalid file name";
      }
      // Write to the file, overwrite or append as given
      if (overwrite)
          outputFile.setContent(text == null ? "" : text);
      else
          outputFile.appendContent(text == null ? "" : text);
      return retVal;
  }

	/**
     * Writes the given text to the output file at the given outfile path.
     * If the file doesn't exist it creates it. Overwrites any text in the
     * file if overwrite is true, otherwise appends it to the end.
     *
     * @param text      the text to be written to the output file
     * @param outfile   the path of the file where the text will be written
     * @param overwrite whether to overwrite the existing text or append to it
     * @return the string any error messages or an empty string
     */
	@Override
	public String exec(List<String> args) {
	  if (args.size() != 4 && args.size() != 2 ) {
        return "echo usage: STRING [>[>] OUTFILE]";
    } else {
        if (args.get(1).startsWith("\"") && args.get(1).endsWith("\"")) {
          if (args.get(1).length() == 2) {
              args.set(1, null);
          } else {
            args.set(1, args.get(1).substring(1, args.get(1).length() - 1));
          }
        } else {
            return "ERROR: STRING must be surrounded by double quotations";
        }
        if (args.size() == 2) {
          return args.get(1) + "\n";
        } else if (args.get(2).equals(">>")) {// Append to file
          return exec_helper(args.get(1), args.get(3), false);
        } else if (args.get(2).equals(">")) { // Write to file
          return exec_helper(args.get(1), args.get(3), true);
        } else {
          return "echo usage: STRING [>[>] OUTFILE]";
        }
    }
	}

}
