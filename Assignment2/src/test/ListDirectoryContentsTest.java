package test;

import org.junit.Before;
import org.junit.Test;

import structures.Directory;
import structures.Directory.InvalidAddition;
import structures.Directory.MissingNameException;
import structures.Directory.NameExistsException;
import structures.File;
import structures.FileTypes.InvalidName;
import structures.Output;
import driver.JShell;
import driver.MySession;

import static org.junit.Assert.assertEquals;

import org.junit.After;

/**
 * Tests that verify the functionality of the ls command in JShell which lists
 * the contents of one or more directories, or file names.
 */
public class ListDirectoryContentsTest {

  MySession session;

  @Before
  public void setUp() {
    session = new MySession(new Output());
  }
  
  @After
  public void tearDown() {
	  session.clearBuffer();
	  session.clearFileSystem();
  }

  /**
   * Listing the contents of the empty root directory
   */
  @Test
  public void testEmptyDirectory() {
    JShell.commandProcessor("ls", session);
    assertEquals("", session.returnBuffer());
    session.clearBuffer();
    JShell.commandProcessor("ls /", session);
    assertEquals("/:", session.returnBuffer());
  }

  /**
   * Listing the contents of the current directory which contains a file and a
   * directory
   */
  @Test
  public void testCurrentDirectory() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
      session.getCurrentDir().add(new File("file1"));
    } catch (NameExistsException | InvalidAddition | InvalidName e) {
    }
    JShell.commandProcessor("ls /", session);
    assertEquals("/: dir1 file1", session.getOutput());
    session.clearBuffer();
    JShell.commandProcessor("ls", session);
    assertEquals("dir1\nfile1\n", session.returnBuffer());
    session.clearBuffer();
  }

  /**
   * List contents on a file which should return the file name
   */
  @Test
  public void testFilePath() {
    try {
      session.getCurrentDir().add(new File("file1"));
    } catch (NameExistsException | InvalidAddition | InvalidName e) {
    }
    JShell.commandProcessor("ls file1", session);
    assertEquals("file1\n", session.returnBuffer());
  }

  /**
   * Multiple directories and a file for list contents which should return each
   * of their contents sorted in alphanumeric order (both the contents and
   * files/directories)
   */
  @Test
  public void testMultipleDirectories() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
      ((Directory) session.getCurrentDir().getChild("dir1")).add(new File("d"));
      ((Directory) session.getCurrentDir().getChild("dir1")).add(new File("b"));
      ((Directory) session.getCurrentDir().getChild("dir1")).add(new File("a"));
      ((Directory) session.getCurrentDir().getChild("dir1")).add(new File("c"));
      session.getCurrentDir().add(new File("file1"));
      session.getCurrentDir().add(new Directory("dir2"));
    } catch (NameExistsException | InvalidAddition | InvalidName
        | MissingNameException e) {
    }
    JShell.commandProcessor("ls file1 / dir2 dir1", session);
    assertEquals("/: dir1 dir2 file1\ndir1: a b c d\ndir2:\nfile1\n",
        session.returnBuffer());
  }

}
