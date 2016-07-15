package test;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import driver.*;
import structures.Output;

/**
 * Tests that verify the functionality of the echo command in JShell which can
 * display strings in the shell or store them in files.
 */
public class DisplayStringTest {

  MySession session;

  @Before
  public void setUp() {
    session = new MySession(new Output());
  }

  @After
  /**
   * Clear the contents in the file system and output buffer
   */
  public void tearDown() {
    session.clearFileSystem();
    session.clearBuffer();
  }

  /*
   * Test that echo requires double quotes
   */
  @Test
  public void testNoQuotes() {
    JShell.commandProcessor("echo hi", session);
    String noQuotesMsg =
        "ERROR: STRING must be surrounded by double quotations";
    assertEquals(noQuotesMsg, session.returnBuffer());
  }

  /*
   * Test that echo requires a string
   */
  @Test
  public void testNoString() {
    JShell.commandProcessor("echo", session);
    String usageMsg = "echo usage: echo STRING (In quotes)";
    assertEquals(usageMsg, session.returnBuffer());
  }

  /*
   * Test that echo requires a string
   */
  @Test
  public void testExtraArgument() {
    JShell.commandProcessor("echo \"one\" two", session);
    String usageMsg = "echo usage: echo STRING (In quotes)";
    assertEquals(usageMsg, session.returnBuffer());
  }

  /*
   * Test that echo requires a string
   */
  @Test
  public void testOneQuote() {
    JShell.commandProcessor("echo \"", session);
    String noQuotesMsg =
        "ERROR: STRING must be surrounded by double quotations";
    assertEquals(noQuotesMsg, session.returnBuffer());
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
   * Displaying a string with quotations to JShell.
   */
  @Test
  public void testStringWithQuote() {
    JShell.commandProcessor("echo \"\"\"", session);
    assertEquals("\"", session.returnBuffer());
  }

}
