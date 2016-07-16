package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.InvalidNameException;
import exceptions.NameExistsException;
import structures.Directory;
import structures.File;
import structures.Output;
import static org.junit.Assert.assertEquals;

public class DisplayWithSubstringTest {

	MySession session;

	@Before
	public void tearDown() {
		session = new MySession(new Output());
		session.clearFileSystem();
		session.clearBuffer();
	}

	/**
	 * Testing for a standard case of grep.
	 */
	@Test
	public void FilePathTest() {
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
	 * Testing for going through a dir.
	 */
	@Test
	public void RecursivePathTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc.* .", session);
		assertEquals(
				"/fileone:abcdefg\n" + "/fileone:abcde\n" + "/fileone:abc",
				session.returnBuffer());
	}

	/**
	 * Testing for a regex that matches everything
	 */
	@Test
	public void AnySubstringTest() {
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

	/**
	 * Testing for a exact substring given.
	 */
	@Test
	public void exactSubstringTest() {
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

	/**
	 * Testing for a dir structure with more than 1 height.
	 */
	@Test
	public void multiDirTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);

			Directory subdir = new Directory("subdir");
			File temp2 = new File("filetwo");
			temp2.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			subdir.add(temp2);
			session.getCurrentDir().add(subdir);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc .", session);
		assertEquals("/fileone:abc\n" + "/subdir/filetwo:abc",
				session.returnBuffer());
	}

	/**
	 * Testing for a dir structure with more than 1 height.
	 */
	@Test
	public void multipleSubDir() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);

			Directory subdir = new Directory("subdir");
			File temp2 = new File("filetwo");
			temp2.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			subdir.add(temp2);
			session.getCurrentDir().add(subdir);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R abc .", session);
		assertEquals("/fileone:abc\n" + "/subdir/filetwo:abc",
				session.returnBuffer());
	}

	/**
	 * Testing the range of numbers in the regex.
	 */
	@Test
	public void numberRangeTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("12348683\n" + "37126312\n" + "12312313213\n"
					+ "1\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R [1-4]* .", session);
		assertEquals("/fileone:12312313213\n" + "/fileone:1",
				session.returnBuffer());
	}

	/**
	 * Testing the range of letters in the regex.
	 */
	@Test
	public void letterCaseTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "ABcDeFG\n" + "ABC\n" + "Abcdefged\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R [a-q]* .", session);
		assertEquals("/fileone:abcdefg", session.returnBuffer());
	}

	@Test
	public void numberLetterMixTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "ABcDeFG\n" + "ABC\n" + "Abcdefged\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -R [a-q]* .", session);
		assertEquals("/fileone:abcdefg", session.returnBuffer());
	}

	/**
	 * Testing for a empty dir.
	 */
	@Test
	public void emptyDirTest() {
		JShell.commandProcessor("grep -R abc .", session);
		assertEquals("", session.returnBuffer());
	}

	/**
	 * Testing for a lower case -r flag.
	 */
	@Test
	public void lowerCaseFlagTest() {
		try {
			File temp = new File("fileone");
			temp.setContent("abcdefg\n" + "abcde\n" + "abc\n");
			session.getCurrentDir().add(temp);
		} catch (NameExistsException | InvalidAdditionException
				| InvalidNameException e) {
		}
		JShell.commandProcessor("grep -r .* .", session);
		assertEquals(
				"/fileone:abcdefg\n" + "/fileone:abcde\n" + "/fileone:abc",
				session.returnBuffer());
	}

	/**
	 * Testing for too little params given.
	 */
	@Test
	public void tooLittleParamsTest() {
		JShell.commandProcessor("grep -R ", session);
		assertEquals("grep usage: grep [options] REGEX PATH ...",
				session.returnBuffer());
	}

	/**
	 * Testing for using a wrong flag.
	 */
	@Test
	public void wrongFlagTest() {
		JShell.commandProcessor("grep -G ", session);
		assertEquals("grep usage: grep [options] REGEX PATH ...",
				session.returnBuffer());
	}

	/*
	 * Testing for too many params.
	 */
	@Test
	public void tooManyParamsTest() {
		JShell.commandProcessor("grep -G a b c d e f", session);
		assertEquals("grep usage: grep [options] REGEX PATH ...",
				session.returnBuffer());
	}
}
