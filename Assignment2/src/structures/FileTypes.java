package structures;

import java.util.regex.Pattern;

/**
 * This abstract class FileTypes provides the skeleton for all files and
 * directories created in this file system. The skeleton methods allow for a
 * user to get and set the name of a file and check if it's a valid name.
 */
public abstract class FileTypes {

    /**
     * Stores the parent directory of the current FileType
     */
    private Directory parent;

    /**
     * Name of file/directory created
     */
    private String name;

    /**
     * Create an object with superclass FileTypes with a name.
     *
     * @param name Name of the FileType object
     * @return new FileTypes object, subclass specifies which type
     * @throws InvalidNameException when invalid name is given
     */
    public FileTypes(String name) throws InvalidNameException {
        if (isValid(name)) // valid name, assign it to name property
            this.name = name;
        else // throw exception for invalid name
            throw new InvalidNameException(name);
    }

    /**
     * Get the parent Directory of current directory.
     *
     * @return The parent Directory of the current directory
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Set the parent of the current FileType object.
     *
     * @param dir The directory to make the parent of this object
     * @throws InvalidSetParentException Thrown when a user tries to set the
     *                                   parent directory of this FileType
     *                                   object but the parent directory doesn't
     *                                   already contain this FileType object
     */
    public void setParent(Directory dir) throws InvalidSetParentException {
        //Only setParent if the given directory already contains this object
        // as a
        // child. This is a safety measure to ensure the child points to the
        // correct parent. This means the child needs to be added first before
        // adding a parent

        boolean childExists = false;
        for (FileTypes ft : dir.getChildren()) {
            if (ft == this) {
                this.parent = dir;
                childExists = true;
                break;
            }
        }

        if (!childExists)
            throw new InvalidSetParentException();

    }

    /**
     * Get the name of a file/directory.
     *
     * @return Name of the FileTypes object
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the FileTypes object.
     *
     * @param name The new name to set
     * @throws InvalidNameException                     when invalid name is
     *                                                  given
     * @throws structures.Directory.NameExistsException Thrown when the new name
     *                                                  already exists in the
     *                                                  parent directory
     */
    public void setName(String name) throws InvalidNameException,
            Directory.NameExistsException {
        if (isValid(name)) // check for name validity
        {
            if (this.getParent() == null)
                this.name = name;
            else if (this.getParent().nameExists(name) == -1)
                this.name = name;
            else
                throw new Directory.NameExistsException(name);
        } else
            throw new InvalidNameException(name);
    }

    /**
     * Determine if the given name is a valid name for Files and Directories.
     *
     * @param name Potential name of file
     * @return whether or not file name is valid
     */
    public boolean isValid(String name) {
        // .. and . are invalid names
        if (name.equals("..") || name.equals(".")) {
            return false;
            // If the name doesn't contain all alphanumeric character return
            // false
        } else {
            Pattern p = Pattern.compile("[^a-zA-Z0-9._-]");
            return !p.matcher(name).find();
        }
    }

    /**
     * The exception class InvalidNameException is invoked when a user tries to
     * give a name for a FileTypes object that is invalid when comparing it with
     * restrictions for characters that can be used.
     */
    public static class InvalidNameException extends Exception {
        /**
         * Serial version ID needed when creating exceptions.
         */
        private static final long serialVersionUID = 59L;

        /**
         * Return a new InvalidNameException exception with specified message.
         *
         * @param name The invalid name
         * @return InvalidNameException
         */
        public InvalidNameException(String name) {
            super("Name " + name + " contains invalid characters");
        }
    }

    /**
     * The InvalidSetParentException is thrown when a user tries to set the
     * parent directory of this FileType object but the parent directory doesn't
     * contain this FileType object
     */
    public static class InvalidSetParentException extends Exception {
        /**
         * Serial version ID needed when creating exceptions.
         */
        private static final long serialVersionUID = 59L;

        /**
         * Return a new InvalidSetParentException
         *
         * @return InvalidNameException exception
         */
        public InvalidSetParentException() {
            super("Unable to set the parent directory of this " +
                    "FileType object. The " +
                    "given directory does not contain this object as a child.");
        }
    }


}
