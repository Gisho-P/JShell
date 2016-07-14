package test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import structures.Directory;
import structures.File;
import driver.JShell;
import driver.MySession;
import structures.Directory.InvalidAdditionException;
import structures.Directory.NameExistsException;
import structures.FileTypes;
import structures.FileTypes.InvalidNameException;
import structures.Output;

public class DisplayWithSubstringTest {

	  MySession session;

	  @Before
	  public void setUp() {
	    session = new MySession(new Output());
	  }
	  
	  @After
	  public void tearDown() {
		  session.clearBuffer();
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
	    } catch (NameExistsException | InvalidAdditionException | InvalidNameException e) {
	    }
	    JShell.commandProcessor("grep abc.* file1", session);
	    assertEquals("abcdefg\n" + "abcde\n" + "abc", session.returnBuffer());
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
	    } catch (NameExistsException | InvalidAdditionException | InvalidNameException e) {
	    }
	    JShell.commandProcessor("grep -R abc.* file1", session);
	    assertEquals("/:abcdefg\n" + "/:abcde\n" + "/:abc", session.returnBuffer());

	  }
	  
	  @Test
	  public void testAnySubstring() {
		    try {
		    	File temp = new File("file1");
		    	temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
		      session.getCurrentDir().add(temp);
		    } catch (NameExistsException | InvalidAdditionException | FileTypes.InvalidNameException e) {
		    }
		    JShell.commandProcessor("grep -R .* file1", session);
		    assertEquals("/:abcdefg\n" + "/:abcde\n" + "/:abc", session.returnBuffer());

	  }
	  
	  @Test
	  public void testEmptySubStringPath() {
		    try {
		    	File temp = new File("file1");
		    	temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
		      session.getCurrentDir().add(temp);
		    } catch (NameExistsException | InvalidAdditionException | FileTypes.InvalidNameException e) {
		    }
		    JShell.commandProcessor("grep -R file1", session);
		    assertEquals("", session.returnBuffer());

		  }
	  
	  @Test
	  public void testSingleSubstring() {
		    try {
		    	File temp = new File("file1");
		    	temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
		      session.getCurrentDir().add(temp);
		    } catch (NameExistsException | Directory.InvalidAdditionException | FileTypes.InvalidNameException e) {
		    }
		    JShell.commandProcessor("grep -R abc file1", session);
		    assertEquals("/:abc", session.returnBuffer());

	  }
}
