package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Routine; // Importa la clase Routine del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class RoutineService { // Inicio de la clase RoutineService - contiene la lógica de negocio para rutinas
    private List<Routine> routines; // Lista que almacena todas las rutinas del sistema
    private final String CSV_FILE = "data/routines.csv"; // Ruta del archivo CSV donde se guardan los datos de rutinas

    public RoutineService() { // Constructor de la clase RoutineService
        this.routines = new ArrayList<>(); // Inicializa la lista de rutinas como ArrayList vacío
        loadFromCSV(); // Carga los datos de rutinas desde el archivo CSV
    }

    public List<Routine> getAll() { // Método para obtener todas las rutinas
        return routines; // Retorna la lista de todas las rutinas
    }

    public Routine getById(String id) {
        for (Routine routine : routines) { // Recorre la lista de rutinas
            if (routine.getId().equals(id)) { // Compara el ID de cada rutina con el ID buscado
                return routine; // Retorna la rutina encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna rutina con ese ID
    }

    public Routine create(Routine routine) { // Método para crear una nueva rutina
        routines.add(routine); // Agrega la nueva rutina a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return routine; // Retorna la rutina creada
    }

    public Routine update(String id, Routine updatedRoutine) { // Método para actualizar una rutina existente
        for (int i = 0; i < routines.size(); i++) { // Recorre la lista de rutinas por índice
            if (routines.get(i).getId().equals(id)) { // Si encuentra la rutina por ID
                updatedRoutine.setId(id); // Mantiene el mismo ID en la rutina actualizada
                routines.set(i, updatedRoutine); // Reemplaza la rutina en la posición i con la actualizada
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedRoutine; // Retorna la rutina actualizada
            }
        }
        return null; // Retorna null si no encontró la rutina para actualizar
    }

    public boolean delete(String id) { // Método para eliminar una rutina por ID
        for (int i = 0; i < routines.size(); i++) { // Recorre la lista de rutinas por índice
            if (routines.get(i).getId().equals(id)) { // Si encuentra la rutina por ID
                routines.remove(i); // Elimina la rutina de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró la rutina para eliminar
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,objective\n"); // Escribe la cabecera del CSV
            
            for (Routine routine : routines) { // Recorre todas las rutinas de la lista
                writer.write(routine.getId() + "," + // Escribe el ID de la rutina
                           routine.getObjective() + "\n"); // Escribe el objetivo y salto de línea
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
            String line = reader.readLine(); // Lee y salta la línea de cabecera
            
            while ((line = reader.readLine()) != null) { // Mientras haya líneas por leer
                String[] data = line.split(","); // Divide la línea por comas para obtener los datos
                if (data.length == 2) { // Verifica que la línea tenga exactamente 2 campos
                    Routine routine = new Routine(data[0], data[1]); // Crea rutina con ID y objetivo
                    routines.add(routine); // Agrega la rutina a la lista
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase RoutineService
