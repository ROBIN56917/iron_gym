package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Exercise; // Importa la clase Exercise del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class ExerciseService { // Inicio de la clase ExerciseService - contiene la lógica de negocio para ejercicios
    private List<Exercise> exercises; // Lista que almacena todos los ejercicios del sistema
    private final String CSV_FILE = "data/exercises.csv"; // Ruta del archivo CSV donde se guardan los datos de ejercicios

    public ExerciseService() { // Constructor de la clase ExerciseService
        this.exercises = new ArrayList<>(); // Inicializa la lista de ejercicios como ArrayList vacío
        loadFromCSV(); // Carga los datos de ejercicios desde el archivo CSV
    }

    public List<Exercise> getAll() { // Método para obtener todos los ejercicios
        return exercises; // Retorna la lista de todos los ejercicios
    }

    public Exercise getByName(String name) {
        for (Exercise exercise : exercises) { // Recorre la lista de ejercicios
            if (exercise.getName().equals(name)) { // Compara el nombre de cada ejercicio con el nombre buscado
                return exercise; // Retorna el ejercicio encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún ejercicio con ese nombre
    }

    public Exercise create(Exercise exercise) { // Método para crear un nuevo ejercicio
        exercises.add(exercise); // Agrega el nuevo ejercicio a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return exercise; // Retorna el ejercicio creado
    }

    public Exercise update(String name, Exercise updatedExercise) { // Método para actualizar un ejercicio existente
        for (int i = 0; i < exercises.size(); i++) { // Recorre la lista de ejercicios por índice
            if (exercises.get(i).getName().equals(name)) { // Si encuentra el ejercicio por nombre
                updatedExercise.setName(name); // Mantiene el mismo nombre en el ejercicio actualizado
                exercises.set(i, updatedExercise); // Reemplaza el ejercicio en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedExercise; // Retorna el ejercicio actualizado
            }
        }
        return null; // Retorna null si no encontró el ejercicio para actualizar
    }

    public boolean delete(String name) { // Método para eliminar un ejercicio por nombre
        for (int i = 0; i < exercises.size(); i++) { // Recorre la lista de ejercicios por índice
            if (exercises.get(i).getName().equals(name)) { // Si encuentra el ejercicio por nombre
                exercises.remove(i); // Elimina el ejercicio de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró el ejercicio para eliminar
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("name,repetitions,sets\n"); // Escribe la cabecera del CSV
            
            for (Exercise exercise : exercises) { // Recorre todos los ejercicios de la lista
                writer.write(exercise.getName() + "," + // Escribe el nombre del ejercicio
                           exercise.getRepetitions() + "," + // Escribe las repeticiones del ejercicio
                           exercise.getSets() + "\n"); // Escribe las series y salto de línea
            }
            
            writer.close(); // Cierra el escritor del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }

    private void loadFromCSV() { // Método privado para cargar datos desde archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            if (!file.exists()) { // Si el archivo no existe
                return; // Sale del método sin hacer nada más
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file)); // Crea lector para el archivo
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    Exercise exercise = new Exercise(
                        data[0], 
                        Integer.parseInt(data[1]), 
                        Integer.parseInt(data[2])
                    );
                    exercises.add(exercise);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
