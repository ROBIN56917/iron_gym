package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {
    private String id;
    private String tipo;
    private EstadoEquipo estado;
    private String descripcion;
}
