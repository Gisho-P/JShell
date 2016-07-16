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
import structures.Directory;
import structures.File;
import structures.Output;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by dhrumil on 14/07/16.
 */
public class CopyFileTest {

  /**
   * Need instance of mysession to store output and filesystem
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
   * Test cp command given the source or destination paths do not exist
   */
  public void testCopyingPathDoesNotExist() {
    // source doesn't exist
    JShell.commandProcessor("cp a b", s);
    assertEquals("There are no files or directories with name a",
        s.returnBuffer());
    JShell.commandProcessor("mkdir a", s);
    s.clearBuffer();
    // dest does not exist
    JShell.commandProcessor("cp a b/c", s);
    assertEquals("There are no files or directories with name b/",
        s.returnBuffer());
  }

  @Test
  /**
   * Test renaming and copying directory using the cp command
   */
  public void testCopyingRenameDirectories() {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    // test renaming a to b
    JShell.commandProcessor("cp /a b", s);
    expected.add("b");
    expected.add("a");
    assertEquals(expected, s.getRootDir().getChildNames());
    JShell.commandProcessor("cd b/b", s);
    expected.clear();
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /a/b", s);
    assertEquals(expected, s.getCurrentDir().getChildNames());
    expected.clear();
    // test renaming c to d
    JShell.commandProcessor("cp c /a/b/d", s);
    expected.add("d");
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /b/b", s);
    expected.remove("d");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test copying and renaming the file at src to the destination name
   */
  public void testCopyingRenameFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // reaname file d to c, d should still remain in current directory
    JShell.commandProcessor("cp d /a/b/c", s);
    expected.add("a");
    expected.add("d");
    assertEquals(expected, s.getRootDir().getChildNames());
    assertEquals("yea", ((File) s.getCurrentDir().getChild("d")).getContent());
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
   * Test copying directory to destination path that leads to file
   */
  public void testCopyingDirectoryToFile() throws MissingNameException {
    // try moving directory to file
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("a");
    JShell.commandProcessor("mkdir a a/b z", s);
    JShell.commandProcessor("echo \"hi\" > /a/b/c", s);
    JShell.commandProcessor("cp z /a/b/c", s);
    assertEquals("Invalid destination path. Can not move source directory.",
        s.returnBuffer());
    s.clearBuffer();
    expected.add("z");
    JShell.commandProcessor("cd a/b/c", s);
    assertEquals("c is not a directory.", s.returnBuffer());
    s.clearBuffer();
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cp z /a/b/c/d", s);
    assertEquals("Invalid destination path. Can not move source directory.",
        s.returnBuffer());
    s.clearBuffer();
  }

  @Test
  /**
   * Test copying file at destination with file at source given both the source
   * and destination paths lead to files
   */
  public void testCopyingFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"hello\" > a/b/c", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // replace file c with d
    JShell.commandProcessor("cp d /a/b/c", s);
    expected.add("a");
    expected.add("d");
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
   * Test copying the directory or file to another directory
   */
  public void testCopyingFileOrDirectoryToDirectory()
      throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    // mv directory c to directory b
    JShell.commandProcessor("mkdir a a/b c c/z", s);
    JShell.commandProcessor("cp c /a/b", s);
    expected.add("a");
    expected.add("c");
    assertEquals(expected, s.getRootDir().getChildNames());
    expected.clear();
    JShell.commandProcessor("cd a/b", s);
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    expected.clear();
    JShell.commandProcessor("cd c", s);
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /c", s);
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /a/b", s);
    JShell.commandProcessor("echo \"yea\" > d", s);
    // move file d to directory c
    JShell.commandProcessor("cp d /a/b/c", s);
    expected.clear();
    expected.add("c");
    expected.add("d");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    expected.clear();
    expected.add("z");
    expected.add("d");
    JShell.commandProcessor("cd c", s);;
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Replace file at destination with file at source if it already exists
   */
  public void testCopyingDirectoryOrFile() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    // replace directory at destination
    JShell.commandProcessor("mkdir a a/b b b/z", s);
    JShell.commandProcessor("cp b a", s);
    expected.add("a");
    expected.add("b");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd a/b", s);
    expected.clear();
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());

    // replace file at destination
    JShell.commandProcessor("echo \"hi\" > m", s);
    JShell.commandProcessor("cd /", s);
    JShell.commandProcessor("echo \"hello\" > m", s);
    JShell.commandProcessor("cp m a/b", s);
    JShell.commandProcessor("cd a/b", s);
    assertEquals("hello",
        ((File) s.getCurrentDir().getChild("m")).getContent());
    JShell.commandProcessor("cd /", s);
    assertEquals("hello",
        ((File) s.getCurrentDir().getChild("m")).getContent());
    assertEquals("", s.returnBuffer());
  }

  @Test
  /**
   * Test copying a directory with children at the destination
   */
  public void testCopyingDirectoryHasChildren() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("a");
    expected.add("b");
    JShell.commandProcessor("mkdir a a/b a/b/c b b/z", s);
    // try replacing directory with children. Should result in an error
    JShell.commandProcessor("cp b a", s);
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());
  }

  @Test
  /**
   * Test copying a directory with file and file with a directory
   */
  public void testCopyingDirectoryWithFileAndFileWithDirectory()
      throws MissingNameException {
    JShell.commandProcessor("mkdir a a/b", s);
    JShell.commandProcessor("echo \"yea\" > b", s);
    // replace directory with a file
    JShell.commandProcessor("cp b a", s);

    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());

    s.clearBuffer();
    // replace file with a directory
    JShell.commandProcessor("mkdir c", s);
    JShell.commandProcessor("echo \"hello\" > a/b/c", s);
    JShell.commandProcessor("cp c a/b", s);

    assertEquals("Error. Type mismatch between "
        + "source file and file being replaced or the "
        + "file being replaced is not empty.", s.returnBuffer());

  }

  @Test
  /**
   * test copying src to a subdirectory
   */
  public void testCopyingParentToSubdirectory() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b", s);
    // should not be able to move src to subdirectory
    JShell.commandProcessor("cp a a/b", s);
    assertEquals(
        "Can not add parent directory as the child of a sub" + " directory.",
        s.returnBuffer());
    s.clearBuffer();
    expected.add("a");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    // should not be able to move root to subdirectory
    JShell.commandProcessor("cp / a", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();
    // should not be able to move directory to itself
    JShell.commandProcessor("cp a a", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();
    // should not be able to move file to itself
    JShell.commandProcessor("echo \"hello\" > z", s);
    JShell.commandProcessor("cp z z", s);
    assertEquals(
        "Can not add parent directory as the child of a sub " + "directory.",
        s.returnBuffer());
    expected.add("z");
    assertEquals(expected, s.getRootDir().getChildNames());
    s.clearBuffer();
  }

  @Test
  /**
   * Test copying subdirectory up few levels
   */
  public void testCopyingSubdirectoryUp() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    // move subdirectory to root
    JShell.commandProcessor("cp a/b/c /", s);
    expected.add("a");
    expected.add("c");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd a/b/c", s);
    assertEquals("", s.returnBuffer());
    JShell.commandProcessor("cd /", s);
    JShell.commandProcessor("echo \"hello\" > a/b/z", s);
    // move subfile to root
    JShell.commandProcessor("cp a/b/z /", s);
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd a/b/", s);
    expected.clear();
    expected.add("c");
    expected.add("z");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    JShell.commandProcessor("cd /a", s);
    // move directory/file to the same location
    JShell.commandProcessor("cp /a/b /a", s);
    expected.clear();
    expected.add("b");
    assertEquals(expected, s.getCurrentDir().getChildNames());
    assertEquals("Error. Type mismatch between source file and"
        + " file being replaced or the file being replaced" + " is not empty.",
        s.returnBuffer());
    s.clearBuffer();
    JShell.commandProcessor("mkdir /x /x/y", s);
    JShell.commandProcessor("cp /x/y /x", s);
    assertEquals("Unable to copy. File already exists in directory.",
        s.returnBuffer());
  }

  @Test
  /**
   * Test copying directory while in the directory
   */
  public void moveDirectoryWhileInIt() throws MissingNameException {
    ArrayList<String> expected = new ArrayList<String>();
    JShell.commandProcessor("mkdir a a/b a/b/c", s);
    // move subdirectory to root while in the subdirectory
    JShell.commandProcessor("cd a/b/c", s);
    JShell.commandProcessor("cp /a/b/c /", s);
    expected.add("a");
    expected.add("c");

    assertEquals(expected, s.getRootDir().getChildNames());
    assertEquals(((Directory) s.getRootDir().getChild("a")).getChild("b"),
        s.getCurrentDir().getParent());
    assertEquals("", s.returnBuffer());
  }

}
