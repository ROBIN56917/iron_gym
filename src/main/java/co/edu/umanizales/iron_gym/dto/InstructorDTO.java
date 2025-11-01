package co.edu.umanizales.iron_gym.dto;

import co.edu.umanizales.iron_gym.model.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para transferencia de datos de Instructor.
 * Se utiliza para exponer solo la información necesaria de un Instructor.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    
    /**
     * Crea un InstructorDTO a partir de una entidad Instructor.
     * @param instructor La entidad Instructor a convertir
     * @return Un nuevo InstructorDTO con los datos del instructor
     */
    public static InstructorDTO from(Instructor instructor) {
        if (instructor == null) {
            return null;
        }
        return new InstructorDTO(
            instructor.getId(),
            instructor.getName(),
            instructor.getEmail(),
            instructor.getPhone(),
            instructor.getSpecialization()
        );
    }
}
