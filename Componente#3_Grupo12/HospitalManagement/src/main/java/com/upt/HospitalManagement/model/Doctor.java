package com.upt.HospitalManagement.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "doctors") 
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "name", nullable = false) // Column name and constraints
    private String name;
    
    @Column(name = "specialization")
    private String specialization; 
    
    

    // This field represents the appointments associated with the doctor
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference    // to prevent infinte recursion repeating 
    private List<Appointment> appointments = new ArrayList<>();

    // Constructors
    public Doctor() {} // Default constructor for JPA

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }
   

    // Getters and Setters
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
    
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

