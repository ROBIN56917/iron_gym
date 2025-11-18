package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Membership; // Importa la clase Membership del paquete model
import co.edu.umanizales.iron_gym.service.MembershipService; // Importa el servicio de membresías
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

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
    public ResponseEntity<Membership> create(@RequestBody Membership membership) { // Método para crear nueva membresía
        try {
            Membership created = membershipService.create(membership); // Crea la membresía usando el servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la membresía creada
        } catch (IllegalArgumentException ex) { // ID duplicado u otra validación
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Membership> update(@PathVariable String id, @RequestBody Membership membership) { // Método para actualizar membresía
        Membership updated = membershipService.update(id, membership); // Actualiza la membresía usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la membresía actualizada
        } else { // Si no se encontró la membresía para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
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
