package commands;

import java.util.*;

import driver.*;

public class ExecuteFromHistory implements Command {

  private MySession s;

  public ExecuteFromHistory(MySession session) {
    s = session;
  }

  // TODO: DO THE DAMN BUILD XML FILE GOD
  @Override
  public void man() {
    s.setOutput("!(1)\t\t\t\tUser Commands\t\t\t\t!(1)"
        + "\n\nNAME\n\t\t! - allows user to execute command "
        + "from history, based\n\t\ton user specified number"
        + "\n\nSYNOPSIS\n\t\t!NUMBER > 0 && <= # of commands run"
        + "\n\nDESCRIPTION\n\t\tThis command allows a user to execute"
        + "a command that they\n\t\thad once run.\n\t\tIn order to do "
        + "so, the user needs to specify which\n\t\tcommand from "
        + "history they\n\t\twant to with a number.\n\t\tE.G. !3 - "
        + "executes the third command typed by a user.");
  }

  @Override
  public void interpret(List<String> args) {
    if (args.size() != 2) {
      s.addError("! usage: !NUMBER > 0");
    } else {
      exec(args);
    }
  }

  @Override
  public void exec(List<String> args) {
    List<Object> set = new ArrayList<Object>();
    try {
      int i = Integer.parseInt(args.get(1));
      set = s.getHistoricalCommand(i);
    } catch (NumberFormatException e) {
      s.addError("! ERROR: Enter a number");
    }

    if (set.size() != 0) {
      if ((boolean) set.get(1)) {
        JShell.commandProcessor((String) set.get(0), s);
      } else {
        s.addError((String) set.get(0));
      }
    }
  }

}
