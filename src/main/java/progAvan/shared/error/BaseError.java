package progAvan.shared.error;

/**
 * Clase base para representar errores en la aplicación
 */
public abstract class BaseError {
    private final String code;
    private final String message;

    protected BaseError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", code, message);
    }
}
