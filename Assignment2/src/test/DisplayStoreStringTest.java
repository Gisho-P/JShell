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
 * Tests that verify the functionality of the echo command in JShell which
 * can display strings in the shell or store them in files.
 */
public class DisplayStoreStringTest {
    
    MySession session;
    
    @Before
    public void setUp() {
        session = new MySession();
    }

    /**
     * Using echo to output an empty string to JShell.
     */
    @Test
    public void testDisplayEmptyString() {
        String message = JShell.commandProcessor("echo \"\"", session);
        assertEquals("", message);
    }
    
    /**
     * Displaying a one word string to JShell.
     */
    @Test
    public void testDisplayOneWord() {
        String message = JShell.commandProcessor("echo \"test\"", session);
        assertEquals("test", message);
    }
    
    /**
     * Displaying a multiple word string to JShell.
     */
    @Test
    public void testDisplayMultipleWords() {
        String message = JShell.commandProcessor("echo \"1 2 3 4 ss\"", session);
        assertEquals("1 2 3 4 ss", message);
    }
    
    /**
     * Stores a string in a file and verifies, no output is returned and
     * the file contains the correct string.
     *
     */
    @Test
    public void testStoreString() {
	String out = JShell.commandProcessor("echo \"test\" > file", session);
        String message = "";
	try {
	    message = ((File) session.getCurrentDir().getChild("file")).getContent();
	} catch (MissingNameException e) {
	    fail("The file was not created");
	}
        assertTrue(out.isEmpty());
        assertEquals("test", message);
    }
    
    /**
     * Stores a string in a file and appends another string onto the same
     * file and verifies no output was sent to shell, and the string was
     * properly stored in the file.
     *
     */
    @Test
    public void testStoreAppendString(){
	String out = JShell.commandProcessor("echo \"test\" > file", session);
	out += JShell.commandProcessor("echo \" add\" >> file", session);
	String message = "";
	try {
	    message = ((File) session.getCurrentDir().getChild("file")).getContent();
	} catch (MissingNameException e) {
	    fail("The file was not created");
	}
        assertTrue(out.isEmpty());
        assertEquals("test\n add", message);
    }
    
    /**
     * Stores a string in a file and overwrites it with another string onto the 
     * same file and verifies no output was sent to shell, and only the second
     * string is stored in the file.
     *
     */
    @Test
    public void testStoreOverwriteString() throws MissingNameException {
	String out = JShell.commandProcessor("echo \"test\" > file", session);
	out += JShell.commandProcessor("echo \"write\" > file", session);
	String message = "";
	try {
	    message = ((File) session.getCurrentDir().getChild("file")).getContent();
	} catch (MissingNameException e) {
	    fail("The file was not created");
	}
        assertTrue(out.isEmpty());
        assertEquals("write", message);
    }
    
    /**
     * Trying to echo to a new file with the same name as a sub directory
     */
    @Test
    public void testFileWithSameNameAsDirectory() {
	try {
	    session.getCurrentDir().add(new Directory("file"));
	} catch (NameExistsException | InvalidAddition | InvalidName e) {
	}
        String message = JShell.commandProcessor("echo \"test\" > file", session);
        assertEquals("ERROR: There is already a subdirectory with the same name", message);
    }
    
}