package driver;

import java.util.List;

public class ChangeDirectory implements Command {

  private MySession s;

  public ChangeDirectory(MySession session) {
    s = session;
  }

  @Override
  public String man() {
    return "CD(1)\t\t\t\tUser Commands\t\t\t\tCD(1)\n\nNAME\n\t"
        + "\tcd - changes the working directory of the shell"
        + "\n\nSYNOPSIS\n\t\tcd DIR\n\nDESCRIPTION\n\t\t"
        + "Changes the current directory of the shell to the one"
        + " specified by DIR.\n\n\t\tThe DIR may be relative to "
        + "the current directory or the full path.\n\n\t\t"
        + ".. indicates the parent directory, and . indicates "
        + "the current directory\n\t\twhen specifying the DIR.";
  }

  @Override
  public String interpret(List<String> args) {
    if (args.size() != 2) {
      return "cd usage: cd DIR"; // error, print usage
    } else { // return output from function call
      return exec(args);
    }
  }

  public String exec(List<String> args) {
    try {
      FileTypes dest = FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
      if (dest instanceof Directory) {
        s.setCurrentDir((Directory) dest);
        return "";
      }else
        return (dest.getName() + " is not a directory.");
    } catch (FilePathInterpreter.InvalidDirectoryPathException e) {
      return "No such dir as " + args.get(1);
    }
  }

}
