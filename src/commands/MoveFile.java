package commands;

import java.util.List;

import structures.Directory;
import structures.Directory.InvalidAddition;
import structures.Directory.MissingNameException;
import structures.Directory.NameExistsException;
import structures.File;
import structures.FileTypes;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.MySession;
import structures.Output;

/**
 * The Class DisplayFile handles the moveing of a file to another dir.
 */
public class MoveFile implements Command {

	// MySession is used to access the files by finding them through the
	// root or current directory
	private MySession s;
	private Output out;

	public MoveFile(MySession session) {
		s = session;
		out = new Output();
	}

	/**
	 * Returns the manual for the mv command.
	 * 
	 * @return the manual for the mv command
	 */
	@Override
	public String man() {
		return "mv(1)\t\t\t\tUser Commands\t\t\t\tmv(1)\n"
				+ "\nNAME\n\t\tmv - displays the contents of one or "
				+ "more files on the standard output\n\nSYNOPSIS\n\t\t"
				+ "mv FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t"
				+ "Conmvenates one or more files to the standard output"
				+ ".\n\t\tCan take any amount of files greater then one as a"
				+ " parameter.";
	}

	/**
	 * Process arguments passed for mv command and determine whether the
	 * command was entered correctly or not.
	 * 
	 * @param args
	 *            Arguments parsed from command
	 * @return The contents of the files given.
	 */
	@Override
	public Output interpret(List<String> args) {
		if (args.size() != 3) {
			return out.withStdError("mv usage: mv Src Dest ...");
		} else {
			return exec(args.subList(1, args.size()));
			// return output from function call
		}
	}

	/**
	 * Copies the file from one directory to another
	 * 
	 * @param args
	 *            Valid arguments parsed from command
	 * @return The contents of the files given.
	 */
	@Override
	public Output exec(List<String> args) {
		try {
			// Getting both the src and dest place
			FileTypes src = FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(0));
			Directory dest = (Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
			
			try {
				Directory parent = (Directory) FilePathInterpreter.interpretMakePath(s.getCurrentDir(), args.get(0));
				
				// Removing the previous if it exists
				if(dest.nameExists(src.getName()) != - 1){
					dest.remove(src.getName());
				}
				
				// Adding either a dir or a file
				if(src instanceof File){
					dest.add(src);
					parent.remove(src.getName());
				}
				else if(src instanceof Directory){
					dest.add(Directory.deepCopy((Directory)src));
					parent.remove(src.getName());
				}
				
			} catch (NameExistsException | InvalidAddition | MissingNameException e) {
				out.addStdError("The file cannot be added either already exists or is not valid.\n");
			}
			
		} catch (InvalidDirectoryPathException e) {
			out.addStdError("The source path does not exist\n");
		}
		return out;
	}
}
