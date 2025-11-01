package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
    private String id;
    private LocalDateTime fecha;
    private Cliente cliente;
    private ClaseGrupal clase;
    private boolean presente;
}
