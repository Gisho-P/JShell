package test;

import driver.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by dhrumil on 26/06/16.
 * Unit Test for the mkdir command
 */
public class MakeDirectoryTest {
    MySession session;
    @Before
    /**
     * Create a new instance of MySession before every testcase
     */
    public void setUp() {
        session = new MySession();
    }

    @Test
    /**
     * Test creating a directory with slash as the name
     */
    public void mkdirCreateDirWithSlash() {
        String message = JShell.commandProcessor("mkdir /", session);
        assertEquals("mkdir: cannot create a directory without a name\n", message);
    }

    @Test
    /**
     * Test creating a directory with multiple slashes as the name
     */
    public void mkdirCreateDirWithMultipleSlash() {
        String message = JShell.commandProcessor("mkdir ////", session);
        assertEquals("mkdir: cannot create a directory without a name\n", message);
    }

    @Test
    /**
     * Test making directories with invalid names such as ..
     */
    public void mkdirInvalidNames() {
        String message = JShell.commandProcessor("mkdir ..", session);
        assertEquals("mkdir: cannot create directory with name '..'. It is invalid.\n", message);
        message = JShell.commandProcessor("mkdir .", session);
        assertEquals("mkdir: cannot create directory with name '.'. It is invalid.\n", message);
        message = JShell.commandProcessor("mkdir one one/..", session);
        assertEquals(session.getRootDir().getChildNames().get(0), "one");
        assertEquals("mkdir: cannot create directory with name 'one/..'. It is invalid.\n", message);
    }


    @Test
    /**
     * Test making directories given invalid paths where a new directory can not be created because its
     * parent directory doesn't exist
     */
    public void mkdirInvalidPaths() {
        //Unable to create because one doesn't exist
        String message = JShell.commandProcessor("mkdir one/two", session);
        assertEquals("mkdir: cannot create directory 'one/two': Invalid Path\n", message);
        //unable to create because two doesn't exist in the root
        message = JShell.commandProcessor("mkdir one one/two two/one", session);
        assertEquals("mkdir: cannot create directory 'two/one': Invalid Path\n", message);
        //unable to create because two doesn't exist under the root
        message = JShell.commandProcessor("mkdir one/../two/../three one/three/four\n", session);
        assertEquals(session.getRootDir().getChildNames().get(0), "one");
        assertEquals("mkdir: cannot create directory 'one/../two/../three': Invalid Path\n" +
                "mkdir: cannot create directory 'one/three/four': Invalid Path\n", message);
    }

    @Test
    /**
     * Test creating a directory when a directory or file with the same name already exists
     */
    public void mkdirDirOrFileExists() throws FileTypes.InvalidName, Directory.NameExistsException, Directory.InvalidAddition {
        //dir with same name exists shouldn't be able to create it
        String message = JShell.commandProcessor("mkdir one one", session);
        assertEquals("mkdir: cannot create directory 'one': File exists\n", message);
        //file with same name exists shouldn't be able to create it
        session.getCurrentDir().add(new File("three"));
        message = JShell.commandProcessor("mkdir three", session);
        assertEquals("mkdir: cannot create directory 'three': File exists\n", message);

    }


    @Test
    /**
     * Testing creating one directory with one layer
     */
    public void mkdirSingle() {
        JShell.commandProcessor("mkdir one", session);
        JShell.commandProcessor("mkdir /two", session);
        JShell.commandProcessor("mkdir three", session);
        ArrayList<String> expected = new ArrayList<String>();
        expected.addAll(Arrays.asList("one", "two", "three"));
        assertEquals(session.getRootDir().getChildNames(), expected);
    }

    @Test
    /**
     * Test creating multiple directories at the same time
     */
    public void mkdirMultipleLayers() {
        //create multiple directories with multiple children in the same command
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir one one/two one/two/three /three", session);
        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir three /four", session);
        //check whether or not all of the directories were created
        expected.addAll(Arrays.asList("one", "three", "four"));
        assertEquals(expected, session.getRootDir().getChildNames());
        expected.clear();
        expected.addAll(Arrays.asList("two", "three"));
        JShell.commandProcessor("cd one", session);
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd two", session);
        expected.clear();
        expected.add("three");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Tests for making directories with slashes at the end. This is a special case, it should create the directories
     */
    public void mkdirSlashAtEnd() {
        //create directories with slashes at the end single layer
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir one/ two/", session);
        expected.addAll(Arrays.asList("one", "two"));
        assertEquals(expected, session.getRootDir().getChildNames());

        //create directories with slashes at the end on layer 2
        JShell.commandProcessor("cd one/", session);
        JShell.commandProcessor("mkdir one/ two/", session);
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //create directories slash at front and end
        JShell.commandProcessor("mkdir /three/", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("three");
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test to see if mkdir can follow combination of slashes
        JShell.commandProcessor("mkdir ../one/two/../../four/", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test to see if mkdir can follow from root combo of slashes and double dots
        JShell.commandProcessor("mkdir /../one/two/../../four/././five/", session);
        JShell.commandProcessor("cd four", session);
        expected.clear();
        expected.addAll(Arrays.asList("five"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Test to check if a directory is created in the correct location after a combination
     * of double dots are used in the path
     */
    public void mkdirDoubleDots() {
        //test using double dots while on root directory
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ../one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());
        JShell.commandProcessor("cd one", session);

        //test using double dots multiple times on child directory
        JShell.commandProcessor("mkdir ../../two", session);
        expected.clear();
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.addAll(Arrays.asList("one", "two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test using double dots in an absolute path
        JShell.commandProcessor("mkdir /../../three", session);
        expected.clear();
        expected.addAll(Arrays.asList("one", "two", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test using a combination of double dots and slashes on an absolute path
        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir /../three/../two/../one/../four", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Test to check if directory is created in the correct location after a combination of single
     * dot ares used
     */
    public void mkdirSingleDots() {
        //Test using dot on relative path on root directory
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ./one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());
        JShell.commandProcessor("cd one", session);

        //Test using dot on child directory multiple times
        JShell.commandProcessor("mkdir ././two", session);
        expected.clear();
        expected.addAll(Arrays.asList("two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test using multiple dots on absolute path
        JShell.commandProcessor("cd ..", session);
        JShell.commandProcessor("mkdir /././three", session);
        expected.clear();
        expected.addAll(Arrays.asList("one", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test using combination of single dots and slashes on absolute path
        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir /./three/././four", session);
        JShell.commandProcessor("cd ../three", session);
        expected.clear();
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    /**
     * Test to see if directory is created in correct location after a combination of slashes
     * and double and single dots are used in the path
     */
    public void mkdirSingleDoubleDots() {
        //use a combination of double and single dots on relative path
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ./.././../../one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());

        //test double dots and multiple directory layers from root node
        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir ../one/two", session);
        expected.clear();
        expected.addAll(Arrays.asList("two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test double dots and multipe directory layers from child node
        JShell.commandProcessor("mkdir ../one/three", session);
        expected.clear();
        expected.addAll(Arrays.asList("two", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        //test combo of double and single dots on relative path
        JShell.commandProcessor("mkdir ../one/./../one/../one/four", session);
        expected.clear();
        expected.addAll(Arrays.asList("two", "three", "four"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

}