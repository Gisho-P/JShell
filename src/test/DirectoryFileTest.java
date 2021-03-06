package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import exceptions.*;
import structures.*;

/**
 * Tests for the Directory and File classes
 */
public class DirectoryFileTest {
  Directory dir;

  @Test
  /**
   * Create a directory with valid name and test if the invalid name exception
   * is thrown
   */
  public void createDirWithValidName() {
    try {
      dir = new Directory("root");
      assertEquals(dir.getName(), "root");
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    }
  }

  @Test(expected = InvalidNameException.class)
  /**
   * Create a directory with .. as the name and test if the invalid name
   * exception is thrown
   */
  public void createDirWithInValidName() throws InvalidNameException {
    dir = new Directory("..");
  }

  @Test(expected = InvalidNameException.class)
  /**
   * Create a directory with a slash in the name and test if the invalid name
   * exception is thrown
   */
  public void createDirWithInValidNameSlash() throws InvalidNameException {
    dir = new Directory("Hi/Bye");
  }

  @Test
  /**
   * Test adding a directory to a directory
   */
  public void addDirectoryToCurrent() {
    try {
      // Add a directory to a directory
      ArrayList<String> child = new ArrayList<String>();
      child.add("one");
      dir = new Directory("root");
      dir.add(new Directory("one"));
      assertEquals(dir.getChildNames(), child);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test adding a file to a directory
   */
  public void addFileToCurrent() {
    try {
      // Add a file to a directory
      ArrayList<String> child = new ArrayList<String>();
      child.add("one");
      dir = new Directory("root");
      dir.add(new File("one"));
      assertEquals(dir.getChildNames(), child);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test Adding a mixture of Files and Directories to a directory
   */
  public void addMultipleFilesAndDirectories() {
    try {
      ArrayList<String> child = new ArrayList<String>();
      // Add files and directories to a directory
      child.add("one");
      child.add("two");
      child.add("three");
      dir = new Directory("root");
      dir.add(new File("one"));
      dir.add(new Directory("two"));
      dir.add(new Directory("three"));

      // Check if all files and directories were inserted
      assertEquals(dir.getChildNames(), child);
      assertEquals((dir.getChild("one")) instanceof File, true);
      assertEquals((dir.getChild("two")) instanceof Directory, true);
      assertEquals((dir.getChild("three")) instanceof Directory, true);

    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    } catch (MissingNameException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = NameExistsException.class)
  /**
   * Test if NameExistsException is thrown when a directory or file with the
   * same name is added to the current directory
   */
  public void addDirectoryWithExistingName() throws NameExistsException {
    try {
      // Add a directory with existing name
      dir = new Directory("one");
      dir.add(new Directory("one"));
      dir.add(new Directory("one"));
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    }
  }

  @Test(expected = InvalidAdditionException.class)
  /**
   * Test if InvalidAdditionException exception is thrown from add method when
   * the current directory is added as the child of the current directory
   */
  public void testInvalidAddition() throws InvalidAdditionException {
    try {
      // Add the current directory to the current directory
      dir = new Directory("one");
      dir.add(dir);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = InvalidAdditionException.class)
  /**
   * Test if InvalidAdditionException exception is thrown from addReplace method
   * when the current directory is added as the child of the current directory
   */
  public void testInvalidAdditionInAddReplace()
      throws InvalidAdditionException {
    try {
      // Add the current directory to the current directory
      dir = new Directory("one");
      dir.addReplace(dir);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    }
  }

  @Test
  /**
   * Test replacing an Existing File or Directory with a File or Directory with
   * the same name
   */
  public void addReplaceExistingFileDirectory() {
    try {
      // Replace Existing Files or Diretories
      dir = new Directory("one");
      dir.add(new Directory("two"));
      assertEquals(dir.getChild("two") instanceof Directory, true);
      dir.addReplace(new File("two"));
      assertEquals(dir.getChild("two") instanceof File, true);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    } catch (MissingNameException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test replacing multiple existing Files or Directories with Files or
   * Directories with the same name
   */
  public void addReplaceExistingFileDirectoryMulti() {
    try {
      // Add/Replace multiple existing files or directories
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.add(new File("three"));
      dir.add(new Directory("four"));
      dir.add(new Directory("five"));
      assertEquals(dir.getChild("two") instanceof Directory, true);
      assertEquals(dir.getChild("three") instanceof File, true);
      assertEquals(dir.getChild("four") instanceof Directory, true);
      assertEquals(dir.getChild("five") instanceof Directory, true);

      ArrayList<FileTypes> multFile = new ArrayList<FileTypes>();
      multFile.add(new File("two"));
      multFile.add(new Directory("three"));
      multFile.add(new File("four"));
      dir.addReplaceMulti(multFile);

      // Tes if the Files/Directories were correctly replaced
      assertEquals(dir.getChild("two") instanceof File, true);
      assertEquals(dir.getChild("three") instanceof Directory, true);
      assertEquals(dir.getChild("four") instanceof File, true);
      assertEquals(dir.getChild("five") instanceof Directory, true);
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    } catch (MissingNameException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test removing a file or directory from the current directory
   */
  public void testRemoving() {
    try {
      // Test removing an existing file or directory
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.addReplace(new File("three"));
      dir.add(new Directory("five"));
      assertEquals(dir.getChildNames().size(), 3);

      dir.remove("two");
      dir.remove("three");
      dir.remove("five");
      assertEquals(dir.getChildNames().size(), 0);

    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    } catch (MissingNameException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test whether or not MissingNameException is thrown when a file or directory
   * that doesn't exist is removed
   */
  @Test(expected = MissingNameException.class)
  public void testRemovingFileDoesNotExist() throws MissingNameException {
    try {
      // Remove a directory that doesn't exist
      dir = new Directory("one");
      dir.add(new Directory("two"));

      dir.remove("three");

    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test to see if nameExists returns -1 when a file or directory doesn't exist
   * and the index of the file or directory in the list otherwise
   */
  public void testNameExists() {
    try {
      // Check if File/Directory with name exists in the current directory
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.add(new Directory("three"));
      dir.add(new Directory("four"));
      assertEquals(dir.nameExists("five"), -1);
      assertEquals(dir.nameExists("three"), 1);

    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test if all of the child directories are returned when getChildDir method
   * is called
   */
  public void testGetChildDir() {
    try {
      // Test retrieving only the children directories
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.add(new File("three"));
      dir.add(new File("four"));
      dir.add(new Directory("five"));
      assertEquals(dir.getChildDirs().size(), 2);
      assertEquals(dir.getChildDirs().get(0).getName(), "two");
      assertEquals(dir.getChildDirs().get(1).getName(), "five");
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test if all of the child Files are returned when getChildFiles method is
   * called
   */
  public void testGetChildFile() {
    try {
      // Test retrieving only the children Files
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.add(new File("three"));
      dir.add(new File("four"));
      dir.add(new Directory("five"));
      assertEquals(dir.getChildFiles().size(), 2);
      assertEquals(dir.getChildFiles().get(0).getName(), "three");
      assertEquals(dir.getChildFiles().get(1).getName(), "four");
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test the path to the directory when the directory has no parents
   */
  public void testEntirePathNoParent() {
    try {
      // Check if the path to parent is correct
      dir = new Directory("one");
      assertEquals(dir.getEntirePath(), "/one");
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    }
  }

  @Test
  /**
   * Test the path to the directory when the directory has parents
   */
  public void testEntirePathMultiple() {
    try {
      // Check if the path to parent is correct
      dir = new Directory("one");
      Directory dir2 = new Directory("two");
      dir.add(dir2);
      Directory dir3 = new Directory("three");
      dir2.add(dir3);
      Directory dir4 = new Directory("four");
      dir3.add(dir4);

      assertEquals(dir4.getEntirePath(), "/one/two/three/four");
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test to check if the names of all of the children in the current directory
   * are returned
   */
  public void testGetChildrenNames() {
    ArrayList<String> childNames = new ArrayList<String>();
    childNames.add("two");
    childNames.add("three");
    childNames.add("four");
    childNames.add("five");

    try {
      // Add children to current directory
      dir = new Directory("one");
      dir.add(new Directory("two"));
      dir.add(new File("three"));
      dir.add(new File("four"));
      dir.add(new Directory("five"));
      // Check if all of the children names were retrieved
      assertEquals(dir.getChildNames(), childNames);
    } catch (InvalidAdditionException invalidAddition) {
      invalidAddition.printStackTrace();
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    } catch (NameExistsException e) {
      e.printStackTrace();
    }
  }

  @Test
  /**
   * Test appending new content to the end of a file
   */
  public void testAppendingFileContent() {
    try {
      File file = new File("one");
      file.setContent("B07 is fantastic!");
      // Append to file
      file.appendContent("Yes it is.");
      assertEquals(file.getContent(), "B07 is fantastic!\nYes it is.");
    } catch (InvalidNameException invalidName) {
      invalidName.printStackTrace();
    }
  }
}
