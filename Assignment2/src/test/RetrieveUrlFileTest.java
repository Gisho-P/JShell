package test;

import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import driver.MySession;
import structures.Directory;
import structures.File;
import structures.FileTypes;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;


public class RetrieveUrlFileTest {
    MySession session;

    @Before
    /**
     * Create a new instance of MySession to start with an empty directory
     */
    public void setUp() {
        session = new MySession();
    }

    @Test
    /**
     * Test creating a file when the given url is invalid
     */
    public void testCreatingFileInvalidUrl() {
        assertEquals(JShell.commandProcessor("curl hi", session), "Malformed " +
                "URL.\n");
    }

    @Test
    /**
     * Test creating a file given a url when a file with the name already exists
     */
    public void testCreatingFileThatExists() throws FileTypes.InvalidName,
            Directory.NameExistsException, Directory.InvalidAddition {
        session.getCurrentDir().add(new File("073.txt"));
        assertEquals(JShell.commandProcessor(
                "curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt", session),
                "073.txt name is already in use in the current directory.\n");

    }

    @Test
    /**
     * Test that the created file has the correct name based on the url
     */
    public void testCreatingFileWithCorrectNameGivenValidUrl() {
        JShell.commandProcessor(
                "curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt", session);
        assertFalse(session.getCurrentDir().nameExists("073.txt") == -1);

    }

    @Test
    /**
     * Test the file created contains the correct content based on the data
     * returned from the url
     */
    public void testFileContainsCorrectContent() throws
            Directory.MissingNameException {
        JShell.commandProcessor(
                "curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt", session);
        assertTrue(((File)session.getCurrentDir().getChild("073.txt"))
                .getContent()
                .contains("how his brothers had\n" +
                "betrayed him, and how he had nevertheless kept silence"));

    }

    @Test
    /**
     * Test creating a file with the correct name when there is a slash at
     * the end of the url
     */
    public void testCreatingFileWithCorrectNameGivenUrlSlashAtEnd() {
        JShell.commandProcessor(
                "curl http://www.google.ca/", session);
        assertFalse(session.getCurrentDir().nameExists("www.google.ca") == -1);

    }
}