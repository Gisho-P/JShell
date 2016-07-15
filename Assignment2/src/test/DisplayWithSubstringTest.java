package test;

import static org.junit.Assert.assertEquals;
import org.junit.*;

import structures.*;
import driver.*;
import exceptions.*;

/**
 * Tests for the grep command.
 */
public class DisplayWithSubstringTest {

  MySession session;

  @Before
  /**
   * Set up before each test case, create a new session instance
   */
  public void setUp() {
    session = new MySession(new Output());
  }

  @After
  /**
   * Tear down after each test case, clear output/error buffers
   */
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
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("grep abc.* file1", session);
    assertEquals("abcdefg\n" + "abcde\n" + "abc", session.returnBuffer());
  }

  /**
   * List contents on a file which should return the file name, search
   * recursively through
   */
  @Test
  public void testRecursivePath() {
    try {
      File temp = new File("file1");
      temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
      session.getCurrentDir().add(temp);
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("grep -R abc.* file1", session);
    assertEquals("/:abcdefg\n" + "/:abcde\n" + "/:abc", session.returnBuffer());
  }

  /**
   * Test for using grep to match any string (* character) for files, should
   * return all files.
   */
  @Test
  public void testAnySubstring() {
    try {
      File temp = new File("file1");
      temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
      session.getCurrentDir().add(temp);
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("grep -R .* file1", session);
    assertEquals("/:abcdefg\n" + "/:abcde\n" + "/:abc", session.returnBuffer());
  }

  /**
   * Test for grep with no search string provided, should return no files/
   */
  @Test
  public void testEmptySubStringPath() {
    try {
      File temp = new File("file1");
      temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
      session.getCurrentDir().add(temp);
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("grep -R file1", session);
    assertEquals("", session.returnBuffer());

  }

  /**
   * Test for grep using a single search string with no wildcards, should only
   * match a file with the same string as a file name.
   */
  @Test
  public void testSingleSubstring() {
    try {
      File temp = new File("file1");
      temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
      session.getCurrentDir().add(temp);
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("grep -R abc file1", session);
    assertEquals("/:abc", session.returnBuffer());
  }
}
