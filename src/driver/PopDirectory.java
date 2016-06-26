package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PopDirectory implements Command {

  public PopDirectory(MySession session) {}

  @Override
  public String man() {
    return "POPD(1)\t\t\t\tUser Commands\t\t\t\tPOPD(1)\n\n"
        + "NAME\n\t\tpopd - removes the last directory pushed,"
        + " and changes the current\n\t\tdirectory to that one"
        + "\n\nSYNOPSIS\n\t\tpopd\n\nDESCRIPTION\n\t\tRemoves "
        + "the last directory that was pushed to the directory "
        + "stack,\n\t\tand changes the current working "
        + "directory to this directory.";
  }

  @Override
  public String interpret(List<String> args) {
    if (args.size() != 1) {
      return "popd usage: popd"; // error, print usage
    } else {
      return exec(args);
    }
  }


  @Override
  public String exec(List<String> args) {
    List<Object> res = DirStack.popd();

    if ((boolean) res.get(1) == false) {
      return (String) res.get(0);
    } else {
      try {
        Class<?> c = Class.forName("driver.ChangeDirectory");
        Object t = c.newInstance();
        Method m = c.getMethod("interpret", List.class);
        args.add((String) res.get(0));
        return (String) m.invoke(t, args);
      } catch (ClassNotFoundException | InstantiationException
          | IllegalAccessException | NoSuchMethodException | SecurityException
          | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

}
