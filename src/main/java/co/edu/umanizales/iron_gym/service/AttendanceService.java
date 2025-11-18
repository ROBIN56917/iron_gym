package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Attendance; // Importa la clase Attendance del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class AttendanceService { // Inicio de la clase AttendanceService - contiene la lógica de negocio para asistencias
    private List<Attendance> attendances; // Lista que almacena todas las asistencias del sistema
    private final String CSV_FILE = "data/attendances.csv"; // Ruta del archivo CSV donde se guardan los datos de asistencias

    public AttendanceService() { // Constructor de la clase AttendanceService
        this.attendances = new ArrayList<>(); // Inicializa la lista de asistencias como ArrayList vacío
        loadFromCSV(); // Carga los datos de asistencias desde el archivo CSV
    }

    public List<Attendance> getAll() { // Método para obtener todas las asistencias
        return attendances; // Retorna la lista de todas las asistencias
    }

    public Attendance getById(String id) { // Método para buscar asistencia por ID
        for (Attendance attendance : attendances) { // Recorre la lista de asistencias
            if (attendance.getId().equals(id)) { // Compara el ID de cada asistencia con el ID buscado
                return attendance; // Retorna la asistencia encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna asistencia con ese ID
    }

    public Attendance create(Attendance attendance) { // Método para crear una nueva asistencia
        attendances.add(attendance); // Agrega la nueva asistencia a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return attendance; // Retorna la asistencia creada
    }

    public Attendance update(String id, Attendance updatedAttendance) { // Método para actualizar una asistencia existente
        for (int i = 0; i < attendances.size(); i++) { // Recorre la lista de asistencias por índice
            if (attendances.get(i).getId().equals(id)) { // Si encuentra la asistencia por ID
                updatedAttendance.setId(id); // Mantiene el mismo ID en la asistencia actualizada
                attendances.set(i, updatedAttendance); // Reemplaza la asistencia en la posición i con la actualizada
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedAttendance; // Retorna la asistencia actualizada
            }
        }
        return null; // Retorna null si no encontró la asistencia para actualizar
    }

    public boolean delete(String id) { // Método para eliminar una asistencia por ID
        for (int i = 0; i < attendances.size(); i++) { // Recorre la lista de asistencias por índice
            if (attendances.get(i).getId().equals(id)) { // Si encuentra la asistencia por ID
                attendances.remove(i); // Elimina la asistencia de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró la asistencia para eliminar
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,dateTime\n"); // Escribe la cabecera del CSV
            
            for (Attendance attendance : attendances) { // Recorre todas las asistencias de la lista
                writer.write(attendance.getId() + "," + // Escribe el ID de la asistencia
                           attendance.getDateTime() + "\n"); // Escribe la fecha y hora y salto de línea
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
                    Attendance attendance = new Attendance( // Crea asistencia con datos
                        data[0], // ID de la asistencia
                        LocalDateTime.parse(data[1]), // Fecha y hora parseada
                        null, // Cliente (null por simplicidad)
                        null  // Clase grupal (null por simplicidad)
                    );
                    attendances.add(attendance); // Agrega la asistencia a la lista
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase AttendanceService
