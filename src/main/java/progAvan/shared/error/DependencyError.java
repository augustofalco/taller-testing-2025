package progAvan.shared.error;

/**
 * Error para operaciones que dependen de datos relacionados o condiciones
 * especiales
 */
public class DependencyError extends BaseError {
    private static final String DEFAULT_CODE = "ERR-DEP";

    public DependencyError(String entity, String dependency) {
        super(DEFAULT_CODE,
                String.format("La operación no puede completarse porque %s depende de %s", entity, dependency));
    }

    public DependencyError(String message) {
        super(DEFAULT_CODE, message);
    }
}
