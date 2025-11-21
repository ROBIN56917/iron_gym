package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.Equipment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class EquipmentService { // Inicio de la clase EquipmentService - contiene la lógica de negocio para equipos
    private List<Equipment> equipments;
    private final String CSV_FILE = "data/equipments.csv";
    private static final String ID_PREFIX = "EQ";
    private int nextIdNumber = 1;

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

    public Equipment create(Equipment equipment) {
        // Verificar si ya existe un equipo con el mismo ID
        if (equipment.getId() != null && !equipment.getId().isEmpty()) {
            // Si se proporciona un ID, verificar que no exista
            if (equipments.stream().anyMatch(e -> e.getId().equals(equipment.getId()))) {
                throw new IllegalArgumentException("Ya existe un equipo con el ID: " + equipment.getId());
            }
        } else {
            // Generar nuevo ID secuencial
            String newId = generateNextId();
            equipment.setId(newId);
        }
        
        equipments.add(equipment);
        saveToCSV();
        return equipment;
    }
    
    private String generateNextId() {
        // Encontrar el número más alto de ID existente
        int maxId = equipments.stream()
            .map(Equipment::getId)
            .filter(id -> id.startsWith(ID_PREFIX))
            .map(id -> id.substring(ID_PREFIX.length()))
            .mapToInt(id -> {
                try {
                    return Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    return 0;
                }
            })
            .max()
            .orElse(0);
            
        // Usar el siguiente número disponible
        int nextId = maxId + 1;
        return String.format("%s%03d", ID_PREFIX, nextId);
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
            
            // Limpiar la lista actual
            if (equipments == null) {
                equipments = new ArrayList<>();
            } else {
                equipments.clear();
            }
            
            // Conjunto para verificar IDs duplicados
            Set<String> existingIds = new HashSet<>();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String id = data[0].trim();
                    
                    // Verificar si el ID ya existe
                    if (!existingIds.add(id)) {
                        System.err.println("Advertencia: ID duplicado encontrado en el archivo CSV: " + id);
                        continue; // Saltar este registro
                    }
                    
                    Equipment equipment = new Equipment(id, data[1].trim(), data[2].trim());
                    equipments.add(equipment);
                }
            }
            reader.close();
            
            // Ordenar la lista por ID para asegurar consistencia
            equipments.sort(Comparator.comparing(Equipment::getId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
