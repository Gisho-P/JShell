package test;

import static org.junit.Assert.*;

import org.junit.Test;

import driver.Directory;
import driver.Directory.InvalidAddition;
import driver.Directory.NameExistsException;
import driver.File;
import driver.FilePathInterpreter;
import driver.FilePathInterpreter.InvalidDirectoryPathException;
import driver.FileTypes;

public class FileTraversalTest {

	@Test
	public void getLevelOneDirectoryTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException{
		Directory expected = new Directory("dir1");
		Directory test = new Directory("root");
		test.add(expected);
		test.add(new Directory("dir2"));
		test.add(new Directory("dir3"));
		test.add(new File("file1"));
		test.add(new File("file2", "c2"));
		
		Directory result = (Directory)FilePathInterpreter.interpretPath(test, "dir1");
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void getLevelOneFileTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException {
		Directory test = new Directory("root");
		test.add(new Directory("test1"));
		test.add(new Directory("test2"));
		test.add(new Directory("test3"));
		File expected = new File("file1", "c1");
		test.add(expected);
		test.add(new File("file2", "c2"));
		
		File result = (File)FilePathInterpreter.interpretPath(test, "file1");
		System.out.println(result.toString());
		assertTrue(result.equals(expected));
	}

	@Test
	public void getLevelTwoDirectoryTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException {
		Directory sub = new Directory("dir1");
		Directory test = new Directory("root");
		test.add(sub);
		Directory expected = new Directory("dir4");
		sub.add(expected);
		test.add(new Directory("dir2"));
		test.add(new Directory("dir3"));
		test.add(new File("file1"));
		test.add(new File("file2", "c2"));
		
		Directory result = (Directory)FilePathInterpreter.interpretPath(test, "dir1/dir4");
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void getLevelTwoFileTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException {
		Directory sub = new Directory("dir1");
		Directory test = new Directory("root");
		test.add(sub);
		sub.add(new Directory("dir4"));
		File expected = new File("file3", "c3");
		sub.add(expected);
		test.add(new Directory("dir2"));
		test.add(new Directory("dir3"));
		test.add(new File("file1"));
		test.add(new File("file2", "c2"));
		
		File result = (File)FilePathInterpreter.interpretPath(test, "dir1/file3");
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void fromRootTest() throws NameExistsException, InvalidAddition, InvalidDirectoryPathException {
		Directory sub = new Directory("dir1");
		Directory test = new Directory("root");
		test.add(sub);
		Directory start = new Directory("dir4");
		sub.add(start);
		File expected = new File("file3", "c3");
		start.add(expected);
		test.add(new Directory("dir2"));
		test.add(new Directory("dir3"));
		test.add(new File("file1"));
		test.add(new File("file2", "c2"));
		
		File result = (File)FilePathInterpreter.interpretPath(start, "/dir1/dir4/file3");
		assertTrue(result.equals(expected));
	}
}
