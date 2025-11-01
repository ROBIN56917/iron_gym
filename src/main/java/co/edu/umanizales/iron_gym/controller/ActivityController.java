package co.edu.umanizales.iron_gym.controller;

import co.edu.umanizales.iron_gym.dto.ActivityWithInstructorDTO;
import co.edu.umanizales.iron_gym.model.Activity;
import co.edu.umanizales.iron_gym.model.Instructor;
import co.edu.umanizales.iron_gym.service.ActivityService;
import co.edu.umanizales.iron_gym.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private InstructorService instructorService;
    
    @GetMapping
    public ResponseEntity<List<ActivityWithInstructorDTO>> getAllActivities() {
        List<ActivityWithInstructorDTO> activities = activityService.findAll()
                .stream()
                .map(activity -> {
                    Instructor instructor = instructorService.findById(activity.getInstructorId()).orElse(null);
                    return ActivityWithInstructorDTO.from(activity, instructor);
                })
                .toList();
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ActivityWithInstructorDTO> getActivityById(@PathVariable String id) {
        Optional<Activity> activityOpt = activityService.findById(id);
        if (activityOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Activity activity = activityOpt.get();
        Instructor instructor = instructorService.findById(activity.getInstructorId()).orElse(null);
        return ResponseEntity.ok(ActivityWithInstructorDTO.from(activity, instructor));
    }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<ActivityWithInstructorDTO>> getActivitiesByInstructor(@PathVariable String instructorId) {
        Optional<Instructor> instructorOpt = instructorService.findById(instructorId);
        if (instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Instructor instructor = instructorOpt.get();
        List<ActivityWithInstructorDTO> activities = activityService.findByInstructorId(instructorId)
                .stream()
                .map(activity -> ActivityWithInstructorDTO.from(activity, instructor))
                .toList();
        return ResponseEntity.ok(activities);
    }
    
    @PostMapping
    public ResponseEntity<ActivityWithInstructorDTO> createActivity(@RequestBody Activity activity) {
        Optional<Instructor> instructorOpt = instructorService.findById(activity.getInstructorId());
        if (instructorOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Activity saved = activityService.save(activity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActivityWithInstructorDTO.from(saved, instructorOpt.get()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ActivityWithInstructorDTO> updateActivity(
            @PathVariable String id, 
            @RequestBody Activity activity) {
        Optional<Instructor> instructorOpt = instructorService.findById(activity.getInstructorId());
        if (instructorOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        activity.setId(id);
        Activity updated = activityService.save(activity);
        return ResponseEntity.ok(ActivityWithInstructorDTO.from(updated, instructorOpt.get()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable String id) {
        boolean deleted = activityService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
