package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.Instructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class InstructorService {
    
    @Value("${csv.instructors.path:src/main/resources/data/instructors.csv}")
    private String csvFilePath;
    
    public List<Instructor> findAll() {
        List<Instructor> instructors = new ArrayList<>();
        try {
            File file = new File(csvFilePath);
            if (!file.exists()) {
                return instructors;
            }
            
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Instructor instructor = Instructor.fromCsv(line);
                    if (instructor != null) {
                        instructors.add(instructor);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructors;
    }
    
    public Optional<Instructor> findById(String id) {
        return findAll().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
    
    public Instructor save(Instructor instructor) {
        List<Instructor> instructors = findAll();
        
        if (instructor.getId() == null || instructor.getId().isEmpty()) {
            instructor.setId(UUID.randomUUID().toString());
        } else {
            instructors.removeIf(i -> i.getId().equals(instructor.getId()));
        }
        
        instructors.add(instructor);
        saveAll(instructors);
        return instructor;
    }
    
    public boolean delete(String id) {
        List<Instructor> instructors = findAll();
        boolean removed = instructors.removeIf(i -> i.getId().equals(id));
        if (removed) {
            saveAll(instructors);
        }
        return removed;
    }
    
    private void saveAll(List<Instructor> instructors) {
        try {
            File file = new File(csvFilePath);
            file.getParentFile().mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Instructor instructor : instructors) {
                    writer.write(instructor.toCsv());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
