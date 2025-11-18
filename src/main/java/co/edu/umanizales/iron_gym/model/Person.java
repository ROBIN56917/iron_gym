package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import jakarta.validation.constraints.Email; // Importa validación para formato de email
import jakarta.validation.constraints.NotBlank; // Importa validación para campos no vacíos
import jakarta.validation.constraints.Pattern; // Importa validación para patrones regex
import jakarta.validation.constraints.Size; // Importa validación para tamaño de cadenas

/**
 * Clase base que representa una persona en el sistema del gimnasio.
 * Contiene información común como identificación, nombre, email y teléfono.
 * Esta es una clase padre para Client y Trainer.
 */
public class Person { // Inicio de la clase Person - clase base para personas del gimnasio
    private String id; // Identificador único de la persona (generado automáticamente)
    
    // Campo nombre con validaciones
    @NotBlank(message = "El nombre es obligatorio") // No puede ser nulo ni vacío
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres") // Límite de longitud
    private String name; // Nombre completo de la persona
    
    // Campo email con validaciones
    @NotBlank(message = "El email es obligatorio") // No puede ser nulo ni vacío
    @Email(message = "El email debe tener un formato válido") // Debe tener formato email@dominio.com
    @Size(max = 150, message = "El email no puede exceder 150 caracteres") // Límite máximo de caracteres
    private String email; // Correo electrónico de la persona
    
    // Campo teléfono con validaciones
    @NotBlank(message = "El teléfono es obligatorio") // No puede ser nulo ni vacío
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos") // Solo números, 10 dígitos
    private String phone; // Número de teléfono de la persona
    
    // Campo identificación con validaciones
    @NotBlank(message = "La identificación es obligatoria") // No puede ser nulo ni vacío
    @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres") // Rango de longitud
    private String identification; // Número de identificación único de la persona
    
    public Person() {
    }
    
    public Person(String id, String name, String email, String phone, String identification) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.identification = identification;
    }
    
    // Constructor para creación (sin ID)
    public Person(String name, String email, String phone, String identification) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.identification = identification;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getIdentification() {
        return identification;
    }
    
    public void setIdentification(String identification) {
        this.identification = identification;
    }
    
    public String getRole() {
        return "PERSON";
    }
}
