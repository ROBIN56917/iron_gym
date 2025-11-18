package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Client; // Importa la clase Client del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class ClientService { // Inicio de la clase ClientService - contiene la lógica de negocio para clientes
    private List<Client> clients; // Lista que almacena todos los clientes del sistema
    private final String CSV_FILE = "data/clients.csv"; // Ruta del archivo CSV donde se guardan los datos de clientes

    public ClientService() { // Constructor de la clase ClientService
        this.clients = new ArrayList<>(); // Inicializa la lista de clientes como ArrayList vacío
        loadFromCSV(); // Carga los datos de clientes desde el archivo CSV
    }

    public List<Client> getAll() { // Método para obtener todos los clientes
        return clients; // Retorna la lista de todos los clientes
    }

    public Client getById(String id) {
        for (Client client : clients) { // Recorre la lista de clientes
            if (client.getId().equals(id)) { // Compara el ID de cada cliente con el ID buscado
                return client; // Retorna el cliente encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún cliente con ese ID
    }

    public Client create(Client client) { // Método para crear un nuevo cliente
        String newId = generateNextId(); // Genera un nuevo ID único para el cliente
        client.setId(newId); // Asigna el nuevo ID al objeto cliente
        clients.add(client); // Agrega el nuevo cliente a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return client; // Retorna el cliente creado
    }
    
    private String generateNextId() { // Método privado para generar el siguiente ID disponible
        int maxId = 0; // Inicializa el máximo ID encontrado en 0
        for (Client client : clients) { // Recorre todos los clientes existentes
            try {
                int idNum = Integer.parseInt(client.getId()); // Convierte el ID a número entero
                if (idNum > maxId) { // Si el ID actual es mayor que el máximo encontrado
                    maxId = idNum; // Actualiza el máximo ID
                }
            } catch (NumberFormatException e) { // Si el ID no es un número válido
                // Ignorar IDs que no sean números
            }
        }
        return String.format("%02d", maxId + 1); // Formatea el nuevo ID con 2 dígitos
    }

    public Client update(String id, Client updatedClient) { // Método para actualizar un cliente existente
        for (int i = 0; i < clients.size(); i++) { // Recorre la lista de clientes por índice
            if (clients.get(i).getId().equals(id)) { // Si encuentra el cliente por ID
                updatedClient.setId(id); // Mantiene el mismo ID en el cliente actualizado
                clients.set(i, updatedClient); // Reemplaza el cliente en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedClient; // Retorna el cliente actualizado
            }
        }
        return null; // Retorna null si no encontró el cliente para actualizar
    }

    public boolean delete(String id) { // Método para eliminar un cliente por ID
        for (int i = 0; i < clients.size(); i++) { // Recorre la lista de clientes por índice
            if (clients.get(i).getId().equals(id)) { // Si encuentra el cliente por ID
                clients.remove(i); // Elimina el cliente de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró el cliente para eliminar
    }

    public List<Client> getClientsWithMembership() { // Método para obtener clientes con membresía activa
        List<Client> result = new ArrayList<>(); // Crea lista para almacenar resultados
        for (Client client : clients) { // Recorre todos los clientes
            if (client.hasMembership()) { // Si el cliente tiene membresía
                result.add(client); // Agrega el cliente a la lista de resultados
            }
        }
        return result; // Retorna la lista de clientes con membresía
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,name,email,phone,identification\n"); // Escribe la cabecera del CSV
            
            for (Client client : clients) { // Recorre todos los clientes de la lista
                writer.write(client.getId() + "," + // Escribe el ID del cliente
                           client.getName() + "," + // Escribe el nombre del cliente
                           client.getEmail() + "," + // Escribe el email del cliente
                           client.getPhone() + "," + // Escribe el teléfono del cliente
                           client.getIdentification() + "\n"); // Escribe la identificación y salto de línea
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
                if (data.length == 5) { // Verifica que la línea tenga exactamente 5 campos
                    Client client = new Client(data[0], data[1], data[2], data[3], data[4], null); // Crea cliente con datos
                    clients.add(client); // Agrega el cliente a la lista
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase ClientService
