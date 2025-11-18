package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Payment; // Importa la clase Payment del paquete model
import co.edu.umanizales.iron_gym.service.PaymentService; // Importa el servicio de pagos
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/payments") // Define la ruta base para todos los endpoints de este controlador
public class PaymentController { // Inicio de la clase PaymentController - maneja peticiones HTTP para pagos
    
    @Autowired // Anotación para inyección automática del servicio
    private PaymentService paymentService; // Servicio que contiene la lógica de negocio para pagos

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Payment>> getAll() { // Método para obtener todos los pagos
        return ResponseEntity.ok(paymentService.getAll()); // Retorna respuesta HTTP 200 con la lista de pagos
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Payment> getById(@PathVariable String id) { // Método para obtener pago por ID
        Payment payment = paymentService.getById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        Payment created = paymentService.create(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable String id, @RequestBody Payment payment) {
        Payment updated = paymentService.update(id, payment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar pago por ID
        boolean deleted = paymentService.delete(id); // Elimina el pago usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el pago para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase PaymentController
