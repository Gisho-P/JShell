package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.*;

public class PopAndPushDirectoriesTest {
  MySession s;

  @Before
  public void setUp() throws Exception {
    s = new MySession();
  }
  
  @After
  public void tearDown() throws Exception {
    DirStack.clear();
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
    String ret = JShell.commandProcessor("pushd /", s);
    assertEquals("No errors", "", ret);
    assertEquals("directory is root", "/", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushAtRootToSomeWhereElse() {
    String ret = JShell.commandProcessor("mkdir a a/b a/c/ a/b/d a/c/e/", s);
    ret = JShell.commandProcessor("pushd a/c/e", s);
    assertEquals("No errors", "", ret);
    assertEquals("Directory at specified DIR", "/a/c/e",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushToDirectoryBeforeCurrent() {
    String ret = JShell.commandProcessor("mkdir a a/b/", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd ..", s);
    assertEquals("No errors", "", ret);
    assertEquals("Dir at parent of dir before pushd", "/a",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushAtDirectoryToDirectory() {
    String ret = JShell.commandProcessor("mkdir a a/b/", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd .", s);
    assertEquals("No errors", "", ret);
    assertEquals("Same directory as before push", "/a/b",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushToDirectoryAfterCurrent() {
    String ret = JShell.commandProcessor("mkdir a a/b/ a/b/c", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd c", s);
    assertEquals("No errors", "", ret);
    assertEquals("dir is child of dir before push", "/a/b/c",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushUseCurrentDotNotation() {
    String ret = JShell.commandProcessor("mkdir a a/b a/b/c a/b/c/d", s);
    ret = JShell.commandProcessor("pushd ./a/b/./c", s);
    assertEquals("", ret);
    assertEquals("/a/b/c", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushUseParentDotNotation() {
    String ret = JShell.commandProcessor("mkdir a a/b a/b/c a/b/c/d/", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd ../..", s);
    assertEquals("", ret);
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushToDifferentFileSubTree() {
    String ret = JShell.commandProcessor("mkdir a b a/c b/d a/e b/f", s);
    ret = JShell.commandProcessor("mkdir a/c/g a/c/h b/d/i b/d/j", s);
    ret = JShell.commandProcessor("cd a/c/g", s);
    ret = JShell.commandProcessor("pushd /b/d/i/", s);
    assertEquals("", ret);
    assertEquals("/b/d/i", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushGoToInvalidPathAtRoot() {
    String ret = JShell.commandProcessor("pushd noodles", s);
    assertNotEquals("should not be successful", "", ret);
    assertEquals("stuck at root", "/", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushGoToInvalidPathNotAtRoot() {
    String ret = JShell.commandProcessor("mkdir a a/b a/b/c", s);
    ret = JShell.commandProcessor("cd a/b", s);
    ret = JShell.commandProcessor("pushd d", s);
    assertNotEquals("should not be successful", "", ret);
    assertEquals("stuck at at last cd'd dir", "/a/b",
        s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushDoMultiplePushes() {
    String ret = JShell.commandProcessor("mkdir a a/b", s);
    ret = JShell.commandProcessor("pushd a", s);
    ret = JShell.commandProcessor("pushd b", s);
    assertEquals("", ret);
    assertEquals("/a/b", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushAtRootDotParentNotation() {
    String ret = JShell.commandProcessor("pushd .", s);
    assertEquals("", ret);
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }
  
  @Test
  public void testPushAtRootDotCurrentNotation() {
    String ret = JShell.commandProcessor("pushd ..", s);
    assertEquals("", ret);
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }
  
}
