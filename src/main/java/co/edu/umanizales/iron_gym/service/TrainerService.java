package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.Trainer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainerService {
    private List<Trainer> trainers;
    private final String CSV_FILE = "data/trainers.csv";

    public TrainerService() {
        this.trainers = new ArrayList<>();
        loadFromCSV();
    }

    public List<Trainer> getAll() {
        return trainers;
    }

    public Trainer getById(String id) {
        for (Trainer trainer : trainers) { // Recorre la lista de entrenadores
            if (trainer.getId().equals(id)) { // Compara el ID de cada entrenador con el ID buscado
                return trainer; // Retorna el entrenador encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún entrenador con ese ID
    }

    public Trainer create(Trainer trainer) { // Método para crear un nuevo entrenador
        String newId = generateNextId(); // Genera un nuevo ID único
        trainer.setId(newId); // Asigna el nuevo ID al entrenador
        trainers.add(trainer); // Agrega el nuevo entrenador a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return trainer; // Retorna el entrenador creado
    }
    
    private String generateNextId() { // Método privado para generar el siguiente ID disponible con prefijo T
        int maxId = 0; // Inicializa el ID máximo en 0
        for (Trainer trainer : trainers) { // Recorre todos los entrenadores
            try {
                String currentId = trainer.getId();
                if (currentId != null && currentId.startsWith("T")) { // IDs esperados con prefijo T
                    int idNum = Integer.parseInt(currentId.substring(1)); // Extrae la parte numérica
                    if (idNum > maxId) {
                        maxId = idNum; // Actualiza el ID máximo
                    }
                }
            } catch (NumberFormatException e) {
                // Ignorar IDs que no cumplan el formato esperado
            }
        }
        return String.format("T%02d", maxId + 1); // Retorna el siguiente ID con formato T + 2 dígitos
    }

    public Trainer update(String id, Trainer updatedTrainer) { // Método para actualizar un entrenador existente
        for (int i = 0; i < trainers.size(); i++) { // Recorre la lista de entrenadores por índice
            if (trainers.get(i).getId().equals(id)) { // Si encuentra el entrenador por ID
                updatedTrainer.setId(id); // Mantiene el mismo ID en el entrenador actualizado
                trainers.set(i, updatedTrainer); // Reemplaza el entrenador en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedTrainer; // Retorna el entrenador actualizado
            }
        }
        return null; // Retorna null si no encontró el entrenador para actualizar
    }

    public boolean delete(String id) { // Método para eliminar un entrenador por ID
        for (int i = 0; i < trainers.size(); i++) { // Recorre la lista de entrenadores por índice
            if (trainers.get(i).getId().equals(id)) { // Si encuentra el entrenador por ID
                trainers.remove(i); // Elimina el entrenador de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró el entrenador para eliminar
    }

    public boolean existsByIdentification(String identification) { // Verifica si ya existe una identificación
        for (Trainer trainer : trainers) {
            if (trainer.getIdentification().equals(identification)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByPhone(String phone) { // Verifica si ya existe un teléfono
        for (Trainer trainer : trainers) {
            if (trainer.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,name,email,phone,identification\n");
            
            for (Trainer trainer : trainers) {
                writer.write(trainer.getId() + "," + 
                           trainer.getName() + "," + 
                           trainer.getEmail() + "," + 
                           trainer.getPhone() + "," +
                           trainer.getIdentification() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                if (data.length == 5) {
                    Trainer trainer = new Trainer(data[0], data[1], data[2], data[3], data[4]);
                    trainers.add(trainer);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
