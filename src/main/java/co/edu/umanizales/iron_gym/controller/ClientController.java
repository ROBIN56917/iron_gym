package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Client; // Importa la clase Client del paquete model
import co.edu.umanizales.iron_gym.service.ClientService; // Importa el servicio de clientes
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST
import jakarta.validation.Valid; // Para activar validaciones sobre el modelo Client (hereda de Person)
import org.springframework.validation.BindingResult; // Para capturar errores de validación
import org.springframework.validation.FieldError; // Para detallar errores por campo

import java.util.List; // Importa la interfaz List para trabajar con colecciones
import java.util.Map; // Para respuestas de error estructuradas
import java.util.HashMap; // Implementación de Map

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/clients") // Define la ruta base para todos los endpoints de este controlador
public class ClientController { // Inicio de la clase ClientController - maneja peticiones HTTP para clientes
    
    @Autowired // Anotación para inyección automática del servicio
    private ClientService clientService; // Servicio que contiene la lógica de negocio para clientes

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Client>> getAll() { // Método para obtener todos los clientes
        return ResponseEntity.ok(clientService.getAll()); // Retorna respuesta HTTP 200 con la lista de clientes
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Client> getById(@PathVariable String id) { // Método para obtener cliente por ID
        Client client = clientService.getById(id); // Busca cliente por ID usando el servicio
        if (client != null) { // Si se encontró el cliente
            return ResponseEntity.ok(client); // Retorna respuesta HTTP 200 con el cliente encontrado
        } else { // Si no se encontró el cliente
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @GetMapping("/with-membership") // Anotación que mapea peticiones HTTP GET a /with-membership
    public ResponseEntity<List<Client>> getClientsWithMembership() { // Método para obtener clientes con membresía
        return ResponseEntity.ok(clientService.getClientsWithMembership()); // Retorna respuesta HTTP 200 con clientes con membresía
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) { // Método para crear nuevo cliente con validaciones
        if (result.hasErrors()) { // Si hay errores de validación en los campos heredados de Person
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        // Validaciones de duplicidad
        if (clientService.isNameDuplicate(client.getName())) {
            Map<String, String> error = new HashMap<>();
            error.put("name", "Ya existe un cliente con este nombre");
            return ResponseEntity.badRequest().body(error);
        }
        if (clientService.isEmailDuplicate(client.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("email", "Ya existe un cliente con este email");
            return ResponseEntity.badRequest().body(error);
        }
        if (clientService.isIdentificationDuplicate(client.getIdentification())) {
            Map<String, String> error = new HashMap<>();
            error.put("identification", "Ya existe un cliente con esta identificación");
            return ResponseEntity.badRequest().body(error);
        }
        if (clientService.isPhoneDuplicate(client.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "Ya existe un cliente con este teléfono");
            return ResponseEntity.badRequest().body(error);
        }
        // Validar que el teléfono no tenga todos los dígitos iguales
        if (isAllSameDigits(client.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        Client created = clientService.create(client); // Crea el cliente usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con el cliente creado
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Client client, BindingResult result) { // Método para actualizar cliente con validaciones
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Client existing = clientService.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        // Duplicados solo si el valor cambia
        if (!existing.getName().equals(client.getName()) && clientService.isNameDuplicate(client.getName())) {
            Map<String, String> error = new HashMap<>();
            error.put("name", "Ya existe otro cliente con este nombre");
            return ResponseEntity.badRequest().body(error);
        }
        if (!existing.getEmail().equals(client.getEmail()) && clientService.isEmailDuplicate(client.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("email", "Ya existe otro cliente con este email");
            return ResponseEntity.badRequest().body(error);
        }
        if (!existing.getIdentification().equals(client.getIdentification()) && clientService.isIdentificationDuplicate(client.getIdentification())) {
            Map<String, String> error = new HashMap<>();
            error.put("identification", "Ya existe otro cliente con esta identificación");
            return ResponseEntity.badRequest().body(error);
        }
        if (!existing.getPhone().equals(client.getPhone()) && clientService.isPhoneDuplicate(client.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "Ya existe otro cliente con este teléfono");
            return ResponseEntity.badRequest().body(error);
        }
        // Validar que el teléfono no tenga todos los dígitos iguales
        if (isAllSameDigits(client.getPhone())) {
            Map<String, String> error = new HashMap<>();
            error.put("phone", "El teléfono no puede tener todos los dígitos iguales");
            return ResponseEntity.badRequest().body(error);
        }
        Client updated = clientService.update(id, client); // Actualiza el cliente usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con el cliente actualizado
        } else { // Si no se encontró el cliente para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar cliente por ID
        boolean deleted = clientService.delete(id); // Elimina el cliente usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el cliente para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    // Método auxiliar para validar que no todos los dígitos sean iguales (mismo criterio que en PersonController)
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
} // Fin de la clase ClientController
