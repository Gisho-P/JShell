package test;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidNameException;
import structures.Output;
import static org.junit.Assert.assertEquals;

/**
 * Tests that verify the functionality of the echo command in JShell which can
 * display strings in the shell or store them in files.
 */
public class DisplayStringTest {

	MySession session;

	@Before
	/**
	 * Current session's attributes
	 */
	public void setUp() {
		session = new MySession(new Output());
	}

	@After
	/**
	 * The filesystem uses singleton design for the root directory. For testing
	 * purposes, the root needs to be set to null everytime.
	 */
	public void tearDown() throws InvalidNameException, NoSuchFieldException,
			IllegalAccessException {
		Field field = session.getRootDir().getClass().getDeclaredField("root");
		field.setAccessible(true);
		field.set(null, null); // setting the ref parameter to null
		session.clearBuffer();
	}

	/**
	 * Test for echo when a string with no quotes is entered
	 */
	@Test
	public void testNoQuotes() {
		JShell.commandProcessor("echo hi", session);
		String noQuotesMsg = "ERROR: STRING must be surrounded by double quotations";
		assertEquals(noQuotesMsg, session.returnBuffer());
	}

	/**
	 * Test for echo when no string is given
	 */
	@Test
	public void testNoString() {
		JShell.commandProcessor("echo", session);
		String usageMsg = "echo usage: echo STRING (In quotes)";
		assertEquals(usageMsg, session.returnBuffer());
	}

	/**
	 * Test for echo when an extra quotation mark has been entered
	 */
	@Test
	public void testExtraArgument() {
		JShell.commandProcessor("echo \"one\" two", session);
		String usageMsg = "echo usage: echo STRING (In quotes)";
		assertEquals(usageMsg, session.returnBuffer());
	}

	/**
	 * Test for echo where only one quote has been entered.
	 */
	@Test
	public void testOneQuote() {
		JShell.commandProcessor("echo \"", session);
		String noQuotesMsg = "ERROR: STRING must be surrounded by double quotations";
		assertEquals(noQuotesMsg, session.returnBuffer());
	}

	/**
	 * Using echo to output an empty string to JShell.
	 */
	@Test
	public void testDisplayEmptyString() {
		JShell.commandProcessor("echo \"\"", session);
		assertEquals("", session.returnBuffer());
	}

	/**
	 * Displaying a one word string to JShell.
	 */
	@Test
	public void testDisplayOneWord() {
		JShell.commandProcessor("echo \"test\"", session);
		assertEquals("test", session.returnBuffer());
	}

	/**
	 * Displaying a multiple word string to JShell.
	 */
	@Test
	public void testDisplayMultipleWords() {
		JShell.commandProcessor("echo \"1 2 3 4 ss\"", session);
		assertEquals("1 2 3 4 ss", session.returnBuffer());
	}

	/**
	 * Displaying a string with quotations to JShell.
	 */
	@Test
	public void testStringWithQuote() {
		JShell.commandProcessor("echo \"\"\"", session);
		assertEquals("\"", session.returnBuffer());
	}
}
