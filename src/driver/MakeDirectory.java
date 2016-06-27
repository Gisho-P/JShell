package driver;

import java.util.List;

public class MakeDirectory implements Command {

  private MySession s;

  public MakeDirectory(MySession session) {
    s = session;
  }

  @Override
  public String man() {
    return "MKDIR(1)\t\t\t\tUser Commands\t\t\t\tMKDIR(1)\n\n"
        + "NAME\n\t\tmkdir - creates directory(s)\n\nSYNOPSIS"
        + "\n\t\tmkdir DIR ...\n\nDESCRIPTION\n\t\tCreates "
        + "directories which may be in the current directory "
        + "or a full\n\t\tpath relative to the root/current " + "directory.";
  }

  @Override
  public String interpret(List<String> cmdArgs) {
    String output;
    if (cmdArgs.size() == 1) {
      output = "mkdir usage: mkdir DIR [DIR2] ...";
    } else {// return output from function call
      output = exec(cmdArgs.subList(1, cmdArgs.size()));
    }
    return output;
  }

  @Override
  public String exec(List<String> directory) {
    String message = "";
    FileTypes parentDir;
    int slashIndex;
    String splitPath[];
    for (String i : directory) {
      try {
        if ((slashIndex = i.indexOf("/")) == -1) {
          s.getCurrentDir().add(new Directory(i));
        } else {
          splitPath = i.split("/");
          if (splitPath.length != 0) {
            parentDir =
                FilePathInterpreter.interpretMakePath(s.getCurrentDir(), i);
            ((Directory) parentDir)
                .add(new Directory(splitPath[splitPath.length - 1]));
          } else
            message += "mkdir: cannot create a directory without a name";
        }
      } catch (Directory.NameExistsException e) {
        message += "mkdir: cannot create directory '" + i + "': File exists";
      } catch (Directory.InvalidAddition invalidAddition) {
        invalidAddition.printStackTrace();
      } catch (FilePathInterpreter.InvalidDirectoryPathException e) {
        message += "mkdir: cannot create directory '" + i + "': Invalid Path";
      } catch (FileTypes.InvalidName invalidName) {
        message += "mkdir: cannot create directory with name '" + i
            + "'. It is invalid.";
      }
    }
    return message;
  }

}
