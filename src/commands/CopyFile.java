package commands;

import java.util.List;

import structures.Directory;
import structures.Directory.InvalidAddition;
import structures.Directory.NameExistsException;
import structures.File;
import structures.FileTypes;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.MySession;
import structures.Output;

/**
 * The Class DisplayFile handles the copying of a file to another dir.
 */
public class CopyFile implements Command {

	// MySession is used to access the files by finding them through the
	// root or current directory
	private MySession s;
	private Output out;

	public CopyFile(MySession session) {
		s = session;
		out = new Output();
	}

	/**
	 * Returns the manual for the cp command.
	 * 
	 * @return the manual for the cp command
	 */
	@Override
	public String man() {
		return "cp(1)\t\t\t\tUser Commands\t\t\t\tcp(1)\n"
				+ "\nNAME\n\t\tcp - displays the contents of one or "
				+ "more files on the standard output\n\nSYNOPSIS\n\t\t"
				+ "cp FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t"
				+ "Concpenates one or more files to the standard output"
				+ ".\n\t\tCan take any amount of files greater then one as a"
				+ " parameter.";
	}

	/**
	 * Process arguments passed for cp command and determine whether the
	 * command was entered correctly or not.
	 * 
	 * @param args
	 *            Arguments parsed from command
	 * @return The contents of the files given.
	 */
	@Override
	public Output interpret(List<String> args) {
		if (args.size() != 3) {
			return out.withStdError("cp usage: cp Src Dest ...");
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
			FileTypes src = FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(0));
			Directory dest = (Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
			
			try {
				if(src instanceof File){
					dest.add(src);
				}
				else if(src instanceof Directory){
					dest.add(Directory.deepCopy((Directory)src));
				}
				
			} catch (NameExistsException | InvalidAddition e) {
				out.addStdError("The file cannot be added either already exists or is not valid.\n");
			}
			
		} catch (InvalidDirectoryPathException e) {
			out.addStdError("The source path does not exist\n");
		}
		return out;
	}
}
