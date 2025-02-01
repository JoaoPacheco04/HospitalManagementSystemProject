package com.upt.HospitalManagement.model;

import jakarta.persistence.*;

/*
 *This is a entity class for the Patient
 */
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "prescribed_medicines", joinColumns = @JoinColumn(name = "patient_id"))
    @MapKeyColumn(name = "medicine_name")
    @Column(name = "quantity")
    private Map<String, Integer> prescribedMedicines = new HashMap<>();

    @Column(name = "is_surgery_required")
    private boolean isSurgeryRequired;

    @Column(name = "bed_days")
    private int bedDays;

    @Column(name = "surgery_days")
    private int surgeryDays;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    

    // Constructor Default
    public Patient() {}

    
    // Constructor using Fields
    
    public Patient(Long id, String name, int age, String medicalHistory, Map<String, Integer> prescribedMedicines,
			boolean isSurgeryRequired, int bedDays, int surgeryDays, String contactNumber) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.medicalHistory = medicalHistory;
		this.prescribedMedicines = prescribedMedicines;
		this.isSurgeryRequired = isSurgeryRequired;
		this.bedDays = bedDays;
		this.surgeryDays = surgeryDays;
		this.contactNumber = contactNumber;
				
    }




	// Getters and Setters for all fields...
	
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public Map<String, Integer> getPrescribedMedicines() {
		return prescribedMedicines;
	}

	public void setPrescribedMedicines(Map<String, Integer> prescribedMedicines) {
		this.prescribedMedicines = prescribedMedicines;
	}

	public boolean isSurgeryRequired() {
		return isSurgeryRequired;
	}

	public void setSurgeryRequired(boolean isSurgeryRequired) {
		this.isSurgeryRequired = isSurgeryRequired;
	}

	public int getBedDays() {
		return bedDays;
	}

	public void setBedDays(int bedDays) {
		this.bedDays = bedDays;
	}

	public int getSurgeryDays() {
		return surgeryDays;
	}

	public void setSurgeryDays(int surgeryDays) {
		this.surgeryDays = surgeryDays;
	}

	public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
   
    
    
}