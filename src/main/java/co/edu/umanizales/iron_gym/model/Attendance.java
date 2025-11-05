package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private String id;
        private LocalDateTime date;
    private Client client;
    private GroupClass groupClass;
    private boolean present;
}
