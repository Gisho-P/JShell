package test;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import driver.*;
import exceptions.InvalidNameException;
import structures.Output;

/**
 * Tests for DisplayPath class
 */
public class DisplayPathTest {
  MySession session;

  @Before
  /**
   * Create a new instance of MySession before every test
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
   * Test calling pwd on empty directory
   */
  public void testDisplayPathEmpty() {
    JShell.commandProcessor("pwd", session);
    assertEquals("/", session.returnBuffer());
  }

  @Test
  /**
   * Test calling pwd on a single directory
   */
  public void testDisplayPathSingleDir() {
    JShell.commandProcessor("mkdir one", session);
    JShell.commandProcessor("cd one", session);
    JShell.commandProcessor("pwd", session);
    assertEquals("/one", session.returnBuffer());
  }

  @Test
  /**
   * Test calling pwd on directories with multiple layers
   */
  public void testDisplayPathMultipleDir() {
    JShell.commandProcessor("mkdir one one/two one/two/three three four",
        session);
    JShell.commandProcessor("cd one/two/three", session);
    JShell.commandProcessor("pwd", session);
    assertEquals("/one/two/three", session.returnBuffer());
  }

  @Test
  /**
   * Test calling pwd on directories where the parent directories all have the
   * same name
   */
  public void testDisplayPathMultipleDirSameName() {
    JShell.commandProcessor("mkdir one one/one one/one/one one/one/one/one",
        session);
    JShell.commandProcessor("cd one/one/one/one", session);
    JShell.commandProcessor("pwd", session);
    assertEquals("/one/one/one/one", session.returnBuffer());
  }

}
