package driver;

import java.util.ArrayList;
//import java.util.Iterator;

/**
 * Created by dhrumil on 12/06/16.
 */
public class Directory extends FileTypes {
    private Directory parent;
    private ArrayList<FileTypes> children;


    /**
     * Create a new Directory with a name
     *
     * @param name Name of the Directory
     */
    public Directory(String name) {
        super(name);
        children = new ArrayList<FileTypes>();
    }

    /**
     * Get the parent Directory
     *
     * @return The parent Directory of the current directory
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Adds FileTypes objects to the current directory
     *
     * @param addObject FileTypes object to be added to the current directory
     * @throws NameExistsException Thrown when FileTypes object with the same already exists in the current directory
     * @throws InvalidAddition     Thrown when user tries to add the current directory object to the current directory
     */
    public void add(FileTypes addObject) throws NameExistsException, InvalidAddition {
        //If user tries to add the current directory object to the current directory
        if (addObject == this)
            throw new InvalidAddition("Can not add the current directory object as the child of the current directory.");
        //Check if FileType object with the same name already exists
        if (nameExists(addObject.getName()) == -1) {
            if (addObject instanceof Directory) {
                ((Directory) addObject).parent = this;
            }
            children.add(addObject);
        } else
            throw new NameExistsException(addObject.getName() + " name is already in use in the current directory.");
    }

    /**
     * Adds the FileTypes object to the current directory. If FileTypes object with same name already exists, it
     * replaces that object with the given object
     *
     * @param addObject FileTypes object to be added to the current directory
     * @throws InvalidAddition Thrown when user tries to add the current directory object to the current directory
     */
    public void addReplace(FileTypes addObject) throws InvalidAddition {
        int index;
        //If user tries to add the current directory object to the current directory
        if (addObject == this)
            throw new InvalidAddition("Can not add the current directory object as the child of the current directory.");

        //Make the parent of the given object this one
        if (addObject instanceof Directory)
            ((Directory) addObject).parent = this;

        //Check if FileType object with the same name already exists
        if ((index = nameExists(addObject.getName())) == -1) {
            children.add(addObject);
        } else {
            children.remove(index);
            children.add(index, addObject);
        }
    }

    /**
     * Adds Multiple FileTypes object to the current directory. If FileTypes object with same name already exists, it
     * replaces that object with the given object
     *
     * @param addObjects Array of FileTypes object to be added to the current directory
     * @throws InvalidAddition Thrown when user tries to add the current directory object to the current directory
     */
    public void addReplaceMulti(ArrayList<FileTypes> addObjects) throws InvalidAddition {
        for (int i = 0; i < addObjects.size(); i++) {
            addReplace(addObjects.get(i));
        }
    }


    /**
     * Remove the file or directory with the given name
     *
     * @param name The name of the file or directory to be removed
     * @throws MissingNameException Thrown when object with that name does not exist
     */
    public void remove(String name) throws MissingNameException {
        int index;
        //If the object exists remove it
        if ((index = nameExists(name)) != -1)
            children.remove(index);
        else
            throw new MissingNameException(name + " does not exist in the current directory\n");
    }


    /**
     * Checks if a FileTypes object with the given name exists in the current directory. Returns an index of where
     * the object is located in the directory. -1 is returned if the object doesn't exist
     *
     * @param name Name of FileTypes object to search for in the current directory
     * @return Location of FileTypes object in the directory. -1 is returned if object doesn't exist
     */
    public int nameExists(String name) {
        //Determine if FileTypes object with given name exists in the current driectory
        ArrayList<String> childNames = getChildNames();
        for (int i = 0; i < childNames.size(); i++) {
            if (childNames.get(i).equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Get the children in the current directory
     *
     * @return A copy of the list of FileTypes objects in the current directory
     */
    public ArrayList<FileTypes> getChildren() {
        return new ArrayList<FileTypes>(children);
    }

    /**
     * Get the directory objects in the current directory
     *
     * @return A list of the directory objects in the current directory
     */
    public ArrayList<Directory> getChildDirs() {
        //Return the directory objects in the current directory
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
        //Return the file objects in the current directory
        ArrayList<File> childFiles = new ArrayList<File>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof File) {
                childFiles.add((File) children.get(i));
            }
        }
        return childFiles;
    }

    /**
     * Get the path to the current directory from the root
     *
     * @return A path to the current directory from root
     */
    public String getEntirePath() {
        //Construct the path to the current directory from the root
        String path = "/" + name;
        Directory current = this;
        while ((current = current.parent) != null) {
          // root doesn't require a slash before the name
          if(current.name == "")
            path = current.name + path;
          else
            path = "/" +current.name + path;
        }
        return path;
    }

    /**
     * Get a list of all of the FileType objects name in the current directory
     *
     * @return List of all of the child names in the current directory
     */
    public ArrayList<String> getChildNames() {
        //Find a list of child names
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
     * @throws MissingNameException Thrown when FileTypes object with given name doesn't exist in the current directory
     */
    public FileTypes getChild(String name) throws MissingNameException {
        int index;
        //Find the FileTypes object with the given name
        if ((index = nameExists(name)) != -1) {
            return children.get(index);
        } else
            throw new MissingNameException("There are no files or directories with name " + name);
    }

    /**
     * Get a string representation of the current directory
     *
     * @return Name of the current directory
     */
    public String toString() {
        return "Directory Name: " + getName();
    }
    
    private boolean equalsRecursive(Directory currentDir, ArrayList<FileTypes> inputDir) {
        int index;
        FileTypes currentFileType;
        for (FileTypes fileType : inputDir) {
            if ((index = currentDir.nameExists(fileType.getName())) == -1) {
                return false;
            } else {
                currentFileType = currentDir.getChildren().get(index);
                if (fileType instanceof File && currentFileType instanceof File) {
                    if (!(((File) fileType).equals(((File) currentFileType))))
                        return false;
                } else if (fileType instanceof Directory && currentFileType instanceof Directory)
                    if (!(equalsRecursive((Directory) currentFileType, ((Directory) fileType).getChildren())))
                        return false;
                else
                    return false;
            }
        }
        return true;

    }


    public class NameExistsException extends Exception {
        private static final long serialVersionUID = 59L;

        public NameExistsException(String message) {
            super(message);
        }
    }

    public class MissingNameException extends Exception {
        private static final long serialVersionUID = 59L;

        public MissingNameException(String message) {
            super(message);
        }
    }

    public class InvalidAddition extends Exception {
        private static final long serialVersionUID = 59L;

        public InvalidAddition(String message) {
            super(message);
        }
    }
}



