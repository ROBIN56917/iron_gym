package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.Client;
import co.edu.umanizales.iron_gym.model.Membership;
import co.edu.umanizales.iron_gym.model.MembershipTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing clients.
 * Handles business logic and CSV file operations.
 */
@Service
public class ClientService {
    
    @Value("${csv.clients.path:src/main/resources/data/clients.csv}")
    private String csvFilePath;
    
    private List<Client> clientsCache = new ArrayList<>();
    private long lastModified = 0;
    
    /**
     * Gets all clients from CSV file.
     * Uses cache for better performance.
     * @return List of all clients
     */
    public List<Client> findAll() {
        try {
            // Check if file was modified to update cache
            File file = new File(csvFilePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            if (file.lastModified() > lastModified) {
                loadClientsFromFile();
                lastModified = file.lastModified();
            }
            
            return new ArrayList<>(clientsCache);
            
        } catch (Exception e) {
            System.err.println("Error reading clients file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds a client by their ID.
     * @param id The client ID to search for
     * @return Optional containing the client if found
     */
    public Optional<Client> findById(String id) {
        try {
            return findAll().stream()
                    .filter(client -> client.getId().equals(id))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding client by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Finds clients by their membership type.
     * @param membershipType The membership type to filter by
     * @return List of clients with specified membership type
     */
    public List<Client> findByMembershipType(String membershipType) {
        try {
            return findAll().stream()
                    .filter(client -> client.getMembership() != null)
                    .filter(client -> client.getMembership().getType().toString().equals(membershipType))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding clients by membership type: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Saves a client to CSV file.
     * Handles both create and update operations.
     * @param client The client to save
     * @return The saved client
     * @throws IllegalArgumentException if client data is invalid
     */
    public Client save(Client client) throws IllegalArgumentException {
        // Validate client data
        validateClient(client);
        
        List<Client> clients = findAll();
        
        try {
            // Generate ID if not provided
            if (client.getId() == null || client.getId().isEmpty()) {
                client.setId(UUID.randomUUID().toString());
            } else {
                // Remove existing client with same ID for update
                clients.removeIf(c -> c.getId().equals(client.getId()));
            }
            
            clients.add(client);
            saveAllToFile(clients);
            
            // Update cache
            clientsCache = new ArrayList<>(clients);
            
            return client;
            
        } catch (Exception e) {
            System.err.println("Error saving client: " + e.getMessage());
            throw new RuntimeException("Failed to save client: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a client by ID.
     * @param id The ID of the client to delete
     * @return true if client was deleted, false if not found
     */
    public boolean delete(String id) {
        List<Client> clients = findAll();
        boolean removed = clients.removeIf(client -> client.getId().equals(id));
        
        if (removed) {
            try {
                saveAllToFile(clients);
                clientsCache = new ArrayList<>(clients);
            } catch (Exception e) {
                System.err.println("Error deleting client: " + e.getMessage());
                throw new RuntimeException("Failed to delete client: " + e.getMessage(), e);
            }
        }
        
        return removed;
    }
    
    /**
     * Exports all clients to CSV format string.
     * @return CSV string containing all clients
     */
    public String exportToCsv() {
        try {
            List<Client> clients = findAll();
            StringBuilder csvBuilder = new StringBuilder();
            
            // CSV Header
            csvBuilder.append("ID,Name,Email,Phone,Membership Type,Start Date,End Date,Price\n");
            
            // CSV Data
            for (Client client : clients) {
                csvBuilder.append(clientToCsvString(client)).append("\n");
            }
            
            return csvBuilder.toString();
            
        } catch (Exception e) {
            System.err.println("Error exporting clients to CSV: " + e.getMessage());
            throw new RuntimeException("Failed to export clients: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates client data before saving.
     * @param client The client to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateClient(Client client) throws IllegalArgumentException {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Client name is required");
        }
        
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Client email is required");
        }
        
        if (!client.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Client phone is required");
        }
        
        // Validate membership if present
        if (client.getMembership() != null) {
            Membership membership = client.getMembership();
            if (membership.getStartDate() != null && membership.getEndDate() != null) {
                if (membership.getStartDate().isAfter(membership.getEndDate())) {
                    throw new IllegalArgumentException("Membership start date cannot be after end date");
                }
            }
        }
    }
    
    /**
     * Loads clients from CSV file into cache.
     */
    private void loadClientsFromFile() {
        List<Client> clients = new ArrayList<>();
        File file = new File(csvFilePath);
        
        if (!file.exists()) {
            clientsCache = clients;
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
                
                if (!line.trim().isEmpty()) {
                    try {
                        Client client = parseClientFromCsv(line);
                        if (client != null) {
                            clients.add(client);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing client line: " + line + " - " + e.getMessage());
                    }
                }
            }
            
            clientsCache = clients;
            
        } catch (IOException e) {
            System.err.println("Error reading clients file: " + e.getMessage());
            clientsCache = new ArrayList<>();
        }
    }
    
    /**
     * Saves all clients to CSV file.
     * @param clients List of clients to save
     */
    private void saveAllToFile(List<Client> clients) {
        File file = new File(csvFilePath);
        
        try {
            // Create directory if it doesn't exist
            file.getParentFile().mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write("ID,Name,Email,Phone,Membership Type,Start Date,End Date,Price\n");
                
                // Write data
                for (Client client : clients) {
                    writer.write(clientToCsvString(client));
                    writer.newLine();
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error writing clients file: " + e.getMessage());
            throw new RuntimeException("Failed to save clients to file", e);
        }
    }
    
    /**
     * Converts a client to CSV string format.
     * @param client The client to convert
     * @return CSV string representation
     */
    private String clientToCsvString(Client client) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(client.getId()).append(",");
        sb.append(client.getName()).append(",");
        sb.append(client.getEmail()).append(",");
        sb.append(client.getPhone()).append(",");
        
        if (client.getMembership() != null) {
            Membership m = client.getMembership();
            sb.append(m.getType()).append(",");
            sb.append(m.getStartDate() != null ? m.getStartDate().toString() : "").append(",");
            sb.append(m.getEndDate() != null ? m.getEndDate().toString() : "").append(",");
            sb.append(m.getPrice());
        } else {
            sb.append(",,,,");
        }
        
        return sb.toString();
    }
    
    /**
     * Parses a client from CSV string.
     * @param csvLine The CSV line to parse
     * @return Parsed Client object
     */
    private Client parseClientFromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 4) {
                return null;
            }
            
            Client client = new Client();
            client.setId(parts[0].trim());
            client.setName(parts[1].trim());
            client.setEmail(parts[2].trim());
            client.setPhone(parts[3].trim());
            
            // Parse membership if available
            if (parts.length >= 8 && !parts[4].trim().isEmpty()) {
                Membership membership = new Membership();
                membership.setId(UUID.randomUUID().toString());
                membership.setType(MembershipTypeEnum.valueOf(parts[4].trim()));
                
                if (!parts[5].trim().isEmpty()) {
                    membership.setStartDate(LocalDate.parse(parts[5].trim()));
                }
                if (!parts[6].trim().isEmpty()) {
                    membership.setEndDate(LocalDate.parse(parts[6].trim()));
                }
                if (!parts[7].trim().isEmpty()) {
                    membership.setPrice(Double.parseDouble(parts[7].trim()));
                }
                
                client.setMembership(membership);
            }
            
            return client;
            
        } catch (Exception e) {
            System.err.println("Error parsing client from CSV: " + e.getMessage());
            return null;
        }
    }
}
