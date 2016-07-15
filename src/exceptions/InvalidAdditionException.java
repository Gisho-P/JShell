package exceptions;

/**
 * InvalidAdditionException exception is used when the user tries to add the current
 * directory as the child of the current directory.
 *
 * @author Dhrumil Patel
 */
public class InvalidAdditionException extends Exception {
    /**
     * Serial ID needed when creating exceptions.
     */
    private static final long serialVersionUID = 59L;

    /**
     * Constructor to create a new InvalidAdditionException exception.
     *
     * @return InvalidAdditionException
     */
    public InvalidAdditionException() {
        super("Can not add parent " +
                "directory as the child of "
                        + "a sub directory.");
    }
}
