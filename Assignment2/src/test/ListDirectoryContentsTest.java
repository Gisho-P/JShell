package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.InvalidNameException;
import exceptions.MissingNameException;
import exceptions.NameExistsException;
import structures.Directory;
import structures.File;
import structures.Output;

import static org.junit.Assert.assertEquals;

/**
 * Tests that verify the functionality of the ls command in JShell which lists
 * the contents of one or more directories, or file names.
 */
public class ListDirectoryContentsTest {

  /** The session */
  MySession session;

  /**
   * Initialize the session.
   */
  @Before
  public void setUp() {
    session = new MySession(new Output());
  }

  /**
   * Tear down. The filesystem uses singleton design for the root directory. For
   * testing purposes, the root needs to be set to null everytime.
   *
   * @throws InvalidNameException the invalid name exception
   * @throws NoSuchFieldException the no such field exception
   * @throws IllegalAccessException the illegal access exception
   */
  @After
  public void tearDown() throws InvalidNameException, NoSuchFieldException,
      IllegalAccessException {
    Field field = session.getRootDir().getClass().getDeclaredField("root");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null

    session.clearBuffer();
  }

  /**
   * Listing the contents of the empty root directory.
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
   * directory.
   */
  @Test
  public void testCurrentDirectory() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
      session.getCurrentDir().add(new File("file1"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("ls /", session);
    assertEquals("/: dir1 file1", session.getOutput());
    session.clearBuffer();

    JShell.commandProcessor("ls", session);
    assertEquals("dir1\nfile1\n", session.returnBuffer());
    session.clearBuffer();
  }

  /**
   * List contents on a file which should return the file name.
   */
  @Test
  public void testFilePath() {
    try {
      session.getCurrentDir().add(new File("file1"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }

    JShell.commandProcessor("ls file1", session);
    assertEquals("file1\n", session.returnBuffer());
  }

  /**
   * Multiple directories and a file for list contents which should return each
   * of their contents sorted in alphanumeric order (both the contents and
   * files/directories).
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
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException | MissingNameException e) {
    }

    JShell.commandProcessor("ls file1 / dir2 dir1", session);
    assertEquals("/: dir1 dir2 file1\ndir1: a b c d\ndir2:\nfile1\n",
        session.returnBuffer());
  }

  /**
   * Tests getting the contents of all the directories in the file system.
   */
  @Test
  public void testRecursiveDirectories() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("d"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("b"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("a"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("c"));
      session.getCurrentDir().add(new File("file1"));
      session.getCurrentDir().add(new Directory("dir2"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException | MissingNameException e) {
    }

    JShell.commandProcessor("ls -R", session);
    String expectedOutput =
        "/: dir1 dir2 file1\n\n/dir1: a b c d\n\n/dir1/a:\n\n/dir1/b:\n\n/dir1/"
            + "c:\n\n/dir1/d:\n\n/dir2:\n\n";
    assertEquals(expectedOutput, session.returnBuffer());
  }

  /**
   * Test getting the contents of all the given directories and their sub
   * directories.
   */
  @Test
  public void testRecursiveDirectoriesPaths() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("d"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("b"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("a"));
      ((Directory) session.getCurrentDir().getChild("dir1"))
          .add(new Directory("c"));
      session.getCurrentDir().add(new File("file1"));
      session.getCurrentDir().add(new Directory("dir2"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException | MissingNameException e) {
    }

    JShell.commandProcessor("ls -R dir1 dir2", session);
    String expectedOutput = "dir1: a b c d\n\ndir1/a:\n\ndir1/b:\n\ndir1/"
        + "c:\n\ndir1/d:\n\n\ndir2:\n\n";
    assertEquals(expectedOutput, session.returnBuffer());
  }

  @Test
  /**
   * Verifies that when an invalid path is given an error is raised
   */
  public void testInvalidPath() {
    JShell.commandProcessor("ls dir1", session);
    String expectedOutput = "No such directory or file with path dir1\n";
    assertEquals(expectedOutput, session.returnBuffer());
  }
}
