package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

/**
 * Representa un ejercicio que puede ser parte de una rutina de entrenamiento.
 * Contiene detalles sobre repeticiones y series.
 */
public class Exercise { // Inicio de la clase Exercise - maneja ejercicios del gimnasio
    private String name; // Nombre del ejercicio (ej: "Press de banca", "Sentadillas")
    private int repetitions; // Número de repeticiones por serie del ejercicio
    private int sets; // Número de series que se deben realizar del ejercicio
    
    public Exercise() { // Constructor vacío (por defecto)
        // No inicializa nada, permite crear objetos Exercise sin parámetros
    }
    
    // Constructor completo con todos los campos
    public Exercise(String name, int repetitions, int sets) {
        this.name = name; // Asigna el nombre del ejercicio proporcionado
        this.repetitions = repetitions; // Asigna el número de repeticiones proporcionado
        this.sets = sets; // Asigna el número de series proporcionado
    }
    
    // Getter para obtener el nombre del ejercicio
    public String getName() {
        return name; // Retorna el valor del campo name
    }
    
    // Setter para establecer el nombre del ejercicio
    public void setName(String name) {
        this.name = name; // Asigna el valor proporcionado al campo name
    }
    
    // Getter para obtener el número de repeticiones
    public int getRepetitions() {
        return repetitions; // Retorna el valor del campo repetitions
    }
    
    // Setter para establecer el número de repeticiones
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions; // Asigna el valor proporcionado al campo repetitions
    }
    
    // Getter para obtener el número de series
    public int getSets() {
        return sets; // Retorna el valor del campo sets
    }
    
    // Setter para establecer el número de series
    public void setSets(int sets) {
        this.sets = sets; // Asigna el valor proporcionado al campo sets
    }
} // Fin de la clase Exercise
