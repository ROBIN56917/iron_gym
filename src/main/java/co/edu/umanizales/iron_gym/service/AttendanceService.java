package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Attendance; // Importa la clase Attendance del paquete model
import co.edu.umanizales.iron_gym.model.Client;
import co.edu.umanizales.iron_gym.model.GroupClass;
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class AttendanceService { // Inicio de la clase AttendanceService - contiene la lógica de negocio para asistencias
    private List<Attendance> attendances; // Lista que almacena todas las asistencias del sistema
    private final String CSV_FILE = "data/attendances.csv"; // Ruta del archivo CSV donde se guardan los datos de asistencias
    private static final DateTimeFormatter CSV_DT = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
    @Autowired
    private ClientService clientService;
    @Autowired
    private GroupClassService groupClassService;

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

    // Verifica si existe una asistencia por ID
    private boolean existsById(String id) {
        return getById(id) != null;
    }

    // Genera el siguiente ID con prefijo 'A' y 2 dígitos (A01, A02, ...)
    private String generateNextId() {
        int max = 0;
        for (Attendance a : attendances) {
            String aid = a.getId();
            if (aid != null && aid.startsWith("A")) {
                try {
                    int num = Integer.parseInt(aid.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("A%02d", max + 1);
    }

    public Attendance create(Attendance attendance) { // Método para crear una nueva asistencia
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance payload is required");
        }
        // Validar client y groupClass
        if (attendance.getClient() == null || attendance.getClient().getId() == null || attendance.getClient().getId().isBlank()) {
            throw new IllegalArgumentException("client.id is required");
        }
        if (attendance.getGroupClass() == null || attendance.getGroupClass().getId() == null || attendance.getGroupClass().getId().isBlank()) {
            throw new IllegalArgumentException("groupClass.id is required");
        }
        // Resolver objetos completos desde servicios
        Client client = clientService.getById(attendance.getClient().getId());
        if (client == null) {
            throw new IllegalArgumentException("Client ID does not exist");
        }
        GroupClass gc = groupClassService.getById(attendance.getGroupClass().getId());
        if (gc == null) {
            throw new IllegalArgumentException("GroupClass ID does not exist");
        }
        // Si no envían ID, generarlo. Si lo envían, validar formato y duplicados
        String id = attendance.getId();
        if (id == null || id.isBlank()) {
            id = generateNextId();
            attendance.setId(id);
        } else {
            // Normalizar y validar duplicados
            if (!id.matches("A\\d{2,}")) { // Al menos dos dígitos
                throw new IllegalArgumentException("Invalid attendance id format. Expected A01, A02, ...");
            }
            if (existsById(id)) {
                throw new IllegalArgumentException("Attendance ID already exists");
            }
        }
        // Asignar objetos completos
        attendance.setClient(client);
        attendance.setGroupClass(gc);
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
            writer.write("id,dateTime,clientId,groupClassId\n"); // Escribe la cabecera del CSV
            
            for (Attendance attendance : attendances) { // Recorre todas las asistencias de la lista
                String clientId = attendance.getClient() != null ? attendance.getClient().getId() : "";
                String groupClassId = attendance.getGroupClass() != null ? attendance.getGroupClass().getId() : "";
                writer.write(attendance.getId() + "," + // Escribe el ID de la asistencia
                           (attendance.getDateTime() == null ? "" : attendance.getDateTime().format(CSV_DT)) + "," +
                           clientId + "," +
                           groupClassId + "\n");
            }
            
            writer.close();
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }

    private void loadFromCSV() { // Método privado para cargar datos desde archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            if (!file.exists()) { // Verifica si el archivo no existe
                return; // Sale del método si el archivo no existe
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file)); // Crea lector para el archivo
            String line; // Variable para almacenar cada línea del archivo
            reader.readLine(); // Lee y descarta la cabecera del CSV
            
            while ((line = reader.readLine()) != null) { // Lee línea por línea hasta llegar al final
                String[] data = line.split(","); // Divide la línea por comas y almacena los valores en un arreglo
                if (data.length >= 4) { // Nuevo formato: id,dateTime,clientId,groupClassId
                    String id = data[0];
                    LocalDateTime dt = null;
                    String dateStr = data[1];
                    if (dateStr != null && !dateStr.isBlank()) {
                        try {
                            dt = LocalDateTime.parse(dateStr, CSV_DT); // dd-MM-yyyy
                        } catch (DateTimeParseException ex1) {
                            dt = LocalDateTime.parse(dateStr); // Fallback ISO
                        }
                    }
                    String clientId = data[2];
                    String groupClassId = data[3];

                    Client client = clientService != null ? clientService.getById(clientId) : null;
                    GroupClass gc = groupClassService != null ? groupClassService.getById(groupClassId) : null;

                    Attendance attendance = new Attendance();
                    attendance.setId(id);
                    attendance.setDateTime(dt);
                    attendance.setClient(client);
                    attendance.setGroupClass(gc);
                    attendances.add(attendance);
                } else if (data.length >= 2) { // Compatibilidad con formato antiguo: id,dateTime
                    Attendance attendance = new Attendance();
                    attendance.setId(data[0]);
                    LocalDateTime dt;
                    try {
                        dt = LocalDateTime.parse(data[1], CSV_DT);
                    } catch (DateTimeParseException ex1) {
                        dt = LocalDateTime.parse(data[1]);
                    }
                    attendance.setDateTime(dt);
                    attendances.add(attendance);
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase AttendanceService
