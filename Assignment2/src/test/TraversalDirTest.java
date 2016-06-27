package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import driver.*;
import org.junit.Test;

public class TraversalDirTest {
	public MySession session;
    
    /**
     * Test adding a directory to a directory
     */
	@Test
    public void cdDoubleDots() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("cd ..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    public void cdDoubleDotsSlash() {
        //Add a directory to a directory
        session = new MySession();
        JShell.commandProcessor("cd ../", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
        JShell.commandProcessor("cd /..", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }

    @Test
    public void cdSingleDot() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("cd .", session);
        assertEquals(session.getCurrentDir(), session.getRootDir());
    }
	
	@Test
    public void cdMultipleSlashes() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("mkdir sub1dir1", session);
        JShell.commandProcessor("cd sub1dir1/////////////", session);
        assertEquals("/sub1dir1: ", JShell.commandProcessor("ls", session));
    }
	
	@Test
    public void cdOneLevel() {
        //Add a directory to a directory
    	session = new MySession();
    	JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("mkdir subdir2", session);
        JShell.commandProcessor("mkdir subdir3", session);
        assertEquals( "/dir1: subdir1 subdir2 subdir3 ", JShell.commandProcessor("ls", session));
        JShell.commandProcessor("cd ..", session);
        assertEquals("/: dir1 ", JShell.commandProcessor("ls", session));
    }
	
	@Test
    public void cdTwoLevel() {
        //Add a directory to a directory
    	session = new MySession();
    	JShell.commandProcessor("mkdir dir1", session);
        JShell.commandProcessor("cd dir1", session);
        JShell.commandProcessor("mkdir subdir1", session);
        JShell.commandProcessor("cd subdir1", session);
        JShell.commandProcessor("mkdir sub2dir1", session);
        JShell.commandProcessor("mkdir sub2dir2", session);
        JShell.commandProcessor("mkdir sub2dir3", session);
        assertEquals(JShell.commandProcessor("ls", session), "/dir1/subdir1: sub2dir1 sub2dir2 sub2dir3 ");
        JShell.commandProcessor("cd ..", session);
        assertEquals(JShell.commandProcessor("ls", session), "/dir1: subdir1 ");
    }

    @Test
    public void cdMixtureOfSlashesDots() {
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
    public void cdGivenPathToFile() throws FileTypes.InvalidName, Directory.NameExistsException, Directory.InvalidAddition {
        //Add a directory to a directory
        session = new MySession();
        session.getRootDir().add(new File("file1"));
        JShell.commandProcessor("mkdir subdir1", session);
        System.out.println(JShell.commandProcessor("ls", session));

        assertEquals("file1 is not a directory.", JShell.commandProcessor("cd file1", session));
    }

	@Test
    public void mkdirMultipleSlashes() {
        //Add a directory to a directory
    	session = new MySession();
        JShell.commandProcessor("mkdir sub1dir1///////", session);
        assertEquals("/: sub1dir1 ", JShell.commandProcessor("ls", session));
        JShell.commandProcessor("cd sub1dir1", session);
        assertEquals("/sub1dir1: ", JShell.commandProcessor("ls", session));
    }

}
