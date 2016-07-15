package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.InvalidNameException;
import exceptions.NameExistsException;
import structures.Directory;
import structures.File;
import structures.Output;

/**
 * Tests that verify the functionality of the cat command in JShell which
 * displays the contents of one or more files.
 */
public class DisplayFileTest {

  /** The session */
  MySession session;

  /**
   * Initialize the session.
   */
  @Before
  public void setUp() {
    session = new MySession(new Output());
  }

  /**
   * Tear down after each test case
   *
   * @throws InvalidNameException the invalid name exception
   * @throws NoSuchFieldException the no such field exception
   * @throws IllegalAccessException the illegal access exception
   */
  @After
  /**
   * The filesystem uses singleton design for the root directory. For testing
   * purposes, the root needs to be set to null everytime.
   */
  public void testTearDown() throws InvalidNameException, NoSuchFieldException,
      IllegalAccessException {
    Field field = session.getRootDir().getClass().getDeclaredField("root");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  /**
   * Tests to make sure when no files are specified a usage message is returned.
   */
  @Test
  public void testNoFiles() {
    JShell.commandProcessor("cat", session);
    String expectedMessage = "cat usage: cat FILE [FILE2] ...";
    assertEquals(expectedMessage, session.returnBuffer());
  }

  /**
   * Tests to make sure that the contents of a file are displayed.
   *
   * @throws NameExistsException the name exists exception
   * @throws InvalidAdditionException the invalid addition exception
   * @throws InvalidNameException the invalid name exception
   */
  @Test
  public void testOneFile() throws NameExistsException,
      InvalidAdditionException, InvalidNameException {
    File fileA = new File("fileA");
    fileA.setContent("file contents\nline2");
    session.getCurrentDir().add(fileA);

    JShell.commandProcessor("cat fileA", session);
    String expectedMessage = "file contents\nline2";
    assertEquals(expectedMessage, session.returnBuffer());
  }

  /**
   * Tests to make sure when no files are specified a usage message is returned.
   *
   * @throws NameExistsException the name exists exception
   * @throws InvalidAdditionException the invalid addition exception
   * @throws InvalidNameException the invalid name exception
   */
  @Test
  public void testMultipleFiles() throws NameExistsException,
      InvalidAdditionException, InvalidNameException {
    File fileA = new File("fileA");
    fileA.setContent("fileA contents\nline2A");
    session.getCurrentDir().add(fileA);

    File fileB = new File("fileB");
    fileB.setContent("fileB contents\nline2B");
    session.getCurrentDir().add(fileB);

    File fileC = new File("fileC");
    fileC.setContent("fileC contents\nline2C");
    session.getCurrentDir().add(fileC);

    JShell.commandProcessor("cat fileA fileB fileC", session);
    String expectedMessage = "fileA contents\nline2A\n\n\n\nfileB contents\n"
        + "line2B\n\n\n\nfileC contents\nline2C";
    assertEquals(expectedMessage, session.returnBuffer());
  }

  /**
   * Tests to make sure when no files are specified a usage message is returned.
   *
   * @throws NameExistsException the name exists exception
   * @throws InvalidAdditionException the invalid addition exception
   * @throws InvalidNameException the invalid name exception
   */
  @Test
  public void testInvalidMultipleFiles() throws NameExistsException,
      InvalidAdditionException, InvalidNameException {
    File fileA = new File("fileA");
    fileA.setContent("fileA contents\nline2A");
    session.getCurrentDir().add(fileA);

    File fileB = new File("fileB");
    fileB.setContent("fileB contents\nline2B");
    session.getCurrentDir().add(fileB);

    File fileC = new File("fileC");
    fileC.setContent("fileC contents\nline2C");
    session.getCurrentDir().add(fileC);

    JShell.commandProcessor("cat fileA fileB fileC fileD", session);
    String expectedMessage = "fileA contents\nline2A\n\n\n\nfileB contents\n"
        + "line2B\n\n\n\nfileC contents\nline2C";
    assertEquals(expectedMessage, session.getOutput());
    expectedMessage = "No such file at fileD";
    assertEquals(expectedMessage, session.getError());
  }

  /**
   * Tests trying to cat a directory, which should return an error message.
   *
   * @throws NameExistsException the name exists exception
   * @throws InvalidAdditionException the invalid addition exception
   * @throws InvalidNameException the invalid name exception
   */
  @Test
  public void testDisplayDirectory() throws NameExistsException,
      InvalidAdditionException, InvalidNameException {
    session.getCurrentDir().add(new Directory("dirA"));

    JShell.commandProcessor("cat dirA", session);
    String expectedMessage = "Unable to cat dir dirA";
    assertEquals(expectedMessage, session.returnBuffer());
  }
}
