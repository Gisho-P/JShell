package driver;

import java.util.List;

public class MakeDirectory implements Command {

	@Override
	public String man() {
		return "MKDIR(1)\t\t\t\tUser Commands\t\t\t\tMKDIR(1)\n\n"
        	   + "NAME\n\t\tmkdir - creates directory(s)\n\nSYNOPSIS" +
               "\n\t\tmkdir DIR ...\n\nDESCRIPTION\n\t\tCreates " +
               "directories which may be in the current directory " +
               "or a full\n\t\tpath relative to the root/current " +
               "directory.";
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
