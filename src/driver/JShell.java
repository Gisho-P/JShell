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

import java.util.Scanner;

public class JShell {

  public static void main(String[] args) {
	  String lastCommand = "";
	  MySession session = new MySession();
	  CommandInterpreter cmd = new CommandInterpreter(session);

	  Scanner input = new Scanner(System.in);
	  
	  // Continually accept commands until the command exit is entered
	  // exit can precede or follow any amount of white spaces and can have
	  // anything after exit and a white space
	  while(lastCommand.matches("[\\s]*exit([\\s]+.*)*") == false){
		  System.out.print(session.getCurrentDir().getEntirePath() + "$: ");
		  lastCommand = input.nextLine();
		  // Save the command to history
		  session.saveCommand(lastCommand);
		  if (lastCommand.matches("[\\s]*exit([\\s]+.*)*") == false) {
			  // Printing the output
			  System.out.println(cmd.interpretCmd(lastCommand));
		  }
	  }
	  
	  input.close();
	  exit();
  }
  
  public static void exit(){
	  System.out.println("DEBUG: Exiting");
  }
}
