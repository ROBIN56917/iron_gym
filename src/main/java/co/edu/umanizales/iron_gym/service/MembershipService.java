package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Membership; // Importa la clase Membership del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring
import org.springframework.beans.factory.annotation.Autowired; // Para inyectar ClientService

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDate; // Importa la clase para manejar fechas sin hora
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class MembershipService { // Inicio de la clase MembershipService - contiene la lógica de negocio para membresías
    private List<Membership> memberships; // Lista que almacena todas las membresías del sistema
    private final String CSV_FILE = "data/memberships.csv"; // Ruta del archivo CSV donde se guardan los datos de membresías
    private static final List<String> ALLOWED_TYPES = List.of("BASIC", "PREMIUM");

    @Autowired
    private ClientService clientService; // Para validar existencia del cliente por ID

    public MembershipService() { // Constructor de la clase MembershipService
        this.memberships = new ArrayList<>(); // Inicializa la lista de membresías como ArrayList vacío
        loadFromCSV(); // Carga los datos de membresías desde el archivo CSV
    }

    public List<Membership> getAll() { // Método para obtener todas las membresías
        return memberships; // Retorna la lista de todas las membresías
    }

    public Membership getById(String id) { // Método para buscar membresía por ID (membershipId)
        for (Membership membership : memberships) { // Recorre la lista de membresías
            if (membership.getId().equals(id)) { // Compara el ID de cada membresía con el ID buscado
                return membership; // Retorna la membresía encontrada
            }
        }
        return null; // Retorna null si no se encontró ninguna membresía con ese ID
    }

    public Membership getByClientId(String clientId) { // Buscar membresía por ID de cliente
        for (Membership membership : memberships) {
            if (clientId != null && clientId.equals(membership.getClientId())) {
                return membership;
            }
        }
        return null;
    }

    private boolean existsByClientId(String clientId) { // Verifica si ya existe una membresía para ese cliente
        return getByClientId(clientId) != null;
    }

    private String generateNextId() { // Genera el siguiente ID con prefijo M y 2 dígitos
        int max = 0;
        for (Membership m : memberships) {
            String mid = m.getId();
            if (mid != null && mid.startsWith("M")) {
                try {
                    int num = Integer.parseInt(mid.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("M%02d", max + 1);
    }

    public Membership create(Membership membership) { // Método para crear una nueva membresía (ID auto)
        if (membership == null) {
            throw new IllegalArgumentException("El payload de la membresía es obligatorio");
        }
        // Validar clientId
        if (membership.getClientId() == null || membership.getClientId().isBlank()) {
            throw new IllegalArgumentException("El clientId es obligatorio");
        }
        if (clientService.getById(membership.getClientId()) == null) { // El clientId debe existir
            throw new IllegalArgumentException("El clientId no existe");
        }
        if (existsByClientId(membership.getClientId())) { // Una membresía por cliente
            throw new IllegalArgumentException("Este cliente ya tiene una membresía");
        }
        if (membership.getType() == null || membership.getType().isBlank()) {
            throw new IllegalArgumentException("El tipo de membresía es obligatorio");
        }
        String normalizedType = membership.getType().trim().toUpperCase();
        if (!ALLOWED_TYPES.contains(normalizedType)) {
            throw new IllegalArgumentException("Tipo de membresía inválido. Permitidos: BASIC, PREMIUM");
        }
        membership.setType(normalizedType);
        // Generar ID Mxx
        String newId = generateNextId();
        membership.setId(newId);
        memberships.add(membership); // Agrega la nueva membresía a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return membership; // Retorna la membresía creada
    }

    public Membership update(String id, Membership updatedMembership) { // Método para actualizar una membresía existente
        for (int i = 0; i < memberships.size(); i++) { // Recorre la lista de membresías por índice
            if (memberships.get(i).getId().equals(id)) { // Si encuentra la membresía por ID
                if (updatedMembership.getType() == null || updatedMembership.getType().isBlank()) {
                    throw new IllegalArgumentException("El tipo de membresía es obligatorio");
                }
                String normalizedType = updatedMembership.getType().trim().toUpperCase();
                if (!ALLOWED_TYPES.contains(normalizedType)) {
                    throw new IllegalArgumentException("Tipo de membresía inválido. Permitidos: BASIC, PREMIUM");
                }
                updatedMembership.setType(normalizedType);
                updatedMembership.setId(id); // Mantiene el mismo ID en la membresía actualizada
                // Mantener el mismo clientId para evitar inconsistencias
                updatedMembership.setClientId(memberships.get(i).getClientId());
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

    public List<String> getTypes() { // Obtiene lista de tipos de membresía sin duplicados
        return new ArrayList<>(ALLOWED_TYPES);
    }

    private void saveToCSV() {
        try {
            File file = new File(CSV_FILE);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,clientId,type,startDate,endDate,price\n");
            
            for (Membership membership : memberships) {
                // Formatear el precio con exactamente 3 decimales
                String formattedPrice = String.format("%.3f", membership.getPrice());
                // Reemplazar coma por punto si es necesario y asegurar formato estándar
                formattedPrice = formattedPrice.replace(",", ".");
                
                writer.write(membership.getId() + "," +
                           membership.getClientId() + "," +
                           membership.getType() + "," + 
                           membership.getStartDate() + "," + 
                           membership.getEndDate() + "," + 
                           formattedPrice + "\n");
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
                if (data.length >= 6) { // Formato: id,clientId,type,startDate,endDate,price
                    Membership membership = new Membership(
                        data[0], // membershipId
                        data[1], // clientId (o antes personId)
                        data[2], // type
                        LocalDate.parse(data[3]), // startDate
                        LocalDate.parse(data[4]), // endDate
                        Double.parseDouble(data[5]) // price
                    );
                    memberships.add(membership);
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase MembershipService
