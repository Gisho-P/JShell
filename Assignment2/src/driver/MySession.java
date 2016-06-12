package driver;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MySession stores, and outputs a history of commands.
 */
public class MySession {
  
  List<String> commandHistory;
  
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
  public void printCommandHistory(int numberOfCommands){
    int historySize = commandHistory.size();
    if(numberOfCommands > historySize || numberOfCommands < 0){
      // Update later to have a consistent format for all error messages
      System.out.println("Invalid number");
    }
    else{
      for(int cmdNumber = historySize - numberOfCommands;
          cmdNumber <= historySize; cmdNumber++){
        System.out.println(cmdNumber + ". " + commandHistory.get(cmdNumber - 1));
      }
    }
  }
  
  /**
   * Prints the command history to stdout.
   */
  public void printCommandHistory(){
    printCommandHistory(commandHistory.size());
  }

}
