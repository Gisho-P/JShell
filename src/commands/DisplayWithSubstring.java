package commands;

import java.util.*;

import structures.*;
import driver.*;
import exceptions.InvalidDirectoryPathException;

/**
 * The Class DisplayWithSubstring handles displaying file with a specific
 * pattern.
 */
public class DisplayWithSubstring implements Command {
	private MySession s;

	public DisplayWithSubstring(MySession session) {
		s = session;
	}

	/**
	 * Returns the manual for the grep command.
	 * 
	 * @return the manual for the grep command
	 */
	@Override
	public void man() {
		s.setOutput("GREP(1)\t\t\t\tUser Commands\t\t\t\tGREP(1)\n"
				+ "\nNAME\n\t\tcat - displays the contents of one or "
				+ "more files on the standard output\n\nSYNOPSIS\n\t\t"
				+ "grep [-OPTIONS] REGEX FILETYPE \n\nDESCRIPTION\n\t\t"
				+ "Prints out a list of all lines of files that contain the regex."
				+ ".\n\t\tCan put -r flag to recrusive through a directory instead."
				+ "");
	}

	/**
	 * Process arguments passed for grep command and determine whether the
	 * command was entered correctly or not.
	 * 
	 * @param args
	 *            Arguments parsed from command
	 * @return The files lines names with the pattern inside.
	 */
	@Override
	public void interpret(List<String> args) {
		if (args.size() < 3) {
			s.addError("grep usage: grep [options] REGEX PATH ...");
		} else {
			exec(args);
			// return output from function call
		}
	}

	// TODO: 11/07/16 Check if lowercase -r is supposed to work as well. I think
	// i
	// saw it somewhere
	/**
	 * Returns the files with the pattern inside.
	 * 
	 * @param args
	 *            Valid arguments parsed from command
	 * @return The files lines names with the pattern inside.
	 */
	public void execDir(List<String> args) {
		// Adding previous dirs
		ArrayList<Directory> currentDirLevel = new ArrayList<Directory>();
		try {
			Directory start = ((Directory) FilePathInterpreter.interpretPath(
					s.getCurrentDir(), args.get(3)));
			currentDirLevel.add(start);
		} catch (InvalidDirectoryPathException ClassCastException) {
			// If it's an invalid path or a dir same case
			s.addError("No such directory as " + args.get(3));
		}
		// Recursively go through all sub dirs
		ArrayList<Directory> nextDirLevel = new ArrayList<Directory>();
		while (!currentDirLevel.isEmpty()) {
			// Loop through all the dirs in the current level
			for (Directory dir : currentDirLevel) {
				for (File path : dir.getChildFiles()) {
					for (String item : getWithSubstring(path, args.get(2))) {
						s.addOutput(path.getAbsolutePath() + ":" + item);
					}
				}
				// Add them to the set of next level
				nextDirLevel.addAll(dir.getChildDirs());
			}
			// Increment the level by one
			currentDirLevel = nextDirLevel;
		}
		// Removing the last blank space
		s.setOutput(s.getOutput().trim());
	}

	/**
	 * Recursively goes through a dir and checks every file and returns all
	 * lines in files that contain the specified pattern.
	 * 
	 * @param args
	 *            Valid arguments parsed from command
	 * @return The files lines names with the pattern inside.
	 */
	@Override
	public void exec(List<String> args) {
		if (args.size() == 4 && args.get(1).toLowerCase().equals("-r")) {
			execDir(args);
		} else {
			// Splitting the file with new lines
			File path = null;
			try {
				path = ((File) FilePathInterpreter.interpretPath(
						s.getCurrentDir(), args.get(2)));
			} catch (ClassCastException | InvalidDirectoryPathException e) {
				// If it's an invalid path or a dir same case
				s.addError("No such file as " + args.get(2));
			}
			for (String item : getWithSubstring(path, args.get(1))) {
				s.addOutput(item);
			}
			// Removing the last blank space
			s.setOutput(s.getOutput().trim());
		}
	}

	/**
	 * Returns a list lines in a file that match a regex.
	 * 
	 * @return List of lines that match a regex
	 */
	public ArrayList<String> getWithSubstring(File file, String regex) {
		String originalLines[] = file.getContent().split("\n");
		ArrayList<String> lines = new ArrayList<String>();

		for (String line : originalLines) {
			// Adding a new line
			if (line.matches(regex)) {
				lines.add(line);
			}
		}
		return lines;
	}
}
