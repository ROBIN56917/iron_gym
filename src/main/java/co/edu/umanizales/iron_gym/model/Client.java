package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents a client of the gym.
 * Inherits from Person and adds membership information.
 * Clients can attend classes and have routines.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends Person {
    private Membership membership;
    
    /**
     * Returns the role of this person in the gym system.
     * @return "CLIENT" as the role
     */
    @Override
    public String getRole() {
        return "CLIENT";
    }
}
