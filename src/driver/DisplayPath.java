package driver;

import java.util.List;

public class DisplayPath implements Command {

	private MySession s;
	
	public DisplayPath(MySession session) {
		s = session;
	}
	
	@Override
	public String man() {
		return "PWD(1)\t\t\t\tUser Commands\t\t\t\tPWD(1)\n\nNAME" +
               "\n\t\tpwd - prints the current directory\n\n" +
               "SYNOPSIS\n\t\tpwd\n\nDESCRIPTION\n\t\tPrints the" +
               " current working directories full path to standard " +
               "output";
	}

	@Override
	public String interpret(List<String> args) {
		return exec(args);
	}

	@Override
	public String exec(List<String> args) {
		if (args.size() != 1) {
			return "pwd usage: pwd";
		} else {
			return s.getCurrentDir().getEntirePath();
		}
	}

}