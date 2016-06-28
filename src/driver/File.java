package driver;

/**
 * Represents files in a filesystem. It has the functionality to store, and
 * modify text data.
 */
public class File extends FileTypes {
    /**
     * Stores file content   *
     */
    private String content;

    /**
     * Create a File with a name
     *
     * @param name Name of the file that will be created
     * @throws InvalidName Thrown when name given to the file is invalid
     */
    public File(String name) throws InvalidName {
        super(name);
        content = "";
    }

    /**
     * Create a File with a name and content
     *
     * @param name    Name of the file that will be created
     * @param content Content to store in the file
     * @throws InvalidName Thrown when name given to file is invalid
     */
    public File(String name, String content) throws InvalidName {
        super(name);
        this.content = content;
    }

    /**
     * Get the content of the file
     *
     * @return The content of the file
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the file
     *
     * @param content The new content to overwrite the file with
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Add new content to the end of the file
     *
     * @param content The new content that needs to be added to the file
     */
    public void appendContent(String content) {
        //Add to the back of the file
        if (this.content.isEmpty())
            setContent(content);
        else
            this.content += "\n" + content;
    }

    /**
     * String representation of a file
     *
     * @return The name of the file
     */
    public String toString() {
        return "File Name: " + getName();
    }

    @Override
    /**
     * Determine if the two files are equal
     */
    public boolean equals(Object obj) {
        if (obj instanceof File) {
            //Check if the name and contents of the files are the same
            if (((File) obj).getName().equals(getName())
                    && ((File) obj).getContent().equals(getContent()))
                return true;
        }
        return false;
    }
}
