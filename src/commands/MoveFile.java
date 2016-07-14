package commands;

import java.util.List;

import driver.FilePathInterpreter;
import exceptions.InvalidDirectoryPathException;
import driver.MySession;
import exceptions.InvalidAdditionException;
import exceptions.InvalidNameException;
import structures.Directory;
import exceptions.MissingNameException;
import exceptions.NameExistsException;
import structures.FileTypes;

/**
 * The Class DisplayFile handles the moveing of a file to another dir.
 */
public class MoveFile implements Command {

    // MySession is used to access the files by finding them through the
    // root or current directory
    private MySession s;

    public MoveFile(MySession session) {
        s = session;
    }

    /**
     * Returns the manual for the mv command.
     *
     * @return the manual for the mv command
     */
    @Override
    public void man() {
        s.setOutput("MV(1)\t\t\t\tUser Commands\t\t\t\tMV(1)\n"
                + "\nNAME\n\t\tmv - Move file from source to destination. \n" +
                "\n" +
                "SYNOPSIS\n" +
                "\t\t" +
                "\n\t\t"
                + "mv FILE1 [FILE2]\n\nDESCRIPTION\n\t\t"
                + "Move the file from the source path to the \n" +
                "destination paths if it is valid.");
    }

    /**
     * Process arguments passed for mv command and determine whether the command
     * was entered correctly or not.
     *
     * @param args Arguments parsed from command
     * @return The contents of the files given.
     */
    @Override
    public void interpret(List<String> args) {
        if (args.size() != 3) {
            s.setError("mv usage: mv SRCPATH [DESTPATH] ...");
        } else {
            exec(args.subList(1, args.size()));
            // return output from function call
        }
    }

    // TODO: 10/07/16 Need to check what happens if the user tries to move the root. Also have some sort of collaboration with cp and mv 
    /**
     * Once this is finished delete the other one
     * @param args
     */
    public void exec2(List<String> args) {
        try {
            FileTypes src = FilePathInterpreter.interpretPath(s.getCurrentDir(),
                    args.get(0));

            Directory dest;
            String newName = "";
            try {
                //Determine if the destination path exists
                dest = (Directory) FilePathInterpreter.interpretPath(s
                                .getCurrentDir(),
                        args.get(1));
            } catch (InvalidDirectoryPathException e) {
                //If the dest path doesn't exist, maybe the command is asking
                // for a rename so check if the parent exists
                dest = (Directory) FilePathInterpreter
                        .interpretMakePath(s.getCurrentDir(),
                                args.get(1));
                String [] names = args.get(1).split("/");
                newName= names[names.length - 1];
            }

            Directory parent = (Directory) FilePathInterpreter.interpretMakePath(s.getCurrentDir(),
                    args.get(0));


            //Are we supposed to replace?
            dest.addReplace(src);
            parent.remove(src.getName());

            if (!newName.equals("")) {
                try {
                    src.setName(newName);
                } catch (NameExistsException e) {
                    e.printStackTrace();
                }
            }


        } catch (InvalidDirectoryPathException e) {
            s.addError("The source or destination path does not exist\n");
        } catch (ClassCastException i) {
            s.addError("The destination path does not lead to a directory\n");
        } catch (InvalidAdditionException invalidAddition) {
            s.addError(invalidAddition.getMessage());
        } catch (MissingNameException e) {
            s.addError(e.getMessage());
        } catch (InvalidNameException invalidName) {
            s.addError(invalidName.getMessage());
        }
    }

    /**
     * Copies the file from one directory to another
     *
     * @param args Valid arguments parsed from command
     * @return The contents of the files given.
     */
    @Override
    public void exec(List<String> args) {
        try {
            // Getting both the src and dest place

            //still need to do case mv a b where file a's name is changed to b
            FileTypes src = FilePathInterpreter.interpretPath(s.getCurrentDir(),
                    args.get(0));


            Directory dest = (Directory) FilePathInterpreter.interpretPath(s.getCurrentDir(),
                    args.get(1));

            try {
                Directory parent = (Directory) FilePathInterpreter.interpretMakePath(s.getCurrentDir(),
                        args.get(0));

                // Removing the previous if it exists
                // Need to double check this
                if (dest.nameExists(src.getName()) != -1) {
                    dest.remove(src.getName());
                }

                dest.add(src);
                parent.remove(src.getName());

            } catch (NameExistsException | InvalidAdditionException | MissingNameException e) {
                s.addError("The file cannot be added. It already " +
                        "exists or is not valid.\n");
            }

        } catch (InvalidDirectoryPathException e) {
            s.addError("The source path does not exist\n");
        } catch (ClassCastException i) {
            s.addError("The destination path leads to a file instead of a " +
                    "directory");
        }
    }
}
