package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    private String nombre;
    private int repeticiones;
    private int series;
    private String descansoEntreSeries;
    private String instrucciones;
    private String grupoMuscular;
}
