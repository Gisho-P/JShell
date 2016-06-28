package test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import driver.Directory;
import driver.File;
import driver.FileTypes;
import driver.JShell;
import driver.MySession;

import static org.junit.Assert.assertEquals;

/**
 * Tests for ChangeDirectory class
 */
public class ChangeDirectoryTest {
    public MySession session;

    @Before
    /**
     *Create new instance of MySession before every test
     */
    public void setUp() {
        session = new MySession();
    }

    /**
     * Test adding a directory to a directory
     */
    @Test
    public void testCdDoubleDots() {
        //Change to parent directory
        JShell.commandProcessor("cd ..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    /**
     *Test changing directory with slash and double dot
     */
    public void testCdDoubleDotsSlash() {
        //change to parent directory with slash at front or end
        JShell.commandProcessor("cd ../", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
        JShell.commandProcessor("cd /..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    /**
     * Test changing directory using single dot
     */
    public void testCdSingleDot() {
        //Directory shouldnt change
        JShell.commandProcessor("cd .", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    /**
     * Test changing directory when there are multiple slashes
     */
    public void testCdMultipleSlashes() {
        //directory should still change if there are multiple slashes at the end
        JShell.commandProcessor("mkdir sub1dir1", session);
        JShell.commandProcessor("cd sub1dir1/////////////", session);
        assertEquals(new ArrayList<String>(),
                session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Test changing directory on filesystem with only one level
     */
    public void testCdOneLevel() {
        //Create a file system with one level
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("mkdir subdir2", session);
        JShell.commandProcessor("mkdir subdir3", session);
        //Test changing directory
        expected.addAll(Arrays.asList("subdir1", "subdir2", "subdir3"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.clear();
        expected.add("dir1");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     *Test changing directory on filesystem with multiple levels
     */
    public void testCdTwoLevel() {
        //Create filesystem with multiple layers
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("cd subdir1", session);
        JShell.commandProcessor("mkdir sub2dir1", session);
        JShell.commandProcessor("mkdir sub2dir2", session);
        JShell.commandProcessor("mkdir sub2dir3", session);
        //Change directory on file system with multiple layers
        expected.addAll(Arrays.asList("sub2dir1", "sub2dir2", "sub2dir3"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.clear();
        expected.add("subdir1");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Test changing directory when combination of slashes and dots are used
     */
    public void testCdMixtureOfSlashesDots() {
        //Create filesystem
        JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("mkdir subdir2", session);
        JShell.commandProcessor("mkdir subdir3", session);
        JShell.commandProcessor("cd subdir1", session);
        //Test changing directory
        JShell.commandProcessor("cd ../subdir1/./../../", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    /**
     * Test changing directory when path to file is given instead
     */
    public void testCdGivenPathToFile() throws FileTypes.InvalidName,
            Directory.NameExistsException, Directory.InvalidAddition {
        //Create file system
        session.getRootDir().add(new File("file1"));
        JShell.commandProcessor("mkdir subdir1", session);
        //Should fail to change directory since file is given
        assertEquals("file1 is not a directory.",
                JShell.commandProcessor("cd file1", session));
    }

}
