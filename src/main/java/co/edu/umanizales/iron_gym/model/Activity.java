package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a fitness activity or class at the gym.
 * Activities are scheduled and led by instructors.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Activity {
    private String id;
    private String name;
    private String description;
    private String instructorId;
    private LocalDateTime dateTime;
    private Integer duration; // in minutes
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private String activityType;
    
    /**
     * Creates an Activity from a CSV line
     * Format: id,name,description,instructorId,dateTime,duration,maxCapacity,currentEnrollment,activityType
     */
    public static Activity fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        
        String[] parts = csvLine.split(",");
        if (parts.length < 9) {
            return null;
        }
        
        try {
            return Activity.builder()
                    .id(parts[0].trim())
                    .name(parts[1].trim())
                    .description(parts[2].trim())
                    .instructorId(parts[3].trim())
                    .dateTime(LocalDateTime.parse(parts[4].trim()))
                    .duration(Integer.valueOf(parts[5].trim()))
                    .maxCapacity(Integer.valueOf(parts[6].trim()))
                    .currentEnrollment(Integer.valueOf(parts[7].trim()))
                    .activityType(parts[8].trim())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Converts Activity to CSV line
     * Format: id,name,description,instructorId,dateTime,duration,maxCapacity,currentEnrollment,activityType
     */
    public String toCsv() {
        return String.join(",",
                id != null ? id : "",
                name != null ? name : "",
                description != null ? description : "",
                instructorId != null ? instructorId : "",
                dateTime != null ? dateTime.toString() : "",
                duration != null ? duration.toString() : "60",
                maxCapacity != null ? maxCapacity.toString() : "20",
                currentEnrollment != null ? currentEnrollment.toString() : "0",
                activityType != null ? activityType : ""
        );
    }
    
    /**
     * Returns the schedule as a formatted string for the DTO
     */
    public String getSchedule() {
        return dateTime != null ? dateTime.toString() : "";
    }
}
