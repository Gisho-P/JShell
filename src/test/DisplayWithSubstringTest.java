package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import driver.Directory;
import driver.File;
import driver.JShell;
import driver.MySession;
import driver.Directory.InvalidAddition;
import driver.Directory.MissingNameException;
import driver.Directory.NameExistsException;
import driver.FileTypes.InvalidName;

public class DisplayWithSubstringTest {

	  MySession session;

	  @Before
	  public void setUp() {
	    session = new MySession();
	  }
	  
	  /**
	   * List contents on a file which should return the file name
	   */
	  @Test
	  public void testFilePath() {
	    try {
	    	File temp = new File("file1");
	    	temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
	      session.getCurrentDir().add(temp);
	    } catch (NameExistsException | InvalidAddition | InvalidName e) {
	    }
	    String message = JShell.commandProcessor("grep abc.* file1", session);
	    assertEquals("abcdefg\n" + "abcde\n" + "abc", message);
	  }
	  
	  /**
	   * List contents on a file which should return the file name
	   */
	  @Test
	  public void testRecursivePath() {
	    try {
	    	File temp = new File("file1");
	    	temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
	      session.getCurrentDir().add(temp);
	    } catch (NameExistsException | InvalidAddition | InvalidName e) {
	    }
	    String message = JShell.commandProcessor("grep -R abc.* file1", session);
	    assertEquals("/:abcdefg\n" + "/:abcde\n" + "/:abc", message);

	  }
}
