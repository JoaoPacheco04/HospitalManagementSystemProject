package com.upt.HospitalManagement.dto;
/*
 * this is the object sent for the client as Invoice
 */
import java.util.List;

public class Invoice {

    private String patientName;
    private Long patientId;
    private List<MedicineDetail> prescribedMedicines;
    private int bedDays;
    private double bedDayCost;
    private int surgeryDays;
    private double surgeryDayCost;
    private double totalCost;

    // Getters and Setters

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public List<MedicineDetail> getPrescribedMedicines() {
        return prescribedMedicines;
    }

    public void setPrescribedMedicines(List<MedicineDetail> prescribedMedicines) {
        this.prescribedMedicines = prescribedMedicines;
    }

    public int getBedDays() {
        return bedDays;
    }

    public void setBedDays(int bedDays) {
        this.bedDays = bedDays;
    }

    public double getBedDayCost() {
        return bedDayCost;
    }

    public void setBedDayCost(double bedDayCost) {
        this.bedDayCost = bedDayCost;
    }

    public int getSurgeryDays() {
        return surgeryDays;
    }

    public void setSurgeryDays(int surgeryDays) {
        this.surgeryDays = surgeryDays;
    }

    public double getSurgeryDayCost() {
        return surgeryDayCost;
    }

    public void setSurgeryDayCost(double surgeryDayCost) {
        this.surgeryDayCost = surgeryDayCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}