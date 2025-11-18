package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Equipment; // Importa la clase Equipment del paquete model
import co.edu.umanizales.iron_gym.service.EquipmentService; // Importa el servicio de equipos
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/equipments") // Define la ruta base para todos los endpoints de este controlador
public class EquipmentController { // Inicio de la clase EquipmentController - maneja peticiones HTTP para equipos
    
    @Autowired // Anotación para inyección automática del servicio
    private EquipmentService equipmentService; // Servicio que contiene la lógica de negocio para equipos

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Equipment>> getAll() { // Método para obtener todos los equipos
        return ResponseEntity.ok(equipmentService.getAll()); // Retorna respuesta HTTP 200 con la lista de equipos
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Equipment> getById(@PathVariable String id) { // Método para obtener equipo por ID
        Equipment equipment = equipmentService.getById(id);
        if (equipment != null) {
            return ResponseEntity.ok(equipment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        return ResponseEntity.ok(equipmentService.getAvailableEquipment());
    }

    @PostMapping
    public ResponseEntity<Equipment> create(@RequestBody Equipment equipment) {
        Equipment created = equipmentService.create(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> update(@PathVariable String id, @RequestBody Equipment equipment) {
        Equipment updated = equipmentService.update(id, equipment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else { // Si no se encontró el equipo para actualizar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar equipo por ID
        boolean deleted = equipmentService.delete(id); // Elimina el equipo usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el equipo para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
} // Fin de la clase EquipmentController
