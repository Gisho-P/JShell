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
