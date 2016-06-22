package driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CommandInterpreter {
	private ShellFunctions functions;
	public CommandInterpreter(MySession session) {
		functions = new ShellFunctions(session);
	}

	public String interpretCmd(String cmd) {
		System.out.println("DEBUG: Your cmd is " + cmd);
		cmd = cmd.trim();
		//cmd = cmd.replaceAll("[\\s]+", " ");

		System.out.println("DEBUG: Your formatted cmd is " + cmd);
		// Store the output here
		String output = "";
		List<String> cmdArgs = new ArrayList<String>();
		
		// Splitting the cmd
		if (cmd.contains("\"")) {
			// If cmd contains a string seperate it while keeping the STRING
			// as one command
			String beforeQuotes = cmd.substring(0, cmd.indexOf("\"")).trim();
			String afterQuotes = cmd.substring(cmd.lastIndexOf("\"") + 1).trim();
			beforeQuotes = beforeQuotes.replaceAll("[\\s]+", " ");
			afterQuotes = afterQuotes.replaceAll("[\\s]+", " ");
			cmdArgs.addAll(Arrays.asList(beforeQuotes.split(" ")));
			cmdArgs.add(cmd.substring(cmd.indexOf("\""), cmd.lastIndexOf("\"") + 1));
			if (!afterQuotes.isEmpty()) {
				cmdArgs.addAll(Arrays.asList(afterQuotes.split(" ")));
			}
		} else {
			cmd = cmd.replaceAll("[\\s]+", " ");
			cmdArgs = Arrays.asList(cmd.split(" "));
		}
		
		System.out.println(Arrays.toString(cmdArgs.toArray()));
		
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
			case "pwd":
				if (cmdArgs.size() != 1) {
					output = "pwd usage: pwd"; // error, print usage
				} else {
					output = functions.pwd(); // return output from function call
				}
				break;
			case "pushd":
				if (cmdArgs.size() != 2) {
					output = "pushd usage: pushd DIR"; // error, print usage
				} else {
					output = functions.pushd(cmdArgs.get(1)); // return output from function call
				}
				break;
			case "popd":
				if (cmdArgs.size() != 1) {	
					output = "popd usage: popd"; // error, print usage
				} else {
					output = functions.popd(); // return output from function call
				}
				break;
			case "history":
				if (cmdArgs.size() > 2) {
					output = "history usage: history [number]";
				} else {
					if (cmdArgs.size() == 2) {
						output = functions.history(cmdArgs.get(1));
					} else {
						output = functions.history();
					}
					 // return output from function call
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
				      output = cmdArgs.get(1);
				    } else if (cmdArgs.get(2).equals(">>")) {// Append to file
					  output = functions.echo(cmdArgs.get(1), cmdArgs.get(3), false);
					} else if (cmdArgs.get(2).equals(">")) { // Write to file
					  output = functions.echo(cmdArgs.get(1), cmdArgs.get(3), true);
					}
				}
				break;
			case "man":
				if (cmdArgs.size() != 2) {
					output = "man usage: man CMD"; // error, print usage
				} else { // return output from function call
					output = functions.man(cmdArgs.get(1));
				}
				break;
			default:
				output = "ERROR: Invalid command.\n";
				break;
		};
		return output;
	}
}
