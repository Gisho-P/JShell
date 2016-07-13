package structures;

import driver.*;
import structures.Directory.InvalidAddition;
import structures.Directory.NameExistsException;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import structures.FileTypes.InvalidName;

/**
 * The Class Output stores standard output and error strings and redirects
 * output to files.
 */
public class Output {
  private String stdOutput;
  private String stdError;
  
  public Output(){
    stdOutput = "";
    stdError = "";
  }
  
  public String getStdOutput() {
     return stdOutput;
  }
  
  public void addStdOutput(String stdOutput){
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
  
  public void addStdError(String standardError){
    if (!stdError.isEmpty()) {
      stdError += "\n";
    }
    stdError += standardError;
  }
  
  public String getAllOutput(){
     return getStdError() + getStdOutput();
  }
  
  public void redirect(String file, String type, Directory directory) {
    File outFile = null;
    // Check if the file exists in the directory
    try {
      outFile = (File) FilePathInterpreter.interpretPath(directory, file);
    }
    // If the file doesn't exist create it
    catch (InvalidDirectoryPathException e) {
      try {
        outFile = createFileFromPath(directory, file);
      } catch (InvalidDirectoryPathException e1) {
         addStdError("ERROR: The directory of the file does not exist");
      } catch (NameExistsException e1) {
        addStdError("ERROR: There is already a subdirectory with the same name");
      } catch (InvalidAddition e1) {
      } catch (InvalidName e1) {
         addStdError("ERROR: That's an invalid file name");
      }
    } catch (ClassCastException e) {
       addStdError("ERROR: There is already a subdirectory with the same name");
    }
    redirectFile(outFile, type);
  }
  
  public void redirectFile(File file, String type) {
    if(file != null) {
      // Write to the file, overwrite or append as given
      if (type.equals(">"))
        file.setContent(getStdOutput());
      else {
        file.appendContent(getStdOutput());getClass();
      }
      this.stdOutput = "";
    }
  }
  
  /**
   * Creates the file from the given path, and throws an exception otherwise.
   * @param directory 
   *
   * @param path the path to the file
   * @return the file
   * @throws InvalidName if the file has an invalid character
   * @throws InvalidDirectoryPathException if parent directory doesn't exist
   * @throws NameExistsException there already is a file/directory with the same
   *         name
   * @throws InvalidAddition adding a file to the same file
   */
  private File createFileFromPath(Directory directory, String path) throws InvalidName,
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
          (Directory) FilePathInterpreter.interpretPath(directory,
              path.substring(0, path.lastIndexOf("/")));
      fileDir.add(outputFile);
    } else {
      // If it's the same directory
      outputFile = new File((String) path);
      directory.add(outputFile);
    }
    return outputFile;
  }
}
