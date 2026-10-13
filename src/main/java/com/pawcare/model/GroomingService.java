package com.pawcare.model;

/** Data only; SQL belongs in the DAO layer. */
public class GroomingService {
    private int id;
    private String serviceName;
    private String description;
    private java.math.BigDecimal price;
    private int durationMinutes;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public java.math.BigDecimal getPrice() { return price; }
    public void setPrice(java.math.BigDecimal price) { this.price = price; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    @Override public String toString() { return serviceName; }
}
