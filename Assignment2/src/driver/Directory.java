package driver;

import java.util.ArrayList;

/**
 * Created by dhrumil on 12/06/16.
 */
public class Directory {
    private String name;
    private Directory parent;
    private ArrayList children;

    public Directory(String name) {
        this.name = name;
    }

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
        parent.addDir(this);
    }

    public void addFiles(ArrayList<File> files) {
        for(int i = 0; i < files.size(); i++) {
            children.add(files.get(i));
        }
    }

    public void addDirs(ArrayList<Directory> directories) {
        for(int i = 0; i < directories.size(); i++) {
            children.add(directories.get(i));
        }
    }

    public void addFile(File file) {
        children.add(file);
    }

    public void addDir(Directory directory) {
        children.add(directory);
    }

    public void addAll(ArrayList<Directory> directories, ArrayList<File> files) {
        addDirs(directories);
        addFiles(files);
    }

    public ArrayList getChildren() {
        return new ArrayList(children);
    }

    public ArrayList<Directory> getChildDirs() {
        ArrayList<Directory> childDir = new ArrayList<Directory>();
        for (int i = 0; i < children.size(); i++ ) {
            if (children.get(i) instanceof Directory) {
                childDir.add((Directory)children.get(i));
            }
        }
        return childDir;
    }

    public ArrayList<File> getChildFiles() {
        ArrayList<File> childFiles = new ArrayList<File>();
        for (int i = 0; i < children.size(); i++ ) {
            if (children.get(i) instanceof File) {
                childFiles.add((File)children.get(i));
            }
        }
        return childFiles;
    }


}
