package progAvan.shared.error;

/**
 * Error para operaciones duplicadas
 */
public class DuplicateError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-DUP";

    public DuplicateError(String entity, String field, String value) {
        super(DEFAULT_CODE, String.format("El %s con %s '%s' ya existe en el sistema", entity, field, value));
    }

    public DuplicateError(String message) {
        super(DEFAULT_CODE, message);
    }
}
