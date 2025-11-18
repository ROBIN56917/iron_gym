package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Equipment; // Importa la clase Equipment del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class EquipmentService { // Inicio de la clase EquipmentService - contiene la lógica de negocio para equipos
    private List<Equipment> equipments; // Lista que almacena todos los equipos del sistema
    private final String CSV_FILE = "data/equipments.csv"; // Ruta del archivo CSV donde se guardan los datos de equipos

    public EquipmentService() { // Constructor de la clase EquipmentService
        this.equipments = new ArrayList<>(); // Inicializa la lista de equipos como ArrayList vacío
        loadFromCSV(); // Carga los datos de equipos desde el archivo CSV
    }

    public List<Equipment> getAll() { // Método para obtener todos los equipos
        return equipments; // Retorna la lista de todos los equipos
    }

    public Equipment getById(String id) {
        for (Equipment equipment : equipments) { // Recorre la lista de equipos
            if (equipment.getId().equals(id)) { // Compara el ID de cada equipo con el ID buscado
                return equipment; // Retorna el equipo encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún equipo con ese ID
    }

    public Equipment create(Equipment equipment) { // Método para crear un nuevo equipo
        equipments.add(equipment); // Agrega el nuevo equipo a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return equipment; // Retorna el equipo creado
    }

    public Equipment update(String id, Equipment updatedEquipment) { // Método para actualizar un equipo existente
        for (int i = 0; i < equipments.size(); i++) { // Recorre la lista de equipos por índice
            if (equipments.get(i).getId().equals(id)) { // Si encuentra el equipo por ID
                updatedEquipment.setId(id); // Mantiene el mismo ID en el equipo actualizado
                equipments.set(i, updatedEquipment); // Reemplaza el equipo en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedEquipment; // Retorna el equipo actualizado
            }
        }
        return null; // Retorna null si no encontró el equipo para actualizar
    }

    public boolean delete(String id) { // Método para eliminar un equipo por ID
        for (int i = 0; i < equipments.size(); i++) { // Recorre la lista de equipos por índice
            if (equipments.get(i).getId().equals(id)) { // Si encuentra el equipo por ID
                equipments.remove(i); // Elimina el equipo de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró el equipo para eliminar
    }

    public List<Equipment> getAvailableEquipment() { // Método para obtener equipos disponibles
        List<Equipment> result = new ArrayList<>(); // Crea lista para almacenar resultados
        for (Equipment equipment : equipments) { // Recorre todos los equipos
            if (equipment.isAvailable()) { // Si el equipo está disponible
                result.add(equipment); // Agrega el equipo a la lista de resultados
            }
        }
        return result; // Retorna la lista de equipos disponibles
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,type,status\n"); // Escribe la cabecera del CSV
            
            for (Equipment equipment : equipments) { // Recorre todos los equipos de la lista
                writer.write(equipment.getId() + "," + // Escribe el ID del equipo
                           equipment.getType() + "," + // Escribe el tipo del equipo
                           equipment.getStatus() + "\n"); // Escribe el estado y salto de línea
            }
            
            writer.close(); // Cierra el escritor del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }

    private void loadFromCSV() {
        try {
            File file = new File(CSV_FILE);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    Equipment equipment = new Equipment(data[0], data[1], data[2]);
                    equipments.add(equipment);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
