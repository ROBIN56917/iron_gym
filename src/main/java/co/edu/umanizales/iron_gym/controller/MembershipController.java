package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Membership; // Importa la clase Membership del paquete model
import co.edu.umanizales.iron_gym.service.MembershipService; // Importa el servicio de membresías
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones
import java.util.Map;
import java.util.HashMap;

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/memberships") // Define la ruta base para todos los endpoints de este controlador
public class MembershipController { // Inicio de la clase MembershipController - maneja peticiones HTTP para membresías
    
    @Autowired // Anotación para inyección automática del servicio
    private MembershipService membershipService; // Servicio que contiene la lógica de negocio para membresías

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Membership>> getAll() { // Método para obtener todas las membresías
        return ResponseEntity.ok(membershipService.getAll()); // Retorna respuesta HTTP 200 con la lista de membresías
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Membership> getById(@PathVariable String id) { // Método para obtener membresía por ID
        Membership membership = membershipService.getById(id); // Busca membresía por ID usando el servicio
        if (membership != null) { // Si se encontró la membresía
            return ResponseEntity.ok(membership); // Retorna respuesta HTTP 200 con la membresía encontrada
        } else { // Si no se encontró la membresía
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @GetMapping("/active") // Anotación que mapea peticiones HTTP GET a /active
    public ResponseEntity<List<Membership>> getActiveMemberships() { // Método para obtener membresías activas
        return ResponseEntity.ok(membershipService.getActiveMemberships()); // Retorna respuesta HTTP 200 con membresías activas
    }

    @GetMapping("/types") // Endpoint para listar los nombres/tipos de membresía existentes
    public ResponseEntity<List<String>> getTypes() {
        return ResponseEntity.ok(membershipService.getTypes());
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<?> create(@RequestBody Membership membership) { // Método para crear nueva membresía
        Map<String, String> errors = new HashMap<>();
        if (membership == null) {
            errors.put("membership", "Payload de membresía es obligatorio");
            return ResponseEntity.badRequest().body(errors);
        }
        if (membership.getClientId() == null || membership.getClientId().isBlank()) {
            errors.put("clientId", "El clientId es obligatorio");
        }
        if (membership.getType() == null || membership.getType().isBlank()) {
            errors.put("type", "El tipo de membresía es obligatorio");
        }
        if (membership.getStartDate() == null) {
            errors.put("startDate", "La fecha de inicio es obligatoria");
        }
        if (membership.getEndDate() == null) {
            errors.put("endDate", "La fecha de fin es obligatoria");
        }
        if (membership.getPrice() <= 0) {
            errors.put("price", "El precio debe ser mayor que 0");
        }
        if (membership.getStartDate() != null && membership.getEndDate() != null &&
            membership.getEndDate().isBefore(membership.getStartDate())) {
            errors.put("dateRange", "La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Membership created = membershipService.create(membership); // Crea la membresía usando el servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la membresía creada
        } catch (IllegalArgumentException ex) { // ID duplicado u otra validación
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            err.put("membresita", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Membership membership) { // Método para actualizar membresía
        Map<String, String> errors = new HashMap<>();
        if (membership == null) {
            errors.put("membership", "Payload de membresía es obligatorio");
            return ResponseEntity.badRequest().body(errors);
        }
        if (membership.getClientId() == null || membership.getClientId().isBlank()) {
            errors.put("clientId", "El clientId es obligatorio");
        }
        if (membership.getType() == null || membership.getType().isBlank()) {
            errors.put("type", "El tipo de membresía es obligatorio");
        }
        if (membership.getStartDate() == null) {
            errors.put("startDate", "La fecha de inicio es obligatoria");
        }
        if (membership.getEndDate() == null) {
            errors.put("endDate", "La fecha de fin es obligatoria");
        }
        if (membership.getPrice() <= 0) {
            errors.put("price", "El precio debe ser mayor que 0");
        }
        if (membership.getStartDate() != null && membership.getEndDate() != null &&
            membership.getEndDate().isBefore(membership.getStartDate())) {
            errors.put("dateRange", "La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Membership updated = membershipService.update(id, membership); // Actualiza la membresía usando el servicio
            if (updated != null) { // Si se actualizó correctamente
                return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la membresía actualizada
            } else { // Si no se encontró la membresía para actualizar
                return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
            }
        } catch (IllegalArgumentException ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            err.put("membresita", ex.getMessage() != null ? ex.getMessage() : "Solicitud inválida");
            return ResponseEntity.badRequest().body(err);
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar membresía por ID
        boolean deleted = membershipService.delete(id); // Elimina la membresía usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró la membresía para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase MembershipController
