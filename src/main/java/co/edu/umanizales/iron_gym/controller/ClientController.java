package co.edu.umanizales.iron_gym.controller;

import co.edu.umanizales.iron_gym.model.Client;
import co.edu.umanizales.iron_gym.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing clients.
 * Provides endpoints for CRUD operations and CSV export.
 */
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*") // Allows requests from any origin
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    /**
     * Gets all clients.
     * HTTP GET: /api/clients
     * @return List of all clients
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        try {
            List<Client> clients = clientService.findAll();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            System.err.println("Error getting all clients: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets a client by ID.
     * HTTP GET: /api/clients/{id}
     * @param id The client ID
     * @return Client if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Optional<Client> client = clientService.findById(id);
            if (client.isPresent()) {
                return ResponseEntity.ok(client.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error getting client by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets clients by membership type.
     * HTTP GET: /api/clients/membership/{type}
     * @param type The membership type (BASIC, PREMIUM, VIP)
     * @return List of clients with specified membership type
     */
    @GetMapping("/membership/{type}")
    public ResponseEntity<List<Client>> getClientsByMembershipType(@PathVariable String type) {
        try {
            if (type == null || type.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<Client> clients = clientService.findByMembershipType(type.toUpperCase());
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            System.err.println("Error getting clients by membership type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Creates a new client.
     * HTTP POST: /api/clients
     * @param client The client to create
     * @return Created client with 201 status, or error response
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        try {
            // Validate input
            if (client == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Save client (service will validate)
            Client savedClient = clientService.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error creating client: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error creating client: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Updates an existing client.
     * HTTP PUT: /api/clients/{id}
     * @param id The client ID to update
     * @param client The updated client data
     * @return Updated client, or error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody Client client) {
        try {
            // Validate input
            if (id == null || id.trim().isEmpty() || client == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if client exists
            Optional<Client> existingClient = clientService.findById(id);
            if (existingClient.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Set ID and update
            client.setId(id);
            Client updatedClient = clientService.save(client);
            return ResponseEntity.ok(updatedClient);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error updating client: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error updating client: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Deletes a client by ID.
     * HTTP DELETE: /api/clients/{id}
     * @param id The client ID to delete
     * @return 204 if deleted, 404 if not found, error response otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            boolean deleted = clientService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error deleting client: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Exports all clients to CSV format.
     * HTTP GET: /api/clients/export/csv
     * @return CSV file for download
     */
    @GetMapping("/export/csv")
    public ResponseEntity<String> exportClientsToCsv() {
        try {
            String csvContent = clientService.exportToCsv();
            
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv")
                    .header("Content-Disposition", "attachment; filename=clients.csv")
                    .body(csvContent);
                    
        } catch (Exception e) {
            System.err.println("Error exporting clients to CSV: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets statistics about clients.
     * HTTP GET: /api/clients/stats
     * @return Statistics about clients
     */
    @GetMapping("/stats")
    public ResponseEntity<ClientStats> getClientStatistics() {
        try {
            List<Client> allClients = clientService.findAll();
            
            ClientStats stats = new ClientStats();
            stats.setTotalClients(allClients.size());
            
            // Count by membership type
            long basicCount = allClients.stream()
                    .filter(c -> c.getMembership() != null && c.getMembership().getType().toString().equals("BASIC"))
                    .count();
            long premiumCount = allClients.stream()
                    .filter(c -> c.getMembership() != null && c.getMembership().getType().toString().equals("PREMIUM"))
                    .count();
            long vipCount = allClients.stream()
                    .filter(c -> c.getMembership() != null && c.getMembership().getType().toString().equals("VIP"))
                    .count();
            
            stats.setBasicMemberships((int) basicCount);
            stats.setPremiumMemberships((int) premiumCount);
            stats.setVipMemberships((int) vipCount);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            System.err.println("Error getting client statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Inner class for client statistics.
     */
    public static class ClientStats {
        private int totalClients;
        private int basicMemberships;
        private int premiumMemberships;
        private int vipMemberships;
        
        // Getters and Setters
        public int getTotalClients() { return totalClients; }
        public void setTotalClients(int totalClients) { this.totalClients = totalClients; }
        
        public int getBasicMemberships() { return basicMemberships; }
        public void setBasicMemberships(int basicMemberships) { this.basicMemberships = basicMemberships; }
        
        public int getPremiumMemberships() { return premiumMemberships; }
        public void setPremiumMemberships(int premiumMemberships) { this.premiumMemberships = premiumMemberships; }
        
        public int getVipMemberships() { return vipMemberships; }
        public void setVipMemberships(int vipMemberships) { this.vipMemberships = vipMemberships; }
    }
}
