package driver;

import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

public class ShellFunctions {

	public ShellFunctions() {
		// TODO Auto-generated constructor stub
	}
	
	// COULD MAKE EACH METHOD PROTECTED,
	// CALL COMMANDS TO DIFFERENT CLASSES HERE
	
	public String mkdir (String[] directory) {
		String retVal = "";
		for (String i: directory) {
			if (i != "." && i != "..") {
				// create dir
			}
		}
		return retVal;
	}
	
	public String cd (String path, MySession session) {
		try {
			Directory dest = (Directory) FilePathInterpreter.interpretPath(session.getCurrentDir(), path);
			session.setCurrentDir(dest);
		} catch (InvalidDirectoryPathException e) {
			// TODO Auto-generated catch block
			return "No such dir as" + path;
		}
		return "";
	}
	
	public String ls (String path) {
		return null; // call directory function on current dir
	}
	
	public String pwd () {
		return null; // call directory function on current dir
	}
	
	public String pushd (String directory) {
		// should do some type of check for valid path entering
		DirStack.pushd(directory);
		return "\n";
	}
	
	public String popd () {
		List <Object> res = DirStack.popd();
		if ((boolean) res.get(1) == false) {
			return (String) res.get(0);
		} else {
			// call FilePathInterpreter or w/e with res.get(0)
			return "";
		}
	}
	
	public String history (String cmdArgs) {
		String retVal = "";
		try {
			int arg = Integer.parseInt(cmdArgs);
			retVal = MySession.printCommandHistory(arg);
			
		} catch (NumberFormatException n) {
			retVal = "history usage: history [number (INTEGER > 0)]";
		}
		return retVal;
	}
	
	public String history() {
		return MySession.printCommandHistory();
	}
	
	public String cat (String[] filePaths, MySession session) {
		String retVal = "";
		for (String i: filePaths) {
			try {
				retVal += (File) FilePathInterpreter.interpretPath(session.getCurrentDir(), i);
			} catch (InvalidDirectoryPathException e) {
				// TODO Auto-generated catch block
				System.out.println("No such dir as" + i);
			} catch(ClassCastException e){
				System.out.println("Unable to cat dir" + i);
			}
		}
		return retVal;
	}

	public String echo (String outfile, boolean overwrite) {
		return null; // mad processing to do
	}
	
	public String man (String command) {
		return MySession.manPages(command);
	}
}