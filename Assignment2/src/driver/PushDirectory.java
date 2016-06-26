package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PushDirectory implements Command {

  private MySession s;

  public PushDirectory(MySession session) {
    s = session;
  }

  @Override
  public String man() {
    return "PUSHD(1)\t\t\t\tUser Commands\t\t\t\tPUSHD(1)\n\n"
        + "NAME\n\t\tpushd - saves the current working "
        + "directory to the stack and changes\n\t\tnew working directory"
        + " to DIR\n\nSYNOPSIS\n\t\tpushd DIR\n\n"
        + "DESCRIPTION\n\t\tSaves the current working "
        + "directory to the top of the directory\n\t\tstack. Changes"
        + " working directory to path specified in DIR.\n\t\t"
        + "Directory stack follows stack behaviour (LIFO).";
  }

  @Override
  public String interpret(List<String> args) {
    if (args.size() != 2) {
      return "pushd usage: pushd DIR";
    } else {
      return exec(args); // args.get(1);
    }
  }

  @Override
  public String exec(List<String> args) {
    DirStack.pushd(s.getCurrentDir().getEntirePath());
    try {
      Class<?> c = Class.forName("driver.ChangeDirectory");
      Object t = c.newInstance();
      Method m = c.getMethod("interpret", List.class);
      return (String) m.invoke(t, args);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | NoSuchMethodException | SecurityException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

}
