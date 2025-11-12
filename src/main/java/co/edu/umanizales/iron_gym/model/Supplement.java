package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents nutritional supplements sold at the gym.
 * Tracks inventory, pricing, and product information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplement {
    private String id;
    private String name;
    private String brand;
    private double price;
    private String description;
    private int availableQuantity;
    private String type; // Example: protein, creatine, vitamins, etc.
}
