package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.*;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidNameException;
import structures.Output;

public class ExecuteHistoryTest {

  /**
   * Initialize session attributes
   */
  MySession s;

  @Before
  public void setUp() throws Exception {
    s = new MySession(new Output());
    s.clearBuffer();
    s.clearFileSystem();
  }

  @After
  /**
   * The filesystem uses singleton design for the root directory. For testing
   * purposes, the root needs to be set to null everytime.
   */
  public void tearDown() throws InvalidNameException, NoSuchFieldException,
      IllegalAccessException {
    Field field = s.getRootDir().getClass().getDeclaredField("root");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
    s.clearBuffer();
  }

  /**
   * Calls the basic output command.
   */
  @Test
  public void testBasicCallCommand() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("!1", s);
    assertEquals(s.getCurrentDir().getName(), "a");
  }

  /**
   * Testing calling command from history that is lower than 1.
   */
  @Test
  public void testCallOutOfLowerBoundsNumber() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("ls", s);
    JShell.commandProcessor("pwd", s);
    s.clearBuffer();
    JShell.commandProcessor("!-1", s);

    assertEquals("out of bounds", "ERROR: Number entered is out of bounds.",
        s.getError());
  }

  /**
   * Testing calling command from history that is higher than command count.
   */
  @Test
  public void testCallOutOfUpperBoundsNumber() {
    JShell.commandProcessor("mkdir a", s);
    s.clearBuffer();
    JShell.commandProcessor("cd a", s);
    s.clearBuffer();
    JShell.commandProcessor("ls", s);
    s.clearBuffer();
    JShell.commandProcessor("pwd", s);
    s.clearBuffer();
    JShell.commandProcessor("!5", s);

    assertEquals("out of bounds", "ERROR: Number entered is out of bounds.",
        s.getError());
  }

  /**
   * Testing calling command that will fail again.
   */
  @Test
  public void testCallCommandThatWillFailWhenCalledAgain() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("mkdir a", s);
    assertEquals("Error for creating same dir should be called", s.getError(),
        "mkdir: cannot create directory 'a': File exists");
  }

  /**
   * Wrong usage error.
   */
  @Test
  public void testForceUsageError() {
    JShell.commandProcessor("!3 swagger", s);
    assertEquals("Get the usage message", s.getError(), "! usage: !NUMBER > 0");
  }
}
