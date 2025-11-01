package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase base que representa a una persona en el sistema del gimnasio.
 * Contiene información común como identificación, nombre, email y teléfono.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private String id;
    private String name;
    private String email;
    private String phone;
}
