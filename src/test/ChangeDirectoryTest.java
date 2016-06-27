package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import driver.*;
import org.junit.Test;

public class ChangeDirectoryTest {
	public MySession session;
    
    /**
     * Test adding a directory to a directory
     */
	@Test
    public void testCdDoubleDots() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("cd ..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    public void testCdDoubleDotsSlash() {
        //Add a directory to a directory
        session = new MySession();
        JShell.commandProcessor("cd ../", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
        JShell.commandProcessor("cd /..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    public void testCdSingleDot() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("cd .", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }
	
	@Test
    public void testCdMultipleSlashes() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("mkdir sub1dir1", session);
        JShell.commandProcessor("cd sub1dir1/////////////", session);
        assertEquals(new ArrayList<String>(), session.getCurrentDir().getChildNames());
    }
	
	@Test
    public void testCdOneLevel() {
        //Add a directory to a directory
    	session = new MySession();
        ArrayList<String> expected = new ArrayList<String>();
    	JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("mkdir subdir2", session);
        JShell.commandProcessor("mkdir subdir3", session);
        expected.addAll(Arrays.asList("subdir1", "subdir2", "subdir3"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.clear();
        expected.add("dir1");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }
	
	@Test
    public void testCdTwoLevel() {
        //Add a directory to a directory
    	session = new MySession();
        ArrayList<String> expected = new ArrayList<String>();
    	JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("cd subdir1", session);
        JShell.commandProcessor("mkdir sub2dir1", session);
        JShell.commandProcessor("mkdir sub2dir2", session);
        JShell.commandProcessor("mkdir sub2dir3", session);
        expected.addAll(Arrays.asList("sub2dir1", "sub2dir2", "sub2dir3"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.clear();
        expected.add("subdir1");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    public void testCdMixtureOfSlashesDots() {
        //Add a directory to a directory
        session = new MySession();
        JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("mkdir subdir2", session);
        JShell.commandProcessor("mkdir subdir3", session);
        JShell.commandProcessor("cd subdir1", session);
        JShell.commandProcessor("cd ../subdir1/./../../", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    public void testCdGivenPathToFile() throws FileTypes.InvalidName, Directory.NameExistsException, Directory.InvalidAddition {
        //Add a directory to a directory
        session = new MySession();
        session.getRootDir().add(new File("file1"));
        JShell.commandProcessor("mkdir subdir1", session);

        assertEquals("file1 is not a directory.", JShell.commandProcessor("cd file1", session));
    }

}
