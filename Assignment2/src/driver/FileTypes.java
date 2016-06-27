package driver;

import java.util.regex.Pattern;

/**
 * Created by dhrumil on 14/06/16.
 */
public abstract class FileTypes {
    private String name;

    /**
     * Create an object with superclass FileTypes with a name
     *
     * @param name Name of the FileType object
     * @throws InvalidName when invalid name is given
     */
    public FileTypes(String name) throws InvalidName {
        if (isValid(name))
            this.name = name;
        else
            throw new InvalidName("Name contains invalid characters");
    }

    /**
     * Get the name
     *
     * @return Name of the FileTypes object
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the FileTypes object
     *
     * @param name The new name to set
     * @throws InvalidName when invalid name is given
     */
    public void setName(String name) throws InvalidName {
        if (isValid(name))
            this.name = name;
        else
            throw new InvalidName("Name contains invalid characters");
    }

    /**
     * Determine if the given name is a valid name for Files and Directories
     *
     * @param name
     * @return
     */
    public boolean isValid(String name) {
        // .. and . are invalid names
        if (name.equals("..") || name.equals(".")) {
            return false;
            // If the name doesn't contain all alphanumeric character resturn false
        } else {
            Pattern p = Pattern.compile("[^a-zA-Z0-9.]");
            return !p.matcher(name).find();
        }
    }


    public class InvalidName extends Exception {
        private static final long serialVersionUID = 59L;

        public InvalidName(String message) {
            super(message);
        }
    }


}
