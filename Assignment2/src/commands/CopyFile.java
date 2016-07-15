package commands;

import java.util.List;

import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.NameExistsException;

/**
 * The Class DisplayFile handles the copying of a file to another dir.
 */
public class CopyFile implements Command {

	// MySession is used to access the files by finding them through the
	// root or current directory
	private MySession s;

	public CopyFile(MySession session) {
		s = session;
	}

	/**
	 * Returns the manual for the cp command.
	 * 
	 * @return the manual for the cp command
	 */
	@Override
	public void man() {
		s.setOutput("cp(1)\t\t\t\tUser Commands\t\t\t\tcp(1)\n"
				+ "\nNAME\n\t\tcp - Copy file from source to destination. \n" +
				"\n" +
				"SYNOPSIS\n" +
				"\t\t" +
				"\n\t\t"
				+ "cp FILE1 [FILE2]\n\nDESCRIPTION\n\t\t"
				+ "Copy the file from the source path to the \n" +
				"destination paths if it is valid.");
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
	public void interpret(List<String> args) {
		if (args.size() != 3) {
			s.setError("cp usage: cp Src Dest");
		} else {
			exec(args.subList(1, args.size()));
			// return output from function call
		}
	}

	public void exec(List<String> args) {
		MoveFile mv = new MoveFile(s);
		mv.exec(args);
		if (s.getError() == "") {
			try {
				mv.getSourceParent().add(mv.getSourceCopy());
				//shouldn't make it to these two branches
			} catch (NameExistsException e) {
				s.addError(e.getMessage());
			} catch (InvalidAdditionException e) {
				s.addError(e.getMessage());
			}
		}
	}

	/**
	 * Copies the file from one directory to another
	 * 
	 * @param args
	 *            Valid arguments parsed from command
	 * @return The contents of the files given.
	 */
//	public void exec(List<String> args) {
//		try {
//			FileTypes src = FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(0));
//			Directory dest = (Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(), args.get(1));
//
//			try {
//				if(src instanceof File){
//					dest.add(File.copy((File)src));
//				}
//				else if(src instanceof Directory){
//					dest.add(Directory.deepCopy((Directory)src));
//				}
//
//			} catch (NameExistsException | InvalidAdditionException e) {
//				s.addError("The file cannot be added either already exists or is not valid.\n");
//			}
//
//		} catch (InvalidDirectoryPathException e) {
//			s.addError("The source path does not exist\n");
//		}
//	}
}
