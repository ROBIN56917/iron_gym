package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Supplement; // Importa la clase Supplement del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class SupplementService { // Inicio de la clase SupplementService - contiene la lógica de negocio para suplementos
    private List<Supplement> supplements; // Lista que almacena todos los suplementos del sistema
    private final String CSV_FILE = "data/supplements.csv"; // Ruta del archivo CSV donde se guardan los datos de suplementos

    public SupplementService() { // Constructor de la clase SupplementService
        this.supplements = new ArrayList<>(); // Inicializa la lista de suplementos como ArrayList vacío
        loadFromCSV(); // Carga los datos de suplementos desde el archivo CSV
    }

    public List<Supplement> getAll() { // Método para obtener todos los suplementos
        return supplements; // Retorna la lista de todos los suplementos
    }

    public Supplement getById(String id) {
        for (Supplement supplement : supplements) { // Recorre la lista de suplementos
            if (supplement.getId().equals(id)) { // Compara el ID de cada suplemento con el ID buscado
                return supplement; // Retorna el suplemento encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún suplemento con ese ID
    }

    public Supplement create(Supplement supplement) { // Método para crear un nuevo suplemento
        supplements.add(supplement); // Agrega el nuevo suplemento a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return supplement; // Retorna el suplemento creado
    }

    public Supplement update(String id, Supplement updatedSupplement) { // Método para actualizar un suplemento existente
        for (int i = 0; i < supplements.size(); i++) { // Recorre la lista de suplementos por índice
            if (supplements.get(i).getId().equals(id)) { // Si encuentra el suplemento por ID
                updatedSupplement.setId(id); // Mantiene el mismo ID en el suplemento actualizado
                supplements.set(i, updatedSupplement); // Reemplaza el suplemento en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedSupplement; // Retorna el suplemento actualizado
            }
        }
        return null; // Retorna null si no encontró el suplemento para actualizar
    }

    public boolean delete(String id) { // Método para eliminar un suplemento por ID
        for (int i = 0; i < supplements.size(); i++) { // Recorre la lista de suplementos por índice
            if (supplements.get(i).getId().equals(id)) { // Si encuentra el suplemento por ID
                supplements.remove(i); // Elimina el suplemento de la lista
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return true; // Retorna true indicando que la eliminación fue exitosa
            }
        }
        return false; // Retorna false si no encontró el suplemento para eliminar
    }

    private void saveToCSV() { // Método privado para guardar los datos en archivo CSV
        try { // Inicia bloque try para manejar posibles excepciones de archivo
            File file = new File(CSV_FILE); // Crea un objeto File con la ruta del CSV
            file.getParentFile().mkdirs(); // Crea la estructura de directorios si no existe
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); // Crea escritor para el archivo
            writer.write("id,name,brand,price\n"); // Escribe la cabecera del CSV
            
            for (Supplement supplement : supplements) { // Recorre todos los suplementos de la lista
                writer.write(supplement.getId() + "," + // Escribe el ID del suplemento
                           supplement.getName() + "," + // Escribe el nombre del suplemento
                           supplement.getBrand() + "," + // Escribe la marca del suplemento
                           supplement.getPrice() + "\n"); // Escribe el precio y salto de línea
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
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Supplement supplement = new Supplement(
                        data[0], 
                        data[1], 
                        data[2], 
                        Double.parseDouble(data[3])
                    );
                    supplements.add(supplement);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
