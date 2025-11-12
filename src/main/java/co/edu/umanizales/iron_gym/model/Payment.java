package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a payment made by a client.
 * Tracks payment amount, method, and transaction details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String id;
    private double amount;
    private LocalDateTime paymentDateTime;
    private PaymentMethod paymentMethod;
    private String reference;
    private String description;
}
