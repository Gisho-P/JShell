package test;

import org.junit.Before;
import org.junit.Test;

import structures.*;
import structures.Directory.*;
import structures.FileTypes.*;
import driver.JShell;
import driver.MySession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;

/**
 * Tests that verify the functionality of the echo command in JShell which can
 * display strings in the shell or store them in files.
 */
public class DisplayStoreStringTest {

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
   * Using echo to output an empty string to JShell.
   */
  @Test
  public void testDisplayEmptyString() {
    JShell.commandProcessor("echo \"\"", session);
    assertEquals("", session.returnBuffer());
  }

  /**
   * Displaying a one word string to JShell.
   */
  @Test
  public void testDisplayOneWord() {
    JShell.commandProcessor("echo \"test\"", session);
    assertEquals("test", session.returnBuffer());
  }

  /**
   * Displaying a multiple word string to JShell.
   */
  @Test
  public void testDisplayMultipleWords() {
    JShell.commandProcessor("echo \"1 2 3 4 ss\"", session);
    assertEquals("1 2 3 4 ss", session.returnBuffer());
  }

  /**
   * Stores a string in a file and verifies, no output is returned and the file
   * contains the correct string.
   *
   */
  @Test
  public void testStoreString() {
    JShell.commandProcessor("echo \"test\" > file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("test", message);
  }

  /**
   * Stores a string in a file and appends another string onto the same file and
   * verifies no output was sent to shell, and the string was properly stored in
   * the file.
   *
   */
  @Test
  public void testStoreAppendString() {
    JShell.commandProcessor("echo \"test\" > file", session);
    JShell.commandProcessor("echo \" add\" >> file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("test\n add", message);
  }

  /**
   * Stores a string in a file and overwrites it with another string onto the
   * same file and verifies no output was sent to shell, and only the second
   * string is stored in the file.
   *
   */
  @Test
  public void testStoreOverwriteString() throws MissingNameException {
    JShell.commandProcessor("echo \"test\" > file", session);
    JShell.commandProcessor("echo \"write\" > file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("write", message);
  }

  /**
   * Trying to echo to a new file with the same name as a sub directory
   */
  @Test
  public void testFileWithSameNameAsDirectory() {
    try {
      session.getCurrentDir().add(new Directory("file"));
    } catch (NameExistsException | InvalidAddition | InvalidName e) {
    }
    JShell.commandProcessor("echo \"test\" > file", session);
    assertEquals("ERROR: There is already a subdirectory with the same name",
        session.getOutput());
  }

}
