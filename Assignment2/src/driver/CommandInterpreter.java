package driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInterpreter {
	private ShellFunctions functions;
	public CommandInterpreter(MySession session) {
		functions = new ShellFunctions(session);
	}

	public String interpretCmd(String cmd) {
		switch (cmdArgs.get(0)) {
			case "mkdir":
				if (cmdArgs.size() == 1) {
					output = "mkdir usage: mkdir DIR [DIR2] ...";
				} else {// return output from function call
					output = functions.mkdir(cmdArgs.subList(1, cmdArgs.size()));
				}
				break;
			case "cd":
				if (cmdArgs.size() != 2) {
					output = "cd usage: cd DIR"; // error, print usage
				} else { // return output from function call
					output = functions.cd(cmdArgs.get(1));
				}
				break;
			case "ls":
				if (cmdArgs.size() > 1){
					output = functions.ls(cmdArgs.subList(1, cmdArgs.size())); // return output from function call
				} else{
				    output = functions.ls();
				}
				break;
			
			case "cat":
				if (cmdArgs.size() < 2) {
					output = "cat usage: cat FILE [FILE2] ...";
				} else {
					output = functions.cat(cmdArgs.subList(1, cmdArgs.size()));
							// return output from function call
				}
				break;
			case "echo":
				if (cmdArgs.size() != 4 && cmdArgs.size() != 2 ) {
					output = "echo usage: STRING [>[>] OUTFILE]";
				} else {
				    if (cmdArgs.get(1).startsWith("\"") && cmdArgs.get(1).endsWith("\"")) {
				      if (cmdArgs.get(1).length() == 2) {
				    	  cmdArgs.set(1, null);
				      } else {
				        cmdArgs.set(1, cmdArgs.get(1).substring(1, cmdArgs.get(1).length() - 1));
				      }
				    } else {
				        output = "ERROR: STRING must be surrounded by double quotations";
				        break;
				    }
				    if (cmdArgs.size() == 2) {
				      output = cmdArgs.get(1) + "\n";
				    } else if (cmdArgs.get(2).equals(">>")) {// Append to file
					  output = functions.echo(cmdArgs.get(1), cmdArgs.get(3), false);
					} else if (cmdArgs.get(2).equals(">")) { // Write to file
					  output = functions.echo(cmdArgs.get(1), cmdArgs.get(3), true);
					}
				}
				break;
		};
	}
}
