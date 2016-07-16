package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.InvalidNameException;
import exceptions.MissingNameException;
import exceptions.NameExistsException;
import structures.File;
import structures.Output;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite containing test cases for the curl command.
 */
public class RetrieveUrlFileTest {

  MySession session;

  @Before
  /**
   * Create a new instance of MySession to start with an empty directory
   */
  public void setUp() {
    session = new MySession(new Output());
  }

  @After
  /**
   * The filesystem uses singleton design for the root directory. For testing
   * purposes, the root needs to be set to null everytime.
   */
  public void tearDown() throws InvalidNameException, NoSuchFieldException,
      IllegalAccessException {
    Field field = session.getRootDir().getClass().getDeclaredField("root");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
    session.clearBuffer();
  }

  @Test
  /**
   * Test creating a file when the given url is invalid
   */
  public void testCreatingFileInvalidUrl() {
    JShell.commandProcessor("curl hi", session);
    assertEquals(session.getError(), "Malformed " + "URL.");
  }

  @Test
  /**
   * Test creating a file given a url when a file with the name already exists
   */
  public void testCreatingFileThatExists() throws InvalidNameException,
          NameExistsException, InvalidAdditionException, MissingNameException {
    session.getCurrentDir().add(new File("073.txt"));
    JShell.commandProcessor("curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt",
        session);
    assertEquals(session.getError(),
        "");
    assertTrue(((File) session.getCurrentDir().getChild("073.txt")).getContent()
            .contains("how his brothers had\n"
                    + "betrayed him, and how he had nevertheless kept " +
                    "silence"));
  }

  @Test
  /**
   * Test that the created file has the correct name based on the url
   */
  public void testCreatingFileWithCorrectNameGivenValidUrl() {
    JShell.commandProcessor("curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt",
        session);
    assertFalse(session.getCurrentDir().nameExists("073.txt") == -1);
  }

  @Test
  /**
   * Test the file created contains the correct content based on the data
   * returned from the url
   */
  public void testFileContainsCorrectContent() throws MissingNameException {
    JShell.commandProcessor("curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt",
        session);
    assertTrue(((File) session.getCurrentDir().getChild("073.txt")).getContent()
        .contains("how his brothers had\n"
            + "betrayed him, and how he had nevertheless kept silence"));
  }

  @Test
  /**
   * Test creating a file with the correct name when there is a slash at the end
   * of the url
   */
  public void testCreatingFileWithCorrectNameGivenUrlSlashAtEnd() {
    JShell.commandProcessor("curl http://www.google.ca/", session);
    assertFalse(session.getCurrentDir().nameExists("www.google.ca") == -1);
  }
}
