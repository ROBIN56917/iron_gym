package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipType {
    private String id;
    private MembershipTypeEnum type;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;

}
