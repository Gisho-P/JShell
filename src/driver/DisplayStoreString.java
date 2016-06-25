package driver;

import java.util.List;

public class DisplayStoreString implements Command {

	@Override
	public String man() {
		return "ECHO(1)\t\t\t\tUser Commands\t\t\t\tECHO(1)\n\n" +
               "NAME\n\t\techo - prints a string to standard output," +
               " or saves a string to a text\n\t\tfile\n\nSYNOPSIS" +
               "\n\t\techo STRING [>[>] OUTFILE]\n\nDESCRIPTION\n\t\t" +
               "Saves the STRING to the OUTFILE if it's provided, " +
               "otherwise it prints\n\t\tthe STRING to the standard " +
               "output.\n\n\t\tIf the OUTFILE is an existing file in" +
               " the directory, it will overwrite\n\t\tthe contents" +
               " of the OUTFILE with the string, otherwise it will" +
               " create\n\t\ta new file with name OUTFILE .\n\n\t\t" +
               "STRING should be a string of characters surrounded " +
               "by double quotation\n\t\tmarks.";
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
