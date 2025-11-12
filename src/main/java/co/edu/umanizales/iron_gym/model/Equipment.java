package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents gym equipment like machines, weights, etc.
 * Tracks the type, status and description of each equipment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    private String id;
    private String type;
    private EquipmentStatus status;
    private String description;
}
