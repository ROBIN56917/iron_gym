package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suplemento {
    private String id;
    private String nombre;
    private String marca;
    private double precio;
    private String descripcion;
    private int cantidadDisponible;
    private String tipo; // Ejemplo: proteina, creatina, vitaminas, etc.
}
