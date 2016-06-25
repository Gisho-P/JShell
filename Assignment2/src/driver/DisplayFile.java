package driver;

import java.util.List;

public class DisplayFile implements Command {

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
	public String format() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exec(List<String> args) {
		// TODO Auto-generated method stub
		return null;
	}

}
