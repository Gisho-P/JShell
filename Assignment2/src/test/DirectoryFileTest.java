package test;

import driver.Directory;
import driver.File;
import driver.FileTypes;
import org.junit.Before;
import org.junit.Test;
import sun.font.TrueTypeFont;

import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * Created by dhrumil on 19/06/16.
 */
public class DirectoryFileTest {
    public Directory dir;

    @Test
    public void createDirWithValidName() {
        try {
            dir = new Directory("root");
            assertEquals(dir.getName(), "root");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test(expected = FileTypes.InvalidName.class)
    public void createDirWithInValidName() throws FileTypes.InvalidName {
        dir = new Directory("..");
    }

    @Test(expected = FileTypes.InvalidName.class)
    public void createDirWithInValidNameSlash() throws FileTypes.InvalidName {
        dir = new Directory("Hi/Bye");
    }

    @Test
    public void addDirectoryToCurrent() {
        try {
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
    public void addFileToCurrent() {
        try {
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
    public void addMultipleFilesAndDirectories() {
        try {
            ArrayList<String> child = new ArrayList<String>();
            child.add("one");
            child.add("two");
            child.add("three");
            dir = new Directory("root");
            dir.add(new File("one"));
            dir.add(new Directory("two"));
            dir.add(new Directory("three"));

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
    public void testInvalidAdditionInAddReplace() throws Directory.InvalidAddition {
        try {
            dir = new Directory("one");
            dir.addReplace(dir);
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test
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
    public void testEntirePathNoParent() {
        try {
            dir = new Directory("one");
            assertEquals(dir.getEntirePath(), "/one");
        } catch (FileTypes.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Test
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