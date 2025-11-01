package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a reservation for a group class in the gym.
 * Tracks client reservations and their status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private String id;
    private LocalDateTime dateTime;
    private Client client;
    private GroupClass groupClass;
    private boolean active;
    private LocalDateTime createdAt;

    /**
     * Cancels the reservation.
     * @return true if the reservation was active and is now cancelled, false otherwise
     */
    public boolean cancel() {
        if (active) {
            active = false;
            return true;
        }
        return false;
    }
}
