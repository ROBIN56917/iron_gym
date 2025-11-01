package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Clase que representa una membresía en el sistema del gimnasio.
 * Contiene información sobre el tipo de membresía, fechas de vigencia y precio.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {
    @EqualsAndHashCode.Include
    private String id;
    
    private MembershipType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
}
