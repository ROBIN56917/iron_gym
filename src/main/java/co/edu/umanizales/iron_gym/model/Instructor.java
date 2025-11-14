package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents an instructor who works at the gym.
 * Instructors can teach activities and have specializations.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Instructor extends Person {
    private String specialization;
    private Boolean certified;
    
    /**
     * Creates an Instructor from a CSV line
     * Format: id,name,email,phone,specialization,certified
     */
    public static Instructor fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            return null;
        }
        
        try {
            return Instructor.builder()
                    .id(parts[0].trim())
                    .name(parts[1].trim())
                    .email(parts[2].trim())
                    .phone(parts[3].trim())
                    .specialization(parts[4].trim())
                    .certified(Boolean.valueOf(parts[5].trim()))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Converts Instructor to CSV line
     * Format: id,name,email,phone,specialization,certified
     */
    public String toCsv() {
        return String.join(",",
                getId() != null ? getId() : "",
                getName() != null ? getName() : "",
                getEmail() != null ? getEmail() : "",
                getPhone() != null ? getPhone() : "",
                specialization != null ? specialization : "",
                certified != null ? certified.toString() : "false"
        );
    }
    
    /**
     * Returns the role of this person in the gym system.
     * @return "INSTRUCTOR" as the role
     */
    @Override
    public String getRole() {
        return "INSTRUCTOR";
    }
}
