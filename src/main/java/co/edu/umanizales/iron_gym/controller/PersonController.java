package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentra esta clase controladora

import co.edu.umanizales.iron_gym.model.Person; // Importa la clase Person del paquete model
import co.edu.umanizales.iron_gym.model.Membership;
import co.edu.umanizales.iron_gym.model.GroupClass;
import co.edu.umanizales.iron_gym.model.Trainer;
import co.edu.umanizales.iron_gym.service.PersonService; // Importa la clase PersonService del paquete service
import co.edu.umanizales.iron_gym.service.MembershipService;
import co.edu.umanizales.iron_gym.service.GroupClassService;
import jakarta.validation.Valid; // Importa la anotación Valid para activar validaciones
import org.springframework.beans.factory.annotation.Autowired; // Para inyectar dependencias automáticamente
import org.springframework.http.HttpStatus; // Para usar códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Para construir respuestas HTTP personalizadas
import org.springframework.validation.BindingResult; // Para capturar resultados de validación
import org.springframework.validation.FieldError; // Para manejar errores de campo específicos
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de controladores REST

import java.util.HashMap; // Para crear mapas clave-valor para respuestas de error
import java.util.List; // Para trabajar con listas de personas
import java.util.Map; // Para manejar colecciones clave-valor genéricas
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController // Anotación que indica que esta es una clase controladora REST
@RequestMapping("/api/persons") // Define la URL base para todos los endpoints de este controlador
public class PersonController { // Inicio de la clase PersonController - maneja las peticiones HTTP
    
    @Autowired // Anotación para inyectar automáticamente una instancia de PersonService
    private PersonService personService; // Servicio que contiene la lógica de negocio para personas
    
    @Autowired
    private MembershipService membershipService;
    
    @Autowired
    private GroupClassService groupClassService;

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Person>> getAll() { // Método para obtener todas las personas
        return ResponseEntity.ok(personService.getAll()); // Retorna respuesta HTTP 200 con la lista de todas las personas
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Person> getById(@PathVariable String id) { // Método para obtener persona por ID
        Person person = personService.getById(id); // Busca la persona por ID usando el servicio
        if (person != null) { // Si se encontró la persona
            return ResponseEntity.ok(person); // Retorna respuesta HTTP 200 con la persona encontrada
        } else { // Si no se encontró la persona
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (Not Found)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<?> create(@Valid @RequestBody Person person, BindingResult result) { // Método para crear nueva persona
        if (result.hasErrors()) { // Si hay errores de validación en los campos
            Map<String, String> errors = new HashMap<>(); // Crea mapa para almacenar errores
            for (FieldError error : result.getFieldErrors()) { // Recorre todos los errores de campo
                errors.put(error.getField(), error.getDefaultMessage()); // Agrega nombre del campo y mensaje de error
            }
            return ResponseEntity.badRequest().body(errors); // Retorna HTTP 400 con los errores de validación
        }
        
        // Verificar si la identificación ya existe
        if (personService.existsByIdentification(person.getIdentification())) { // Si la identificación ya está en uso
            Map<String, String> error = new HashMap<>(); // Crea mapa para el error específico
            error.put("identification", "Ya existe una persona con esta identificación"); // Mensaje de error
            return ResponseEntity.badRequest().body(error); // Retorna HTTP 400 con el error de duplicidad
        }
        
        // Verificar si el teléfono ya existe
        if (personService.existsByPhone(person.getPhone())) { // Si el teléfono ya está en uso
            Map<String, String> error = new HashMap<>(); // Crea mapa para el error específico
            error.put("phone", "Ya existe una persona con este teléfono"); // Mensaje de error
            return ResponseEntity.badRequest().body(error); // Retorna HTTP 400 con el error de duplicidad
        }
        
        // Validar que el teléfono no tenga todos los números iguales
        if (isAllSameDigits(person.getPhone())) { // Si todos los dígitos del teléfono son iguales
            Map<String, String> error = new HashMap<>(); // Crea mapa para el error específico
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales"); // Mensaje de error
            return ResponseEntity.badRequest().body(error); // Retorna HTTP 400 con el error de validación
        }
        
        Person created = personService.create(person); // Crea la nueva persona usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna HTTP 201 con la persona creada
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Person person, BindingResult result) { // Método para actualizar persona
        if (result.hasErrors()) { // Si hay errores de validación en los campos
            Map<String, String> errors = new HashMap<>(); // Crea mapa para almacenar errores
            for (FieldError error : result.getFieldErrors()) { // Recorre todos los errores de campo
                errors.put(error.getField(), error.getDefaultMessage()); // Agrega nombre del campo y mensaje de error
            }
            return ResponseEntity.badRequest().body(errors); // Retorna HTTP 400 con los errores de validación
        }
        
        // Verificar si la identificación ya existe en otra persona
        Person existingPerson = personService.getById(id); // Obtiene la persona existente por ID
        if (existingPerson != null && !existingPerson.getIdentification().equals(person.getIdentification())) { // Si la identificación cambió
            if (personService.existsByIdentification(person.getIdentification())) { // Si la nueva identificación ya existe
                Map<String, String> error = new HashMap<>(); // Crea mapa para el error específico
                error.put("identification", "Ya existe otra persona con esta identificación"); // Mensaje de error
                return ResponseEntity.badRequest().body(error); // Retorna HTTP 400 con el error de duplicidad
            }
        }
        
        // Verificar si el teléfono ya existe en otra persona
        if (existingPerson != null && !existingPerson.getPhone().equals(person.getPhone())) {
            if (personService.existsByPhone(person.getPhone())) {
                Map<String, String> error = new HashMap<>();
                error.put("phone", "Ya existe otra persona con este teléfono");
                return ResponseEntity.badRequest().body(error);
            }
        }
        
        // Validar que el teléfono no tenga todos los números iguales
        if (isAllSameDigits(person.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        
        Person updated = personService.update(id, person);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = personService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Método auxiliar para validar que no todos los dígitos sean iguales
    private boolean isAllSameDigits(String phone) { // Método privado para verificar si todos los dígitos del teléfono son iguales
        if (phone == null || phone.length() != 10) { // Verifica si el teléfono es nulo o no tiene 10 dígitos
            return false; // Retorna false si no cumple las condiciones básicas
        }
        char firstDigit = phone.charAt(0); // Obtiene el primer dígito del teléfono
        for (int i = 1; i < phone.length(); i++) { // Recorre desde el segundo dígito hasta el final
            if (phone.charAt(i) != firstDigit) { // Compara cada dígito con el primero
                return false; // Si encuentra un dígito diferente, retorna false
            }
        }
        return true; // Si todos los dígitos son iguales, retorna true
    }

    // Endpoint: obtener la membresía asociada al ID de persona
    @GetMapping("/{id}/membership")
    public ResponseEntity<Membership> getMembershipByPerson(@PathVariable String id) {
        Person person = personService.getById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        Membership membership = membershipService.getByClientId(id);
        if (membership == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(membership);
    }

    // Endpoint: obtener las clases donde está registrado el ID de persona
    @GetMapping("/{id}/classes")
    public ResponseEntity<List<GroupClass>> getClassesByPerson(@PathVariable String id) {
        Person person = personService.getById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        List<GroupClass> classes = new ArrayList<>();
        for (GroupClass gc : groupClassService.getAll()) {
            if (gc.getRegisteredClients() != null) {
                boolean enrolled = gc.getRegisteredClients().stream().anyMatch(c -> id.equals(c.getId()));
                if (enrolled) {
                    classes.add(gc);
                }
            }
        }
        return ResponseEntity.ok(classes);
    }

    // Endpoint: obtener entrenadores de las clases donde está el ID de persona (sin duplicados)
    @GetMapping("/{id}/trainers")
    public ResponseEntity<List<Trainer>> getTrainersByPerson(@PathVariable String id) {
        Person person = personService.getById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        List<GroupClass> classes = new ArrayList<>();
        for (GroupClass gc : groupClassService.getAll()) {
            if (gc.getRegisteredClients() != null) {
                boolean enrolled = gc.getRegisteredClients().stream().anyMatch(c -> id.equals(c.getId()));
                if (enrolled) {
                    classes.add(gc);
                }
            }
        }
        List<Trainer> trainers = classes.stream()
                .map(GroupClass::getTrainer)
                .filter(t -> t != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Trainer::getId, t -> t, (a, b) -> a),
                        m -> new ArrayList<>(m.values())));
        return ResponseEntity.ok(trainers);
    }
}
 // Fin de la clase PersonController
