package co.edu.umanizales.iron_gym.dto;

import co.edu.umanizales.iron_gym.model.Activity;
import co.edu.umanizales.iron_gym.model.Instructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWithInstructorDTO {
    private String id;
    private String name;
    private String description;
    private String schedule;
    private int maxCapacity;
    private InstructorDTO instructor;
    
    public static ActivityWithInstructorDTO from(Activity activity, Instructor instructor) {
        ActivityWithInstructorDTO dto = new ActivityWithInstructorDTO();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setDescription(activity.getDescription());
        dto.setSchedule(activity.getSchedule());
        dto.setMaxCapacity(activity.getMaxCapacity());
        
        if (instructor != null) {
            dto.setInstructor(InstructorDTO.from(instructor));
        }
        
        return dto;
    }
}
