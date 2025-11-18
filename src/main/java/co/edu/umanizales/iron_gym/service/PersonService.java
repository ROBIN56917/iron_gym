package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Person; // Importa la clase Person del paquete model
import org.springframework.beans.factory.annotation.Value; // Para inyectar valores desde archivos de configuración
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring
import jakarta.annotation.PostConstruct; // Para ejecutar métodos después de la construcción del bean

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class PersonService { // Inicio de la clase PersonService - contiene la lógica de negocio para personas
    private List<Person> persons; // Lista que almacena todas las personas del sistema
    @Value("${csv.persons.path}") // Inyecta el valor de la propiedad csv.persons.path desde application.properties
    private String csvFilePath; // Ruta del archivo CSV donde se guardan los datos de personas

    public PersonService() { // Constructor de la clase PersonService
        this.persons = new ArrayList<>(); // Inicializa la lista de personas como ArrayList vacío
    }

    @PostConstruct // Anotación que ejecuta este método después de que el bean sea construido
    public void init() { // Método de inicialización del servicio
        loadFromCSV(); // Carga los datos de personas desde el archivo CSV
    }

    public List<Person> getAll() { // Método para obtener todas las personas
        // Recargar datos del CSV cada vez para obtener los cambios más recientes
        persons.clear(); // Limpia la lista actual de personas
        loadFromCSV(); // Vuelve a cargar los datos desde el archivo CSV
        return persons; // Retorna la lista actualizada de personas
    }

    public Person getById(String id) { // Método para buscar persona por ID
        for (Person person : persons) { // Recorre la lista de personas
            if (person.getId().equals(id)) { // Compara el ID de cada persona con el ID buscado
                return person; // Retorna la persona encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna persona con ese ID
    }

    public Person create(Person person) { // Método para crear una nueva persona
        String newId = generateNextId(); // Genera un nuevo ID único para la persona
        person.setId(newId);
        persons.add(person);
        saveToCSV();
        return person;
    }
    
    private String generateNextId() {
        int maxId = 0;
        for (Person person : persons) {
            try {
                int idNum = Integer.parseInt(person.getId());
                if (idNum > maxId) {
                    maxId = idNum;
                }
            } catch (NumberFormatException e) {
                // Ignorar IDs que no sean números
            }
        }
        String newId = String.format("%02d", maxId + 1);
        
        // Validación adicional para asegurar que el ID no existe
        while (isIdExists(newId)) {
            maxId++;
            newId = String.format("%02d", maxId + 1);
        }
        
        return newId;
    }
    
    private boolean isIdExists(String id) {
        for (Person person : persons) {
            if (person.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Person update(String id, Person updatedPerson) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId().equals(id)) {
                updatedPerson.setId(id);
                persons.set(i, updatedPerson);
                saveToCSV();
                return updatedPerson;
            }
        }
        return null;
    }

    public boolean delete(String id) { // Método para eliminar una persona por ID
        for (int i = 0; i < persons.size(); i++) { // Recorre la lista de personas por índice
            if (persons.get(i).getId().equals(id)) { // Si encuentra la persona por ID
                persons.remove(i); // Elimina la persona de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró la persona para eliminar
    }
    
    public boolean existsByIdentification(String identification) { // Método para verificar si existe una identificación
        for (Person person : persons) { // Recorre todas las personas existentes
            if (person.getIdentification().equals(identification)) { // Compara la identificación
                return true; // Retorna true si encuentra la identificación
            }
        }
        return false; // Retorna false si no encuentra la identificación
    }
    
    public boolean existsByPhone(String phone) { // Método para verificar si existe un teléfono
        for (Person person : persons) { // Recorre todas las personas existentes
            if (person.getPhone().equals(phone)) { // Compara el teléfono
                return true; // Retorna true si encuentra el teléfono
            }
        }
        return false; // Retorna false si no encuentra el teléfono
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try {
            File file = new File(csvFilePath);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,name,email,phone,identification\n");
            
            for (Person person : persons) {
                writer.write(person.getId() + "," + 
                           person.getName() + "," + 
                           person.getEmail() + "," + 
                           person.getPhone() + "," +
                           person.getIdentification() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromCSV() {
        try {
            File file = new File(csvFilePath);
            if (!file.exists()) {
                System.out.println("Archivo CSV no encontrado: " + csvFilePath);
                return;
            }
            
            System.out.println("Cargando datos desde: " + csvFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(file)); // Crea lector para el archivo
            String line = reader.readLine(); // Lee y salta la línea de cabecera
            
            while ((line = reader.readLine()) != null) { // Mientras haya líneas por leer
                String[] data = line.split(","); // Divide la línea por comas para obtener los datos
                if (data.length == 5) { // Verifica que la línea tenga exactamente 5 campos
                    Person person = new Person(data[0], data[1], data[2], data[3], data[4]); // Crea persona con datos
                    persons.add(person); // Agrega la persona a la lista
                    System.out.println("Persona cargada: " + person.getId() + " - " + person.getName()); // Mensaje informativo
                }
            }
            
            reader.close(); // Cierra el lector del archivo
            System.out.println("Total personas cargadas: " + persons.size()); // Muestra total cargado
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase PersonService
