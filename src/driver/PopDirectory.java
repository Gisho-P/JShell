package driver;

import java.util.List;

public class PopDirectory implements Command {

	@Override
	public String man() {
		return "POPD(1)\t\t\t\tUser Commands\t\t\t\tPOPD(1)\n\n" +
               "NAME\n\t\tpopd - removes the last directory pushed," +
               " and changes the current\n\t\tdirectory to that one" +
               "\n\nSYNOPSIS\n\t\tpopd\n\nDESCRIPTION\n\t\tRemoves " +
               "the last directory that was pushed to the directory " +
               "stack,\n\t\tand changes the current working " +
               "directory to this directory.";
	}

	@Override
	public Object format(List<String> args) {
		if (args.size() != 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String exec(List<String> args) {
		boolean o = (boolean) format(args);
		
		if (o) {
			List<Object> res = DirStack.popd();
			if ((boolean) res.get(1) == false) {
	            return (String) res.get(0);
	        } else {
	            // call FilePathInterpreter or w/e with res.get(0)
	        	return cd((String) res.get(0));
	        }
		} else {
			return "popd usage: popd"; // error, print usage
		}
	}

}
