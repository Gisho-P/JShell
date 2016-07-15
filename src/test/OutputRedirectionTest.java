package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import driver.JShell;
import driver.MySession;
import structures.Directory;
import exceptions.InvalidAdditionException;
import exceptions.MissingNameException;
import exceptions.NameExistsException;
import structures.File;
import exceptions.InvalidNameException;
import structures.Output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests that verify the functionality of the echo command in JShell which can
 * display strings in the shell or store them in files.
 */
public class OutputRedirectionTest {

  MySession session;

  @Before
  public void setUp() {
    session = new MySession(new Output());
  }

  @After
  /**
   * Clear the contents in the file system and output buffer
   */
  public void tearDown() {
    session.clearFileSystem();
    session.clearBuffer();
  }

  /**
   * Stores a string in a file and verifies, no output is returned and the file
   * contains the correct string.
   *
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
   * Trying to echo to a new file with the same name as a sub directory
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
   * Trying to redirect the output of change directory which should result
   * in no redirection because cd has no stdout
   */
  @Test
  public void testRedirectChangeDirectory(){
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
   * Trying to redirect the output of change directory which should result
   * in no redirection because cd has no stdout
   * @throws InvalidNameException 
   */
  @Test
  public void testRedirectCopyFile() throws InvalidNameException{
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
      assertEquals(((File) session.getCurrentDir().getChild("file1Dup")).
          getContent(), "file\ncontents\n");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }
  
  /**
   * Trying to redirect the output of change directory which should result
   * in no redirection because cd has no stdout
   * @throws InvalidNameException 
   * @throws MissingNameException 
   */
  @Test
  public void testAbsoluteDir() throws InvalidNameException, MissingNameException{
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
      assertEquals(((File) session.getCurrentDir().getChild("hi")).
          getContent(), "hi");
    } catch (MissingNameException e) {
      fail("File wasn't created");
    }
  }

}
