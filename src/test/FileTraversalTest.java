package test;

import static org.junit.Assert.*;

import org.junit.Test;

import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes;

public class FileTraversalTest {

    @Test
    public void getLevelOneDirectoryTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {
        Directory expected = new Directory("dir1");
        Directory test = new Directory("");
        test.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));
        Directory result = (Directory) FilePathInterpreter.interpretPath(test, "dir1");
        assertTrue(result.equals(expected));
    }

    @Test
    public void getLevelOneFileTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory test = new Directory("root");
        test.add(new Directory("test1"));
        test.add(new Directory("test2"));
        test.add(new Directory("test3"));
        File expected = new File("file1", "c1");
        test.add(expected);
        test.add(new File("file2", "c2"));

        File result = (File) FilePathInterpreter.interpretPath(test, "file1");
        assertTrue(result.equals(expected));

    }

    @Test
    public void getLevelTwoDirectoryTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory sub = new Directory("dir1");
        Directory test = new Directory("root");
        test.add(sub);
        Directory expected = new Directory("dir4");
        sub.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));

        Directory result = (Directory) FilePathInterpreter.interpretPath(test, "dir1/dir4");
        assertTrue(result.equals(expected));

    }

    @Test
    public void getLevelTwoFileTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory sub = new Directory("dir1");
        Directory test = new Directory("root");
        test.add(sub);
        sub.add(new Directory("dir4"));
        File expected = new File("file3", "c3");
        sub.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));

        File result = (File) FilePathInterpreter.interpretPath(test, "dir1/file3");
        assertTrue(result.equals(expected));

    }

    @Test
    public void fromRootFileTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory sub = new Directory("dir1");
        Directory test = new Directory("root");
        test.add(sub);
        Directory start = new Directory("dir4");
        sub.add(start);
        File expected = new File("file3", "c3");
        start.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));

        File result = (File) FilePathInterpreter.interpretPath(start, "/dir1/dir4/file3");
        assertTrue(result.equals(expected));

    }

    @Test
    public void fromRootDirTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory sub = new Directory("dir1");
        Directory test = new Directory("root");
        test.add(sub);
        Directory start = new Directory("dir4");
        sub.add(start);
        Directory expected = new Directory("expectedDir");
        start.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));

        Directory result = (Directory) FilePathInterpreter.interpretPath(start, "/dir1/dir4/expectedDir");
        assertTrue(result.equals(expected));
    }

    @Test
    public void getDirSlashes() throws FileTypes.InvalidName, NameExistsException, InvalidAddition, InvalidDirectoryPathException {
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        //Should return the child directory
        Directory result = (Directory) FilePathInterpreter.interpretPath(root, "child/");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/child/");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child, "///////");
        assertTrue(result.equals(root));
    }

    @Test
    public void getParentDoubleDots() throws FileTypes.InvalidName, NameExistsException, InvalidAddition, InvalidDirectoryPathException {
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        //Should return the child directory
        Directory result = (Directory) FilePathInterpreter.interpretPath(child, "..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/../child/../../../..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/../child/../../../../");
        assertTrue(result.equals(root));
    }

    @Test
    public void singleDots() throws FileTypes.InvalidName, NameExistsException, InvalidAddition, InvalidDirectoryPathException {
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        //Should return the child directory
        Directory result = (Directory) FilePathInterpreter.interpretPath(child, ".");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, ".");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "./");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/./child/./.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/./child/././");
        assertTrue(result.equals(child));
    }

    @Test
    public void mixtureOfDotsSlashes() throws FileTypes.InvalidName, NameExistsException, InvalidAddition, InvalidDirectoryPathException {
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        child.add(new Directory("grandchild"));
        //Should return the child directory
        Directory result = (Directory) FilePathInterpreter.interpretPath(child, "./../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/./../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "../.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/.././");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "child/./grandchild/.././grandchild/./../");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/./../child/grandchild/./../grandchild/.././");
        assertTrue(result.equals(child));

    }




    @Test
    public void mkDirRootTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, FileTypes.InvalidName {

        Directory start = new Directory("root");

        Directory result = (Directory) FilePathInterpreter.interpretMakePath(start, "test");
        assertTrue(result.equals(start));

        result = (Directory) FilePathInterpreter.interpretMakePath(start, "/test");
        assertTrue(result.equals(start));

        result = (Directory) FilePathInterpreter.interpretMakePath(start, "../test");
        assertTrue(result.equals(start));

    }

    @Test
    public void testInterpretMakePathGivenRootPath() throws InvalidDirectoryPathException, FileTypes.InvalidName, NameExistsException, InvalidAddition {
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        Directory result = (Directory) FilePathInterpreter.interpretMakePath(root, "/");
        //Return the root in this case
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretMakePath(child, "/");
        assertTrue(result.equals(root));
    }

//    @Test
//    public void testInterpretMakePathParent() throws InvalidDirectoryPathException, FileTypes.InvalidName, NameExistsException, InvalidAddition {
//        Directory root = new Directory("");
//        Directory child = new Directory("child");
//        Directory grandchild = new Directory("grandchild");
//        root.add(child);
//        child.add(grandchild);
//        //Should return the root in this case
//        Directory result = (Directory) FilePathInterpreter.interpretMakePath(grandchild, "/child/grandchild");
//        assertTrue(result.equals(child));
//        result = (Directory) FilePathInterpreter.interpretMakePath(child, "/child");
//        assertTrue(result.equals(child));
//    }

//    @Test
//    public void testInterpretMakePathSlashes() throws InvalidDirectoryPathException, FileTypes.InvalidName, NameExistsException, InvalidAddition {
//        Directory root = new Directory("");
//        Directory child = new Directory("child");
//        root.add(child);
//        //Should return the root in this case
//        Directory result = (Directory) FilePathInterpreter.interpretPath(child, "///////");
//        assertTrue(result.equals(root));
//        result = (Directory) FilePathInterpreter.interpretMakePath(child, "/child");
//        assertTrue(result.equals(root));
//        result = (Directory) FilePathInterpreter.interpretMakePath(child, "child/");
//        assertTrue(result.equals(root));
//        result = (Directory) FilePathInterpreter.interpretMakePath(child, "/child/");
//        assertTrue(result.equals(root));
//    }



}
