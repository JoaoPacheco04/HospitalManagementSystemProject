package com.upt.HospitalManagement.model;
/*
 * This class is for the entity Appointments 
 * 
 */



import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 
@Table(name = "appointments") 
public class Appointment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "date", nullable = false) 
    private LocalDate date; // Appointment date

    @ManyToOne 
    @JoinColumn(name = "doctor_id", nullable = false) 
    @JsonBackReference
    private Doctor doctor; // Doctor assigned to this appointment

    @ManyToOne // Many appointments can be linked to one patient
    @JoinColumn(name = "patient_id", nullable = false) 
    private Patient patient; // Patient for this appointment

    @Enumerated(EnumType.STRING) 
    @Column(name = "shift", nullable = false) 
    private Shift shift; // Shift for this appointment

    // Constructor
    public Appointment(LocalDate date, Doctor doctor, Patient patient, Shift shift) {
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
        this.shift = shift;
    }

    // Default constructor (required by JPA)
    public Appointment() {}

    
    
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}

    

