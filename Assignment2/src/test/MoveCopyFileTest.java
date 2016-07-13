package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import structures.*;
import driver.JShell;
import driver.MySession;

import static org.junit.Assert.assertEquals;

/**
 * Tests for ChangeDirectory class
 */
public class MoveCopyFileTest {
	public MySession session;

	@Before
	/**
	 * Create new instance of MySession before every test
	 */
	public void setUp() {
		session = new MySession(new Output());
	}
	
	@After
	public void tearDown() {
		session.clearBuffer();
	}

	/**
	 * Test adding a directory to a directory
	 */
	@Test
	public void testCdDoubleDots() {
		// Change to parent directory
		JShell.commandProcessor("mv test1 test2", session);
		assertEquals(session.getCurrentDir(), session.getRootDir());
	}
}
