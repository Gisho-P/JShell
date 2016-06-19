package driver;

import java.util.List;

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
	
	public String cd (String directory) {
		return null; // file path inteerpreter
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
	
	public String cat (String[] files) {
		String retVal = "";
		for (String i: files) {
			//retVal += ;
			// some how get file and display contents
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