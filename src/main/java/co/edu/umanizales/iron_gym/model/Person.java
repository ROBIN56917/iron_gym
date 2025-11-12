package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Base class that represents a person in the gym system.
 * Contains common information like identification, name, email and phone.
 * This is a parent class for Client and Trainer.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String email;
    private String phone;
    
    /**
     * Returns the role of this person in the gym system.
     * Child classes should override this method.
     * @return the role as a string
     */
    public String getRole() {
        return "PERSON";
    }
}
