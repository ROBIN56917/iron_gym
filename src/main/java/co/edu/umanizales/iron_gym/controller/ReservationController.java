package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Reservation; // Importa la clase Reservation del paquete model
import co.edu.umanizales.iron_gym.service.ReservationService; // Importa el servicio de reservas
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/reservations") // Define la ruta base para todos los endpoints de este controlador
public class ReservationController { // Inicio de la clase ReservationController - maneja peticiones HTTP para reservas
    
    @Autowired // Anotación para inyección automática del servicio
    private ReservationService reservationService; // Servicio que contiene la lógica de negocio para reservas

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Reservation>> getAll() { // Método para obtener todas las reservas
        return ResponseEntity.ok(reservationService.getAll()); // Retorna respuesta HTTP 200 con la lista de reservas
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Reservation> getById(@PathVariable String id) { // Método para obtener reserva por ID
        Reservation reservation = reservationService.getById(id); // Busca reserva por ID usando el servicio
        if (reservation != null) { // Si se encontró la reserva
            return ResponseEntity.ok(reservation); // Retorna respuesta HTTP 200 con la reserva encontrada
        } else { // Si no se encontró la reserva
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) { // Método para crear nueva reserva
        Reservation created = reservationService.create(reservation); // Crea la reserva usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la reserva creada
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Reservation> update(@PathVariable String id, @RequestBody Reservation reservation) { // Método para actualizar reserva
        Reservation updated = reservationService.update(id, reservation); // Actualiza la reserva usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la reserva actualizada
        } else { // Si no se encontró la reserva para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar reserva por ID
        boolean deleted = reservationService.delete(id); // Elimina la reserva usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró la reserva para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase ReservationController
