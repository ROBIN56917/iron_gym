package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

/**
 * Representa suplementos nutricionales vendidos en el gimnasio.
 * Rastrea precios e información del producto.
 */
public class Supplement { // Inicio de la clase Supplement - maneja suplementos del gimnasio
    private String id; // Identificador único del suplemento
    private String name; // Nombre del suplemento (ej: "Proteína Whey", "Creatina")
    private String brand; // Marca del suplemento (ej: "Optimum Nutrition", "MuscleTech")
    private double price; // Precio de venta del suplemento
    
    public Supplement() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Supplement sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Supplement(String id, String name, String brand, double price) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.name = name; // Asigna el nombre del suplemento proporcionado
        this.brand = brand; // Asigna la marca del suplemento proporcionada
        this.price = price; // Asigna el precio del suplemento proporcionado
    }
    
    // Getter para obtener el ID del suplemento
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID del suplemento
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener el nombre del suplemento
    public String getName() {
        return name; // Retorna el valor del campo name
    }
    
    // Setter para establecer el nombre del suplemento
    public void setName(String name) {
        this.name = name; // Asigna el valor proporcionado al campo name
    }
    
    // Getter para obtener la marca del suplemento
    public String getBrand() {
        return brand; // Retorna el valor del campo brand
    }
    
    // Setter para establecer la marca del suplemento
    public void setBrand(String brand) {
        this.brand = brand; // Asigna el valor proporcionado al campo brand
    }
    
    // Getter para obtener el precio del suplemento
    public double getPrice() {
        return price; // Retorna el valor del campo price
    }
    
    // Setter para establecer el precio del suplemento
    public void setPrice(double price) {
        this.price = price; // Asigna el valor proporcionado al campo price
    }
} // Fin de la clase Supplement
