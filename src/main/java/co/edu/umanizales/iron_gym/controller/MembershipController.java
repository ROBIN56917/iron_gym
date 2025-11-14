package co.edu.umanizales.iron_gym.controller;

import co.edu.umanizales.iron_gym.model.Membership;
import co.edu.umanizales.iron_gym.model.MembershipTypeEnum;
import co.edu.umanizales.iron_gym.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing gym memberships.
 * Provides endpoints for CRUD operations and membership management.
 */
@RestController
@RequestMapping("/api/memberships")
@CrossOrigin(origins = "*")
public class MembershipController {
    
    @Autowired
    private MembershipService membershipService;
    
    /**
     * Gets all memberships.
     * HTTP GET: /api/memberships
     * @return List of all memberships
     */
    @GetMapping
    public ResponseEntity<List<Membership>> getAllMemberships() {
        try {
            List<Membership> memberships = membershipService.findAll();
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            System.err.println("Error getting all memberships: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets a membership by ID.
     * HTTP GET: /api/memberships/{id}
     * @param id The membership ID
     * @return Membership if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Membership> getMembershipById(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Optional<Membership> membership = membershipService.findById(id);
            if (membership.isPresent()) {
                return ResponseEntity.ok(membership.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error getting membership by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets memberships by type.
     * HTTP GET: /api/memberships/type/{type}
     * @param type The membership type (BASIC, PREMIUM, VIP)
     * @return List of memberships with specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Membership>> getMembershipsByType(@PathVariable String type) {
        try {
            if (type == null || type.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MembershipTypeEnum membershipType;
            try {
                membershipType = MembershipTypeEnum.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            
            List<Membership> memberships = membershipService.findByType(membershipType);
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            System.err.println("Error getting memberships by type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets active memberships.
     * HTTP GET: /api/memberships/active
     * @return List of active memberships
     */
    @GetMapping("/active")
    public ResponseEntity<List<Membership>> getActiveMemberships() {
        try {
            List<Membership> activeMemberships = membershipService.findActiveMemberships();
            return ResponseEntity.ok(activeMemberships);
        } catch (Exception e) {
            System.err.println("Error getting active memberships: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets expired memberships.
     * HTTP GET: /api/memberships/expired
     * @return List of expired memberships
     */
    @GetMapping("/expired")
    public ResponseEntity<List<Membership>> getExpiredMemberships() {
        try {
            List<Membership> expiredMemberships = membershipService.findExpiredMemberships();
            return ResponseEntity.ok(expiredMemberships);
        } catch (Exception e) {
            System.err.println("Error getting expired memberships: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Creates a new membership.
     * HTTP POST: /api/memberships
     * @param membership The membership to create
     * @return Created membership with 201 status, or error response
     */
    @PostMapping
    public ResponseEntity<Membership> createMembership(@RequestBody Membership membership) {
        try {
            if (membership == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Membership savedMembership = membershipService.save(membership);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMembership);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error creating membership: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error creating membership: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Creates a membership with automatic pricing.
     * HTTP POST: /api/memberships/auto
     * @param request Auto membership creation request
     * @return Created membership
     */
    @PostMapping("/auto")
    public ResponseEntity<Membership> createAutoMembership(@RequestBody AutoMembershipRequest request) {
        try {
            if (request == null || request.getType() == null || request.getStartDate() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getDurationMonths() <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            Membership membership = membershipService.createMembership(
                request.getType(),
                request.getStartDate(),
                request.getDurationMonths()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(membership);
            
        } catch (Exception e) {
            System.err.println("Error creating auto membership: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Updates an existing membership.
     * HTTP PUT: /api/memberships/{id}
     * @param id The membership ID to update
     * @param membership The updated membership data
     * @return Updated membership, or error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<Membership> updateMembership(@PathVariable String id, @RequestBody Membership membership) {
        try {
            if (id == null || id.trim().isEmpty() || membership == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Optional<Membership> existingMembership = membershipService.findById(id);
            if (existingMembership.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            membership.setId(id);
            Membership updatedMembership = membershipService.save(membership);
            return ResponseEntity.ok(updatedMembership);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error updating membership: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error updating membership: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Deletes a membership by ID.
     * HTTP DELETE: /api/memberships/{id}
     * @param id The membership ID to delete
     * @return 204 if deleted, 404 if not found, error response otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            boolean deleted = membershipService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error deleting membership: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Gets price for membership type.
     * HTTP GET: /api/memberships/price/{type}
     * @param type The membership type
     * @return Price information
     */
    @GetMapping("/price/{type}")
    public ResponseEntity<PriceResponse> getMembershipPrice(@PathVariable String type) {
        try {
            if (type == null || type.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MembershipTypeEnum membershipType;
            try {
                membershipType = MembershipTypeEnum.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            
            double price = membershipService.calculatePrice(membershipType);
            PriceResponse response = new PriceResponse(membershipType, price);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error getting membership price: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Request class for automatic membership creation.
     */
    public static class AutoMembershipRequest {
        private MembershipTypeEnum type;
        private LocalDate startDate;
        private int durationMonths = 1;
        
        // Getters and Setters
        public MembershipTypeEnum getType() { return type; }
        public void setType(MembershipTypeEnum type) { this.type = type; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public int getDurationMonths() { return durationMonths; }
        public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
    }
    
    /**
     * Response class for price information.
     */
    public static class PriceResponse {
        private MembershipTypeEnum type;
        private double monthlyPrice;
        
        public PriceResponse(MembershipTypeEnum type, double monthlyPrice) {
            this.type = type;
            this.monthlyPrice = monthlyPrice;
        }
        
        // Getters
        public MembershipTypeEnum getType() { return type; }
        public double getMonthlyPrice() { return monthlyPrice; }
    }
}
