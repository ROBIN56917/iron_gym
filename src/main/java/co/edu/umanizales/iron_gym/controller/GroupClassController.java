package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.GroupClass; // Importa la clase GroupClass del paquete model
import co.edu.umanizales.iron_gym.service.GroupClassService; // Importa el servicio de clases grupales
import co.edu.umanizales.iron_gym.model.Trainer; // Importa la clase Trainer
import co.edu.umanizales.iron_gym.service.TrainerService; // Importa el servicio de entrenadores
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/group-classes") // Define la ruta base para todos los endpoints de este controlador
public class GroupClassController { // Inicio de la clase GroupClassController - maneja peticiones HTTP para clases grupales
    
    @Autowired // Anotación para inyección automática del servicio
    private GroupClassService groupClassService; // Servicio que contiene la lógica de negocio para clases grupales
    
    @Autowired // Servicio de entrenadores para buscar por ID
    private TrainerService trainerService;

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<GroupClass>> getAll() { // Método para obtener todas las clases grupales
        return ResponseEntity.ok(groupClassService.getAll()); // Retorna respuesta HTTP 200 con la lista de clases grupales
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<GroupClass> getById(@PathVariable String id) { // Método para obtener clase grupal por ID
        GroupClass groupClass = groupClassService.getById(id); // Busca clase grupal por ID usando el servicio
        if (groupClass != null) { // Si se encontró la clase grupal
            return ResponseEntity.ok(groupClass); // Retorna respuesta HTTP 200 con la clase grupal encontrada
        } else { // Si no se encontró la clase grupal
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @GetMapping("/available") // Anotación que mapea peticiones HTTP GET a /available
    public ResponseEntity<List<GroupClass>> getAvailableClasses() { // Método para obtener clases grupales disponibles
        return ResponseEntity.ok(groupClassService.getAvailableClasses()); // Retorna respuesta HTTP 200 con clases disponibles
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<GroupClass> create(@RequestBody GroupClass groupClass) { // Método para crear nueva clase grupal
        GroupClass created = groupClassService.create(groupClass); // Crea la clase grupal usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la clase creada
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<GroupClass> update(@PathVariable String id, @RequestBody GroupClass groupClass) { // Método para actualizar clase grupal
        GroupClass updated = groupClassService.update(id, groupClass); // Actualiza la clase grupal usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la clase actualizada
        } else { // Si no se encontró la clase grupal para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar clase grupal por ID
        boolean deleted = groupClassService.delete(id); // Elimina la clase grupal usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró la clase grupal para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    // Endpoint sencillo para asignar un entrenador a una clase
    @PostMapping("/{classId}/assign-trainer/{trainerId}")
    public ResponseEntity<?> assignTrainerToClass(@PathVariable String classId, @PathVariable String trainerId) {
        GroupClass groupClass = groupClassService.getById(classId);
        if (groupClass == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clase no encontrada");
        }
        Trainer trainer = trainerService.getById(trainerId);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrenador no encontrado");
        }
        boolean ok = groupClassService.assignTrainerToClass(classId, trainer);
        if (ok) {
            return ResponseEntity.ok("Entrenador asignado exitosamente");
        }
        return ResponseEntity.badRequest().body("No se pudo asignar el entrenador");
    }
}
 // Fin de la clase GroupClassController
