package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Representa un pago realizado por un cliente.
 * Rastrea el monto del pago, método y detalles de la transacción.
 */
public class Payment { // Inicio de la clase Payment - maneja pagos del gimnasio
    private String id; // Identificador único de la transacción de pago
    @Positive(message = "El monto debe ser positivo")
    private double amount; // Monto total del pago realizado
    @NotNull(message = "La fecha y hora del pago es obligatoria")
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime dateTime; // Fecha y hora exacta en que se realizó el pago
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod; // Método de pago utilizado (ej: "Tarjeta", "Efectivo", "Transferencia")
    @NotBlank(message = "El cliente es obligatorio")
    private String clientId; // Identificador del cliente asociado al pago
    
    public Payment() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Payment sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Payment(String id, double amount, LocalDateTime dateTime, String paymentMethod) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.amount = amount; // Asigna el monto del pago proporcionado
        this.dateTime = dateTime; // Asigna la fecha y hora proporcionadas
        this.paymentMethod = paymentMethod; // Asigna el método de pago proporcionado
    }
    
    // Constructor con cliente asociado
    public Payment(String id, double amount, LocalDateTime dateTime, String paymentMethod, String clientId) {
        this.id = id;
        this.amount = amount;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
        this.clientId = clientId;
    }
    
    // Getter para obtener el ID del pago
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID del pago
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener el monto del pago
    public double getAmount() {
        return amount; // Retorna el valor del campo amount
    }
    
    // Setter para establecer el monto del pago
    public void setAmount(double amount) {
        this.amount = amount; // Asigna el valor proporcionado al campo amount
    }
    
    // Getter para obtener la fecha y hora del pago
    public LocalDateTime getDateTime() {
        return dateTime; // Retorna el valor del campo dateTime
    }
    
    // Setter para establecer la fecha y hora del pago
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime; // Asigna el valor proporcionado al campo dateTime
    }
    
    // Getter para obtener el método de pago
    public String getPaymentMethod() {
        return paymentMethod; // Retorna el valor del campo paymentMethod
    }
    
    // Setter para establecer el método de pago
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod; // Asigna el valor proporcionado al campo paymentMethod
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
} // Fin de la clase Payment
