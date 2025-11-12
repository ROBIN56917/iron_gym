package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workout routine for gym clients.
 * Contains a list of exercises with difficulty and duration information.
 * Trainers can create routines for their clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routine {
    private String id;
    private String objective;
    private List<Exercise> exercises;
    private String difficultyLevel;
    private int durationMinutes;
    
    /**
     * Adds an exercise to this routine.
     * @param exercise the exercise to add
     */
    public void addExercise(Exercise exercise) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.add(exercise);
    }
}
