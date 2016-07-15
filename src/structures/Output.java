package structures;

import driver.*;
import exceptions.*;

/**
 * The Output class stores, retrieves and clears the standard output/error
 * buffers and redirects output to files.
 */
public class Output {

  /** The standard output buffer */
  private String stdOutput;

  /** The standard error buffer */
  private String stdError;

  /**
   * Instantiates a new output object with empty output and error buffers.
   * 
   * @return new Output object
   */
  public Output() {
    stdOutput = "";
    stdError = "";
  }

  /**
   * Gets the std output buffer.
   *
   * @return the std output
   */
  public String getStdOutput() {
    return stdOutput;
  }

  /**
   * Adds the given string to std output buffer.
   *
   * @param stdOutput the string to be added to stdOut
   */
  public void addStdOutput(String stdOutput) {
    if (!this.stdOutput.isEmpty()) { // if output isn't empty add a newline
      this.stdOutput += "\n";
    }
    this.stdOutput += stdOutput;
  }

  /**
   * Sets the std output buffer to the given string.
   *
   * @param stdOutput the string to replace the current std output
   */
  public void setStdOutput(String stdOutput) {
    this.stdOutput = stdOutput;
  }

  /**
   * Clears the std output and std error buffers.
   */
  public void clear() {
    stdOutput = "";
    stdError = "";
  }

  /**
   * Gets the std error buffer.
   *
   * @return std error buffer
   */
  public String getStdError() {
    return stdError;
  }

  /**
   * Sets the std error buffer to the given string.
   *
   * @param standardError the new stdError
   */
  public void setStdError(String standardError) {
    stdError = standardError;
  }

  /**
   * Adds the string specified to std error buffer.
   *
   * @param standardError the string to be added to stdError
   */
  public void addStdError(String standardError) {
    if (!stdError.isEmpty()) { // if error buffer isn't empty, add newline
      stdError += "\n";
    }
    stdError += standardError;
  }

  /**
   * Adds the error to std error buffer and clears std output buffer.
   *
   * @param standardError the error to be added to std error
   */
  public void addErrorClearOutput(String standardError) {
    addStdError(standardError);
    setStdOutput("");
  }

  /**
   * Gets contents from the error and output buffers.
   *
   * @return the error and output buffers contents
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

    try { // Check if the file exists in the directory
      outFile = FilePathInterpreter.interpretPath(curDir, file);
    } catch (InvalidDirectoryPathException e) {
      try { // If the file doesn't exist create it
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
   * Creates the file from the given path or throws an exception otherwise.
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
    } else { // If it's the same directory
      outputFile = new File((String) path);
      curDir.add(outputFile);
    }
    return outputFile;
  }
}
