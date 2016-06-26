package test;

import static org.junit.Assert.*;

import org.junit.Test;

import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.MissingNameException;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes;
import driver.JShell;
import driver.MySession;

public class GeneralShellTest {

	@Test
	public void changeDirectory() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();
		
		System.out.println(JShell.commandProcessor("mkdir usr", session));
		System.out.println(JShell.commandProcessor("mkdir /hello", session));
		System.out.println(JShell.commandProcessor("ls", session));
		System.out.println(JShell.commandProcessor("cd usr/", session));
		System.out.println(JShell.commandProcessor("pwd", session));
		System.out.println(JShell.commandProcessor("mkdir john", session));
		System.out.println(JShell.commandProcessor("mkdir /test", session));
		System.out.println(JShell.commandProcessor("ls", session));
		System.out.println(JShell.commandProcessor("cd /", session));
		System.out.println(JShell.commandProcessor("ls", session));
		System.out.println(JShell.commandProcessor("mkdir hello/hi", session));
		System.out.println(JShell.commandProcessor("cd .", session));
		System.out.println(JShell.commandProcessor("ls", session));
		System.out.println(session.getCurrentDir().getChildren().toString());
		JShell.commandProcessor("cd hello", session);
		System.out.println(JShell.commandProcessor("ls", session));
		
		//System.out.println(session.getCurrentDir().getChild("hi"));
		System.out.println(session.getCurrentDir().getChildren().toString());
	}

	@Test
	public void mkdirMult() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();
		JShell.commandProcessor("mkdir usr usr/hello /usr/hello/bye", session);

		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd usr", session);
		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd hello", session);
		System.out.println(session.getCurrentDir().getChildNames());
	}

	@Test
	public void cdParent() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();

		JShell.commandProcessor("mkdir usr usr/hello /usr/hello/bye", session);
		JShell.commandProcessor("cd ..", session);//Should remain at the root node in this case
		System.out.println(session.getCurrentDir().getChildNames());
	}

	@Test
	public void cdParentTwo() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();

		JShell.commandProcessor("mkdir usr usr/hello /usr/hello/bye", session);
		JShell.commandProcessor("cd usr/..", session);
		System.out.println(session.getCurrentDir().getChildNames());

	}

	@Test
	public void cdCurrent() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();

		JShell.commandProcessor("mkdir usr usr/hello /usr/hello/bye", session);
		JShell.commandProcessor("cd usr/.", session);
		System.out.println(session.getCurrentDir().getChildNames());

	}

	@Test
	public void mkDirDots() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();

		JShell.commandProcessor("mkdir usr usr/hello /usr/hello/bye", session);

		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd usr", session);
		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd hello", session);
		System.out.println(session.getCurrentDir().getChildNames());

	}

	@Test
	public void mkDirFromChild() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession session = new MySession();
		
		JShell.commandProcessor("mkdir usr", session);

		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd usr", session);
		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("mkdir /hello", session);
		System.out.println(session.getCurrentDir().getChildNames());

		JShell.commandProcessor("cd ..", session);
		System.out.println(session.getCurrentDir().getChildNames());
		System.out.println("ls");

	}
}
