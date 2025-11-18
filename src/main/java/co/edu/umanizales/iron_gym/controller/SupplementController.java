package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Supplement; // Importa la clase Supplement del paquete model
import co.edu.umanizales.iron_gym.service.SupplementService; // Importa el servicio de suplementos
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/supplements") // Define la ruta base para todos los endpoints de este controlador
public class SupplementController { // Inicio de la clase SupplementController - maneja peticiones HTTP para suplementos
    
    @Autowired // Anotación para inyección automática del servicio
    private SupplementService supplementService; // Servicio que contiene la lógica de negocio para suplementos

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Supplement>> getAll() { // Método para obtener todos los suplementos
        return ResponseEntity.ok(supplementService.getAll()); // Retorna respuesta HTTP 200 con la lista de suplementos
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Supplement> getById(@PathVariable String id) { // Método para obtener suplemento por ID
        Supplement supplement = supplementService.getById(id); // Busca suplemento por ID usando el servicio
        if (supplement != null) { // Si se encontró el suplemento
            return ResponseEntity.ok(supplement); // Retorna respuesta HTTP 200 con el suplemento encontrado
        } else { // Si no se encontró el suplemento
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @PostMapping // Anotación que mapea peticiones HTTP POST a este método
    public ResponseEntity<Supplement> create(@RequestBody Supplement supplement) { // Método para crear nuevo suplemento
        Supplement created = supplementService.create(supplement); // Crea el suplemento usando el servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // Retorna respuesta HTTP 201 con el suplemento creado
    }

    @PutMapping("/{id}") // Anotación que mapea peticiones HTTP PUT con parámetro de ruta
    public ResponseEntity<Supplement> update(@PathVariable String id, @RequestBody Supplement supplement) { // Método para actualizar suplemento
        Supplement updated = supplementService.update(id, supplement); // Actualiza el suplemento usando el servicio
        if (updated != null) { // Si se actualizó correctamente
            return ResponseEntity.ok(updated); // Retorna respuesta HTTP 200 con el suplemento actualizado
        } else { // Si no se encontró el suplemento para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = supplementService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
