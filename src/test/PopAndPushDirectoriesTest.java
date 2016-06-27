package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import driver.*;

public class PopAndPushDirectoriesTest {
  MySession s;

  @Before
  public void setUp() throws Exception {
    s = new MySession();
  }
  
  @Test
  public void testPopEmptyStackNeverAddedAtRoot() {
    String ret = JShell.commandProcessor("popd", s);
    assertEquals("At root, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at root still", "/",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPopEmptyStackNeverAddedNotAtRoot() {
    String ret = JShell.commandProcessor("mkdir home home/a", s);
    ret = JShell.commandProcessor("cd home/a", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("At another dir, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", ret);
    assertEquals("Directory should be at root still", "/home/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPopEmptyStackOnceAddedAtRoot() {
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
  public void testPopEmptyStackOnceAddedNotAtRoot() {
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
  public void testPopNonEmptyStackPopToLocationBefore() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a", s);
    ret = JShell.commandProcessor("pushd b", s);
    ret = JShell.commandProcessor("popd", s);
    assertEquals("No errors", "", ret);
    assertEquals("Directory is before b", "/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPopNonEmptyStackPopToSameLocation() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd .", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is location before pop", "/a/b",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPopNonEmptyStackPopToLocationAfter() {
    String ret = JShell.commandProcessor("mkdir a mkdir a/b mkdir a/b/c", s);
    ret = JShell.commandProcessor("cd a/b/c", s);
    ret = JShell.commandProcessor("pushd ..", s);
    ret = JShell.commandProcessor("popd", s);
    
    assertEquals("No errors", "", ret);
    assertEquals("Directory is after b", "/a/b/c",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPopNonEmptyStackPopBetweenSubTrees() {
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
  public void testPopNonEmptyStackMultiplePops() {
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
  
  @Test
  public void testPushAtRootToRoot() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushAtRootToSomeWhereElse() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushToDirectoryBeforeCurrent() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushAtDirectoryToDirectory() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushToDirectoryAfterCurrent() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushUseCurrentDotNotation() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushUseParentDotNotation() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushToDifferentFileSubTree() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushGoToInvalidPath() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushDoMultiplePushes() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushAtRootDotParentNotation() {
    assertEquals();
    assertEquals();
  }
  
  @Test
  public void testPushAtRootDotCurrentNotation() {
    assertEquals();
    assertEquals();
  }
  
}
