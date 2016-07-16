package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidNameException;
import exceptions.MissingNameException;
import structures.File;
import structures.Output;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for mv command.
 */
public class MoveFileTest {

  /**
   * Need instance of session to store output and filesystem
   */
  MySession s;

  @Before
  /**
   * Reinitialize MySession to clear output
   */
  public void setUp() {
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
  }

  @Test
  /**
   * Test mv command given the source or destination paths do not exist
   */
  public void testMovingPathDoesNotExist() {
    // source doesn't exist
    JShell.commandProcessor("mv a b", s);
    assertEquals("There are no files or directories with name a",
        s.returnBuffer());

    JShell.commandProcessor("mkdir a", s);
    s.clearBuffer();
    // dest does not exist
    JShell.commandProcessor("mv a b/c", s);
    assertEquals("There are no files or directories with name b/",
        s.returnBuffer());
  }

  @Test
  /**
   * Test renaming directory using the mv command
   */
  public void testMovingRenameDirectories() {
    ArrayList<String> expected = new ArrayList<String>();

    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    // test renaming a to b
    JShell.commandProcessor("mv /a b", s);
    expected.add("b");
    assertEquals(expected, s.getRootDir().getChildNames());

    JShell.commandProcessor("cd b", s);
    assertEquals(expected, s.getCurrentDir().getChildNames());

    JShell.commandProcessor("cd b", s);
    expected.clear();
    expected.add("c");
    // test renaming c to d
    assertEquals(expected, s.getCurrentDir().getChildNames());

    JShell.commandProcessor("mv c /b/b/d", s);
    expected.clear();
    expected.add("d");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test renaming the file at src to the destination name
   */
  public void testMovingRenameFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // reaname file d to c
    JShell.commandProcessor("mv d /a/b/c", s);
    expected.add("a");
    assertEquals(expected, s.getRootDir().getChildNames());
    expected.clear();
    JShell.commandProcessor("cd a/b", s);
    // ensure that the contents of the file are the same
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("yea", ((File) s.getCurrentDir().getChild("c")).getContent());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test moving directory to destination path that leads to file
   */
  public void testMovingDirectoryToFile() throws MissingNameException {
    // try moving directory to file
    JShell.commandProcessor("mkdir a a/b z", s);
    JShell.commandProcessor("echo \"hi\" > /a/b/c", s);
    JShell.commandProcessor("mv z /a/b/c", s);
    assertEquals("Invalid destination path. Can not move source directory.",
        s.returnBuffer());
    s.clearBuffer();

    JShell.commandProcessor("mv z /a/b/c/d", s);
    assertEquals("Invalid destination path. Can not move source directory.",
        s.returnBuffer());
  }

  @Test
  /**
   * Test replacing file at destination with file at source given both the
   * source and destination paths lead to files
   */
  public void testReplacingFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();

    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"hello\" > a/b/c", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // replace file c with d
    JShell.commandProcessor("mv d /a/b/c", s);
    expected.add("a");
    assertEquals(expected, s.getRootDir().getChildNames());

    expected.clear();
    JShell.commandProcessor("cd a/b", s);

    expected.add("c");
    // ensure that the contents of the file changed
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("yea", ((File) s.getCurrentDir().getChild("c")).getContent());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test moving the directory or file to another directory
   */
  public void testMovingFileOrDirectoryToDirectory()
      throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    // mv directory c to directory b
    JShell.commandProcessor("mkdir a a/b c c/z", s);
    JShell.commandProcessor("mv c /a/b", s);
    expected.add("a");
    assertEquals(expected, s.getRootDir().getChildNames());

    expected.clear();
    JShell.commandProcessor("cd a/b", s);
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());

    expected.clear();
    JShell.commandProcessor("cd c", s);
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());

    JShell.commandProcessor("cd ..", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // move file d to directory c
    JShell.commandProcessor("mv d /a/b/c", s);
    JShell.commandProcessor("cd c", s);
    expected.add("d");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("", s.returnBuffer());

  }

  @Test
  /**
   * Replace file at destination with file at source if it already exists
   */
  public void testReplaceDirectoryOrFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    // replace directory at destination
    JShell.commandProcessor("mkdir a a/b b b/z", s);
    JShell.commandProcessor("mv b a", s);
    expected.add("a");
    assertEquals(expected, s.getCurrentDir().getChildNames());

    JShell.commandProcessor("cd a/b", s);
    expected.clear();
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());

    // replace file at destination
    JShell.commandProcessor("echo \"hi\" > m", s);
    JShell.commandProcessor("cd /", s);
    JShell.commandProcessor("echo \"hello\" > m", s);
    JShell.commandProcessor("mv m a/b", s);
    JShell.commandProcessor("cd a/b", s);
    assertEquals("hello",
        ((File) s.getCurrentDir().getChild("m")).getContent());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test replacing a directory with children at the destination
   */
  public void testReplaceDirectoryHasChildren() throws MissingNameException {
    JShell.commandProcessor("mkdir a a/b a/b/c b b/z", s);
    // try replacing directory with children. Should result in an error
    JShell.commandProcessor("mv b a", s);
    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());
  }

  @Test
  /**
   * Test replacing a directory with file and file with a directory
   */
  public void testReplaceDirectoryWithFileAndFileWithDirectory()
      throws MissingNameException {
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"yea\" > b", s);
    // replace directory with a file
    JShell.commandProcessor("mv b a", s);
    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());

    s.clearBuffer();
    // replace file with a directory
    JShell.commandProcessor("mkdir c", s);
    JShell.commandProcessor("echo \"hello\" > a/b/c", s);
    JShell.commandProcessor("mv c a/b", s);

    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());

  }

  @Test
  /**
   * test moving src to a subdirectory
   */
  public void testMovingParentToSubdirectory() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b", s);
    // should not be able to move src to subdirectory
    JShell.commandProcessor("mv a a/b", s);
    assertEquals(
        "Can not add parent directory as the child of a sub" + " directory.",
        s.returnBuffer());
    s.clearBuffer();

    expected.add("a");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    // should not be able to move root to subdirectory
    JShell.commandProcessor("mv / a", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();

    // should not be able to move directory to itself
    JShell.commandProcessor("mv a a", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();

    // should not be able to move file to itself
    JShell.commandProcessor("echo \"hello\" > z", s);
    JShell.commandProcessor("mv z z", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    expected.add("z");
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();
  }

  @Test
  /**
   * Test moving subdirectory up few levels
   */
  public void testMovingSubdirectoryUp()
          throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    //move subdirectory to root
    JShell.commandProcessor("mv a/b/c /", s);
    expected.add("a");
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("echo \"hello\" > a/b/z", s);
    //move subfile to root
    JShell.commandProcessor("mv a/b/z /", s);
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd a/b/", s);
    expected.clear();
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /a", s);
    //move directory/file to the same location
    JShell.commandProcessor("mv /a/b /a", s);
    expected.add("b");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("", s.returnBuffer());
  }
  @Test
  /**
   * Test moving directory while in the directory
   */
  public void moveDirectoryWhileInIt()
          throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    //move subdirectory to root while in the subdirectory
    JShell.commandProcessor("cd a/b/c", s);
    JShell.commandProcessor("mv /a/b/c /", s);
    expected.add("a");
    expected.add("c");
    assertEquals(expected, s.getRootDir().getChildNames());
    assertEquals(s.getRootDir(), s.getCurrentDir().getParent());
    assertEquals("", s.returnBuffer());
  }

}
