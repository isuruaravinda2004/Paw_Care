package com.pawcare.model;

/** Data only; SQL belongs in the DAO layer. */
public class Appointment {
    private int id;
    private int customerId;
    private int petId;
    private int serviceId;
    private java.time.LocalDate date;
    private java.time.LocalTime time;
    private String status;
    private java.math.BigDecimal bookedPrice;
    private int durationMinutes;
    private String customerName;
    private String petName;
    private String serviceName;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public java.time.LocalDate getDate() { return date; }
    public void setDate(java.time.LocalDate date) { this.date = date; }
    public java.time.LocalTime getTime() { return time; }
    public void setTime(java.time.LocalTime time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.math.BigDecimal getBookedPrice() { return bookedPrice; }
    public void setBookedPrice(java.math.BigDecimal bookedPrice) { this.bookedPrice = bookedPrice; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    @Override public String toString() { return "#" + id + " | " + customerName + " | " + serviceName + " | " + date; }
}
