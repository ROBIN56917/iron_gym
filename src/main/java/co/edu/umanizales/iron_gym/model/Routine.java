package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rutina {
    private String id;
    private String objetivo;
    private List<Exercise> exercises;
    private String nivelDificultad;
    private int duracionMinutos;
    
    public void agregarEjercicio(Exercise exercise) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.add(exercise);
    }
}
