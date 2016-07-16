package test;

import static org.junit.Assert.*;

import org.junit.*;

import driver.JShell;
import driver.MySession;
import structures.Output;

public class ExecuteHistoryTest {

  /**
   * Initialize session attributes
   * */
  MySession s;
  
  @Before
  public void setUp() throws Exception {
    s = new MySession(new Output());
  }

  @Test
  public void testBasicCallCommand() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("!1", s);
    assertEquals("no errors/output", s.returnBuffer(), "");
  }
  
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

  @Test
  public void testCallCommandThatCallsSameCommand() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("!1", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("!1", s);
    JShell.commandProcessor("ls", s);
    assertEquals("Should have a created", s.returnBuffer(), "a");
    
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("pwd", s);
    assertEquals("Should have a created", s.getOutput(), "/a/a/a");
  }
  
  @Test
  public void testCallCommandThatWillFailWhenCalledAgain() {
    JShell.commandProcessor("mkdir a", s);
    JShell.commandProcessor("mkdir a", s);
    assertEquals("Error for creating same dir should be called", s.getError(),
        "mkdir: cannot create directory 'a': File exists");
    
  }
  
  @Test
  public void testForceUsageError() {
    JShell.commandProcessor("!3 swagger", s);
    assertEquals("Get the usage message", s.getError(), "! usage: !NUMBER > 0");
  }
}
