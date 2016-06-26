package driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

public class ListDirectoryContents implements Command {

	private MySession s;
	
	public ListDirectoryContents(MySession session) {
		s = session;
	}
	
	@Override
	public String man() {
		return "LS(1)\t\t\t\tUser Commands\t\t\t\tLS(1)\n\nNAME\n\t"
        	   + "\tls - prints out all of the contents of one or many " +
               "files/directories\n\nSYNOPSIS\n\t\tls [PATH ...]\n\n" +
               "DESCRIPTION\n\t\tPrints out the contents of files/" +
               "directories.\n\n\t\tIf PATH is not specified, prints " +
               "out the contents of the current\n\t\tdirectory by " +
               "default.\n\n\t\tIf PATH is specified, prints out the " +
               "contents of the files/directories\n\t\tfor each PATH " +
               "given";
	}
	
	@Override
	public String interpret(List<String> args) {
		return exec(args);
	}

	/**
	 * Lists the name of the given file/directory followed by the contents
	 * if it is a directory.
	 *
	 * @param paths the paths of directories/files to be listed
	 * @return a list of the contents of each of the given paths
	 */
	private String execMult(List<String> paths) {
		String retVal = "";
		ArrayList<String> childNames = new ArrayList<String>();
		String fileName = "";
		// Iterate through each path and get the dir names in each directory or
		// the file name if it's a file
		for (String i : paths) {
			// First we assume the path points to a directory and get the
			// directory then add their children to the list
			try {
				childNames.addAll(((Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(), i)).getChildNames());
				fileName = i + ": ";
			} catch (InvalidDirectoryPathException e) {
				System.out.println("No such directory as " + i);
			} catch (ClassCastException e) {
				// If it wasn't a directory then we assume it's a file and
				// get the file name
				try {
					fileName = (((File) FilePathInterpreter.interpretPath(s.getCurrentDir(), i)).getName());
					// If it doesn't throw exception it means it exists, overwrite with path
					fileName = i;
				} catch (InvalidDirectoryPathException e1) {
					System.out.println("No such directory or file as " + i);
				}
			}
			// Sort the list of children directories/files alphabetically
			Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
			retVal += fileName;
			for (String childName : childNames) {
				retVal += childName + " ";
			}
			retVal += "\n";
			childNames.clear();
		}
		return retVal.substring(0, retVal.length()-1);
}

	/**
	 * Returns the current directory name followed by it's contents.
	 *
	 * @param args the args
	 * @return the contents of the directory
	 */
	@Override
	public String exec(List<String> args) {
		if (args.size() > 1) {
			return execMult(args.subList(1, args.size())); // return output from function call
		} else	{
			String retVal = s.getCurrentDir().getEntirePath() + ": ";
			ArrayList<String> childNames = s.getCurrentDir().getChildNames();
			Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
			for (String childName : childNames) {
				retVal += childName + " ";
			}
			return retVal;
		}
	}

}
