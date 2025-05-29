package progAvan.shared.error;

/**
 * Error para datos de validación incorrectos
 */
public class ValidationError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-VAL";

    public ValidationError(String field, String reason) {
        super(DEFAULT_CODE, String.format("Error de validación en campo '%s': %s", field, reason));
    }

    public ValidationError(String message) {
        super(DEFAULT_CODE, message);
    }
}
