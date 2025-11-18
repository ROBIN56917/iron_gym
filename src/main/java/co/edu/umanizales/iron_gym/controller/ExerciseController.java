package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Exercise; // Importa la clase Exercise del paquete model
import co.edu.umanizales.iron_gym.service.ExerciseService; // Importa el servicio de ejercicios
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/exercises") // Define la ruta base para todos los endpoints de este controlador
public class ExerciseController { // Inicio de la clase ExerciseController - maneja peticiones HTTP para ejercicios
    
    @Autowired // Anotación para inyección automática del servicio
    private ExerciseService exerciseService; // Servicio que contiene la lógica de negocio para ejercicios

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Exercise>> getAll() { // Método para obtener todos los ejercicios
        return ResponseEntity.ok(exerciseService.getAll()); // Retorna respuesta HTTP 200 con la lista de ejercicios
    }

    @GetMapping("/{name}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Exercise> getByName(@PathVariable String name) { // Método para obtener ejercicio por nombre
        Exercise exercise = exerciseService.getByName(name); // Busca ejercicio por nombre usando el servicio
        if (exercise != null) { // Si se encontró el ejercicio
            return ResponseEntity.ok(exercise); // Retorna respuesta HTTP 200 con el ejercicio encontrado
        } else { // Si no se encontró el ejercicio
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<Exercise> create(@RequestBody Exercise exercise) { // Método para crear nuevo ejercicio
        Exercise created = exerciseService.create(exercise); // Crea el ejercicio usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con el ejercicio creado
    }

    @PutMapping("/{name}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Exercise> update(@PathVariable String name, @RequestBody Exercise exercise) { // Método para actualizar ejercicio
        Exercise updated = exerciseService.update(name, exercise); // Actualiza el ejercicio usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con el ejercicio actualizado
        } else { // Si no se encontró el ejercicio para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{name}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String name) { // Método para eliminar ejercicio por nombre
        boolean deleted = exerciseService.delete(name); // Elimina el ejercicio usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el ejercicio para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase ExerciseController
