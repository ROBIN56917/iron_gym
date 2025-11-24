package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Attendance; // Importa la clase Attendance del paquete model
import co.edu.umanizales.iron_gym.service.AttendanceService; // Importa el servicio de asistencias
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import co.edu.umanizales.iron_gym.model.Client;
import co.edu.umanizales.iron_gym.model.GroupClass;

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/attendances") // Define la ruta base para todos los endpoints de este controlador
public class AttendanceController { // Inicio de la clase AttendanceController - maneja peticiones HTTP para asistencias
    
    @Autowired // Anotación para inyección automática del servicio
    private AttendanceService attendanceService; // Servicio que contiene la lógica de negocio para asistencias

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Attendance>> getAll() { // Método para obtener todas las asistencias
        return ResponseEntity.ok(attendanceService.getAll()); // Retorna respuesta HTTP 200 con la lista de asistencias
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Attendance> getById(@PathVariable String id) { // Método para obtener asistencia por ID
        Attendance attendance = attendanceService.getById(id); // Busca asistencia por ID usando el servicio
        if (attendance != null) { // Si se encontró la asistencia
            return ResponseEntity.ok(attendance); // Retorna respuesta HTTP 200 con la asistencia encontrada
        } else { // Si no se encontró la asistencia
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<?> create(@RequestBody Attendance attendance) { // Método para crear nueva asistencia
        try {
            Attendance created = attendanceService.create(attendance); // Crea la asistencia usando el servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la asistencia creada
        } catch (IllegalArgumentException ex) {
            java.util.Map<String, String> errors = new java.util.HashMap<>();
            errors.put("error", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            return ResponseEntity.badRequest().body(errors);
        }
    }

    // Endpoint alterno para crear asistencia usando IDs planos en el payload
    @PostMapping("/from-ids")
    public ResponseEntity<?> createFromIds(@RequestBody Map<String, String> payload) {
        Map<String, String> errors = new HashMap<>();
        String id = payload.getOrDefault("id", null);
        String dateTimeStr = payload.get("dateTime");
        String clientId = payload.get("clientId");
        String groupClassId = payload.get("groupClassId");

        if (clientId == null || clientId.isBlank()) {
            errors.put("clientId", "clientId es obligatorio");
        }
        if (groupClassId == null || groupClassId.isBlank()) {
            errors.put("groupClassId", "groupClassId es obligatorio");
        }
        LocalDateTime dt = null;
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            errors.put("dateTime", "dateTime es obligatorio (formato ISO: YYYY-MM-DDTHH:MM)");
        } else {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
                dt = LocalDateTime.parse(dateTimeStr, fmt);
            } catch (Exception ex) {
                errors.put("dateTime", "dateTime debe estar en formato dd-MM-yyyy'T'HH:mm");
            }
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Attendance attendance = new Attendance();
        if (id != null && !id.isBlank()) {
            attendance.setId(id);
        }
        attendance.setDateTime(dt);
        Client c = new Client();
        c.setId(clientId);
        attendance.setClient(c);
        GroupClass gc = new GroupClass();
        gc.setId(groupClassId);
        attendance.setGroupClass(gc);

        Attendance created = attendanceService.create(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Attendance attendance) { // Método para actualizar asistencia
        try {
            Attendance updated = attendanceService.update(id, attendance); // Actualiza la asistencia usando el servicio
            if (updated != null) { // Si se actualizó correctamente
                return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la asistencia actualizada
            } else { // Si no se encontró la asistencia para actualizar
                return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
            }
        } catch (IllegalArgumentException ex) {
            java.util.Map<String, String> errors = new java.util.HashMap<>();
            errors.put("error", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar asistencia por ID
        boolean deleted = attendanceService.delete(id); // Elimina la asistencia usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró la asistencia para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase AttendanceController
