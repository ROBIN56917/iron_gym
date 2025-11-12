package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an exercise that can be part of a workout routine.
 * Contains details about repetitions, sets, and instructions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    private String name;
    private int repetitions;
    private int sets;
    private String restBetweenSets;
    private String instructions;
    private String muscleGroup;
}
