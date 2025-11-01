package co.edu.umanizales.iron_gym.service;

import co.edu.umanizales.iron_gym.model.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class ActivityService {
    
    @Value("${csv.activities.path:src/main/resources/data/activities.csv}")
    private String csvFilePath;
    
    public List<Activity> findAll() {
        List<Activity> activities = new ArrayList<>();
        try {
            File file = new File(csvFilePath);
            if (!file.exists()) {
                return activities;
            }
            
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Activity activity = Activity.fromCsv(line);
                    if (activity != null) {
                        activities.add(activity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activities;
    }
    
    public Optional<Activity> findById(String id) {
        return findAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
    
    public List<Activity> findByInstructorId(String instructorId) {
        return findAll().stream()
                .filter(a -> a.getInstructorId().equals(instructorId))
                .toList();
    }
    
    public Activity save(Activity activity) {
        List<Activity> activities = findAll();
        
        if (activity.getId() == null || activity.getId().isEmpty()) {
            activity.setId(UUID.randomUUID().toString());
        } else {
            activities.removeIf(a -> a.getId().equals(activity.getId()));
        }
        
        activities.add(activity);
        saveAll(activities);
        return activity;
    }
    
    public boolean delete(String id) {
        List<Activity> activities = findAll();
        boolean removed = activities.removeIf(a -> a.getId().equals(id));
        if (removed) {
            saveAll(activities);
        }
        return removed;
    }
    
    private void saveAll(List<Activity> activities) {
        try {
            File file = new File(csvFilePath);
            file.getParentFile().mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Activity activity : activities) {
                    writer.write(activity.toCsv());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
