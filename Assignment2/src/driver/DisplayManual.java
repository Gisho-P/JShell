package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class DisplayManual implements Command {

	@Override
	public String man() {
		return "MAN(1)\t\t\t\tUser Commands\t\t\t\tMAN(1)\n\nNAME"
        	   + "\n\t\tman - prints the documentation for a specified" +
               " command\n\nSYNOPSIS\n\t\tman CMD\n\nDESCRIPTION\n\t\t" +
               "Prints out the documentation for the CMD which " +
               "contains information\n\t\ton how to use the command";
	}

	@Override
	public Object format(List<String> args) {
		List<Object> f = new ArrayList<Object>();
		
		if (args.size() != 2) {
			f.add("man usage: man CMD");
			f.add(false);
		} else {
			f.add(args.get(1));
			f.add(true);
		}
		return f;
	}

	@Override
	public String exec(List<String> args) {
		List<Object> output = (List<Object>) format(args);
		String retVal = "";
		
		if ((boolean) output.get(1) == false) {
			retVal = (String) output.get(0);
		} else {
			try {
				Class<?> c = Class.forName(
						"driver." +
						MySession.commandToClass.get((String) output.get(0)));
				Object t = c.newInstance();
				Method m = c.getMethod("man");
				retVal = (String) m.invoke(t, (Object[]) null);
			} catch (ClassNotFoundException | InstantiationException | 
					IllegalAccessException | NoSuchMethodException | 
					SecurityException | IllegalArgumentException |
					InvocationTargetException e) {
				retVal = "ERROR: Command does not exist.";
			}
		}
		return retVal;
	}

}
