package test;

import static org.junit.Assert.*;

import org.junit.Test;

import driver.CommandInterpreter;
import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.MissingNameException;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes;
import driver.MySession;

public class GeneralShellTest {

	@Test
	public void changeDirectory() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException, MissingNameException{
		MySession sess = new MySession();
		CommandInterpreter cmd = new CommandInterpreter(sess);
		
		cmd.interpretCmd("mkdir usr");
		cmd.interpretCmd("mkdir /hello");
		
		System.out.println(sess.getCurrentDir().getChild("usr"));
		System.out.println(sess.getCurrentDir().getChildren().toString());
	}
}
