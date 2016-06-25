package driver;

import java.util.List;

public class ChangeDirectory implements Command {

	@Override
	public String man() {
		return "CD(1)\t\t\t\tUser Commands\t\t\t\tCD(1)\n\nNAME\n\t"
               + "\tcd - changes the working directory of the shell" +
               "\n\nSYNOPSIS\n\t\tcd DIR\n\nDESCRIPTION\n\t\t" +
               "Changes the current directory of the shell to the one" +
               " specified by DIR.\n\n\t\tThe DIR may be relative to " +
               "the current directory or the full path.\n\n\t\t" +
               ".. indicates the parent directory, and . indicates " +
               "the current directory\n\t\twhen specifying the DIR.";
	}

	@Override
	public String exec(List<String> cmdArgs) {
		String output;
		if (cmdArgs.size() != 2) {
			output = "cd usage: cd DIR"; // error, print usage
		} else { // return output from function call
			output = execHelper(cmdArgs.get(1));
		}
		return output;
	}

	public String execHelper(String path) {
		try {
			Directory dest = (Directory) FilePathInterpreter.interpretPath(MySession.getCurrentDir(), path);
			MySession.setCurrentDir(dest);
		} catch (FilePathInterpreter.InvalidDirectoryPathException e) {
			return "No such dir as " + path;
		}
		return "";
	}

}
