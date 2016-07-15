package test;

import org.junit.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import driver.*;
import structures.*;
import exceptions.*;

/**
 * The Class OutputRedirectionTest tests our various cases of redirecting
 * standard output in to a file.
 */
public class OutputRedirectionTest {

  /** The session used for the tests. */
  MySession session;

  /**
   * Sets the up.
   */
  @Before
  public void setUp() {
    session = new MySession(new Output());
  }

  /**
   * Tear down.
   *
   * @throws InvalidNameException the invalid name exception
   * @throws NoSuchFieldException the no such field exception
   * @throws IllegalAccessException the illegal access exception
   */
  @After
  /**
   * The filesystem uses singleton design for the root directory. For testing
   * purposes, the root needs to be set to null everytime.
   */
  public void tearDown() throws InvalidNameException, NoSuchFieldException,
      IllegalAccessException {
    Field field = session.getRootDir().getClass().getDeclaredField("root");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  /**
   * Stores a string in a file and verifies, no output is returned and the file
   * contains the correct string.
   */
  @Test
  public void testStoreString() {
    JShell.commandProcessor("echo \"test\" > file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("test", message);
  }

  /**
   * Stores a string in a file and appends another string onto the same file and
   * verifies no output was sent to shell, and the string was properly stored in
   * the file.
   *
   */
  @Test
  public void testStoreAppendString() {
    JShell.commandProcessor("echo \"test\" > file", session);
    JShell.commandProcessor("echo \" add\" >> file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("test\n add", message);
  }

  /**
   * Stores a string in a file and overwrites it with another string onto the
   * same file and verifies no output was sent to shell, and only the second
   * string is stored in the file.
   *
   * @throws MissingNameException the missing name exception
   */
  @Test
  public void testStoreOverwriteString() throws MissingNameException {
    JShell.commandProcessor("echo \"test\" > file", session);
    JShell.commandProcessor("echo \"write\" > file", session);
    String message = "";
    try {
      message = ((File) session.getCurrentDir().getChild("file")).getContent();
    } catch (MissingNameException e) {
      fail("The file was not created");
    }
    assertTrue(session.getOutput().isEmpty());
    assertEquals("write", message);
  }

  /**
   * Trying to echo to a new file with the same name as a sub directory.
   */
  @Test
  public void testFileWithSameNameAsADirectory() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
    }
    JShell.commandProcessor("echo \"test\" > dir1", session);
    assertEquals("ERROR: There is already a subdirectory with the same name",
        session.returnBuffer());
  }

  /**
   * Verifies that redirecting the output of a command that produces no
   * output does not create the file.
   */
  @Test
  public void testRedirectNoOutput() {
    try {
      session.getCurrentDir().add(new Directory("dir1"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
      fail("Couldn't create a directory");
    }
    JShell.commandProcessor("cd dir1 > a", session);
    session.setCurrentDir(session.getRootDir());
    // Verify the file wasn't created
    assertTrue(!session.getCurrentDir().getChildNames().contains("a"));
  }

  /**
   * Tests redirecting the output of a display file call in shell.
   *
   * @throws InvalidNameException the invalid name exception
   */
  @Test
  public void testRedirectCopyFile() throws InvalidNameException {
    File testFile = new File("file1");
    testFile.setContent("file\ncontents\n");
    try {
      session.getCurrentDir().add(testFile);
    } catch (NameExistsException | InvalidAdditionException e) {
      fail("Couldn't create file");
    }
    JShell.commandProcessor("cat file1 > file1Dup", session);
    session.setCurrentDir(session.getRootDir());
    // Verify the file was created
    try {
      assertEquals(
          ((File) session.getCurrentDir().getChild("file1Dup")).getContent(),
          "file\ncontents\n");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }

  /**
   * Tests redirecting to a file given an absolute path to it.
   *
   * @throws InvalidNameException the invalid name exception
   * @throws MissingNameException the missing name exception
   */
  @Test
  public void testAbsoluteDir()
      throws InvalidNameException, MissingNameException {
    try {
      session.getCurrentDir().add(new Directory("dir"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
      fail("Couldn't create a directory");
    }
    session.setCurrentDir((Directory) session.getCurrentDir().getChild("dir"));
    JShell.commandProcessor("echo \"hi\" > /hi", session);
    session.setCurrentDir(session.getRootDir());
    // Verify the file was created
    try {
      assertEquals(((File) session.getCurrentDir().getChild("hi")).getContent(),
          "hi");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }

  /**
   * Tests redirecting to a file in a directory relative to the current one.
   *
   * @throws InvalidNameException the invalid name exception
   * @throws MissingNameException the missing name exception
   */
  @Test
  public void testRelativeDir()
      throws InvalidNameException, MissingNameException {
    try {
      Directory dir = new Directory("dirA");
      dir.add(new Directory("subDir"));
      session.getCurrentDir().add(dir);
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
      fail("Couldn't create a directory");
    }
    session.setCurrentDir((Directory) session.getCurrentDir().getChild("dirA"));
    JShell.commandProcessor("echo \"hi\" > subDir/hi", session);
    // Verify the file was created
    try {
      assertEquals(
          ((File) ((Directory) session.getCurrentDir().getChild("subDir"))
              .getChild("hi")).getContent(),
          "hi");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }

  /**
   * Tests redirecting to a file with a parent directory that does not exist.
   */
  @Test
  public void testNonExistingParent() {
    JShell.commandProcessor("echo \"hi\" > noOne/hi", session);
    assertEquals(session.returnBuffer(),
        "ERROR: The directory of the file does not exist");
  }

  /**
   * Tests redirecting a file that has an invalid name.
   */
  @Test
  public void testInvalidFileName() {
    JShell.commandProcessor("echo \"hi\" > $", session);
    assertEquals(session.returnBuffer(), "ERROR: That's an invalid file name");
  }

  /**
   * Tests that output is redirected to the file and error is sent to shell
   * when both are present.
   *
   * @throws InvalidNameException the invalid name exception
   * @throws MissingNameException the missing name exception
   */
  @Test
  public void testErrorAndOutput()
      throws InvalidNameException, MissingNameException {
    try {
      session.getCurrentDir().add(new Directory("dirA"));
      session.getCurrentDir().add(new Directory("dirB"));
    } catch (NameExistsException | InvalidAdditionException
        | InvalidNameException e) {
      fail("Couldn't create a directory");
    }
    JShell.commandProcessor("ls dirA dirB dirC > file", session);
    String expectedOutput = "dirA:\ndirB:";
    // Verify the file was created
    try {
      assertEquals(
          ((File) session.getCurrentDir().getChild("file")).getContent(),
          expectedOutput);
      assertEquals(session.returnBuffer(),
          "No such directory or file with path dirC\n");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }

}
