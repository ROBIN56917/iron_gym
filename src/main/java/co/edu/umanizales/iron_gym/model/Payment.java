package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private String id;
    private double monto;
    private LocalDateTime fecha;
    private MetodoPago metodoPago;
    private String referencia;
    private String descripcion;
}
