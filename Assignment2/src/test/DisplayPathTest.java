package test;

import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import driver.MySession;
import structures.Output;

import static org.junit.Assert.assertEquals;

import org.junit.After;

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
  public void tearDown() {
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
