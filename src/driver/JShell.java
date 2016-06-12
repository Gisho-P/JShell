// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name:songjin8
// UT Student #:1001545571
// Author:Jin Song
//
// Student2:
// UTORID user_name:pushpa10
// UT Student #:1001624628
// Author:Girrshotan Pushparajah
//
// Student3:
// UTORID user_name:
// UT Student #:
// Author:
//
// Student4:
// UTORID user_name:
// UT Student #:
// Author:
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
	  CommandInterpreter cmd = new CommandInterpreter();
	  MySession session = new MySession();
	  
	  Scanner in = new Scanner(System.in);
	  
	  while(lastCommand.equals("exit") == false){
		  System.out.println("DEBUG: Enter a command");
		  lastCommand = in.nextLine();
		  // Save the command to history
		  session.saveCommand(lastCommand);
		  // Printing the output
		  System.out.println(cmd.interpretCmd(lastCommand));
	  }
	  exit();
  }
  
  public static void exit(){
	  System.out.println("DEBUG: Exiting");
  }
}
