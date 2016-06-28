package test;

import org.junit.Test;

import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the FilePathInterpreter
 */
public class FileTraversalTest {

    @Test
    /**
     * Test interpreting path of file system with only one level when path
     * leads to dir
     */
    public void getLevelOneDirectoryTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
        Directory expected = new Directory("dir1");
        Directory test = new Directory("");
        test.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));
        //interpret path to dir single layer
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(test, "dir1");
        assertTrue(result.equals(expected));
    }

    @Test
    /**
     * Test interpreting path of file system with only one level when path
     * leads to a file
     */
    public void getLevelOneFileTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
        Directory test = new Directory("root");
        test.add(new Directory("test1"));
        test.add(new Directory("test2"));
        test.add(new Directory("test3"));
        File expected = new File("file1", "c1");
        test.add(expected);
        test.add(new File("file2", "c2"));
        //interpret path to dir multi layer
        File result = (File) FilePathInterpreter.interpretPath(test, "file1");
        assertTrue(result.equals(expected));

    }

    @Test
    /**
     * Test interpreting path of file system with only two levels when path
     * leads to dir
     */
    public void getLevelTwoDirectoryTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
        Directory sub = new Directory("dir1");
        Directory test = new Directory("root");
        test.add(sub);
        Directory expected = new Directory("dir4");
        sub.add(expected);
        test.add(new Directory("dir2"));
        test.add(new Directory("dir3"));
        test.add(new File("file1"));
        test.add(new File("file2", "c2"));
        //interpret path to dir multi layer
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(test,
                "dir1/dir4");
        assertTrue(result.equals(expected));

    }

    @Test
    /**
     * Test interpreting path of file system with only multi levels when path
     * leads to a file
     */
    public void getLevelTwoFileTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
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
        //interpret path to file multi layer
        File result;
        result = (File) FilePathInterpreter.interpretPath(test,
                "dir1/file3");
        assertTrue(result.equals(expected));

    }

    @Test
    /**
     * Test interpreting absolute path when it leads to a file
     */
    public void fromRootFileTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
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
        //Test if the absolute path is interpreted
        File result;
        result = (File) FilePathInterpreter.interpretPath(start,
                "/dir1/dir4/file3");
        assertTrue(result.equals(expected));

    }

    @Test
    /**
     * Test interpreting absolute path when it leads to a dir
     */
    public void fromRootDirTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
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
        //Test if the absolute path to dir is interpreted
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(start,
                "/dir1/dir4/expectedDir");
        assertTrue(result.equals(expected));
    }

    @Test
    /**
     * Test interpreting path if it only contains slashes
     */
    public void getDirSlashes() throws FileTypes.InvalidName,
            NameExistsException, InvalidAddition,
            InvalidDirectoryPathException {
        //Create FileSystem
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        //Should return the child directory
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(root, "child/");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/child/");
        assertTrue(result.equals(child));
        //Should interpret path to be root
        result = (Directory) FilePathInterpreter.interpretPath(child, "///////");
        assertTrue(result.equals(root));
    }

    @Test
    /**
     * Test interpreting paths that create a mixture of double dots
     */
    public void getParentDoubleDots() throws FileTypes.InvalidName,
            NameExistsException, InvalidAddition,
            InvalidDirectoryPathException {
        //Create FileSystem
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(child,
                "..");
        assertTrue(result.equals(root));
        //Test interpreting paths that create a mixture of double dots
        result = (Directory) FilePathInterpreter.interpretPath(root, "..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child,
                "/child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/child/..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/../child/../../../..");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/../child/../../../../");
        assertTrue(result.equals(root));
    }

    @Test
    /**
     * Test interpreting paths that create a mixture of single dots
     */
    public void singleDots() throws FileTypes.InvalidName,
            NameExistsException, InvalidAddition,
            InvalidDirectoryPathException {
        //Create FileSystem
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        //Test interpreting paths that create a mixture of single dots
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(child,
                ".");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, ".");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "./");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root, "/.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child,
                "/child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/child/.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/./child/./.");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "/./child/././");
        assertTrue(result.equals(child));
    }

    @Test
    /**
     *Test interpreting paths that contain a mixture of dots and slashes
     */
    public void mixtureOfDotsSlashes() throws FileTypes.InvalidName,
            NameExistsException, InvalidAddition,
            InvalidDirectoryPathException {
        //Create FileSystem
        Directory root = new Directory("");
        Directory child = new Directory("child");
        root.add(child);
        child.add(new Directory("grandchild"));
        //Should return the child directory
        Directory result;
        result = (Directory) FilePathInterpreter.interpretPath(child, "./../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/./../");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "../.");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(child, "/.././");
        assertTrue(result.equals(root));
        result = (Directory) FilePathInterpreter.interpretPath(root,
                "child/./grandchild/.././grandchild/./../");
        assertTrue(result.equals(child));
        result = (Directory) FilePathInterpreter.interpretPath(child,
                "/./../child/grandchild/./../grandchild/.././");
        assertTrue(result.equals(child));

    }

    @Test
    /**
     * Test interpretMakePath function which should interpret the path leading
     * only up to the parent
     */
    public void mkDirRootTest() throws NameExistsException,
            InvalidAddition, InvalidDirectoryPathException,
            FileTypes.InvalidName {
        //Create FileSystem
        Directory start = new Directory("root");

        Directory result;
        //interpretMakePath should return the parent of the last node in the
        //path
        result = (Directory) FilePathInterpreter.interpretMakePath
                (start, "test");
        assertTrue(result.equals(start));

        result = (Directory) FilePathInterpreter.interpretMakePath(start,
                "/test");
        assertTrue(result.equals(start));

        result = (Directory) FilePathInterpreter.interpretMakePath(start,
                "../test");
        assertTrue(result.equals(start));

    }


}
