package driver;

import java.util.List;

public interface Command {
  public String man();

  public String interpret(List<String> args);

  public String exec(List<String> args);
}
