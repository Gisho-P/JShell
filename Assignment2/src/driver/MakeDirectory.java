package driver;

import java.util.List;

public class MakeDirectory implements Command {

	@Override
	public String man() {
		return "MKDIR(1)\t\t\t\tUser Commands\t\t\t\tMKDIR(1)\n\n"
        	   + "NAME\n\t\tmkdir - creates directory(s)\n\nSYNOPSIS" +
               "\n\t\tmkdir DIR ...\n\nDESCRIPTION\n\t\tCreates " +
               "directories which may be in the current directory " +
               "or a full\n\t\tpath relative to the root/current " +
               "directory.";
	}

	@Override
	public String exec(List<String> cmdArgs, MySession session) {
		String output;
		if (cmdArgs.size() == 1) {
			output = "mkdir usage: mkdir DIR [DIR2] ...";
		} else {// return output from function call
			output = execHelper(cmdArgs.subList(1, cmdArgs.size()), session);
		}
		return output;
	}

	private String execHelper(List<String> directory, MySession session) {
		String message = "";
		FileTypes parentDir;
		int slashIndex;
		String splitPath[];
		for (String i : directory) {
			try {
				if ((slashIndex = i.indexOf("/")) == -1) {
					session.getCurrentDir().add(new Directory(i));
				} else {
					splitPath = i.split("/");
					if (splitPath.length != 0) {
						parentDir = FilePathInterpreter.interpretMakePath(session.getCurrentDir(), i);
//                        System.out.println(((Directory) parentDir).getName());
						((Directory) parentDir).add(new Directory(splitPath[splitPath.length - 1]));
					} else
						message += "mkdir can't create a directory without a name";
				}
			} catch (Directory.NameExistsException e) {
				message += "mkdir: cannot create directory '" + i + "': File exists\n";
			} catch (Directory.InvalidAddition invalidAddition) {
				invalidAddition.printStackTrace();
			} catch (FilePathInterpreter.InvalidDirectoryPathException e) {
				message += "mkdir: cannot create directory '" + i + "': Invalid Path\n";
			} catch (FileTypes.InvalidName invalidName) {
				message += "mkdir: cannot create directory with name " + i + ". The name is invalid.\n";
			}
		}
		return message;
	}

}
