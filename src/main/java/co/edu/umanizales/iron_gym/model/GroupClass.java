package co.edu.umanizales.iron_gym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group fitness class offered at the gym.
 * Classes have a schedule, capacity limit, and assigned trainer.
 * Clients can register for these classes.
 */
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
    
    /**
     * Adds a client to this class if there is space available.
     * @param client the client to add
     * @return true if the client was added successfully, false if class is full
     */
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
