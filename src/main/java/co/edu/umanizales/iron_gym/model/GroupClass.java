package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupClass  {
    private String id;
    private String name;
    private int maxCapacity;
    private LocalTime schedule;
    private Trainer trainer;
    private List<Client> registeredClients;
    
    public boolean addClient(Client client) {
        if (registeredClients == null) {
            registeredClients = new ArrayList<>();
        }
        if (registeredClients.size() < maxCapacity) {
            return registeredClients.add(client);
        }
        return false;
    }
}
