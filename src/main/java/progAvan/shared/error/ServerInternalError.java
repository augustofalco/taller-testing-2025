package progAvan.shared.error;

/**
 * Error para errores internos del servidor
 */
public class ServerInternalError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-INT";

    public ServerInternalError(Exception ex) {
        super(DEFAULT_CODE, String.format("Error interno del servidor: %s", ex.getMessage()));
    }

    public ServerInternalError(String message) {
        super(DEFAULT_CODE, message);
    }
}
