package commands;

import java.util.List;

import driver.MySession;
import structures.Output;

public class ExecuteFromHistory implements Command {

	private MySession s;
	private Output out;
	
	public ExecuteFromHistory(MySession session) {
	    s = session;
	    out = new Output();
	}
	
	@Override
	public String man() {
		return "!(1)\t\t\t\tUser Commands\t\t\t\t!(1)"
				+ "\n\nNAME\n\t\t! - allows user to execute command "
				+ "from history, based\n\t\ton user specified number"
				+ "\n\nSYNOPSIS\n\t\t!NUMBER > 0 && <= # of commands run"
				+ "\n\nDESCRIPTION\n\t\tThis command allows a user to execute"
				+ "a command that they\n\t\thad once run.\n\t\tIn order to do "
				+ "so, the user needs to specify which\n\t\tcommand from "
				+ "history they\n\t\twant to with a number.\n\t\tE.G. !3 - "
				+ "executes the third command typed by a user.";
	}

	@Override
	public Output interpret(List<String> args) {
		return (args.size() == 2) ? exec(args) : out.withStdError("! usage: !NUMBER > 0");
	}

	@Override
	public Output exec(List<String> args) {
		// TODO Auto-generated method stub
		return null;
	}

}
