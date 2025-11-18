package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Routine; // Importa la clase Routine del paquete model
import co.edu.umanizales.iron_gym.service.RoutineService; // Importa el servicio de rutinas
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/routines") // Define la ruta base para todos los endpoints de este controlador
public class RoutineController { // Inicio de la clase RoutineController - maneja peticiones HTTP para rutinas
    
    @Autowired // Anotación para inyección automática del servicio
    private RoutineService routineService; // Servicio que contiene la lógica de negocio para rutinas

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Routine>> getAll() { // Método para obtener todas las rutinas
        return ResponseEntity.ok(routineService.getAll()); // Retorna respuesta HTTP 200 con la lista de rutinas
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Routine> getById(@PathVariable String id) { // Método para obtener rutina por ID
        Routine routine = routineService.getById(id); // Busca rutina por ID usando el servicio
        if (routine != null) { // Si se encontró la rutina
            return ResponseEntity.ok(routine); // Retorna respuesta HTTP 200 con la rutina encontrada
        } else { // Si no se encontró la rutina
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<Routine> create(@RequestBody Routine routine) { // Método para crear nueva rutina
        Routine created = routineService.create(routine); // Crea la rutina usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con la rutina creada
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Routine> update(@PathVariable String id, @RequestBody Routine routine) { // Método para actualizar rutina
        Routine updated = routineService.update(id, routine); // Actualiza la rutina usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con la rutina actualizada
        } else { // Si no se encontró la rutina para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar rutina por ID
        boolean deleted = routineService.delete(id); // Elimina la rutina usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró la rutina para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase RoutineController
