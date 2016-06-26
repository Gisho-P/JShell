package driver;

import java.util.List;

public class ExitProgram implements Command {

	private MySession s;
	
	public ExitProgram(MySession session) {
		s = session;
	}
	
	@Override
	public String man() {
		return "EXIT(1)\t\t\t\tUser Commands\t\t\t\tEXIT(1)\n\n"
        	   + "NAME\n\t\texit - terminates the shell\n\nSYNOPSIS\n\t\t" +
               "exit\n\nDESCRIPTION\n\t\tTerminates the users" +
               " session with the shell which removes their ability" +
               "\n\t\tto process any more commands.";
	}

	@Override
	public String exec(List<String> args) {return null;}

	@Override
	public String interpret(List<String> args) {return null;}

}
