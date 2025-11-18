package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Membership; // Importa la clase Membership del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDate; // Importa la clase para manejar fechas sin hora
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class MembershipService { // Inicio de la clase MembershipService - contiene la lógica de negocio para membresías
    private List<Membership> memberships; // Lista que almacena todas las membresías del sistema
    private final String CSV_FILE = "data/memberships.csv"; // Ruta del archivo CSV donde se guardan los datos de membresías

    public MembershipService() { // Constructor de la clase MembershipService
        this.memberships = new ArrayList<>(); // Inicializa la lista de membresías como ArrayList vacío
        loadFromCSV(); // Carga los datos de membresías desde el archivo CSV
    }

    public List<Membership> getAll() { // Método para obtener todas las membresías
        return memberships; // Retorna la lista de todas las membresías
    }

    public Membership getById(String id) { // Método para buscar membresía por ID
        for (Membership membership : memberships) { // Recorre la lista de membresías
            if (membership.getId().equals(id)) { // Compara el ID de cada membresía con el ID buscado
                return membership; // Retorna la membresía encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna membresía con ese ID
    }

    public Membership create(Membership membership) { // Método para crear una nueva membresía
        memberships.add(membership); // Agrega la nueva membresía a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return membership; // Retorna la membresía creada
    }

    public Membership update(String id, Membership updatedMembership) { // Método para actualizar una membresía existente
        for (int i = 0; i < memberships.size(); i++) { // Recorre la lista de membresías por índice
            if (memberships.get(i).getId().equals(id)) { // Si encuentra la membresía por ID
                updatedMembership.setId(id); // Mantiene el mismo ID en la membresía actualizada
                memberships.set(i, updatedMembership); // Reemplaza la membresía en la posición i con la actualizada
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedMembership; // Retorna la membresía actualizada
            }
        }
        return null; // Retorna null si no encontró la membresía para actualizar
    }

    public boolean delete(String id) { // Método para eliminar una membresía por ID
        for (int i = 0; i < memberships.size(); i++) { // Recorre la lista de membresías por índice
            if (memberships.get(i).getId().equals(id)) { // Si encuentra la membresía por ID
                memberships.remove(i);
                saveToCSV();
                return true;
            }
        }
        return false;
    }

    public List<Membership> getActiveMemberships() {
        List<Membership> result = new ArrayList<>();
        for (Membership membership : memberships) {
            if (membership.isActive()) {
                result.add(membership);
            }
        }
        return result;
    }

    private void saveToCSV() {
        try {
            File file = new File(CSV_FILE);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,type,startDate,endDate,price\n");
            
            for (Membership membership : memberships) {
                writer.write(membership.getId() + "," + 
                           membership.getType() + "," + 
                           membership.getStartDate() + "," + 
                           membership.getEndDate() + "," + 
                           membership.getPrice() + "\n");
            }
            
            writer.close();
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
                if (data.length == 5) { // Verifica que la línea tenga exactamente 5 campos
                    Membership membership = new Membership( // Crea membresía con datos
                        data[0], // ID de la membresía
                        data[1], // Tipo de membresía
                        LocalDate.parse(data[2]), // Fecha de inicio parseada
                        LocalDate.parse(data[3]), // Fecha de fin parseada
                        Double.parseDouble(data[4]) // Precio convertido a double
                    );
                    memberships.add(membership); // Agrega la membresía a la lista
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase MembershipService
