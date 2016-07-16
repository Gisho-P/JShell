package commands;

import java.util.List;

import driver.*;
import exceptions.*;
import structures.*;

/**
 * The Class MoveFile replicates the mv command in shell. It allows the
 * moving and renaming of files
 */
public class MoveFile implements Command {

    // MySession is used to access the files by finding them through the
    // root or current directory
    private MySession s;

    /**
     * Used to store a copy of the file or directory being moved
     */
    private FileTypes sourceCopy;

    /**
     * Used to store the parent directory of the FileType being moved
     */
    private Directory sourceParent;

    /**
     * Used to determine if the current node is being moved
     */
    private boolean currentDirIsSame = false;

    /**
     * Used to get copy of object being moved
     * @return copy of object being moved
     */
    public FileTypes getSourceCopy() {
        return sourceCopy;
    }

    /**
     * Used to get parent of object being moved
     * @return copy of object being moved
     */
    public Directory getSourceParent() {
        return sourceParent;
    }

    /**
     * Used to determine if current directory is being moved
     * @return whether or not the current directory being moved
     */
    public boolean isCurrentDirSame() {
        return currentDirIsSame;
    }

    /**
     * Create a move file object
     * @param session
     */
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
                "\n"
                + "SYNOPSIS\n" + "\t\t" + "\n\t\t"
                + "mv FILE1 [FILE2]\n\nDESCRIPTION\n\t\t"
                + "Move the file from the source path to the \n"
                + "destination paths if it is valid.");
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

    /**
     * Used to perform the mv command. Moves directories or files from src to
     * the specified destination
     */
    public void exec(List<String> args) {
        currentDirIsSame  = false;
        try {
            FileTypes src =
                    FilePathInterpreter.interpretPath(s.getCurrentDir(),
                            args.get(0));
            FileTypes dest;
            String newName = "";
            try {
                // Determine if the destination path exists
                dest =
                        FilePathInterpreter.interpretPath(s.getCurrentDir(),
                                args.get(1));
            } catch (InvalidDirectoryPathException e) {
                // If the dest path doesn't exist, maybe the command is asking
                // for a rename
                dest = FilePathInterpreter.interpretMakePath(s.getCurrentDir(),
                        args.get(1));
                String[] names = args.get(1).split("/");
                newName = names[names.length - 1];
            }
            if (FileTypes.isInvalidAddition(src, dest))
                throw new InvalidAdditionException();
            sourceCopy = FileTypes.deepCopy(src);
            Directory parent = src.getParent();
            sourceParent = parent;
            //Determine if the current directory needs to be moved
            if (src == s.getCurrentDir())
                currentDirIsSame = true;
            if (dest instanceof File) {
                destIsFile(src, dest, parent);
            } else {
                moveAndReplace(src, (Directory) dest, newName, parent);
            }
        } catch (InvalidDirectoryPathException | InvalidAdditionException
                | MissingNameException | InvalidNameException |
                NameExistsException e) {
            s.addError(e.getMessage());
        }
    }

    /**
     *
     * @param src The src node to move
     * @param dest The destination to move the source to
     * @param parent The parent of the src node
     * @throws MissingNameException When required file is not present
     * @throws InvalidNameException When Invalid name is entered for the
     * directory
     * @throws NameExistsException When a file or directory with same name is
     * being added
     * @throws InvalidAdditionException Thrown when parent is being added to
     * subdirectory
     */
    private void destIsFile(FileTypes src, FileTypes dest, Directory parent)
            throws MissingNameException, InvalidNameException,
            NameExistsException, InvalidAdditionException {
        if (src instanceof File) {
            // If src and destination lead to file, then replace the file in
            // the destination with the file in the src
            Directory dParent = dest.getParent();
            parent.remove(src.getName());
            src.setName(dest.getName());
            dParent.addReplace(src);
        } else {
            // moving directory to a file case
            s.addError(
                    "Invalid destination path. Can not move " +
                            "source directory.");
        }
    }

    /**
     * Used to move and replace files in cases where the src file is a directory
     * @param src The src node to move
     * @param dest The destination to move the source to
     * @param newName Used to determine whether or not renaming is required
     * @param parent The parent of the src node
     * @throws MissingNameException When required file is not present
     * @throws InvalidNameException When Invalid name is entered for the
     * directory
     * @throws NameExistsException When a file or directory with same name is
     * being added
     * @throws InvalidAdditionException Thrown when parent is being added to
     * subdirectory
     */
    private void moveAndReplace(FileTypes src, Directory dest, String newName,
                                Directory parent) throws MissingNameException,
            InvalidNameException, NameExistsException,
            InvalidAdditionException {
        Directory dDest = dest;
        // Rename case
        if (!newName.equals("")) {
            parent.remove(src.getName());
            src.setName(newName);
            dDest.add(src);
        } else {
            // determine whether or not to move
            boolean move = false;
            // move if dest doesn't contain same name as src
            if (dDest.nameExists(src.getName()) == -1) {
                move = true;
            } else {
                FileTypes dChild = dDest.getChild(src.getName());
                if ((src instanceof Directory && dChild instanceof Directory
                        && ((Directory) dChild).size() == 0)
                        || (src instanceof File && dChild instanceof File))
                    move = true;
            }

            // add or replace the file in dest
            if (move) {
                parent.remove(src.getName());
                dDest.addReplace(src);
            } else
                s.addError("Error. Type mismatch between "
                        + "source file and file being replaced or the "
                        + "file being replaced is not empty.");
        }
    }
}
