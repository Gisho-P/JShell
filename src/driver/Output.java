package driver;

import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes.InvalidName;

/**
 * The Class Output stores standard output and error strings and redirects
 * output to files.
 */
public class Output {
  private String stdOutput;
  private String stdError;
  
  public Output(String standardOutput, String standardError){
    stdOutput = standardOutput;
    stdError = standardError;
  }
  
  public Output(String standardError){
    stdError = standardError;
    stdOutput = "";
  }
  
  public Output(){
    stdOutput = "";
    stdError = "";
  }
  
  public String getStdOutput() {
     return stdOutput;
  }

  public void addStdOutput(String stdOutput) {
    this.stdOutput += stdOutput;
  }
  
  public void setStdOutput(String stdOutput) {
    this.stdOutput = stdOutput;
  }
  
  public Output withStdOutput(String stdOutput, Boolean overwrite) {
    if(overwrite)
      this.stdOutput = stdOutput;
    else
      this.stdOutput += stdOutput;
     return this;
  }

  public String getStdError() {
     return stdError;
  }
  
  public void addStdError(String standardError){
    stdError += standardError;
  }
  
  public Output withStdError(String standardError){
    stdError += standardError;
     return this;
  }
  
  public String getAllOutput(){
     return stdError + stdOutput;
  }
  
  public void redirect(MySession s, boolean overWrite, String file, String txt){
    File outFile = null;
    // Check if the file exists in the directory
    try {
      outFile = (File) FilePathInterpreter.interpretPath(s.getCurrentDir(),
          file);
    }
    // If the file doesn't exist create it
    catch (InvalidDirectoryPathException e) {
      try {
        outFile = createFileFromPath(file, s);
      } catch (InvalidDirectoryPathException e1) {
         addStdError("ERROR: The directory of the file does not exist");
      } catch (NameExistsException e1) {
         addStdError(
             "ERROR: There is already a subdirectory with the same name");
      } catch (InvalidAddition e1) {
      } catch (InvalidName e1) {
         addStdError("ERROR: That's an invalid file name");
      }
    } catch (ClassCastException e) {
       addStdError("ERROR: There is already a subdirectory with the same name");
    }
    if(outFile != null){
      // Write to the file, overwrite or append as given
      if (overWrite)
        outFile.setContent(txt == null ? "" : txt);
      else
        outFile.appendContent(txt == null ? "" : txt);
    }
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
  private File createFileFromPath(String path, MySession s) throws InvalidName,
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
