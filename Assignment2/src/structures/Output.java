package structures;

import driver.*;
import exceptions.InvalidAdditionException;
import exceptions.NameExistsException;
import exceptions.InvalidDirectoryPathException;
import exceptions.InvalidNameException;

/**
 * The Class Output stores standard output and error strings and redirects
 * output to files.
 */
public class Output {
  private String stdOutput;
  private String stdError;

  public Output() {
    stdOutput = "";
    stdError = "";
  }

  public String getStdOutput() {
    return stdOutput;
  }

  public void addStdOutput(String stdOutput) {
    if (!this.stdOutput.isEmpty()) {
      this.stdOutput += "\n";
    }
    this.stdOutput += stdOutput;
  }

  public void setStdOutput(String stdOutput) {
    this.stdOutput = stdOutput;
  }

  public void clear() {
    stdOutput = "";
    stdError = "";
  }

  public String getStdError() {
    return stdError;
  }

  public void setStdError(String standardError) {
    stdError = standardError;
  }

  public void addStdError(String standardError) {
    if (!stdError.isEmpty()) {
      stdError += "\n";
    }
    stdError += standardError;
  }

  public void addErrorClearOutput(String standardError) {
    addStdError(standardError);
    setStdOutput("");
  }

  public String getAllOutput() {
    return getStdError() + getStdOutput();
  }

  public void redirect(String file, String type, Directory curDir,
      Directory rootDir) {
    FileTypes outFile = null;
    // Check if the file exists in the directory
    try {
      outFile = FilePathInterpreter.interpretPath(curDir, file);
    } catch (InvalidDirectoryPathException e) {
      // If the file doesn't exist create it
      try {
        outFile = createFileFromPath(curDir, rootDir, file);
      } catch (InvalidDirectoryPathException e1) {
        addErrorClearOutput("ERROR: The directory of the file does not exist");
      } catch (NameExistsException e1) {
        addErrorClearOutput(
            "ERROR: There is already a subdirectory with the same name");
      } catch (InvalidAdditionException e1) {
      } catch (InvalidNameException e1) {
        addErrorClearOutput("ERROR: That's an invalid file name");
      }
    }
    if (outFile instanceof File)
      redirectFile((File) outFile, type);
    else
      addErrorClearOutput(
          "ERROR: There is already a subdirectory with the same name");
  }

  public void redirectFile(File file, String type) {
    if (file != null) {
      // Write to the file, overwrite or append as given
      if (type.equals(">"))
        file.setContent(getStdOutput());
      else {
        file.appendContent(getStdOutput());
        getClass();
      }
      this.stdOutput = "";
    }
  }

  /**
   * Creates the file from the given path, and throws an exception otherwise.
   * 
   * @param directory
   *
   * @param path the path to the file
   * @return the file
   * @throws InvalidNameException if the file has an invalid character
   * @throws InvalidDirectoryPathException if parent directory doesn't exist
   * @throws NameExistsException there already is a file/directory with the same
   *         name
   * @throws InvalidAdditionException adding a file to the same file
   */
  private File createFileFromPath(Directory curDir, Directory rootDir,
      String path)
      throws InvalidNameException, InvalidDirectoryPathException,
      NameExistsException, InvalidAdditionException {
    File outputFile = new File("");
    // Check if the file is going to be in the current directory
    // or a different directory
    if (((String) path).contains("/")) {
      // If it's a different directory get that directory and add the file
      // to it
      outputFile = new File(path.substring(path.lastIndexOf("/") + 1));
      Directory fileDir = (Directory) FilePathInterpreter
          .interpretPath(path.startsWith("/") ? rootDir : curDir
              , path.substring(0, path.lastIndexOf("/")));
      fileDir.add(outputFile);
    } else {
      // If it's the same directory
      outputFile = new File((String) path);
      curDir.add(outputFile);
    }
    return outputFile;
  }
}