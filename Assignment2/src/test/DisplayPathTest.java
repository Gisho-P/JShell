package test;

import driver.JShell;
import driver.MySession;
import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dhrumil on 27/06/16.
 */
public class DisplayPathTest {
    MySession session;

    @Before
    /**
     * Create a new instance of MySession before every test
     */
    public void setUp() {
        session = new MySession();
    }

    @Test
    /**
     * Test calling pwd on empty directory
     */
    public void testDisplayPathEmpty() {
        String path = JShell.commandProcessor("pwd", session);
        assertEquals("/", path);
    }

    @Test
    /**
     * Test calling pwd on a single directory
     */
    public void testDisplayPathSingleDir() {
        JShell.commandProcessor("mkdir one", session);
        JShell.commandProcessor("cd one", session);
        String path = JShell.commandProcessor("pwd", session);
        assertEquals("/one", path);
    }

    @Test
    /**
     * Test calling pwd on directories with multiple layers
     */
    public void testDisplayPathMultipleDir() {
        JShell.commandProcessor("mkdir one one/two one/two/three three four", session);
        JShell.commandProcessor("cd one/two/three", session);
        String path = JShell.commandProcessor("pwd", session);
        assertEquals("/one/two/three", path);
    }

    @Test
    /**
     * Test calling pwd on directories where the parent directoris all have the same name
     */
    public void testDisplayPathMultipleDirSameName() {
        JShell.commandProcessor("mkdir one one/one one/one/one one/one/one/one", session);
        JShell.commandProcessor("cd one/one/one/one", session);
        String path = JShell.commandProcessor("pwd", session);
        assertEquals("/one/one/one/one", path);
    }



}