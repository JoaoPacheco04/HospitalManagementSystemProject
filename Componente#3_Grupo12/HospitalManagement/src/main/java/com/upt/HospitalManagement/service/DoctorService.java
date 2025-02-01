package com.upt.HospitalManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upt.HospitalManagement.model.Appointment;
import com.upt.HospitalManagement.model.Doctor;
import com.upt.HospitalManagement.repository.DoctorRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get a doctor by ID
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doctor with ID " + id + " not found.")); // exception if element doesmt exist
    }

    // Add a new doctor
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Delete a doctor by ID
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new NoSuchElementException("Doctor with ID " + id + " not found.");
        }
        doctorRepository.deleteById(id);
    }

    // Get appointments by doctor ID
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId); // Ensure doctor exists
        return doctor.getAppointments(); // Lazy fetch of appointments
    }
    public List<Doctor> getAllDoctorsApp() {
        return doctorRepository.findAll();
    }
    
}