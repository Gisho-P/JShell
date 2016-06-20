package driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import driver.FilePathInterpreter.InvalidDirectoryPathException;

public class ShellFunctions {
    private MySession session;
    public ShellFunctions(MySession session) {
        this.session = session;
    }

    // COULD MAKE EACH METHOD PROTECTED,
    // CALL COMMANDS TO DIFFERENT CLASSES HERE

    public String mkdir(String[] directory) {
        String message = "";
        FileTypes parentDir;
        int slashIndex;
        for (String i : directory) {
            try {
                if ((slashIndex = i.indexOf("/")) == -1) {
                    session.getCurrentDir().add(new Directory(i));
                } else if (slashIndex == 0) {
                    parentDir = FilePathInterpreter.interpretMakePath(session.getRootDir(), i);
                    ((Directory) parentDir).add(new Directory(i));
                } else {
                    parentDir = FilePathInterpreter.interpretMakePath(session.getCurrentDir(), i);
                    ((Directory) parentDir).add(new Directory(i));
                }
            } catch (Directory.NameExistsException e) {
                message += "mkdir: cannot create directory '" + i + "': File exists\n";
            } catch (Directory.InvalidAddition invalidAddition) {
                invalidAddition.printStackTrace();
            } catch (InvalidDirectoryPathException e) {
                message += "mkdir: cannot create directory ‘" + i + "’: No such file or directory\n";
            }
        }
        return message;
    }

    public String cd(String path) {
        try {
            Directory dest = (Directory) FilePathInterpreter.interpretPath(session.getCurrentDir(), path);
            session.setCurrentDir(dest);
        } catch (InvalidDirectoryPathException e) {
            // TODO Auto-generated catch block
            return "No such dir as" + path;
        }
        return "";
    }

    public String ls(String[] paths) {
      String retVal = "";
      for (String i : paths) {
          try {
            ArrayList<String> childNames = ((Directory) FilePathInterpreter.interpretPath(session.getCurrentDir(), i)).getChildNames();
            Collections.sort(childNames, String.CASE_INSENSITIVE_ORDER);
            for (String childName: childNames){
              retVal += childName + " ";
            }
          } catch (InvalidDirectoryPathException e) {
              // TODO Auto-generated catch block
              System.out.println("No such file as" + i);
          } catch (ClassCastException e) {
            try {
              retVal += (File) FilePathInterpreter.interpretPath(session.getCurrentDir(), i);
            } catch (InvalidDirectoryPathException e1) {
              // TODO Auto-generated catch block
              System.out.println("No such directory or file as" + i);
            };
          }
      }
      return retVal;
    }
    
    public String ls() {
      return ls( new String[] {session.getCurrentDir().getEntirePath()});
    }

    public String pwd() {
        return session.getCurrentDir().getEntirePath();
    }

    public String pushd(String directory) {
        // should do some type of check for valid path entering
        DirStack.pushd(directory);
        return "\n";
    }

    public String popd() {
        List<Object> res = DirStack.popd();
        if ((boolean) res.get(1) == false) {
            return (String) res.get(0);
        } else {
            // call FilePathInterpreter or w/e with res.get(0)
            return "";
        }
    }

    public String history(String cmdArgs) {
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

    public String cat(String[] filePaths) {
        String retVal = "";
        for (String i : filePaths) {
            try {
                retVal += (File) FilePathInterpreter.interpretPath(session.getCurrentDir(), i);
            } catch (InvalidDirectoryPathException e) {
                // TODO Auto-generated catch block
                System.out.println("No such dir as" + i);
            } catch (ClassCastException e) {
                System.out.println("Unable to cat dir" + i);
            }
        }
        return retVal;
    }

    public String echo(String outfile, boolean overwrite) {
      String retVal = "";
        // Check if the file exists in the directory
        // If it does set it as the file/ otherwise create a new one
        // overwrite as given by parameter
      return retVal;
    }

    public String man(String command) {
        return MySession.manPages(command);
    }
}