package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Reservation; // Importa la clase Reservation del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.time.format.DateTimeParseException; // Manejo de errores al parsear fechas
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class ReservationService { // Inicio de la clase ReservationService - contiene la lógica de negocio para reservas
    private List<Reservation> reservations; // Lista que almacena todas las reservas del sistema
    private final String CSV_FILE = "data/reservations.csv"; // Ruta del archivo CSV donde se guardan los datos de reservas

    public ReservationService() { // Constructor de la clase ReservationService
        this.reservations = new ArrayList<>(); // Inicializa la lista de reservas como ArrayList vacío
        loadFromCSV(); // Carga los datos de reservas desde el archivo CSV
    }

    public List<Reservation> getAll() { // Método para obtener todas las reservas
        return reservations; // Retorna la lista de todas las reservas
    }

    public Reservation getById(String id) { // Método para buscar reserva por ID
        for (Reservation reservation : reservations) { // Recorre la lista de reservas
            if (reservation.getId().equals(id)) { // Compara el ID de cada reserva con el ID buscado
                return reservation; // Retorna la reserva encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna reserva con ese ID
    }

    public Reservation create(Reservation reservation) { // Método para crear una nueva reserva
        reservations.add(reservation); // Agrega la nueva reserva a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return reservation; // Retorna la reserva creada
    }

    public Reservation update(String id, Reservation updatedReservation) { // Método para actualizar una reserva existente
        for (int i = 0; i < reservations.size(); i++) { // Recorre la lista de reservas por índice
            if (reservations.get(i).getId().equals(id)) { // Si encuentra la reserva por ID
                updatedReservation.setId(id); // Mantiene el mismo ID en la reserva actualizada
                reservations.set(i, updatedReservation); // Reemplaza la reserva en la posición i con la actualizada
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedReservation; // Retorna la reserva actualizada
            }
        }
        return null; // Retorna null si no encontró la reserva para actualizar
    }

    public boolean delete(String id) { // Método para eliminar una reserva por ID
        for (int i = 0; i < reservations.size(); i++) { // Recorre la lista de reservas por índice
            if (reservations.get(i).getId().equals(id)) { // Si encuentra la reserva por ID
                reservations.remove(i); // Elimina la reserva de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró la reserva para eliminar
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,dateTime\n"); // Escribe la cabecera del CSV
            
            for (Reservation reservation : reservations) { // Recorre todas las reservas de la lista
                writer.write(reservation.getId() + "," + // Escribe el ID de la reserva
                           reservation.getDateTime() + "\n"); // Escribe la fecha y hora y salto de línea
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
                if (data.length >= 2) {
                    String id = data[0] != null ? data[0].trim() : "";
                    String dateStr = data[1] != null ? data[1].trim() : "";

                    if (id.isEmpty() || "null".equalsIgnoreCase(id)) {
                        continue; // ID requerido
                    }

                    LocalDateTime dt = null;
                    if (!dateStr.isEmpty() && !"null".equalsIgnoreCase(dateStr)) {
                        try {
                            dt = LocalDateTime.parse(dateStr);
                        } catch (DateTimeParseException ex) {
                            continue; // Fecha inválida, omite la fila
                        }
                    }

                    Reservation reservation = new Reservation(
                        id,
                        dt,
                        null,
                        null
                    );
                    reservations.add(reservation);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
