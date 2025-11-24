package co.edu.umanizales.iron_gym.model;

import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;
import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Represents a gym membership in the system.
 * Contains information about the membership type, validity dates and price.
 */
public class Membership {
    private String id;
    @JsonAlias({"clientId", "personId"})
    private String clientId; // Referencia al ID del cliente dueño de la membresía (acepta personId en requests)
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    
    public Membership() {
    }
    
    public Membership(String id, String clientId, String type, LocalDate startDate, LocalDate endDate, double price) {
        this.id = id;
        this.clientId = clientId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    // Método para verificar si la membresía está activa actualmente
    public boolean isActive() { // Retorna true si la membresía está vigente, false si no
        LocalDate today = LocalDate.now(); // Obtiene la fecha actual del sistema
        if (startDate != null && endDate != null) { // Verifica que ambas fechas existan
            if (today.isAfter(startDate) && today.isBefore(endDate)) { // Si hoy está después del inicio y antes del fin
                return true; // La membresía está activa
            } else if (today.isEqual(startDate) || today.isEqual(endDate)) { // Si hoy es exactamente el día de inicio o fin
                return true; // La membresía está activa (incluye los días límite)
            } else { // Si hoy está fuera del rango de fechas
                return false; // La membresía no está activa
            }
        } else { // Si alguna de las fechas es nula
            return false; // La membresía no está activa por datos incompletos
        }
    }

    // Formato de precio en pesos colombianos para respuestas JSON (e.g., "$100.000")
    public String getPriceFormatted() {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            nf.setMaximumFractionDigits(0);
            nf.setMinimumFractionDigits(0);
            String formatted = nf.format(Math.round(this.price));
            return formatted.replaceAll("\\s+", "");
        } catch (Exception e) {
            return "$" + String.format("%,.0f", this.price).replace(',', '.');
        }
    }
} // Fin de la clase Membership
