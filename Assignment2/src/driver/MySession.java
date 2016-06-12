package driver;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MySession stores, and outputs a history of commands.
 */
public class MySession {
  
  protected static List<String> commandHistory;
  
  public MySession(){
    commandHistory = new ArrayList<String>();
  }
  
  /**
   * Saves the command to the command history.
   *
   * @param command the command to be added to history
   */
  public void saveCommand(String command){
    commandHistory.add(command);
  }
  
  /**
   * Given a number n less then the number of commands in history
   * prints the last n commands to stdout
   *
   * @param numberOfCommands the number of commands to be printed from history
   */
  public static String printCommandHistory(int numberOfCommands){
	  int historySize = commandHistory.size();
	  if(numberOfCommands < 0){
		  // Update later to have a consistent format for all error messages
		  return "history usage: history [NUMBER >= 0]";
	  } else {	
		  String output = "";
		  for(int cmdNumber = historySize - numberOfCommands;
				  cmdNumber <= historySize; cmdNumber++){
			  output = output + cmdNumber + ". " +
				  commandHistory.get(cmdNumber - 1) + "\n";
		  }
		  return output;
	  }
  }
  
  /**
   * Prints the command history to stdout.
   */
  public static String printCommandHistory(){
    return printCommandHistory(commandHistory.size());
  }

}
