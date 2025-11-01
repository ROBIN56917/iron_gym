package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseGrupal {
    private String id;
    private String nombre;
    private int cupoMaximo;
    private LocalTime horario;
    private Entrenador entrenador;
    private List<Cliente> clientesInscritos;
    
    public boolean agregarCliente(Cliente cliente) {
        if (clientesInscritos == null) {
            clientesInscritos = new ArrayList<>();
        }
        if (clientesInscritos.size() < cupoMaximo) {
            return clientesInscritos.add(cliente);
        }
        return false;
    }
}
