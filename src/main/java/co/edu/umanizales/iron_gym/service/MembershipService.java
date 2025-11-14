package co.edu.umanizales.iron_gym.service;

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
 * Service class for managing gym memberships.
 * Handles business logic and CSV file operations.
 */
@Service
public class MembershipService {
    
    @Value("${csv.memberships.path:src/main/resources/data/memberships.csv}")
    private String csvFilePath;
    
    private List<Membership> membershipsCache = new ArrayList<>();
    private long lastModified = 0;
    
    /**
     * Gets all memberships from CSV file.
     * Uses cache for better performance.
     * @return List of all memberships
     */
    public List<Membership> findAll() {
        try {
            File file = new File(csvFilePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            if (file.lastModified() > lastModified) {
                loadMembershipsFromFile();
                lastModified = file.lastModified();
            }
            
            return new ArrayList<>(membershipsCache);
            
        } catch (Exception e) {
            System.err.println("Error reading memberships file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds a membership by its ID.
     * @param id The membership ID to search for
     * @return Optional containing the membership if found
     */
    public Optional<Membership> findById(String id) {
        try {
            return findAll().stream()
                    .filter(membership -> membership.getId().equals(id))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding membership by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Finds memberships by type.
     * @param type The membership type to filter by
     * @return List of memberships with specified type
     */
    public List<Membership> findByType(MembershipTypeEnum type) {
        try {
            return findAll().stream()
                    .filter(membership -> membership.getType().equals(type))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding memberships by type: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds active memberships (valid today).
     * @return List of active memberships
     */
    public List<Membership> findActiveMemberships() {
        try {
            LocalDate today = LocalDate.now();
            return findAll().stream()
                    .filter(membership -> membership.getStartDate() != null)
                    .filter(membership -> membership.getEndDate() != null)
                    .filter(membership -> 
                        !today.isBefore(membership.getStartDate()) && 
                        !today.isAfter(membership.getEndDate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding active memberships: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds expired memberships.
     * @return List of expired memberships
     */
    public List<Membership> findExpiredMemberships() {
        try {
            LocalDate today = LocalDate.now();
            return findAll().stream()
                    .filter(membership -> membership.getEndDate() != null)
                    .filter(membership -> today.isAfter(membership.getEndDate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding expired memberships: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Saves a membership to CSV file.
     * Handles both create and update operations.
     * @param membership The membership to save
     * @return The saved membership
     * @throws IllegalArgumentException if membership data is invalid
     */
    public Membership save(Membership membership) throws IllegalArgumentException {
        validateMembership(membership);
        
        List<Membership> memberships = findAll();
        
        try {
            if (membership.getId() == null || membership.getId().isEmpty()) {
                membership.setId(UUID.randomUUID().toString());
            } else {
                memberships.removeIf(m -> m.getId().equals(membership.getId()));
            }
            
            memberships.add(membership);
            saveAllToFile(memberships);
            
            membershipsCache = new ArrayList<>(memberships);
            
            return membership;
            
        } catch (Exception e) {
            System.err.println("Error saving membership: " + e.getMessage());
            throw new RuntimeException("Failed to save membership: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a membership by ID.
     * @param id The ID of the membership to delete
     * @return true if membership was deleted, false if not found
     */
    public boolean delete(String id) {
        List<Membership> memberships = findAll();
        boolean removed = memberships.removeIf(membership -> membership.getId().equals(id));
        
        if (removed) {
            try {
                saveAllToFile(memberships);
                membershipsCache = new ArrayList<>(memberships);
            } catch (Exception e) {
                System.err.println("Error deleting membership: " + e.getMessage());
                throw new RuntimeException("Failed to delete membership: " + e.getMessage(), e);
            }
        }
        
        return removed;
    }
    
    /**
     * Calculates the price for a membership type.
     * @param type The membership type
     * @return The price for the membership type
     */
    public double calculatePrice(MembershipTypeEnum type) {
        switch (type) {
            case BASIC:
                return 29.99;
            case PREMIUM:
                return 59.99;
            case VIP:
                return 99.99;
            default:
                return 0.0;
        }
    }
    
    /**
     * Creates a new membership with automatic pricing.
     * @param type The membership type
     * @param startDate The start date
     * @param durationMonths Duration in months
     * @return Created membership
     */
    public Membership createMembership(MembershipTypeEnum type, LocalDate startDate, int durationMonths) {
        try {
            Membership membership = new Membership();
            membership.setType(type);
            membership.setStartDate(startDate);
            membership.setEndDate(startDate.plusMonths(durationMonths));
            membership.setPrice(calculatePrice(type) * durationMonths);
            
            return save(membership);
            
        } catch (Exception e) {
            System.err.println("Error creating membership: " + e.getMessage());
            throw new RuntimeException("Failed to create membership: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates membership data before saving.
     * @param membership The membership to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateMembership(Membership membership) throws IllegalArgumentException {
        if (membership == null) {
            throw new IllegalArgumentException("Membership cannot be null");
        }
        
        if (membership.getType() == null) {
            throw new IllegalArgumentException("Membership type is required");
        }
        
        if (membership.getStartDate() == null) {
            throw new IllegalArgumentException("Membership start date is required");
        }
        
        if (membership.getEndDate() == null) {
            throw new IllegalArgumentException("Membership end date is required");
        }
        
        if (membership.getStartDate().isAfter(membership.getEndDate())) {
            throw new IllegalArgumentException("Membership start date cannot be after end date");
        }
        
        if (membership.getPrice() < 0) {
            throw new IllegalArgumentException("Membership price cannot be negative");
        }
    }
    
    /**
     * Loads memberships from CSV file into cache.
     */
    private void loadMembershipsFromFile() {
        List<Membership> memberships = new ArrayList<>();
        File file = new File(csvFilePath);
        
        if (!file.exists()) {
            membershipsCache = memberships;
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                if (!line.trim().isEmpty()) {
                    try {
                        Membership membership = parseMembershipFromCsv(line);
                        if (membership != null) {
                            memberships.add(membership);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing membership line: " + line + " - " + e.getMessage());
                    }
                }
            }
            
            membershipsCache = memberships;
            
        } catch (IOException e) {
            System.err.println("Error reading memberships file: " + e.getMessage());
            membershipsCache = new ArrayList<>();
        }
    }
    
    /**
     * Saves all memberships to CSV file.
     * @param memberships List of memberships to save
     */
    private void saveAllToFile(List<Membership> memberships) {
        File file = new File(csvFilePath);
        
        try {
            file.getParentFile().mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID,Type,Start Date,End Date,Price\n");
                
                for (Membership membership : memberships) {
                    writer.write(membershipToCsvString(membership));
                    writer.newLine();
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error writing memberships file: " + e.getMessage());
            throw new RuntimeException("Failed to save memberships to file", e);
        }
    }
    
    /**
     * Converts a membership to CSV string format.
     * @param membership The membership to convert
     * @return CSV string representation
     */
    private String membershipToCsvString(Membership membership) {
        return String.join(",",
            membership.getId(),
            membership.getType().toString(),
            membership.getStartDate().toString(),
            membership.getEndDate().toString(),
            String.valueOf(membership.getPrice())
        );
    }
    
    /**
     * Parses a membership from CSV string.
     * @param csvLine The CSV line to parse
     * @return Parsed Membership object
     */
    private Membership parseMembershipFromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 5) {
                return null;
            }
            
            Membership membership = new Membership();
            membership.setId(parts[0].trim());
            membership.setType(MembershipTypeEnum.valueOf(parts[1].trim()));
            membership.setStartDate(LocalDate.parse(parts[2].trim()));
            membership.setEndDate(LocalDate.parse(parts[3].trim()));
            membership.setPrice(Double.parseDouble(parts[4].trim()));
            
            return membership;
            
        } catch (Exception e) {
            System.err.println("Error parsing membership from CSV: " + e.getMessage());
            return null;
        }
    }
}
