package driver;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class DirStack {

	private static Stack<String> directories;
	
	public DirStack() {
		directories = new Stack<String>();
	}
	
	public static List<Object> popd() {
		String newDir = "";
		boolean success = false;
		try {
			newDir = directories.pop();
			success = true;
		}
		catch (EmptyStackException e) {
			newDir = "ERROR: Empty stack, nothing to pop";
		}
		
		List<Object> popCheck = new ArrayList<Object>();
		popCheck.add(newDir);
		popCheck.add(success);
		
		return popCheck;
	}
	
	public static String pushd(String directory) { // error checking for invalid path
		directories.push(directory);
		return "\n";
	}

}
