package driver;

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
				output = ""; // return output from function call
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
			} else {
				output = ""; // return output from function call
			}
		case "man":
			if (cmdArgs.length != 2) {
				output = "man usage: man CMD"; // error, print usage
			} else {
				output = ""; // return output from function call
			}
		};
		return output;
	}
}
