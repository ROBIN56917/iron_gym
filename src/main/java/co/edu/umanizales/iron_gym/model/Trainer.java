package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trainer who works at the gym.
 * Trainers can be assigned to teach group classes.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Trainer extends Person {
    private List<GroupClass> assignedClasses;
    private String specialization;
    
    /**
     * Adds a group class to this trainer's schedule.
     * @param groupClass the class to add
     */
    public void addClass(GroupClass groupClass) {
        if (assignedClasses == null) {
            assignedClasses = new ArrayList<>();
        }
        if (!assignedClasses.contains(groupClass)) {
            assignedClasses.add(groupClass);
        }
    }
    
    /**
     * Returns the role of this person in the gym system.
     * @return "TRAINER" as the role
     */
    @Override
    public String getRole() {
        return "TRAINER";
    }
}
