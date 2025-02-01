package com.upt.HospitalManagement.dto;
/*
 * This DTO is to apply for the appointment
 */
import java.time.LocalDate;

import com.upt.HospitalManagement.model.Shift;

public class AppointmentRequest {
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private Shift shift;
    
 
    public AppointmentRequest(Long doctorId, Long patientId, LocalDate date, Shift shift) {
		super();
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.date = date;
		this.shift = shift;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Shift getShift() {
		return shift;
	}
	public void setShift(Shift shift) {
		this.shift = shift;
	}


}