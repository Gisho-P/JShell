package driver;

import java.util.Arrays;

public class CommandInterpreter {
	private ShellFunctions functions;
	public CommandInterpreter(MySession session) {
		functions = new ShellFunctions(session);
	}

	public String interpretCmd(String cmd) {
		System.out.println("DEBUG: Your cmd is " + cmd);
		cmd = cmd.trim();
		cmd = cmd.replaceAll("[\\s]+", " ");
		System.out.println("DEBUG: Your formatted cmd is " + cmd);
		// Store the output here
		String output = "";

		// Splitting the cmd
		String[] cmdArgs = cmd.split(" ");
		switch (cmdArgs[0]) {
			case "mkdir":
				if (cmdArgs.length == 1) {
					output = "mkdir usage: mkdir DIR [DIR2] ...";
				} else {// return output from function call
					output = functions.mkdir(
							Arrays.copyOfRange(cmdArgs, 1, cmdArgs.length));
				}
				break;
			case "cd":
				if (cmdArgs.length != 2) {
					output = "cd usage: cd DIR"; // error, print usage
				} else { // return output from function call
					output = functions.cd(cmdArgs[1]);
				}
				break;
			case "ls":
				if (cmdArgs.length > 2) {
					output = "ls usage: ls [PATH]"; // error, print usage
				} else if (cmdArgs.length == 2){
					output = functions.ls(Arrays.copyOfRange(
					    cmdArgs, 1, cmdArgs.length)); // return output from function call
				} else{
				    output = functions.ls();
				}
				break;
			case "pwd":
				if (cmdArgs.length != 1) {
					output = "pwd usage: pwd"; // error, print usage
				} else {
					output = functions.pwd(); // return output from function call
				}
				break;
			case "pushd":
				if (cmdArgs.length != 2) {
					output = "pushd usage: pushd DIR"; // error, print usage
				} else {
					output = functions.pushd(cmdArgs[1]); // return output from function call
				}
				break;
			case "popd":
				if (cmdArgs.length != 1) {	
					output = "popd usage: popd"; // error, print usage
				} else {
					output = functions.popd(); // return output from function call
				}
				break;
			case "history":
				if (cmdArgs.length > 2) {
					output = "history usage: history [number]";
				} else {
					if (cmdArgs.length == 2) {
						output = functions.history(cmdArgs[1]);
					} else {
						output = functions.history();
					}
					 // return output from function call
				}
				break;
			case "cat":
				if (cmdArgs.length < 2) {
					output = "cat usage: cat FILE [FILE2] ...";
				} else {
					output = functions.cat(
							Arrays.copyOfRange(cmdArgs, 1, cmdArgs.length));
							// return output from function call
				}
				break;
			case "echo":
				if (cmdArgs.length > 4 || cmdArgs.length < 2 ) {
					output = "echo usage: STRING [>[>] OUTFILE]";
				} else {
					if (cmdArgs.length == 2) {
					
					} else if (cmdArgs.length == 3) {
						// Append to file
					    if(cmdArgs[2].equals(">>")){
						  output = functions.echo(cmdArgs[3], false);
						}
					    // Write to file
					    else if(cmdArgs[2].equals(">")){
					      output = functions.echo(cmdArgs[3], true);
					    }
					} else {
						
					}
				}
				break;
			case "man":
				if (cmdArgs.length != 2) {
					output = "man usage: man CMD"; // error, print usage
				} else { // return output from function call
					output = functions.man(cmdArgs[1]);
				}
				break;
			default:
				output = "ERROR: Invalid command.\n";
		};
		return output;
	}
}