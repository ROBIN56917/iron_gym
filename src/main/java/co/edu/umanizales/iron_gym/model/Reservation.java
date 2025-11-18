package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas

/**
 * Representa una reserva para una clase grupal en el gimnasio.
 * Rastrea las reservas de los clientes y su estado.
 */
public class Reservation { // Inicio de la clase Reservation - maneja reservas del gimnasio
    private String id; // Identificador único de la reserva
    private LocalDateTime dateTime; // Fecha y hora en que se realizó la reserva
    private Client client; // Referencia al cliente que hizo la reserva
    private GroupClass groupClass; // Referencia a la clase grupal reservada
    
    public Reservation() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Reservation sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Reservation(String id, LocalDateTime dateTime, Client client, GroupClass groupClass) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.dateTime = dateTime; // Asigna la fecha y hora proporcionadas
        this.client = client; // Asigna el cliente proporcionado
        this.groupClass = groupClass; // Asigna la clase grupal proporcionada
    }
    
    // Getter para obtener el ID de la reserva
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID de la reserva
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener la fecha y hora de la reserva
    public LocalDateTime getDateTime() {
        return dateTime; // Retorna el valor del campo dateTime
    }
    
    // Setter para establecer la fecha y hora de la reserva
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime; // Asigna el valor proporcionado al campo dateTime
    }
    
    // Getter para obtener el cliente que hizo la reserva
    public Client getClient() {
        return client; // Retorna el valor del campo client
    }
    
    // Setter para establecer el cliente que hizo la reserva
    public void setClient(Client client) {
        this.client = client; // Asigna el valor proporcionado al campo client
    }
    
    // Getter para obtener la clase grupal reservada
    public GroupClass getGroupClass() {
        return groupClass; // Retorna el valor del campo groupClass
    }
    
    // Setter para establecer la clase grupal reservada
    public void setGroupClass(GroupClass groupClass) {
        this.groupClass = groupClass; // Asigna el valor proporcionado al campo groupClass
    }
} // Fin de la clase Reservation
