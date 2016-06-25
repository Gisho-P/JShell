package driver;

import java.util.List;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class DirStack {

	static List<String> directories = new ArrayList<String>();
	
	public static List<Object> popd() {
		String newDir = "";
		boolean success = false;
		try {
			newDir = directories.remove(directories.size()-1);
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
	
	public static void pushd(String currPath) {
		directories.add(currPath);
	}

}
