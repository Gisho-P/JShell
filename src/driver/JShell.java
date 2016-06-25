// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: songjin8
// UT Student #: 1001545571
// Author: Jin Song
//
// Student2:
// UTORID user_name: pushpa10
// UT Student #: 1001624628
// Author: Girrshotan Pushparajah
//
// Student3:
// UTORID user_name: pateld59
// UT Student #: 1001517815
// Author: Dhrumil Patel
//
// Student4:
// UTORID user_name: bhuiya16
// UT Student #: 1001280026
// Author: Adnan Bhuiyan
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JShell {

	public static String callFunction(List<String> args) {
		String output = "";

		try {
			Class<?> c = Class.forName("driver." +
						 MySession.commandToClass.get(args.get(0)));
			Object t = c.newInstance();
			Method m = c.getMethod("exec", List.class);
			output = (String) m.invoke(t, args);
		} catch (ClassNotFoundException | InstantiationException | 
				IllegalAccessException | NoSuchMethodException | 
				SecurityException | IllegalArgumentException |
				InvocationTargetException e) {
			output = "ERROR: Invalid Command.";
		}

		return output;
	}

	public static String commandProcessor(String cmd) {		
		cmd = cmd.trim();

		// Store the output here
		List<String> cmdArgs = new ArrayList<String>();
		
		// Splitting the cmd
		if (cmd.contains("\"")) {
			// If cmd contains a string separate it while keeping the STRING
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
		
		return callFunction(cmdArgs);
	}
	
	public static void main(String[] args) {
		String lastCommand = "";
		MySession session = new MySession();
		Scanner input = new Scanner(System.in);
	  
		// Continually accept commands until the command exit is entered
		// exit can precede or follow any amount of white spaces and can have
		// anything after exit and a white space
		while(lastCommand.matches("[\\s]*exit[\\s]*") == false){
			System.out.print(session.getCurrentDir().getEntirePath() + "$: ");
			lastCommand = input.nextLine();
			// Save the command to history
			session.saveCommand(lastCommand);
			if (lastCommand.matches("[\\s]*exit[\\s]*") == false) {
				// Printing the output
				System.out.println(commandProcessor(lastCommand));
			}
		}
	  
		input.close();
	}

}
