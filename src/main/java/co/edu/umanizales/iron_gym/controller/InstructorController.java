package co.edu.umanizales.iron_gym.controller;

import co.edu.umanizales.iron_gym.dto.InstructorDTO;
import co.edu.umanizales.iron_gym.model.Instructor;
import co.edu.umanizales.iron_gym.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {
    
    @Autowired
    private InstructorService instructorService;
    
    @GetMapping
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.findAll()
                .stream()
                .map(InstructorDTO::from)
                .toList();
        return ResponseEntity.ok(instructors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InstructorDTO> getInstructorById(@PathVariable String id) {
        return instructorService.findById(id)
                .map(InstructorDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<InstructorDTO> createInstructor(@RequestBody Instructor instructor) {
        Instructor saved = instructorService.save(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstructorDTO.from(saved));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InstructorDTO> updateInstructor(
            @PathVariable String id, 
            @RequestBody Instructor instructor) {
        instructor.setId(id);
        Instructor updated = instructorService.save(instructor);
        return ResponseEntity.ok(InstructorDTO.from(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable String id) {
        boolean deleted = instructorService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
