package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Trainer; // Importa la clase Trainer del paquete model
import co.edu.umanizales.iron_gym.service.TrainerService; // Importa el servicio de entrenadores
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones
import co.edu.umanizales.iron_gym.service.PersonService;
import co.edu.umanizales.iron_gym.model.Person;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/trainers") // Define la ruta base para todos los endpoints de este controlador
public class TrainerController { // Inicio de la clase TrainerController - maneja peticiones HTTP para entrenadores
    
    @Autowired // Anotación para inyección automática del servicio
    private TrainerService trainerService; // Servicio que contiene la lógica de negocio para entrenadores
    
    @Autowired
    private PersonService personService;

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Trainer>> getAll() { // Método para obtener todos los entrenadores
        return ResponseEntity.ok(trainerService.getAll()); // Retorna respuesta HTTP 200 con la lista de entrenadores
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Trainer> getById(@PathVariable String id) { // Método para obtener entrenador por ID
        Trainer trainer = trainerService.getById(id); // Busca entrenador por ID usando el servicio
        if (trainer != null) { // Si se encontró el entrenador
            return ResponseEntity.ok(trainer); // Retorna respuesta HTTP 200 con el entrenador encontrado
        } else { // Si no se encontró el entrenador
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<?> create(@Valid @RequestBody Trainer trainer, BindingResult result) { // Método para crear nuevo entrenador
        if (result.hasErrors()) { // Validaciones de bean validation (email, phone, etc.)
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        // Duplicados en identificación y teléfono como en persons
        if (trainerService.existsByIdentification(trainer.getIdentification())) {
            Map<String, String> error = new HashMap<>();
            error.put("identification", "Ya existe un entrenador con esta identificación");
            return ResponseEntity.badRequest().body(error);
        }
        if (trainerService.existsByPhone(trainer.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "Ya existe un entrenador con este teléfono");
            return ResponseEntity.badRequest().body(error);
        }
        // Validación de teléfono con todos los dígitos iguales
        if (isAllSameDigits(trainer.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        
        Trainer created = trainerService.create(trainer); // Crea el entrenador usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con el entrenador creado
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Trainer trainer, BindingResult result) { // Método para actualizar entrenador
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Trainer existing = trainerService.getById(id);
        if (existing != null) {
            if (!existing.getIdentification().equals(trainer.getIdentification()) &&
                trainerService.existsByIdentification(trainer.getIdentification())) {
                Map<String, String> error = new HashMap<>();
                error.put("identification", "Ya existe otro entrenador con esta identificación");
                return ResponseEntity.badRequest().body(error);
            }
            if (!existing.getPhone().equals(trainer.getPhone()) &&
                trainerService.existsByPhone(trainer.getPhone())) {
                Map<String, String> error = new HashMap<>();
                error.put("phone", "Ya existe otro entrenador con este teléfono");
                return ResponseEntity.badRequest().body(error);
            }
        }
        if (isAllSameDigits(trainer.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        
        Trainer updated = trainerService.update(id, trainer); // Actualiza el entrenador usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con el entrenador actualizado
        } else { // Si no se encontró el entrenador para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar entrenador por ID
        boolean deleted = trainerService.delete(id); // Elimina el entrenador usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el entrenador para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    // Crear/Asignar entrenador a partir de una persona existente
    @PostMapping("/from-person/{personId}")
    public ResponseEntity<?> createFromPerson(@PathVariable String personId) {
        Person person = personService.getById(personId);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada");
        }
        // Validaciones de duplicidad contra entrenadores
        if (trainerService.existsByIdentification(person.getIdentification())) {
            Map<String, String> error = new HashMap<>();
            error.put("identification", "Ya existe un entrenador con esta identificación");
            return ResponseEntity.badRequest().body(error);
        }
        if (trainerService.existsByPhone(person.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "Ya existe un entrenador con este teléfono");
            return ResponseEntity.badRequest().body(error);
        }
        if (isAllSameDigits(person.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        Trainer trainer = new Trainer(null, person.getName(), person.getEmail(), person.getPhone(), person.getIdentification());
        Trainer created = trainerService.create(trainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Método auxiliar similar al de PersonController
    private boolean isAllSameDigits(String phone) {
        if (phone == null || phone.length() != 10) {
            return false;
        }
        char firstDigit = phone.charAt(0);
        for (int i = 1; i < phone.length(); i++) {
            if (phone.charAt(i) != firstDigit) {
                return false;
            }
        }
        return true;
    }
} // Fin de la clase TrainerController
