package com.upt.HospitalManagement.dto;
/*
 * this DTO is for the list of all doctors to show only limited details
 */
public class DoctorLimitedDTO {
    
    private Long id;
    private String name;
    private String specialization;

    // Constructor
    public DoctorLimitedDTO(Long id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }
    
    // Default constructor
    public DoctorLimitedDTO() {
        // No initialization (defaults to null or 0 for primitives)
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    @Override
	public String toString() {
		return "DoctorLimitedDTO [id=" + id + ", name=" + name + ", specialization=" + specialization + "]\n";
	}
    
}
