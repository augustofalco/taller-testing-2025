package progAvan.shared.error;

/**
 * Error para errores internos del servidor
 */
public class InternalError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-INT";

    public InternalError(Exception ex) {
        super(DEFAULT_CODE, String.format("Error interno del servidor: %s", ex.getMessage()));
    }

    public InternalError(String message) {
        super(DEFAULT_CODE, message);
    }
}
