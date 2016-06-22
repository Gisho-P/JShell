package driver;

import java.util.Arrays;
import java.util.stream.Stream;

public class CommandInterpreter {
	private ShellFunctions functions;
	public CommandInterpreter(MySession session) {
		functions = new ShellFunctions(session);
	}

	public String interpretCmd(String cmd) {
		System.out.println("DEBUG: Your cmd is " + cmd);
		cmd = cmd.trim();
		cmd = cmd.replaceAll("[\\s]+", " ");
		if (!cmd.startsWith("\"") || !cmd.endsWith("\"")) {
			return "ERROR: Command incorrectly inputted (need quotations"
					+ " around command)";
		}
		cmd = cmd.substring(1, cmd.length()-1).trim();
		System.out.println("DEBUG: Your formatted cmd is " + cmd);
		// Store the output here
		String output = "";
		String[] cmdArgs = {};
		
		// Splitting the cmd
		if(cmd.contains("\"")){
		  // If cmd contains a string seperate it while keeping the STRING
		  // as one command
		  String beforeQuotes = cmd.substring(0, cmd.indexOf("\"")).trim();
		  String afterQuotes = cmd.substring(cmd.lastIndexOf("\"") + 1).trim();
		  cmdArgs = beforeQuotes.split(" ");
		  cmdArgs = Stream.concat(Arrays.stream(cmdArgs), Arrays.stream(
		      new String [] {cmd.substring(cmd.indexOf("\""), cmd.lastIndexOf("\"") + 1)}))
              .toArray(String[]::new);
		  if(!afterQuotes.isEmpty())
		    cmdArgs = Stream.concat(Arrays.stream(cmdArgs), Arrays.stream(
              afterQuotes.split(" ")))
              .toArray(String[]::new);
		}
		else
		  cmdArgs = cmd.split(" ");
		
		System.out.println(Arrays.toString(cmdArgs));
		
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
				if (cmdArgs.length > 1){
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
				if (cmdArgs.length != 4 && cmdArgs.length != 2 ) {
					output = "echo usage: STRING [>[>] OUTFILE]";
				} else {
				    if(cmdArgs[1].startsWith("\"") && cmdArgs[1].endsWith("\"")){
				      if(cmdArgs[1].length() == 2)
				        cmdArgs[1] = null;
				      else
				        cmdArgs[1] = cmdArgs[1].substring(1, cmdArgs[1].length() - 1);
				    } else{
				        output = "ERROR: STRING must be surrounded by double quotations";
				        break;
				    }
				    if(cmdArgs.length == 2)
				      output = cmdArgs[1];
				    
					// Append to file
				    else if(cmdArgs[2].equals(">>")){
					  output = functions.echo(cmdArgs[1], cmdArgs[3], false);
					}
					// Write to file
					else if(cmdArgs[2].equals(">")){
					  output = functions.echo(cmdArgs[1], cmdArgs[3], true);
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
