package driver;

import java.util.ArrayList;

/**
 * Created by dhrumil on 12/06/16.
 */
public class Directory {
    private String name;
    private Directory parent;
    private ArrayList children;


    /**
     * Create a new Directory with a name
     *
     * @param name Name of the Directory
     */
    public Directory(String name) {
        this.name = name;
        children = new ArrayList();
    }

    /**
     * Change the name of the directory
     *
     * @param name New Directory name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the current directory
     *
     * @return The name of the directory
     */
    public String getName() {
        return name;
    }

    /**
     * Get the parent Directory
     *
     * @return The parent Directory of the current directory
     */
    public Directory getParent() {
        return parent;
    }


    public void add(Object addObject) throws NameExistsException, InvalidObjectType {
        if (addObject instanceof File) {
            if (nameExists(((File) addObject).getName()) == -1)
                children.add(addObject);
            else
                throw new NameExistsException(((File) addObject).getName() + " name is already in use in the current directory.");
        } else if (addObject instanceof Directory) {
            Directory dir = (Directory) addObject;
            if (nameExists((dir).getName()) == -1) {
                dir.parent = this;
                children.add(addObject);
            } else
                throw new NameExistsException(dir.getName() + " name is already in use in the current directory.");
        } else
            throw new InvalidObjectType("Object needs to be an instance of Directory or File");
    }

    public void addReplace(Object addObject) throws InvalidObjectType {
        int index;
        if (addObject instanceof File) {
            if ((index = nameExists(((File) addObject).getName())) == -1)
                children.add(addObject);
            else {
                children.remove(index);
                children.add(index, addObject);
            }

        } else if (addObject instanceof Directory) {
            ((Directory) addObject).parent = this;
            if ((index = nameExists(((Directory) addObject).getName())) == -1) {
                children.add(addObject);
            } else {
                children.remove(index);
                children.add(index, addObject);
            }

        } else
            throw new InvalidObjectType("Object needs to be an instance of Directory or File");
    }

    public void addReplaceMulti(ArrayList addObjects) throws InvalidObjectType {
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

    public ArrayList getChildren() {
        return new ArrayList(children);
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
        Object child;
        for (int i = 0; i < children.size(); i++) {
            if ((child = children.get(i)) instanceof File)
                childNames.add(((File) child).getName());
            else if (child instanceof Directory) {
                childNames.add(((Directory) child).getName());
            }
        }
        return childNames;
    }

    public Object getChild(String name) throws MissingNameException {
        int index;
        if ((index = nameExists(name)) != -1) {
            return children.get(index);
        }else
            throw new MissingNameException("There are no files or directories with name " + name);


    }

    public String toString() {
        return "Directory Name: " + getName();
    }

    public class NameExistsException extends Exception {
        public NameExistsException(String message) {
            super(message);
        }
    }

    public class MissingNameException extends Exception {
        public MissingNameException(String message) {
            super(message);
        }
    }

    public class InvalidObjectType extends Exception {
        public InvalidObjectType(String message) {
            super(message);
        }
    }
}



