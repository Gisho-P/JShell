package structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a directory in the FileSystem. It can be used to store
 * sub-directories and files. It also stores a reference to the parent
 * directory.
 *
 * @author Dhrumil Patel
 */
public class Directory extends FileTypes {

    /**
     * Stores the root of the filesystem.
     */
    private static Directory root = null;
    /**
     * Stores the children of the current directory
     */
    private ArrayList<FileTypes> children;

    /**
     * Create a new Directory object with a specified name
     *
     * @param name Name of the Directory
     * @return new Directory object
     */
    public Directory(String name) throws InvalidName {
        super(name); // store name in super class' name attribute
        children = new ArrayList<FileTypes>();
    }

    /**
     * Allows the user to create a new File System. If a file system already
     * exists, the root of the existing file system is returned
     * @return A new file system if an instance of it doesn't already exist.
     * Otherwise it returns the existing file system.
     */
    public static Directory createFileSystem() {
        if (root == null) {
            try {
                root = new Directory("");
                //Should never get to this point since it is not an invalid name
            } catch (InvalidName invalidName) {
                invalidName.printStackTrace();
            }
        }
        return root;
    }

    /**
     * Adds a FileTypes (file or directory) object to the current directory.
     *
     * @param addObject FileTypes object to be added to the current directory
     * @throws NameExistsException Thrown when FileTypes object with the same
     *                             already exists in the current directory
     * @throws InvalidAddition     Thrown when user tries to add the current
     *                             directory object to the current directory
     */
    public void add(FileTypes addObject)
            throws NameExistsException, InvalidAddition {
        // If user tries to add the current or parent directory object to the
        // current directory

        Directory current = this;
        while (current != null) {
            if (addObject == current)
                throw new InvalidAddition(
                        "Can not add the current directory or parent " +
                                "directory as the child of "
                                + "the current directory.");
            current = current.getParent();
        }
        // Check if FileType object with the same name already exists
        if (nameExists(addObject.getName()) == -1) {
            children.add(addObject);
            try {
                addObject.setParent(this);
                //Should never reach the catch part since we added the object
                // to the children list
            } catch (InvalidSetParentException e) {
                e.printStackTrace();
            }
        } else
            throw new NameExistsException(addObject.getName()
                    + " name is already in use in the current directory.");
    }

    /**
     * Adds the FileTypes object to the current directory. If FileTypes object
     * with same name already exists, it replaces that object with the given
     * object
     *
     * @param addObject FileTypes object to be added to the current directory
     * @throws InvalidAddition Thrown when user tries to add the current
     *                         directory object to the current directory
     */
    public void addReplace(FileTypes addObject) throws InvalidAddition {
        int index;
        // If user tries to add the current directory object to the current
        // directory
        Directory current = this;
        while (current != null) {
            if (addObject == current)
                throw new InvalidAddition(
                        "Can not add the current directory or parent " +
                                "directory as the child of"
                                + " the current directory.");
            current = current.getParent();
        }

//        if (addObject instanceof Directory)
//            ((Directory) addObject).parent = this;

        // Check if FileType object with the same name already exists
        if ((index = nameExists(addObject.getName())) == -1) {
            children.add(addObject);
        } else {
            children.remove(index);
            children.add(index, addObject);
        }

        try {
            addObject.setParent(this);
            //Should never reach the catch part since we added the object
            // to the children list
        } catch (InvalidSetParentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds Multiple FileTypes object to the current directory. If FileTypes
     * object with same name already exists, it replaces that object with the
     * given object
     *
     * @param addObjects Array of FileTypes object to be added to the current
     *                   directory
     * @throws InvalidAddition Thrown when user tries to add the current
     *                         directory object to the current directory
     */
    public void addReplaceMulti(ArrayList<FileTypes> addObjects)
            throws InvalidAddition {
        for (int i = 0; i < addObjects.size(); i++) {
            addReplace(addObjects.get(i));
        }
    }


    /**
     * Remove the file or directory with the given name.
     *
     * @param name The name of the file or directory to be removed
     * @throws MissingNameException Thrown when object with that name does not
     *                              exist
     */
    public void remove(String name) throws MissingNameException {
        int index;
        // If the object exists remove it
        if ((index = nameExists(name)) != -1)
            children.remove(index);
        else
            throw new MissingNameException(
                    name + " does not exist in the current directory\n");
    }


    /**
     * Checks if a FileTypes object with the given name exists in the current
     * directory. Returns an index of where the object is located in the
     * directory. -1 is returned if the object doesn't exist
     *
     * @param name Name of FileTypes object to search for in the current
     *             directory
     * @return Location of FileTypes object in the directory. -1 is returned if
     * object doesn't exist
     */
    public int nameExists(String name) {
        // Determine if FileTypes object with given name exists in the current
        // directory
        ArrayList<String> childNames = getChildNames();
        for (int i = 0; i < childNames.size(); i++) {
            if (childNames.get(i).equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Get the children in the current directory.
     *
     * @return A copy of the list of FileTypes objects in the current directory
     */
    public ArrayList<FileTypes> getChildren() {
        return new ArrayList<FileTypes>(children);
    }

    /**
     * Get the directory objects in the current directory.
     *
     * @return A list of the directory objects in the current directory
     */
    public ArrayList<Directory> getChildDirs() {
        // Return the directory objects in the current directory
        ArrayList<Directory> childDir = new ArrayList<Directory>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof Directory) {
                childDir.add((Directory) children.get(i));
            }
        }
        return childDir;
    }

    /**
     * Get the file objects in the current directory
     *
     * @return A list of the file objects in the current directory
     */
    public ArrayList<File> getChildFiles() {
        // Return the file objects in the current directory
        ArrayList<File> childFiles = new ArrayList<File>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof File) {
                childFiles.add((File) children.get(i));
            }
        }
        return childFiles;
    }

    /**
     * Get the path to the current directory from a parent directory.
     *
     * @return A path to the current directory from a parent directory
     */
    public String getPath(Directory dir2Parent) {
        // Construct the path to the current directory from the root
        String path = "/" + getName();
        Directory current = this;
        while ((current = current.getParent()) != dir2Parent) {
            // root doesn't require a slash before the name
            if (current.getName() == "")
                path = current.getName() + path;
            else
                path = "/" + current.getName() + path;
        }
        return path;
    }

    /**
     * Get the path to the current directory from the root.
     *
     * @return A path to the current directory from root
     */
    public String getEntirePath() {
        return getPath(null);
    }

    /**
     * Get a list of all of the FileType objects name in the current directory
     *
     * @return List of all of the child names in the current directory
     */
    public ArrayList<String> getChildNames() {
        // Find a list of child names
        ArrayList<String> childNames = new ArrayList<String>();
        for (int i = 0; i < children.size(); i++) {
            childNames.add(children.get(i).getName());
        }
        return childNames;
    }

    /**
     * Get the FileTypes object by its name
     *
     * @param name Name of the FileTypes object
     * @return FileTypes object with the given name
     * @throws MissingNameException Thrown when FileTypes object with given name
     *                              doesn't exist in the current directory
     */
    public FileTypes getChild(String name) throws MissingNameException {
        int index;
        // Find the FileTypes object with the given name
        if ((index = nameExists(name)) != -1) {
            return children.get(index);
        } else
            throw new MissingNameException(
                    "There are no files or directories with name " + name);
    }

    /**
     * Checks whether a file is a child of the this directory and all it's sub
     * directories recursively.
     */
    public boolean hasDeepChild(FileTypes wanted) {
        Queue<Directory> children = new LinkedList<Directory>();
        children.addAll(this.getChildDirs());
        while (!children.isEmpty()) {
            // Dequeuing our latest
            Directory curr = children.poll();
            for (FileTypes file : curr.getChildren()) {
                // Checking if they're equal
                if (file.equals(wanted)) {
                    return true;
                }
            }
            // Adding the children
            children.addAll(curr.getChildDirs());
        }
        return false;
    }

    /**
     * Create a deep copy of the directory and all of its contents
     *
     * @param dir Directory to be copied
     * @return the copied Directory
     */
    public static Directory deepCopy(Directory dir) {
        Directory newDir = null;
        //This should always work since the original directory couldn't have
        // been created with an invalidName
        try {
            newDir = new Directory(dir.getName());
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }

        for (FileTypes fileType : dir.getChildren()) {
            //Depending on the type of the File, add a copy of it ot the
            // new directory
            try {
                if (fileType instanceof File) {
                    newDir.add(File.copy((File) fileType));
                } else if (fileType instanceof Directory) {
                    newDir.add(deepCopy((Directory) fileType));
                }
            } catch (NameExistsException e) {
                e.printStackTrace();
            } catch (InvalidAddition invalidAddition) {
                invalidAddition.printStackTrace();
            }
        }

        return newDir;
    }

    /**
     * Get a string representation of the current directory.
     *
     * @return Name of the current directory
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * NameExistsException is used when a file or directory with the same name
     * already exists in the current directory.
     *
     * @author Dhrumil Patel
     */
    public static class NameExistsException extends Exception {
        /**
         * Serial ID needed when creating exceptions.
         */
        private static final long serialVersionUID = 59L;

        /**
         * Constructor to create a new NameExistsException exception.
         *
         * @param message Message to display when throwing exception
         * @return NameExistsException
         */
        public NameExistsException(String message) {
            super(message);
        }
    }

    /**
     * MissingNameException is used when a user tries to remove a file or
     * directory that doesn't exist.
     *
     * @author Dhrumil Patel
     */
    public static class MissingNameException extends Exception {
        /**
         * Serial ID needed when creating exceptions.
         */
        private static final long serialVersionUID = 59L;

        /**
         * Constructor to create a new MissingNameException exception.
         *
         * @param message Message to display when throwing exception
         * @return MissingNameException
         */
        public MissingNameException(String message) {
            super(message);
        }
    }

    /**
     * InvalidAddition exception is used when the user tries to add the current
     * directory as the child of the current directory.
     *
     * @author Dhrumil Patel
     */
    public static class InvalidAddition extends Exception {
        /**
         * Serial ID needed when creating exceptions.
         */
        private static final long serialVersionUID = 59L;

        /**
         * Constructor to create a new InvalidAddition exception.
         *
         * @param message Message to display when throwing exception
         * @return InvalidAddition
         */
        public InvalidAddition(String message) {
            super(message);
        }
    }
}


