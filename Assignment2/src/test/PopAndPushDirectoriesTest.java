package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidNameException;
import structures.Output;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for pushp and popd commands, testing the PushDirectory and PopDirectory
 * classes
 */
public class PopAndPushDirectoriesTest {
  MySession s;

  @Before
  /**
   * Create a new instance of MySession before every testcase
   */
  public void setUp() throws Exception {
    s = new MySession(new Output());
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
    s.clearDirectoryStack();
  }

  @Test
  /**
   * Test popping from empty stack at root
   */
  public void testPopEmptyStackNeverAddedAtRoot() {
    JShell.commandProcessor("popd", s);
    assertEquals("At root, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", s.returnBuffer());
    assertEquals("Directory should be at root still", "/",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping from empty stack not at root
   */
  public void testPopEmptyStackNeverAddedNotAtRoot() {
    JShell.commandProcessor("mkdir home home/a", s);
    JShell.commandProcessor("cd home/a", s);
    JShell.commandProcessor("popd", s);
    assertEquals("At another dir, nothing added to stack: should be an error\n",
        "ERROR: Empty stack, nothing to pop", s.returnBuffer());
    assertEquals("Directory should be at root still", "/home/a",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping from stack that contains only the root path until it becomes
   * empty
   */
  public void testPopEmptyStackOnceAddedAtRoot() {
    JShell.commandProcessor("mkdir home home/a home/a/b", s);
    JShell.commandProcessor("cd home/a", s);
    JShell.commandProcessor("pushd /", s);
    JShell.commandProcessor("popd", s);
    JShell.commandProcessor("popd", s);
    assertEquals("Stack should be empty now, error",
        "ERROR: Empty stack, nothing to pop", s.returnBuffer());
    assertEquals("Directory should be at last correctly popped dir", "/home/a",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping from stack that contains a child node until it becomes empty
   */
  public void testPopEmptyStackOnceAddedNotAtRoot() {
    JShell.commandProcessor("mkdir home home/a home/a/b", s);
    JShell.commandProcessor("cd home/a", s);
    JShell.commandProcessor("pushd b", s);
    JShell.commandProcessor("popd", s);
    JShell.commandProcessor("popd", s);

    assertEquals("Stack should be empty now, error",
        "ERROR: Empty stack, nothing to pop", s.returnBuffer());
    assertEquals("Directory should be at last correctly popped dir", "/home/a",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test if popping from non-empty stack takes you to the prev location
   */
  public void testPopNonEmptyStackPopToLocationBefore() {
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    JShell.commandProcessor("cd a", s);
    JShell.commandProcessor("pushd b", s);
    JShell.commandProcessor("popd", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory is before b", "/a",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping dot
   */
  public void testPopNonEmptyStackPopToSameLocation() {
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd .", s);
    JShell.commandProcessor("popd", s);

    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory is location before pop", "/a/b",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping to location after placement before pop
   */
  public void testPopNonEmptyStackPopToLocationAfter() {
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    JShell.commandProcessor("cd a/b/c", s);
    JShell.commandProcessor("pushd ..", s);
    JShell.commandProcessor("popd", s);

    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory is after b", "/a/b/c",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping stack after between two subtrees (jump from one end to the
   * other)
   */
  public void testPopNonEmptyStackPopBetweenSubTrees() {
    JShell.commandProcessor("mkdir a b a/c b/d a/e b/f", s);
    JShell.commandProcessor("mkdir a/c/g b/f/h b/f/h/i b/f/h/j", s);
    JShell.commandProcessor("cd b/f/h/j", s);
    JShell.commandProcessor("pushd /b", s);
    JShell.commandProcessor("cd ../a/c/g", s);
    JShell.commandProcessor("popd", s);

    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory is another subtree", "/b/f/h/j",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test popping multiple times in sequence
   */
  public void testPopNonEmptyStackMultiplePops() {
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    JShell.commandProcessor("pushd a", s);
    JShell.commandProcessor("pushd b", s);
    JShell.commandProcessor("pushd c", s);
    JShell.commandProcessor("popd", s);
    JShell.commandProcessor("popd", s);
    JShell.commandProcessor("popd", s);

    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory is root", "/", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing root at root
   */
  public void testPushAtRootToRoot() {
    JShell.commandProcessor("pushd /", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("directory is root", "/", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing root in child directory
   */
  public void testPushAtRootToSomeWhereElse() {
    JShell.commandProcessor("mkdir a a/b a/c/ a/b/d a/c/e/", s);
    JShell.commandProcessor("pushd a/c/e", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Directory at specified DIR", "/a/c/e",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing to a directory before the current directory before push
   */
  public void testPushToDirectoryBeforeCurrent() {
    JShell.commandProcessor("mkdir a a/b/", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd ..", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Dir at parent of dir before pushd", "/a",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing current directory (dot notation)
   */
  public void testPushAtDirectoryToDirectory() {
    JShell.commandProcessor("mkdir a a/b/", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd .", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("Same directory as before push", "/a/b",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing child directory
   */
  public void testPushToDirectoryAfterCurrent() {
    JShell.commandProcessor("mkdir a a/b/ a/b/c", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd c", s);
    assertEquals("No errors", "", s.returnBuffer());
    assertEquals("dir is child of dir before push", "/a/b/c",
        s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing paths with multiple single dots
   */
  public void testPushUseCurrentDotNotation() {
    JShell.commandProcessor("mkdir a a/b a/b/c a/b/c/d", s);
    JShell.commandProcessor("pushd ./a/b/./c", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/a/b/c", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing paths with multiple double dots
   */
  public void testPushUseParentDotNotation() {
    JShell.commandProcessor("mkdir a a/b a/b/c a/b/c/d/", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd ../..", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing a path that leads to a subdirectory
   */
  public void testPushToDifferentFileSubTree() {
    JShell.commandProcessor("mkdir a b a/c b/d a/e b/f", s);
    JShell.commandProcessor("mkdir a/c/g a/c/h b/d/i b/d/j", s);
    JShell.commandProcessor("cd a/c/g", s);
    JShell.commandProcessor("pushd /b/d/i/", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/b/d/i", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing invalid path at root
   */
  public void testPushGoToInvalidPathAtRoot() {
    JShell.commandProcessor("pushd noodles", s);
    assertNotEquals("should not be successful", "", s.returnBuffer());
    assertEquals("stuck at root", "/", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing invalid path not at root
   */
  public void testPushGoToInvalidPathNotAtRoot() {
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    JShell.commandProcessor("cd a/b", s);
    JShell.commandProcessor("pushd d", s);
    assertNotEquals("should not be successful", "", s.returnBuffer());
    assertEquals("stuck at at last cd'd dir", "/a/b",
        s.getCurrentDir().getEntirePath());
  }

  private void assertNotEquals(String s, String s1, String s2) {
  }

  @Test
  /**
   * Test multiple pushes in a sequence
   */
  public void testPushDoMultiplePushes() {
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("pushd a", s);
    JShell.commandProcessor("pushd b", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/a/b", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing double dot at root
   */
  public void testPushAtRootDotParentNotation() {
    JShell.commandProcessor("pushd .", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }

  @Test
  /**
   * Test pushing single dot at root
   */
  public void testPushAtRootDotCurrentNotation() {
    JShell.commandProcessor("pushd ..", s);
    assertEquals("", s.returnBuffer());
    assertEquals("/", s.getCurrentDir().getEntirePath());
  }
}
