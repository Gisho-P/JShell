package driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommandInterpreter {

	public CommandInterpreter() {
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
			} else {
				output = ""; // return output from function call
			}
		case "cd":
			if (cmdArgs.length != 2) {
				output = "cd usage: cd DIR"; // error, print usage
			} else {
				output = "";  // return output from function call
			}
		case "ls":
			if (cmdArgs.length > 2) {
				output = "ls usage: ls [PATH]"; // error, print usage
			} else {
				output = ""; // return output from function call
			}
		case "pwd":
			if (cmdArgs.length != 1) {
				output = "pwd usage: pwd"; // error, print usage
			} else {
				output = ""; // return output from function call
			}
		case "pushd":
			if (cmdArgs.length != 2) {
				output = "pushd usage: pushd DIR"; // error, print usage
			} else {
				output = ""; // return output from function call
			}
		case "popd":
			if (cmdArgs.length != 1) {	
				output = "popd usage: popd"; // error, print usage
			} else {
				output = ""; // return output from function call
			}
		case "history":
			if (cmdArgs.length > 2) {
				output = "history usage: history [number]";
			} else {
				if (cmdArgs.length == 2) {
					try {
						int arg = Integer.parseInt(cmdArgs[1]);
						output = MySession.printCommandHistory(arg);
						
					} catch (NumberFormatException n) {
						output = "history usage: history [number >= 0]";
					}
				} else {
					output = MySession.printCommandHistory();
				}
				 // return output from function call
			}
		case "cat":
			if (cmdArgs.length < 2) {
				output = "cat usage: cat FILE [FILE2] ...";
			} else {
				output = ""; // return output from function call
			}
		case "echo":
			if (cmdArgs.length != 2 || cmdArgs.length != 4) {
				output = "echo usages: echo STRING [> OUTFILE] " +
						 "or echo STRING >> OUTFILE";
			} else if (cmdArgs.length == 2) {
				if (cmdArgs[2].matches("[^>]*>(>{0,1})[^>]*")) {
					
				} else {
					output = cmdArgs[1]; // return output from function call
				}
				
			} else if (cmdArgs.length == 4) {
				if (cmdArgs[2].matches("[>]+") && cmdArgs[2].length() <= 2) {
					//pass
				} else {
					output = "echo usage: echo STRING >[>] OUTFILE";
				}
			}
		case "man":
			if (cmdArgs.length != 2) {
				output = "man usage: man CMD"; // error, print usage
			} else {
				output = manCommand(cmdArgs[1]); // return output from function call
			}
		};
		return output;
	}
	
	/**
	 * Given a command gets the text file with the documentation to the command
	 * and returns a string with the contents of the file.
	 *
	 * @param cmd the command that's man page is getting retrieved
	 * @return the string containing the commands man page
	 */
	public String manCommand(String cmd) {
	    String manPage = "";
	   // Get the file with the man page for the given command
		try (BufferedReader br = new BufferedReader(
		    new FileReader(System.getProperty("user.dir") +
		        "\\src\\driver\\commands\\" + cmd+".txt"))) {
		  
		   String manLine = null;
		   // Store each line of the man page to a string
		   while ((manLine = br.readLine()) != null) {
		       manPage = manPage + manLine + "\n";
		   }
		   
		} catch (FileNotFoundException e) {
		    return (cmd + " is not a valid command");
        } catch (IOException e) {
    }
		return manPage;
	}
}
