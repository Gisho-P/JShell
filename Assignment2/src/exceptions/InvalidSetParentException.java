package exceptions;

/**
 * The InvalidSetParentException is thrown when a user tries to set the
 * parent directory of this FileType object but the parent directory doesn't
 * contain this FileType object
 */
public class InvalidSetParentException extends Exception {
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
