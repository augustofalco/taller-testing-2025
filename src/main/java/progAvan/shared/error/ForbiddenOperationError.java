package progAvan.shared.error;

/**
 * Error para operaciones prohibidas o no autorizadas
 */
public class ForbiddenOperationError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-FORB";

    public ForbiddenOperationError(String operation, String reason) {
        super(DEFAULT_CODE, String.format("Operación '%s' no permitida: %s", operation, reason));
    }

    public ForbiddenOperationError(String message) {
        super(DEFAULT_CODE, message);
    }
}
