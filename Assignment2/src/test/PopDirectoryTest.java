package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import driver.*;

public class PopDirectoryTest {
  MySession s;

  @Before
  public void setUp() throws Exception {
    s = new MySession();
  }
  
  @Test
  public void emptyStackNeverAddedAtRoot() {
    String ret = JShell.commandProcessor("popd", s);
    assertEquals("At root, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at root still", "/",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void emptyStackNeverAddedNotAtRoot() {
    String ret = JShell.commandProcessor("mkdir home home/a", s);
    ret = JShell.commandProcessor("cd home/a", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("At another dir, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at root still", "/home/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void emptyStackOnceAddedAtRoot() {
    String ret = JShell.commandProcessor("mkdir home home/a home/a/b", s);
    ret = JShell.commandProcessor("cd home/a", s);
    ret = JShell.commandProcessor("pushd /", s);
    ret = JShell.commandProcessor("popd", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("Stack should be empty now, error",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at last correctly popped dir", "/home/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void emptyStackOnceAddedNotAtRoot() {
    String ret = JShell.commandProcessor("mkdir home home/a home/a/b", s);
    ret = JShell.commandProcessor("cd home/a", s);
    ret = JShell.commandProcessor("pushd b", s);
    ret = JShell.commandProcessor("popd", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("Stack should be empty now, error",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at last correctly popped dir", "/home/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void nonEmptyStackPopToLocationBefore() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a", s);
    ret = JShell.commandProcessor("pushd b", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("No errors", "", ret);
    assertEquals("Directory is before b", "/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void nonEmptyStackPopToSameLocation() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd .", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is location before pop", "/a/b",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void nonEmptyStackPopToLocationAfter() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a/b/c", s);
    ret = JShell.commandProcessor("pushd ..", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is after b", "/a/b/c",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void nonEmptyStackPopBetweenSubTrees() {
    String ret = JShell.commandProcessor("mkdir a b a/c b/d a/e b/f", s);
    ret = JShell.commandProcessor("mkdir a/c/g b/f/h b/f/h/i b/f/h/j", s);
    ret = JShell.commandProcessor("cd b/f/h/j", s);
    ret = JShell.commandProcessor("pushd /b", s);
    ret = JShell.commandProcessor("cd ../a/c/g", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is another subtree", "/b/f/h/j",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  public void nonEmptyStackMultiplePops() {
    String ret = JShell.commandProcessor("mkdir a a/b a/b/c", s);
    ret = JShell.commandProcessor("pushd a", s);
    ret = JShell.commandProcessor("pushd b", s);
    ret = JShell.commandProcessor("pushd c", s);
    ret = JShell.commandProcessor("popd", s);
    ret = JShell.commandProcessor("popd", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is root", "/",
        s.getCurrentDir().getEntirePath());
  }
  
}
