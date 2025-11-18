package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

/**
 * Representa un entrenador que trabaja en el gimnasio.
 * Los entrenadores pueden ser asignados para enseñar clases grupales.
 */
public class Trainer extends Person { // Inicio de la clase Trainer - hereda de Person
    private List<GroupClass> assignedClasses; // Lista de clases grupales asignadas al entrenador
    
    public Trainer() { // Constructor vacío (por defecto)
        super(); // Llama al constructor de la clase padre Person
        this.assignedClasses = new ArrayList<>(); // Inicializa la lista de clases asignadas como ArrayList vacío
    }
    
    // Constructor con datos básicos del entrenador
    public Trainer(String id, String name, String email, String phone, String identification) {
        super(id, name, email, phone, identification); // Llama al constructor de Person con datos básicos
        this.assignedClasses = new ArrayList<>(); // Inicializa la lista de clases asignadas como ArrayList vacío
    }
    
    // Getter para obtener la lista de clases asignadas
    public List<GroupClass> getAssignedClasses() {
        return assignedClasses; // Retorna la lista de clases grupales asignadas
    }
    
    // Setter para establecer la lista de clases asignadas
    public void setAssignedClasses(List<GroupClass> assignedClasses) {
        this.assignedClasses = assignedClasses; // Asigna la lista proporcionada al campo assignedClasses
    }
    
    // Método para agregar una clase a la lista de clases asignadas
    public void addClass(GroupClass groupClass) {
        if (assignedClasses == null) { // Verifica si la lista no ha sido inicializada
            assignedClasses = new ArrayList<>(); // Inicializa la lista si es nula
        }
        
        boolean exists = false; // Bandera para verificar si la clase ya existe
        for (int i = 0; i < assignedClasses.size(); i++) { // Recorre la lista de clases asignadas
            if (assignedClasses.get(i).equals(groupClass)) { // Compara cada clase con la clase a agregar
                exists = true; // Marca como existente si encuentra coincidencia
                break; // Sale del bucle porque ya encontró la clase
            }
        }
        
        if (!exists) { // Si la clase no existe en la lista
            assignedClasses.add(groupClass); // Agrega la nueva clase a la lista
        }
    }
    
    @Override // Anotación que indica que sobrescribe un método de la clase padre
    public String getRole() {
        return "TRAINER"; // Retorna el rol específico para entrenador
    }
} // Fin de la clase Trainer
