package driver;

import java.util.List;

public interface Command {
	public String man();
	//public Object format(List<String> args);
	public String exec(List<String> args, MySession session);
}
