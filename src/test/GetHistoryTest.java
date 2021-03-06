package test;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidNameException;
import structures.Output;
import static org.junit.Assert.assertEquals;

/**
 * The Class GetHistoryTest tests out sessions history function.
 */
public class GetHistoryTest {

  MySession session;

  /**
   * Set up before any tests are run, create session instance
   */
  @Before
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

  /**
   * Saves 5 commands to the session, and gets all history to ensure it returns
   * the string in the correct format & with the right content.
   */
  @Test
  public void testGetHistoryAll() {
    String expectedHistory = "";
    // Test getting the history of all commands
    for (Integer i = 0; i < 5; i++) {
      session.saveCommand("echo \"cmd " + i.toString() + "\" > newFile");
      if (i != 4)
        expectedHistory +=
            ((i + 1) + (". " + "echo \"cmd " + i + "\" > " + "newFile")) + "\n";
      else
        expectedHistory +=
            ((i + 1) + (". " + "echo \"cmd " + i + "\"" + " > newFile"));
    }

    JShell.commandProcessor("history", session);
    assertEquals(session.returnBuffer(), expectedHistory);
  }

  /**
   * Tests getting the history of the last two commands when 5 were entered.
   */
  @Test
  public void testGetHistoryLastTwo() {
    String expectedHistory = "";
    // Test getting the history of the last two commands
    for (Integer i = 0; i < 5; i++) {
      session.saveCommand("echo \"cmd " + i.toString() + "\" > newFile");
      if (i > 2)
        if (i != 4)
          expectedHistory +=
              ((i + 1) + (". " + "echo \"cmd " + i + "\" > " + "newFile"))
                  + "\n";
        else
          expectedHistory +=
              ((i + 1) + (". " + "echo \"cmd " + i + "\" > " + "newFile"));
    }

    JShell.commandProcessor("history 2", session);
    assertEquals(session.returnBuffer(), expectedHistory);
  }

  /**
   * Tests getting the history of 0 commands is empty.
   */
  @Test
  public void testGetHistoryLastZero() {
    String expectedHistory = "";
    // Test getting the history of 0 commands
    for (Integer i = 0; i < 5; i++) {
      session.saveCommand("echo \"cmd " + i.toString() + "\" > newFile");
    }

    JShell.commandProcessor("history 0", session);
    assertEquals(session.returnBuffer(), expectedHistory);
  }

  /**
   * Saves 5 commands to the session, and tries to get the last 6 commands saved
   * but it'll only return what it has
   */
  @Test
  public void testGetHistoryMoreThenAll() {
    String expectedHistory = "";
    // Test getting the history of 6 commands
    for (Integer i = 0; i < 5; i++) {
      session.saveCommand("echo \"cmd " + i.toString() + "\" > newFile");
      if (i != 4)
        expectedHistory +=
            ((i + 1) + (". " + "echo \"cmd " + i + "\" > " + "newFile")) + "\n";
      else
        expectedHistory +=
            ((i + 1) + (". " + "echo \"cmd " + i + "\"" + " > newFile"));
    }

    JShell.commandProcessor("history 6", session);
    assertEquals(session.returnBuffer(), expectedHistory);
  }
}
