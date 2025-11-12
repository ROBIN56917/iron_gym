package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents when a client attends a group class.
 * Tracks attendance records for gym classes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private String id;
    private LocalDateTime checkInDateTime;
    private Client client;
    private GroupClass groupClass;
    private boolean present;
}
