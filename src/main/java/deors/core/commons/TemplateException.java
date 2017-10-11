package deors.core.commons;

/**
 * Generic exception class for template processing.
 *
 * <p>This exception class is used to identify errors occurring during template processing
 * outside other common input/output or runtime exceptions.
 *
 * @author deors
 * @version 1.0
 */
public final class TemplateException
    extends Exception {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -3713111696958558771L;

    /**
     * Exception constructor.
     */
    public TemplateException() {
        super();
    }

    /**
     * Exception constructor.
     *
     * @param message the exception message
     */
    public TemplateException(String message) {
        super(message);
    }

    /**
     * Exception constructor.
     *
     * @param cause the exception cause
     */
    public TemplateException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception constructor.
     *
     * @param message the exception message
     * @param cause the exception cause
     */
    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
