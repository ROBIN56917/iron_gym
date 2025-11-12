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
 * Represents a gym membership in the system.
 * Contains information about the membership type, validity dates and price.
 * Each client can have one active membership.
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
    
    private MembershipTypeEnum type;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
}
