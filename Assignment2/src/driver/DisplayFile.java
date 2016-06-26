package driver;

import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

public class DisplayFile implements Command {

	private MySession s;
	
	public DisplayFile(MySession session) {
		s = session;
	}
	
	@Override
	public String man() {
		return "CAT(1)\t\t\t\tUser Commands\t\t\t\tCAT(1)\n" +
               "\nNAME\n\t\tcat - displays the contents of one or " +
               "more files on the standard output\n\nSYNOPSIS\n\t\t" +
               "cat FILE1 [FILE2 ...]\n\nDESCRIPTION\n\t\t" +
               "Concatenates one or more files to the standard output" +
               ".\n\t\tCan take any amount of files greater then one as a" +
               " parameter.";
	}

	@Override
	public String interpret(List<String> args) {
		if (args.size() < 2) {
			return "cat usage: cat FILE [FILE2] ...";
		} else {
			return exec(args.subList(1, args.size()));
					// return output from function call
		}
	}

	@Override
	public String exec(List<String> args) {
		String retVal = "";
        Boolean firstFile = true;
        
        for (String i : args) {
            // print three line breaks in between files
            if (!firstFile)
                retVal += "\n\n\n";
            try {
                retVal += ((File) FilePathInterpreter.interpretPath(s.getCurrentDir(), i)).getContent();
            } catch (InvalidDirectoryPathException e) {
                retVal = "No such dir as " + i;
            } catch (ClassCastException e) {
                retVal = "Unable to cat dir " + i;
            }
            if (firstFile)
                firstFile = false;
        }
        return retVal;
	}

}
