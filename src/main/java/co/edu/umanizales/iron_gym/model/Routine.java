package co.edu.umanizales.iron_gym.model; // Declara el paquete donde se encuentra esta clase

import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

/**
 * Representa una rutina de entrenamiento para clientes del gimnasio.
 * Contiene una lista de ejercicios e información del objetivo.
 */
public class Routine { // Inicio de la clase Routine - maneja rutinas de entrenamiento
    private String id; // Identificador único de la rutina
    private String objective; // Objetivo de la rutina (ej: "Pérdida de peso", "Ganancia muscular")
    private List<Exercise> exercises; // Lista de ejercicios que componen la rutina
    
    public Routine() { // Constructor vacío (por defecto)
        this.exercises = new ArrayList<>(); // Inicializa la lista de ejercicios como ArrayList vacío
    }
    
    // Constructor con ID y objetivo
    public Routine(String id, String objective) {
        this.id = id; // Asigna el ID proporcionado al campo id de este objeto
        this.objective = objective; // Asigna el objetivo proporcionado
        this.exercises = new ArrayList<>(); // Inicializa la lista de ejercicios como ArrayList vacío
    }
    
    // Getter para obtener el ID de la rutina
    public String getId() {
        return id; // Retorna el valor del campo id
    }
    
    // Setter para establecer el ID de la rutina
    public void setId(String id) {
        this.id = id; // Asigna el valor proporcionado al campo id
    }
    
    // Getter para obtener el objetivo de la rutina
    public String getObjective() {
        return objective; // Retorna el valor del campo objective
    }
    
    // Setter para establecer el objetivo de la rutina
    public void setObjective(String objective) {
        this.objective = objective; // Asigna el valor proporcionado al campo objective
    }
    
    // Getter para obtener la lista de ejercicios
    public List<Exercise> getExercises() {
        return exercises; // Retorna la lista de ejercicios de la rutina
    }
    
    // Setter para establecer la lista de ejercicios
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises; // Asigna la lista proporcionada al campo exercises
    }
    
    // Método para agregar un ejercicio a la rutina
    public void addExercise(Exercise exercise) {
        if (exercises == null) { // Verifica si la lista no ha sido inicializada
            exercises = new ArrayList<>(); // Inicializa la lista si es nula
        }
        exercises.add(exercise); // Agrega el ejercicio a la lista de ejercicios
    }
} // Fin de la clase Routine
