package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.GroupClass;
import co.edu.umanizales.iron_gym.model.Trainer;
import co.edu.umanizales.iron_gym.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupClassService {
    private List<GroupClass> groupClasses;
    private final String CSV_FILE = "data/group_classes.csv";
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private ClientService clientService;

    public GroupClassService() {
        this.groupClasses = new ArrayList<>();
        loadFromCSV();
    }

    public List<GroupClass> getAll() {
        return groupClasses;
    }

    public GroupClass getById(String id) {
        for (GroupClass groupClass : groupClasses) { // Recorre la lista de clases grupales
            if (groupClass.getId().equals(id)) { // Compara el ID de cada clase grupal con el ID buscado
                return groupClass; // Retorna la clase grupal encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna clase grupal con ese ID
    }

    public GroupClass create(GroupClass groupClass) { // Método para crear una nueva clase grupal
        if (groupClass == null) {
            return null;
        }
        if (getById(groupClass.getId()) != null) {
            return null;
        }
        groupClasses.add(groupClass); // Agrega la nueva clase grupal a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return groupClass; // Retorna la clase grupal creada
    }

    public GroupClass update(String id, GroupClass updatedGroupClass) { // Método para actualizar una clase grupal existente
        for (int i = 0; i < groupClasses.size(); i++) { // Recorre la lista de clases grupales por índice
            if (groupClasses.get(i).getId().equals(id)) { // Si encuentra la clase grupal por ID
                updatedGroupClass.setId(id); // Mantiene el mismo ID en la clase grupal actualizada
                groupClasses.set(i, updatedGroupClass); // Reemplaza la clase grupal en la posición i con la actualizada
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedGroupClass; // Retorna la clase grupal actualizada
            }
        }
        return null; // Retorna null si no encontró la clase grupal para actualizar
    }

    public boolean delete(String id) { // Método para eliminar una clase grupal por ID
        for (int i = 0; i < groupClasses.size(); i++) { // Recorre la lista de clases grupales por índice
            if (groupClasses.get(i).getId().equals(id)) { // Si encuentra la clase grupal por ID
                groupClasses.remove(i); // Elimina la clase grupal de la lista
                saveToCSV();
                return true;
            }
        }
        return false;
    }

    public List<GroupClass> getAvailableClasses() {
        List<GroupClass> result = new ArrayList<>();
        for (GroupClass groupClass : groupClasses) {
            if (!groupClass.isFull()) {
                result.add(groupClass);
            }
        }
        return result;
    }

    // Método sencillo para asignar un entrenador a una clase grupal
    public boolean assignTrainerToClass(String classId, Trainer trainer) {
        GroupClass groupClass = getById(classId);
        if (groupClass == null || trainer == null) {
            return false;
        }
        groupClass.setTrainer(trainer);
        saveToCSV();
        return true;
    }

    public boolean registerClientToClass(String classId, Client client) {
        GroupClass groupClass = getById(classId);
        if (groupClass == null || client == null) {
            return false;
        }
        if (groupClass.getRegisteredClients() != null) {
            for (Client c : groupClass.getRegisteredClients()) {
                if (c.getId().equals(client.getId())) {
                    return false;
                }
            }
        }
        if (!groupClass.addClient(client)) {
            return false;
        }
        saveToCSV();
        return true;
    }

    private void saveToCSV() {
        try {
            File file = new File(CSV_FILE);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,name,maxCapacity,schedule,trainerId,clientIds\n");
            
            for (GroupClass groupClass : groupClasses) {
                String trainerId = groupClass.getTrainer() != null ? groupClass.getTrainer().getId() : "";
                String clientIds = (groupClass.getRegisteredClients() == null || groupClass.getRegisteredClients().isEmpty())
                        ? ""
                        : groupClass.getRegisteredClients().stream().map(Client::getId).collect(Collectors.joining(";"));
                writer.write(groupClass.getId() + "," +
                           groupClass.getName() + "," +
                           groupClass.getMaxCapacity() + "," +
                           groupClass.getSchedule() + "," +
                           trainerId + "," +
                           clientIds + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                if (data.length >= 4) {
                    GroupClass groupClass = new GroupClass(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2]),
                        data[3]
                    );
                    if (data.length >= 5 && data[4] != null && !data[4].isEmpty() && trainerService != null) {
                        Trainer tr = trainerService.getById(data[4]);
                        if (tr != null) {
                            groupClass.setTrainer(tr);
                        }
                    }
                    if (data.length >= 6 && data[5] != null && !data[5].isEmpty() && clientService != null) {
                        String[] ids = data[5].split(";");
                        for (String cid : ids) {
                            Client cl = clientService.getById(cid);
                            if (cl != null) {
                                groupClass.addClient(cl);
                            }
                        }
                    }
                    groupClasses.add(groupClass);
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase GroupClassService
