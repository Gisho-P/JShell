package commands;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.List;

import structures.Directory;
import structures.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.MySession;

/**
 * The Class DisplayWithSubstring handles displaying file with a specific
 * pattern.
 */
public class DisplayWithSubstring implements Command {
//TODO: remove use of two methods for exec, simple recursive way of checking
	// MySession is used to access the files by finding them through the
	// root or current directory
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
				+ "grep FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t"
				+ "Concatenates one or more files to the standard output"
				+ ".\n\t\tCan take any amount of files greater then one as a"
				+ " parameter.");
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
	// TODO: 11/07/16 Check if lowercase -r is supposed to work as well. I think i saw it somewhere
	/**
	 * Returns the files with the pattern inside.
	 * 
	 * @param args
	 *            Valid arguments parsed from command
	 * @return The files lines names with the pattern inside.
	 */
	public void execDir(List<String> args) {
		if (!args.get(1).equals("-R")) {
			s.addError("Wrong options for grep no such flag as "
					+ args.get(1));
			return;
		}

		// Adding previous dirs
		ArrayList<Directory> currentDirLevel = new ArrayList<Directory>();
		currentDirLevel.addAll(s.getCurrentDir().getChildDirs());

		// Loop for current files right npw
		for (File path : s.getCurrentDir().getChildFiles()) {
			String lines[] = path.getContent().split("\n");
			for (String line : lines) {
				// Adding a new line
				if (line.matches(args.get(2))) {
					// Adds the new line to std out
					s.addOutput(s.getCurrentDir().getEntirePath() + ":" + line);
				}
			}
		}

		// Recursively go through all sub dirs

		ArrayList<Directory> nextDirLevel = new ArrayList<Directory>();
		while (!currentDirLevel.isEmpty()) {
			// Loop through all the dirs in the current level
			for (Directory dir : currentDirLevel) {
				for (File path : s.getCurrentDir().getChildFiles()) {
					String lines[] = path.getContent().split("\n");
					for (String line : lines) {
						// Adding a new line
						if (line.matches(args.get(2))) {
							s.addOutput(line + "\n" + path);
						}
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
		if (args.size() > 3) {
			execDir(args);
		} else {
			// Splitting the file with new lines
			File path = null;
			try {
				path = ((File) FilePathInterpreter.interpretPath(
						s.getCurrentDir(), args.get(2)));
			} catch (InvalidDirectoryPathException ClassCastException) {
				// If it's an invalid path or a dir same case

				s.addError("No such file as " + args.get(2));
			}

			String lines[] = path.getContent().split("\n");

			for (String line : lines) {
				// Adding a new line
				if (line.matches(args.get(1))) {
					s.addOutput(line + "\n");
				}
			}
			// Removing the last blank space
			s.setOutput(s.getOutput().trim());
		}
	}
}
