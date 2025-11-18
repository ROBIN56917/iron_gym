package co.edu.umanizales.iron_gym.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group fitness class offered at the gym.
 * Classes have a schedule, capacity limit, and assigned trainer.
 */
public class GroupClass {
    private String id;
    private String name;
    private int maxCapacity;
    private String schedule;
    private Trainer trainer;
    private List<Client> registeredClients;
    
    public GroupClass() {
        this.registeredClients = new ArrayList<>();
    }
    
    public GroupClass(String id, String name, int maxCapacity, String schedule) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.schedule = schedule;
        this.registeredClients = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public Trainer getTrainer() {
        return trainer;
    }
    
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
    
    public List<Client> getRegisteredClients() {
        return registeredClients;
    }
    
    public void setRegisteredClients(List<Client> registeredClients) {
        this.registeredClients = registeredClients;
    }
    
    public boolean addClient(Client client) {
        if (registeredClients == null) {
            registeredClients = new ArrayList<>();
        }
        
        if (registeredClients.size() < maxCapacity) {
            registeredClients.add(client);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isFull() {
        if (registeredClients.size() >= maxCapacity) {
            return true;
        } else {
            return false;
        }
    }
}
