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
import structures.File;
import structures.Output;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by dhrumil on 14/07/16.
 */
public class MoveFileTest {

    MySession s;
    @Before
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
        field.set(null, null); //setting the ref parameter to null
        s.clearBuffer();
    }

    @Test
    public void testMovingPathDoesNotExist() {
        JShell.commandProcessor("mv a b", s);
        assertEquals("There are no files or directories with name a",
                s.returnBuffer());
        JShell.commandProcessor("mkdir a", s);
        s.clearBuffer();
        JShell.commandProcessor("mv a b/c", s);
        assertEquals("There are no files or directories with name b/",
                s.returnBuffer());
    }

    @Test
    public void testMovingRenameDirectories() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b a/b/c", s);
        JShell.commandProcessor("mv /a b", s);
        expected.add("b");
        assertEquals(expected, s.getRootDir().getChildNames());
        JShell.commandProcessor("cd b", s);
        assertEquals(expected, s.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd b", s);
        expected.clear();
        expected.add("c");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        JShell.commandProcessor("mv c /b/b/d", s);
        expected.clear();
        expected.add("d");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        assertEquals("", s.returnBuffer());
    }

    @Test
    public void testMovingRenameFile() throws MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b", s);
        JShell.commandProcessor("echo \"yea\" > d", s);
        JShell.commandProcessor("mv d /a/b/c", s);
        expected.add("a");
        assertEquals(expected, s.getRootDir().getChildNames());
        expected.clear();
        JShell.commandProcessor("cd a/b", s);
        expected.add("c");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        assertEquals("yea", ((File)s.getCurrentDir().getChild("c"))
                .getContent());
        assertEquals("", s.returnBuffer());
    }

    @Test
    public void testMovingDirectoryToFile() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b z", s);
        JShell.commandProcessor("echo \"hi\" > /a/b/c", s);
        JShell.commandProcessor("mv z /a/b/c", s);
        assertEquals("Invalid destination path. Can not move source directory.",
                s.returnBuffer());
        s.clearBuffer();
        JShell.commandProcessor("mv z /a/b/c/d", s);
        assertEquals("Invalid destination path. Can not move source directory.",
                s.returnBuffer());
    }

    @Test
    public void testReplacingFile() throws MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b", s);
        JShell.commandProcessor("echo \"hello\" > a/b/c", s);
        JShell.commandProcessor("echo \"yea\" > d", s);
        JShell.commandProcessor("mv d /a/b/c", s);
        expected.add("a");
        assertEquals(expected, s.getRootDir().getChildNames());
        expected.clear();
        JShell.commandProcessor("cd a/b", s);
        expected.add("c");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        assertEquals("yea", ((File)s.getCurrentDir().getChild("c"))
                .getContent());
        assertEquals("", s.returnBuffer());
    }

    @Test
    public void testMovingNameDoesNotExistDestination() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b c c/z", s);
        JShell.commandProcessor("mv c /a/b", s);
        expected.add("a");
        assertEquals(expected, s.getRootDir().getChildNames());
        expected.clear();
        JShell.commandProcessor("cd a/b", s);
        expected.add("c");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        expected.clear();
        JShell.commandProcessor("cd c", s);
        expected.add("z");
        assertEquals(expected, s.getCurrentDir().getChildNames());

        JShell.commandProcessor("cd ..", s);
        JShell.commandProcessor("echo \"yea\" > d", s);
        JShell.commandProcessor("mv d /a/b/c", s);
        JShell.commandProcessor("cd c", s);
        expected.add("d");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        assertEquals("", s.returnBuffer());

    }

    @Test
    public void testReplaceDirectoryOrFile() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b b b/z", s);
        JShell.commandProcessor("mv b a", s);
        expected.add("a");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd a/b", s);
        expected.clear();
        expected.add("z");
        assertEquals(expected, s.getCurrentDir().getChildNames());

        JShell.commandProcessor("echo \"hi\" > m", s);
        JShell.commandProcessor("cd /", s);
        JShell.commandProcessor("echo \"hello\" > m", s);
        JShell.commandProcessor("mv m a/b", s);
        JShell.commandProcessor("cd a/b", s);
        assertEquals("hello", ((File)s.getCurrentDir().getChild("m")).getContent());
        assertEquals("", s.returnBuffer());
    }

    @Test
    public void testReplaceDirectoryHasChildren() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b a/b/c b b/z", s);
        JShell.commandProcessor("mv b a", s);

        assertEquals("Unable to move. Type mismatch between " +
                "source file and file being replaced or the " +
                "file being replaced is not empty.", s.returnBuffer());
    }

    @Test
    public void testReplaceDirectoryWithFileAndFileWithDirectory() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b", s);
        JShell.commandProcessor("echo \"yea\" > b", s);
        JShell.commandProcessor("mv b a", s);

        assertEquals("Unable to move. Type mismatch between " +
                "source file and file being replaced or the " +
                "file being replaced is not empty.", s.returnBuffer());

        s.clearBuffer();

        JShell.commandProcessor("mkdir c", s);
        JShell.commandProcessor("echo \"hello\" > a/b/c", s);
        JShell.commandProcessor("mv c a/b", s);

        assertEquals("Unable to move. Type mismatch between " +
                "source file and file being replaced or the " +
                "file being replaced is not empty.", s.returnBuffer());

    }

    @Test
    public void testMovingParentToSubdirectory() throws
            MissingNameException {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir a a/b", s);
        JShell.commandProcessor("mv a a/b", s);
        assertEquals("Can not add parent directory as the child of a sub" +
                " directory.", s.returnBuffer());
        s.clearBuffer();
        expected.add("a");
        assertEquals(expected, s.getCurrentDir().getChildNames());
        JShell.commandProcessor("mv / a", s);
        assertEquals("Can not add parent directory as the child of a sub " +
                "directory.", s.returnBuffer());
        assertEquals(expected, s.getRootDir().getChildNames());
        s.clearBuffer();

        JShell.commandProcessor("mv a a", s);
        assertEquals("Can not add parent directory as the child of a sub " +
                "directory.", s.returnBuffer());
        assertEquals(expected, s.getRootDir().getChildNames());
        s.clearBuffer();

        JShell.commandProcessor("echo \"hello\" > z", s);
        JShell.commandProcessor("mv z z", s);
        assertEquals("Can not add parent directory as the child of a sub " +
                "directory.", s.returnBuffer());
        expected.add("z");
        assertEquals(expected, s.getRootDir().getChildNames());
        s.clearBuffer();



    }











}