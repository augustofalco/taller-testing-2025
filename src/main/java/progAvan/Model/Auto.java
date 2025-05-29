package progAvan.Model;

import java.util.regex.Matcher;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "auto")
@Getter
@Setter
public class Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "modelo_id", referencedColumnName = "id")
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

    @Column(unique = true)
    @Pattern(regexp = "([A-Z]{3}\\d{3})|([A-Z]{2}\\d{3}[A-Z]{2})", message = "El formato de la patente no es válido (AAA000 o AA000AA)")
    private String patente;

    private String anio;

    private boolean estado;

    @Nullable()
    private boolean habilitado;

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean validarPatente(String patente) {
        if (patente == null) {
            return false;
        }
        // Patrón para patentes nuevas: AA000AA
        String patronNuevo = "[A-Z]{2}\\d{3}[A-Z]{2}";
        // Patrón para patentes viejas: AAA000
        String patronViejo = "[A-Z]{3}\\d{3}";
        // Combinar ambos patrones con el operador OR (|)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patronNuevo + "|" + patronViejo);
        Matcher matcher = pattern.matcher(patente);
        return matcher.matches();
    }

    public boolean getHabilitado() {
        return this.habilitado;
    }
}
