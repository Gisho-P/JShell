package structures;

import driver.*;
import exceptions.*;

/**
 * The Class Output stores standard output and error strings and redirects
 * output to files.
 */
public class Output {
  
  /** The standard output. */
  private String stdOutput;
  
  /** The standard error. */
  private String stdError;

  /**
   * Instantiates a new output with empty output and error.
   */
  public Output() {
    stdOutput = "";
    stdError = "";
  }

  /**
   * Gets the std output.
   *
   * @return the std output
   */
  public String getStdOutput() {
    return stdOutput;
  }

  /**
   * Adds the given string to stdOut.
   *
   * @param stdOutput the string to be added to stdOut
   */
  public void addStdOutput(String stdOutput) {
    if (!this.stdOutput.isEmpty()) {
      this.stdOutput += "\n";
    }
    this.stdOutput += stdOutput;
  }

  /**
   * Sets the stdOut to the given String.
   *
   * @param stdOutput the string to replace the currend stdOut
   */
  public void setStdOutput(String stdOutput) {
    this.stdOutput = stdOutput;
  }

  /**
   * Clears the stdOut and stdError.
   */
  public void clear() {
    stdOutput = "";
    stdError = "";
  }

  /**
   * Gets the stdError.
   *
   * @return the stdError
   */
  public String getStdError() {
    return stdError;
  }

  /**
   * Sets the stdError to the given string.
   *
   * @param standardError the new stdError
   */
  public void setStdError(String standardError) {
    stdError = standardError;
  }

  /**
   * Adds the string to stdEroor.
   *
   * @param standardError the string to be added to stdError
   */
  public void addStdError(String standardError) {
    if (!stdError.isEmpty()) {
      stdError += "\n";
    }
    stdError += standardError;
  }

  /**
   * Adds the error to stdError and clears stdOut.
   *
   * @param standardError the error to be added to stdError
   */
  public void addErrorClearOutput(String standardError) {
    addStdError(standardError);
    setStdOutput("");
  }

  /**
   * Gets the all output.
   *
   * @return the all output
   */
  public String getAllOutput() {
    return getStdError() + getStdOutput();
  }

  /**
   * Redirects the current stdOutput to the given file, and overwrites or
   * appends as specified in type.
   *
   * @param file the file
   * @param type the type
   * @param curDir the cur dir
   * @param rootDir the root dir
   */
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
      addContent((File) outFile, type);
    // If the outFile isn't a File and another error wasn't raised then
    // raise an error
    else if (!getStdOutput().isEmpty())
      addErrorClearOutput(
          "ERROR: There is already a subdirectory with the same name");
  }

  /**
   * Writes the output to the file and overwrites/appends as given by type.
   *
   * @param file the file where output will be stored
   * @param type whether to append or overwrite to the file
   */
  public void addContent(File file, String type) {
    if (file != null) {
      if (type.equals(">"))
        file.setContent(getStdOutput());
      else {
        file.appendContent(getStdOutput());
        getClass();
      }
      setStdOutput("");
    }
  }

  /**
   * Creates the file from the given path, and throws an exception otherwise.
   *
   * @param curDir the cur dir
   * @param rootDir the root dir
   * @param path the path to the file
   * @return the file
   * @throws InvalidNameException if the file has an invalid character
   * @throws InvalidDirectoryPathException if parent directory doesn't exist
   * @throws NameExistsException there already is a file/directory with the same
   *         name
   * @throws InvalidAdditionException adding a file to the same file
   */
  private File createFileFromPath(Directory curDir, Directory rootDir,
      String path) throws InvalidNameException, InvalidDirectoryPathException,
      NameExistsException, InvalidAdditionException {
    File outputFile = new File("");
    // Check if the file is going to be in the current directory
    // or a different directory
    if (((String) path).contains("/")) {
      // If it's a different directory get that directory and add the file
      // to it
      outputFile = new File(path.substring(path.lastIndexOf("/") + 1));
      Directory fileDir = (Directory) FilePathInterpreter.interpretPath(
          path.startsWith("/") ? rootDir : curDir,
          path.substring(0, path.lastIndexOf("/")));
      fileDir.add(outputFile);
    } else {
      // If it's the same directory
      outputFile = new File((String) path);
      curDir.add(outputFile);
    }
    return outputFile;
  }
}
