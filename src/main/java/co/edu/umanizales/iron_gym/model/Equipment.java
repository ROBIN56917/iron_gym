package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

/**
 * Representa equipos del gimnasio como máquinas, pesas, etc.
 * Rastrea el tipo y estado de cada equipo.
 */
public class Equipment { // Inicio de la clase Equipment - maneja equipos del gimnasio
    private String id; // Identificador único del equipo
    private String type; // Tipo de equipo (ej: "Máquina de pesas", "Cinta de correr")
    private String status; // Estado del equipo (ej: "AVAILABLE", "MAINTENANCE", "OUT_OF_SERVICE")
    
    public Equipment() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Equipment sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Equipment(String id, String type, String status) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.type = type; // Asigna el tipo de equipo proporcionado
        this.status = status; // Asigna el estado del equipo proporcionado
    }
    
    // Getter para obtener el ID del equipo
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID del equipo
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener el tipo de equipo
    public String getType() {
        return type; // Retorna el valor del campo type
    }
    
    // Setter para establecer el tipo de equipo
    public void setType(String type) {
        this.type = type; // Asigna el valor proporcionado al campo type
    }
    
    // Getter para obtener el estado del equipo
    public String getStatus() {
        return status; // Retorna el valor del campo status
    }
    
    // Setter para establecer el estado del equipo
    public void setStatus(String status) {
        this.status = status; // Asigna el valor proporcionado al campo status
    }
    
    // Método para verificar si el equipo está disponible para uso
    public boolean isAvailable() { // Retorna true si el equipo está disponible, false si no
        if (status != null && status.equals("AVAILABLE")) { // Verifica si el estado no es nulo y es "AVAILABLE"
            return true; // El equipo está disponible para uso
        } else { // Si el estado es nulo o diferente de "AVAILABLE"
            return false; // El equipo no está disponible
        }
    }
} // Fin de la clase Equipment
