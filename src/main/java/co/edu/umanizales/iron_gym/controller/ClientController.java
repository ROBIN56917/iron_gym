package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Client; // Importa la clase Client del paquete model
import co.edu.umanizales.iron_gym.service.ClientService; // Importa el servicio de clientes
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

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
    public ResponseEntity<Client> create(@RequestBody Client client) { // Método para crear nuevo cliente
        Client created = clientService.create(client); // Crea el cliente usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con el cliente creado
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Client> update(@PathVariable String id, @RequestBody Client client) { // Método para actualizar cliente
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
} // Fin de la clase ClientController
