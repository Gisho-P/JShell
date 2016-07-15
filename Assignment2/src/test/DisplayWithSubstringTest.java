package test;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import structures.*;
import driver.*;
import exceptions.*;

public class DisplayWithSubstringTest {

	public static MySession session = new MySession(new Output());

	@After
	public void tearDown() {
		session.clearFileSystem();
		session.clearBuffer();
		System.out.println(session.getCurrentDir().size());
	}

	/**
	 * List contents on a file which should return the file name
	 */
	@Test
	public void testFilePathTest() {
		try {
			File temp = new File("file1");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep abc.* file1", session);
		assertEquals("abcdefg\n" + "abcde\n" + "abc", session.returnBuffer());
	}

	/**
	 * List contents on a file which should return the file name
	 */
	@Test
	public void testRecursivePathTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			System.out.println(session.getRootDir().size());
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc.* .", session);
		assertEquals(
				"/fileone:abcdefg\n" + "/fileone:abcde\n" + "/fileone:abc",
				session.returnBuffer());
	}

	@Test
	public void testAnySubstringTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R .* .", session);
		assertEquals(
				"/fileone:abcdefg\n" + "/fileone:abcde\n" + "/fileone:abc",
				session.returnBuffer());

	}

	@Test
	public void testSingleSubstringTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc .", session);
		assertEquals("/fileone:abc", session.returnBuffer());
	}

	@Test
	public void with() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc .", session);
		assertEquals("/fileone:abc", session.returnBuffer());
	}
}
