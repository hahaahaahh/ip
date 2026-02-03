package chimi;

/**
 * Represents exceptions specific to the Chimi application.
 */
public class ChimiException extends Exception {
    /**
     * Constructs a ChimiException with the specified error message.
     *
     * @param message The error message.
     */
    public ChimiException(String message) {
        super(message);
    }
}