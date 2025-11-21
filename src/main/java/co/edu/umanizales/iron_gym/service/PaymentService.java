package co.edu.umanizales.iron_gym.service; // Declara el paquete donde se encuentra esta clase de servicio

import co.edu.umanizales.iron_gym.model.Payment; // Importa la clase Payment del paquete model
import org.springframework.stereotype.Service; // Anotación que marca esta clase como un servicio de Spring

import java.io.*; // Importa todas las clases para manejo de archivos
import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.time.format.DateTimeParseException; // Importa excepción para parseo de fecha/hora inválida
import java.util.ArrayList; // Importa ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para trabajar con colecciones

@Service // Anotación que indica que esta es una clase de servicio gestionada por Spring
public class PaymentService { // Inicio de la clase PaymentService - contiene la lógica de negocio para pagos
    private List<Payment> payments; // Lista que almacena todos los pagos del sistema
    private final String CSV_FILE = "data/payments.csv"; // Ruta del archivo CSV donde se guardan los datos de pagos

    public PaymentService() { // Constructor de la clase PaymentService
        this.payments = new ArrayList<>(); // Inicializa la lista de pagos como ArrayList vacío
        loadFromCSV(); // Carga los datos de pagos desde el archivo CSV
    }

    public List<Payment> getAll() { // Método para obtener todos los pagos
        return payments; // Retorna la lista de todos los pagos
    }

    public Payment getById(String id) { // Método para buscar pago por ID
        for (Payment payment : payments) { // Recorre la lista de pagos
            if (payment.getId().equals(id)) { // Compara el ID de cada pago con el ID buscado
                return payment; // Retorna el pago encontrado
            }
        }
        return null; // Retorna null si no se encontró ningún pago con ese ID
    }

    public Payment create(Payment payment) { // Método para crear un nuevo pago
        payments.add(payment); // Agrega el nuevo pago a la lista
        saveToCSV(); // Guarda los cambios en el archivo CSV
        return payment; // Retorna el pago creado
    }

    public Payment update(String id, Payment updatedPayment) { // Método para actualizar un pago existente
        for (int i = 0; i < payments.size(); i++) { // Recorre la lista de pagos por índice
            if (payments.get(i).getId().equals(id)) { // Si encuentra el pago por ID
                updatedPayment.setId(id); // Mantiene el mismo ID en el pago actualizado
                payments.set(i, updatedPayment); // Reemplaza el pago en la posición i con el actualizado
                saveToCSV(); // Guarda los cambios en el archivo CSV
                return updatedPayment; // Retorna el pago actualizado
            }
        }
        return null; // Retorna null si no encontró el pago para actualizar
    }

    public boolean delete(String id) { // Método para eliminar un pago por ID
        for (int i = 0; i < payments.size(); i++) { // Recorre la lista de pagos por índice
            if (payments.get(i).getId().equals(id)) { // Si encuentra el pago por ID
                payments.remove(i);
                saveToCSV();
                return true;
            }
        }
        return false;
    }

    private void saveToCSV() {
        try {
            File file = new File(CSV_FILE);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,amount,dateTime,paymentMethod\n");
            
            for (Payment payment : payments) {
                writer.write(payment.getId() + "," + 
                           payment.getAmount() + "," + 
                           payment.getDateTime() + "," + 
                           payment.getPaymentMethod() + "\n");
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
            
            BufferedReader reader = new BufferedReader(new FileReader(file)); // Crea lector para el archivo
            String line = reader.readLine(); // Lee y salta la línea de cabecera
            
            while ((line = reader.readLine()) != null) { // Mientras haya líneas por leer
                String[] data = line.split(","); // Divide la línea por comas para obtener los datos
                if (data.length == 4) { // Verifica que la línea tenga exactamente 4 campos
                    String id = data[0] != null ? data[0].trim() : "";
                    String amountStr = data[1] != null ? data[1].trim() : "";
                    String dateStr = data[2] != null ? data[2].trim() : "";
                    String methodStr = data[3] != null ? data[3].trim() : "";

                    if (id.isEmpty()) {
                        continue; // ID es requerido
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(amountStr);
                    } catch (NumberFormatException ex) {
                        continue; // Monto inválido, omite la fila
                    }

                    LocalDateTime dt = null;
                    if (!dateStr.isEmpty() && !"null".equalsIgnoreCase(dateStr)) {
                        try {
                            dt = LocalDateTime.parse(dateStr);
                        } catch (DateTimeParseException ex) {
                            continue; // Fecha inválida, omite la fila
                        }
                    }

                    String method = (methodStr.isEmpty() || "null".equalsIgnoreCase(methodStr)) ? null : methodStr;

                    Payment payment = new Payment(id, amount, dt, method); // Crea pago con datos saneados
                    payments.add(payment); // Agrega el pago a la lista
                }
            }
            
            reader.close(); // Cierra el lector del archivo
        } catch (IOException e) { // Captura excepciones de entrada/salida
            e.printStackTrace(); // Imprime el error en la consola para depuración
        }
    }
} // Fin de la clase PaymentService
