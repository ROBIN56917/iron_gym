package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Representa cuando un cliente asiste a una clase grupal.
 * Registra los registros de asistencia para las clases del gimnasio.
 */
public class Attendance { // Inicio de la clase Attendance - maneja registros de asistencia
    private String id; // Identificador único del registro de asistencia
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime dateTime; // Fecha y hora exacta de la asistencia
    private Client client; // Referencia al cliente que asistió
    private GroupClass groupClass; // Referencia a la clase grupal a la que asistió
    
    public Attendance() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Attendance sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Attendance(String id, LocalDateTime dateTime, Client client, GroupClass groupClass) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.dateTime = dateTime; // Asigna la fecha y hora proporcionadas
        this.client = client; // Asigna el cliente proporcionado
        this.groupClass = groupClass; // Asigna la clase grupal proporcionada
    }
    
    // Getter para obtener el ID del registro de asistencia
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID del registro de asistencia
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener la fecha y hora de la asistencia
    public LocalDateTime getDateTime() {
        return dateTime; // Retorna el valor del campo dateTime
    }
    
    // Setter para establecer la fecha y hora de la asistencia
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime; // Asigna el valor proporcionado al campo dateTime
    }
    
    // Getter para obtener el cliente que asistió
    public Client getClient() {
        return client; // Retorna el valor del campo client
    }
    
    // Setter para establecer el cliente que asistió
    public void setClient(Client client) {
        this.client = client; // Asigna el valor proporcionado al campo client
    }
    
    // Getter para obtener la clase grupal
    public GroupClass getGroupClass() {
        return groupClass; // Retorna el valor del campo groupClass
    }
    
    // Setter para establecer la clase grupal
    public void setGroupClass(GroupClass groupClass) {
        this.groupClass = groupClass; // Asigna el valor proporcionado al campo groupClass
    }
} // Fin de la clase Attendance
