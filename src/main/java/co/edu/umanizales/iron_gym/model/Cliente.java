package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Clase que representa a un cliente del gimnasio.
 * Hereda de Person y agrega información de membresía.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Person {
    private Membership membership;
    
    @Override
    public String getRole() {
        return "CLIENT";
    }
}
