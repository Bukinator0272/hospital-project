package org.example.model.entity;

import java.time.LocalDate;


public class Recipe {

    private Long id;
    private String description;
    private Patient patient;
    private Doctor doctor;
    private LocalDate creationDate;
    private LocalDate periodOfValidity;
    private String priority;

    public Recipe(String description, Patient patient, Doctor doctor, LocalDate periodOfValidity, String priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = LocalDate.now();
        this.periodOfValidity = periodOfValidity;
        this.priority = priority;
    }

    public Recipe(Long id, String description, Patient patient, Doctor doctor, LocalDate creationDate, LocalDate periodOfValidity, String priority) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = creationDate;
        this.periodOfValidity = periodOfValidity;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getPeriodOfValidity() {
        return periodOfValidity;
    }

    public void setPeriodOfValidity(LocalDate periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
