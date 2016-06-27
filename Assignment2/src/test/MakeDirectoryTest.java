package test;

import driver.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by dhrumil on 26/06/16.
 */
public class MakeDirectoryTest {
    MySession session;
    @Before
    public void setUp() {
        session = new MySession();
    }

    @Test
    public void mkdirCreateDirWithSlash() {
        String message = JShell.commandProcessor("mkdir /", session);
        assertEquals("mkdir: cannot create a directory without a name\n", message);
    }

    @Test
    public void mkdirCreateDirWithMultipleSlash() {
        String message = JShell.commandProcessor("mkdir ////", session);
        assertEquals("mkdir: cannot create a directory without a name\n", message);
    }

    @Test
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
    public void mkdirInvalidPaths() {
        String message = JShell.commandProcessor("mkdir one/two", session);
        assertEquals("mkdir: cannot create directory 'one/two': Invalid Path\n", message);
        message = JShell.commandProcessor("mkdir one one/two two/one", session);
        assertEquals("mkdir: cannot create directory 'two/one': Invalid Path\n", message);
        message = JShell.commandProcessor("mkdir one/../two/../three one/three/four\n", session);
        assertEquals(session.getRootDir().getChildNames().get(0), "one");
        assertEquals("mkdir: cannot create directory 'one/../two/../three': Invalid Path\n" +
                "mkdir: cannot create directory 'one/three/four': Invalid Path\n", message);
    }

    @Test
    public void mkdirDirOrFileExists() throws FileTypes.InvalidName, Directory.NameExistsException, Directory.InvalidAddition {
        String message = JShell.commandProcessor("mkdir one one", session);
        assertEquals("mkdir: cannot create directory 'one': File exists\n", message);
        session.getCurrentDir().add(new File("three"));
        message = JShell.commandProcessor("mkdir three", session);
        assertEquals("mkdir: cannot create directory 'three': File exists\n", message);

    }


    @Test
    public void mkdirSingle() {
        JShell.commandProcessor("mkdir one", session);
        JShell.commandProcessor("mkdir /two", session);
        JShell.commandProcessor("mkdir three", session);
        ArrayList<String> expected = new ArrayList<String>();
        expected.addAll(Arrays.asList("one", "two", "three"));
        assertEquals(session.getRootDir().getChildNames(), expected);
    }

    @Test
    public void mkdirMultipleLayers() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir one one/two one/two/three /three", session);
        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir three /four", session);
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
    public void mkdirSlashAtEnd() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir one/ two/", session);
        expected.addAll(Arrays.asList("one", "two"));
        assertEquals(expected, session.getRootDir().getChildNames());

        JShell.commandProcessor("cd one/", session);
        JShell.commandProcessor("mkdir one/ two/", session);
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("mkdir /three/", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("three");
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("mkdir ../one/two/../../four/", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    public void mkdirDoubleDots() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ../one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());
        JShell.commandProcessor("cd one", session);

        JShell.commandProcessor("mkdir ../../two", session);
        expected.clear();
        assertEquals(expected, session.getCurrentDir().getChildNames());
        JShell.commandProcessor("cd ..", session);
        expected.addAll(Arrays.asList("one", "two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("mkdir /../../three", session);
        expected.clear();
        expected.addAll(Arrays.asList("one", "two", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir /../three/../two/../one/../four", session);
        JShell.commandProcessor("cd ..", session);
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    public void mkdirSingleDots() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ./one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());
        JShell.commandProcessor("cd one", session);

        JShell.commandProcessor("mkdir ././two", session);
        expected.clear();
        expected.addAll(Arrays.asList("two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("cd ..", session);
        JShell.commandProcessor("mkdir /././three", session);
        expected.clear();
        expected.addAll(Arrays.asList("one", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir /./three/././four", session);
        JShell.commandProcessor("cd ../three", session);
        expected.clear();
        expected.add("four");
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

    @Test
    public void mkdirSingleDoubleDots() {
        ArrayList<String> expected = new ArrayList<String>();
        JShell.commandProcessor("mkdir ./.././../../one", session);
        expected.addAll(Arrays.asList("one"));
        assertEquals(expected, session.getRootDir().getChildNames());

        JShell.commandProcessor("cd one", session);
        JShell.commandProcessor("mkdir ../one/two", session);
        expected.clear();
        expected.addAll(Arrays.asList("two"));
        assertEquals(expected, session.getCurrentDir().getChildNames());

        JShell.commandProcessor("mkdir ../one/three", session);
        expected.clear();
        expected.addAll(Arrays.asList("two", "three"));
        assertEquals(expected, session.getCurrentDir().getChildNames());


        JShell.commandProcessor("mkdir ../one/./../one/../one/four", session);
        expected.clear();
        expected.addAll(Arrays.asList("two", "three", "four"));
        assertEquals(expected, session.getCurrentDir().getChildNames());
    }

}