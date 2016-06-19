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

    public void add(FileTypes addObject) throws NameExistsException, InvalidAddition {
        //If user tries to add the current directory object to the current directory
        if (addObject == this)
            throw new InvalidAddition("Can not add the current directory object as the child of the current directory.");
        if (nameExists(addObject.getName()) == -1) {
            if (addObject instanceof Directory) {
                ((Directory) addObject).parent = this;
            }
            children.add(addObject);
        } else
            throw new NameExistsException(addObject.getName() + " name is already in use in the current directory.");
    }

//    public void add(Object addObject) throws NameExistsException, InvalidObjectType {
//        if (addObject instanceof File) {
//            if (nameExists(((File) addObject).getName()) == -1)
//                children.add(addObject);
//            else
//                throw new NameExistsException(((File) addObject).getName() + " name is already in use in the current directory.");
//        } else if (addObject instanceof Directory) {
//            Directory dir = (Directory) addObject;
//            if (nameExists((dir).getName()) == -1) {
//                dir.parent = this;
//                children.add(addObject);
//            } else
//                throw new NameExistsException(dir.getName() + " name is already in use in the current directory.");
//        } else
//            throw new InvalidObjectType("Object needs to be an instance of Directory or File");
//    }

//    public void addReplace(Object addObject) throws InvalidObjectType {
//        int index;
//        if (addObject instanceof File) {
//            if ((index = nameExists(((File) addObject).getName())) == -1)
//                children.add(addObject);
//            else {
//                children.remove(index);
//                children.add(index, addObject);
//            }
//
//        } else if (addObject instanceof Directory) {
//            ((Directory) addObject).parent = this;
//            if ((index = nameExists(((Directory) addObject).getName())) == -1) {
//                children.add(addObject);
//            } else {
//                children.remove(index);
//                children.add(index, addObject);
//            }
//
//        } else
//            throw new InvalidObjectType("Object needs to be an instance of Directory or File");
//    }

    public void addReplace(FileTypes addObject) throws InvalidAddition {
        int index;
        //If user tries to add the current directory object to the current directory
        if (addObject == this)
            throw new InvalidAddition("Can not add the current directory object as the child of the current directory.");

        if (addObject instanceof Directory)
            ((Directory) addObject).parent = this;

        if ((index = nameExists(addObject.getName())) == -1) {
            children.add(addObject);
        } else {
            children.remove(index);
            children.add(index, addObject);
        }
    }

    public void addReplaceMulti(ArrayList<FileTypes> addObjects) throws InvalidAddition{
        for (int i = 0; i < addObjects.size(); i++) {
            addReplace(addObjects.get(i));
        }
    }


    /**
     * Remove the file or directory with the given name
     *
     * @param name The name of the file or directory to be removed
     * @throws MissingNameException
     */
    public void remove(String name) throws MissingNameException {
        int index;
        if ((index = nameExists(name)) != -1)
            children.remove(index);
        else
            throw new MissingNameException(name + " does not exist in the current directory\n");
    }


    public int nameExists(String name) {
        ArrayList<String> childNames = getChildNames();
        for (int i = 0; i < childNames.size(); i++) {
            if (childNames.get(i).equals(name))
                return i;
        }
        return -1;
    }

    public ArrayList<FileTypes> getChildren() {
        return new ArrayList<FileTypes>(children);
    }

    public ArrayList<Directory> getChildDirs() {
        ArrayList<Directory> childDir = new ArrayList<Directory>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof Directory) {
                childDir.add((Directory) children.get(i));
            }
        }
        return childDir;
    }

    public ArrayList<File> getChildFiles() {
        ArrayList<File> childFiles = new ArrayList<File>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof File) {
                childFiles.add((File) children.get(i));
            }
        }
        return childFiles;
    }

    public String getEntirePath() {
        String path = "/" + name;
        Directory current = this;
        while ((current = current.parent) != null) {
            path = "/" + current.name + path;
        }
        return path;
    }

    public ArrayList<String> getChildNames() {
        ArrayList<String> childNames = new ArrayList<String>();
        for (int i = 0; i < children.size(); i++) {
            childNames.add(children.get(i).getName());
        }
        return childNames;
    }

    public FileTypes getChild(String name) throws MissingNameException {
        int index;
        if ((index = nameExists(name)) != -1) {
            return children.get(index);
        } else
            throw new MissingNameException("There are no files or directories with name " + name);
    }

    public String toString() {
        return "Directory Name: " + getName();
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



