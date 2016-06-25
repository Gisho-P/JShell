package driver;

import java.util.List;

public class ListDirectoryContents implements Command {

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
