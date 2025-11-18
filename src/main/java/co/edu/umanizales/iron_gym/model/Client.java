package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

/**
 * Representa un cliente del gimnasio.
 * Hereda de Person y agrega información de membresía.
 */
public class Client extends Person { // Inicio de la clase Client - hereda de Person
    private Membership membership; // Membresía actual del cliente (puede ser null si no tiene)
    
    public Client() { // Constructor vacío (por defecto)
        super(); // Llama al constructor de la clase padre Person
    }
    
    // Constructor completo con todos los campos incluyendo membresía
    public Client(String id, String name, String email, String phone, String identification, Membership membership) {
        super(id, name, email, phone, identification); // Llama al constructor de Person con datos básicos
        this.membership = membership; // Asigna la membresía proporcionada
    }
    
    // Getter para obtener la membresía del cliente
    public Membership getMembership() {
        return membership; // Retorna el valor del campo membership
    }
    
    // Setter para establecer la membresía del cliente
    public void setMembership(Membership membership) {
        this.membership = membership; // Asigna el valor proporcionado al campo membership
    }
    
    @Override // Anotación que indica que sobrescribe un método de la clase padre
    public String getRole() {
        return "CLIENT"; // Retorna el rol específico para cliente
    }
    
    // Método para verificar si el cliente tiene membresía activa
    public boolean hasMembership() {
        if (membership != null) { // Si el campo membership no es nulo
            return true; // El cliente tiene membresía
        } else { // Si el campo membership es nulo
            return false; // El cliente no tiene membresía
        }
    }
} // Fin de la clase Client
