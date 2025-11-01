package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membresia {
    private String id;
    private TipoMembresia tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precio;
}
