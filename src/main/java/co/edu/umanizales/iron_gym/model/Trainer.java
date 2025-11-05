package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Trainer extends Persona {
    private List<ClaseGrupal> clasesAsignadas;
    private String especializacion;
    
    public Entrenador(String id, String nombre, String email, String telefono, String especializacion) {
        super(id, nombre, email, telefono);
        this.especializacion = especializacion;
        this.clasesAsignadas = new ArrayList<>();
    }
    
    public void agregarClase(ClaseGrupal clase) {
        if (!clasesAsignadas.contains(clase)) {
            clasesAsignadas.add(clase);
        }
    }
    
    @Override
    public String rol() {
        return "ENTRENADOR";
    }
}
