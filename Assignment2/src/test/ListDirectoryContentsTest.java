package test;

import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.MissingNameException;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FileTypes.InvalidName;
import driver.JShell;
import driver.MySession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Gisho on 27/06/16.
 * Tests that verify the functionality of the ls command in JShell which
 * lists the contents of one or more directories, or file names.
 */
public class ListDirectoryContentsTest {
    
    MySession session;
    
    @Before
    public void setUp() {
        session = new MySession();
    }

    /**
     * Listing the contents of the empty root directory
     */
    @Test
    public void testEmptyDirectory() {
        String message = JShell.commandProcessor("ls", session);
        String messagePath = JShell.commandProcessor("ls /", session);
        assertEquals("", message);
        assertEquals("/:", messagePath);
    }
 
    /**
     * Listing the contents of the current directory which contains a file
     * and a directory
     */
    @Test
    public void testCurrentDirectory() {
	try {
	    session.getCurrentDir().add(new Directory("dir1"));
	    session.getCurrentDir().add(new File("file1"));
	} catch (NameExistsException | InvalidAddition | InvalidName e) {
	}
        String message = JShell.commandProcessor("ls", session);
        String messagePath = JShell.commandProcessor("ls /", session);
        assertEquals("dir1 file1", message);
        assertEquals("/: dir1 file1", messagePath);
    }
    
    /**
     * List contents on a file which should return the file name
     */
    @Test
    public void testFilePath() {
	try {
	    session.getCurrentDir().add(new File("file1"));
	} catch (NameExistsException | InvalidAddition | InvalidName e) {
	}
        String message = JShell.commandProcessor("ls file1", session);
        assertEquals("file1", message);
    }
    
    /**
     * Multiple directories and a file for list contents which should
     * return each of their contents sorted in alphanumeric order 
     * (both the contents and files/directories)
     */
    @Test
    public void testMultipleDirectories() {
	try {
	    session.getCurrentDir().add(new Directory("dir1"));
	    ((Directory)session.getCurrentDir().getChild("dir1")).add(new File("d"));
	    ((Directory)session.getCurrentDir().getChild("dir1")).add(new File("b"));
	    ((Directory)session.getCurrentDir().getChild("dir1")).add(new File("a"));
	    ((Directory)session.getCurrentDir().getChild("dir1")).add(new File("c"));
	    session.getCurrentDir().add(new File("file1"));
	    session.getCurrentDir().add(new Directory("dir2"));
	} catch (NameExistsException | InvalidAddition | InvalidName | MissingNameException e) {
	}
        String message = JShell.commandProcessor("ls file1 / dir2 dir1", session);
        assertEquals("/: dir1 dir2 file1\ndir1: a b c d\ndir2:\nfile1", message);
    }
    
}