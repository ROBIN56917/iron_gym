package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String id;
    private double amount;
    private LocalDateTime date;
    private PaymentMethod paymentMethod;
    private String referencia;
    private String description;
}
