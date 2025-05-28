package progAvan.shared.error;

/**
 * Error para entidad no encontrada
 */
public class NotFoundError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-NF";

    public NotFoundError(String entity, String identifier) {
        super(DEFAULT_CODE, String.format("%s con identificador '%s' no encontrado", entity, identifier));
    }

    public NotFoundError(String message) {
        super(DEFAULT_CODE, message);
    }
}
