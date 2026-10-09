package backend;

/**
 * Thrown by the service layer when the DATABASE itself fails (server down, SQL error).
 * Normal "no, that's not allowed" outcomes are returned as false/null instead,
 * so the GUI can tell "your application was rejected" apart from "the database is broken".
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
