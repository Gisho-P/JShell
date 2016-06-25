package test;

import driver.Directory;
import driver.File;
import driver.FileTypes;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * Created by dhrumil on 19/06/16.
 */
public class DirectoryFileTest {
    public Directory dir;

    @Test
    /**
     * Create a directory with valid name and test if the invalid name exception is thrown
     */
    public void createDirWithValidName() {
        try {
            dir = new Directory("root");
            assertEquals(dir.getName(), "root");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test(expected = FileTypes.InvalidName.class)
    /**
     * Create a directory with .. as the name and test if the invalid name exception is thrown
     */
    public void createDirWithInValidName() throws FileTypes.InvalidName {
        dir = new Directory("..");
    }

    @Test(expected = FileTypes.InvalidName.class)
    /**
     * Create a directory with a slash in the name and test if the invalid name exception is thrown
     */
    public void createDirWithInValidNameSlash() throws FileTypes.InvalidName {
        dir = new Directory("Hi/Bye");
    }

    @Test
    /**
     * Test adding a directory to a directory
     */
    public void addDirectoryToCurrent() {
        try {
            //Add a directory to a directory
            ArrayList<String> child = new ArrayList<String>();
            child.add("one");
            dir = new Directory("root");
            dir.add(new Directory("one"));
            assertEquals(dir.getChildNames(), child);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Test adding a file to a directory
     */
    public void addFileToCurrent() {
        try {
            //Add a file to a directory
            ArrayList<String> child = new ArrayList<String>();
            child.add("one");
            dir = new Directory("root");
            dir.add(new File("one"));
            assertEquals(dir.getChildNames(), child);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
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
            //Add files and directories to a directory
            child.add("one");
            child.add("two");
            child.add("three");
            dir = new Directory("root");
            dir.add(new File("one"));
            dir.add(new Directory("two"));
            dir.add(new Directory("three"));

            //Check if all files and directories were inserted
            assertEquals(dir.getChildNames(), child);
            assertEquals((dir.getChild("one")) instanceof File, true);
            assertEquals((dir.getChild("two")) instanceof Directory, true);
            assertEquals((dir.getChild("three")) instanceof Directory, true);

        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        } catch (Directory.MissingNameException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Directory.NameExistsException.class)
    /**
     * Test if NameExistsException is thrown when a directory or file with the same name is added to the current
     * directory
     */
    public void addDirectoryWithExistingName() throws Directory.NameExistsException {
        try {
            dir = new Directory("one");
            dir.add(new Directory("one"));
            dir.add(new Directory("one"));
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        }
    }

    @Test(expected = Directory.InvalidAddition.class)
    /**
     * Test if InvalidAddition exception is thrown from add method when the current directory is added as the
     * child of the current directory
     */
    public void testInvalidAddition() throws Directory.InvalidAddition {
        try {
            dir = new Directory("one");
            dir.add(dir);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Directory.InvalidAddition.class)
    /**
     * Test if InvalidAddition exception is thrown from addReplace method when the current directory is added as the
     * child of the current directory
     */
    public void testInvalidAdditionInAddReplace() throws Directory.InvalidAddition {
        try {
            dir = new Directory("one");
            dir.addReplace(dir);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test
    /**
     * Test replacing an Existing File or Directory with a File or Directory with the same name
     */
    public void addReplaceExistingFileDirectory() {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            assertEquals(dir.getChild("two") instanceof Directory, true);
            dir.addReplace(new File("two"));
            assertEquals(dir.getChild("two") instanceof File, true);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        } catch (Directory.MissingNameException e) {
            e.printStackTrace();
        }

    }

    @Test
    /**
     * Test replacing multiple existing Files or Directories with Files or Directories with the same name
     */
    public void addReplaceExistingFileDirectoryMulti() {
        try {
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

            assertEquals(dir.getChild("two") instanceof File, true);
            assertEquals(dir.getChild("three") instanceof Directory, true);
            assertEquals(dir.getChild("four") instanceof File, true);
            assertEquals(dir.getChild("five") instanceof Directory, true);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        } catch (Directory.MissingNameException e) {
            e.printStackTrace();
        }

    }

    @Test
    /**
     * Test removing a file or directory from the current directory
     */
    public void testRemoving() {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            dir.addReplace(new File("three"));
            dir.add(new Directory("five"));
            assertEquals(dir.getChildNames().size(), 3);


            dir.remove("two");
            dir.remove("three");
            dir.remove("five");
            assertEquals(dir.getChildNames().size(), 0);


        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        } catch (Directory.MissingNameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test whether or not MissingNameException is thrown when a file or directory that doesn't exist is removed
     * @throws Directory.MissingNameException
     */
    @Test(expected = Directory.MissingNameException.class)
    public void testRemovingFileDoesNotExist() throws Directory.MissingNameException {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));

            dir.remove("three");


        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Test to see if nameExists returns -1 when a file or directory doesn't exist and the index of the file or
     * directory in the list otherwise
     */
    public void testNameExists() {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            dir.add(new Directory("three"));
            dir.add(new Directory("four"));
            assertEquals(dir.nameExists("five"), -1);
            assertEquals(dir.nameExists("three"), 1);

        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Test if all of the child directories are returned when getChildDir method is called
     */
    public void testGetChildDir() {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            dir.add(new File("three"));
            dir.add(new File("four"));
            dir.add(new Directory("five"));
            assertEquals(dir.getChildDirs().size(), 2);
            assertEquals(dir.getChildDirs().get(0).getName(), "two");
            assertEquals(dir.getChildDirs().get(1).getName(), "five");
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     *  Test if all of the child Files are returned when getChildFiles method is called
     */
    public void testGetChildFile() {
        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            dir.add(new File("three"));
            dir.add(new File("four"));
            dir.add(new Directory("five"));
            assertEquals(dir.getChildFiles().size(), 2);
            assertEquals(dir.getChildFiles().get(0).getName(), "three");
            assertEquals(dir.getChildFiles().get(1).getName(), "four");
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Test the path to the directory when the directory has no parents
     */
    public void testEntirePathNoParent() {
        try {
            dir = new Directory("one");
            assertEquals(dir.getEntirePath(), "/one");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test
    /**
     * Test the path to the directory when the directory has parents
     */
    public void testEntirePathMultiple() {
        try {
            dir = new Directory("one");
            Directory dir2 = new Directory("two");
            dir.add(dir2);
            Directory dir3 = new Directory("three");
            dir2.add(dir3);
            Directory dir4 = new Directory("four");
            dir3.add(dir4);

            assertEquals(dir4.getEntirePath(), "/one/two/three/four");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (Directory.NameExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Test to check if the names of all of the children in the current directory are returned
     */
    public void testGetChildrenNames() {
        ArrayList<String> childNames = new ArrayList<String>();
        childNames.add("two");
        childNames.add("three");
        childNames.add("four");
        childNames.add("five");

        try {
            dir = new Directory("one");
            dir.add(new Directory("two"));
            dir.add(new File("three"));
            dir.add(new File("four"));
            dir.add(new Directory("five"));
            assertEquals(dir.getChildNames(), childNames);
        } catch (Directory.InvalidAddition invalidAddition) {
            invalidAddition.printStackTrace();
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (Directory.NameExistsException e) {
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
            file.appendContent("Yes it is.");
            assertEquals(file.getContent(), "B07 is fantastic!\nYes it is.");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }


    }


}