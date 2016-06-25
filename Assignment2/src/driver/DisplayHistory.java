package driver;

import java.util.ArrayList;
import java.util.List;

public class DisplayHistory implements Command {

	@Override
	public String man() {
		return "HISTORY(1)\t\t\t\tUser Commands\t\t\t\t"
        	   + "HISTORY(1)\n\nNAME\n\t\thistory - prints out the recent " +
               "commands entered in the shell\n\nSYNOPSIS\n\t\t" +
               "history [NUMBER]\n\nDESCRIPTION\n\t\tPrints out " +
               "the commands entered in the shell from the first " +
               "one entered\n\t\tto the last.\n\n\t\tIf NUMBER is" +
               " specified, will print only the last NUMBER " +
               "amount of\n\t\tcommands that were entered.";
	}
	
	@Override
	public Object format(List<String> args) {
		List<Object> ret = new ArrayList<Object>();
		if (args.size() > 2) {
			ret.add(false);
			ret.add("history usage: history [number]");
		} else {
			if (args.size() == 2) {
				try {
		            int arg = Integer.parseInt(args.get(1));
		            ret.add(true);
		            ret.add(arg);
		        } catch (NumberFormatException n) {
		        	ret.add(false);
		            ret.add("history usage: history [number (INTEGER >= 0)]");
		        }
			} else {
				ret.add(true);
				ret.add(-1);
			}
		}
		return ret;
	}

	@Override
	public String exec(List<String> args) {
		List<Object> o = (List<Object>) format(args);
		
		if ((boolean) o.get(0) == false) {
			return (String) o.get(1);
		} else {
			if ((Integer) o.get(1) == -1) {
				return MySession.printCommandHistory(); // no param
			} else {
				return MySession.printCommandHistory((Integer) o.get(1)); // param
			}
		}
	}

}
