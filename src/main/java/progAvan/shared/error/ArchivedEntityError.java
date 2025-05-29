package progAvan.shared.error;

/**
 * Error para entidades que existen pero están archivadas/deshabilitadas
 */
public class ArchivedEntityError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-ARCH";

    public ArchivedEntityError(String entity, String field, String value) {
        super(DEFAULT_CODE,
                String.format("El %s con %s '%s' ya existe en el sistema, pero está archivado", entity, field, value));
    }

    public ArchivedEntityError(String message) {
        super(DEFAULT_CODE, message);
    }
}
