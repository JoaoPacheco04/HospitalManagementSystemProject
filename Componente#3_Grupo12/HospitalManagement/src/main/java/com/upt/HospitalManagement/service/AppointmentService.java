package com.upt.HospitalManagement.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upt.HospitalManagement.model.Appointment;
import com.upt.HospitalManagement.model.Doctor;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagement.model.Shift;
import com.upt.HospitalManagement.repository.AppointmentRepository;
import com.upt.HospitalManagement.repository.DoctorRepository;
import com.upt.HospitalManagement.repository.PatientRepository;


@Service
public class AppointmentService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public boolean isDoctorAvailable(Long doctorId, LocalDate date, Shift shift) {
        try {
            System.out.println("Checking availability for doctorId: " + doctorId + ", date: " + date + ", shift: " + shift);
            
            // Fetch the doctor by ID 
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                System.out.println("Doctor not found for ID: " + doctorId);
                return false; // Doctor doesn't exist
            }

            // Check if there are any appointments already booked for the given doctor on the same date and shift
            for (Appointment appointment : doctor.getAppointments()) {
                if (appointment.getDate().equals(date) && appointment.getShift() == shift) {
                    return false; // Doctor is already booked during this time slot
                }
            }

            // Doctor is available
            System.out.println("Doctor is available for the requested time slot.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if there is an exception
        }
    }

    // Book an appointment
    public Appointment bookAppointment(Long doctorId, Long patientId, LocalDate date, Shift shift) {
        // Check if the doctor is available
        if (!isDoctorAvailable(doctorId, date, shift)) {
            throw new IllegalStateException("Doctor is not available for this time slot.");
        }

        // Fetch the doctor and patient entities
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setShift(shift);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        // Save the appointment to the database
        return appointmentRepository.save(appointment);
    }
}