package test;

import static org.junit.Assert.*;

import org.junit.Test;
import driver.MySession;

public class MySessionTest {

	/**
	 * Saves 5 commands to the session, and gets all history to ensure it
	 * returns the string in the correct format & with the right content.
	 */
	@Test
	public void getHistoryAll(){
	  MySession session = new MySession();
	  String expectedHistory = "";
	  for(Integer i = 0; i < 5; i++){
	    session.saveCommand("echo \"cmd " + i.toString()  + "\" > newFile");
	    expectedHistory += ((i + 1) + (". " + "echo \"cmd " + i  + "\" > newFile")) + "\r\n";
	  }
	  assertTrue(MySession.printCommandHistory().equals(expectedHistory));
	}
	
	@Test
	    public void getHistoryLastTwo(){
	      MySession session = new MySession();
	      String expectedHistory = "";
	      for(Integer i = 0; i < 5; i++){
	        session.saveCommand("echo \"cmd " + i.toString()  + "\" > newFile");
	        if(i > 2)
	          expectedHistory += ((i + 1) + (". " + "echo \"cmd " + i  + "\" > newFile")) + "\r\n";
	      }
	      assertTrue(MySession.printCommandHistory(2).equals(expectedHistory));
	    }
}
