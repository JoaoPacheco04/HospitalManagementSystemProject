package com.upt.HospitalManagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.upt.HospitalManagement.dto.DoctorLimitedDTO;
import com.upt.HospitalManagement.model.Appointment;
import com.upt.HospitalManagement.model.Doctor;
import com.upt.HospitalManagement.service.DoctorService;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctorsApp() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, String>> getDoctorDetailsById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            Map<String, String> response = new HashMap<>();
            response.put("name", doctor.getName());
            response.put("specialization", doctor.getSpecialization());
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorService.addDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    //  delete a doctor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Doctor with ID " + id + " deleted successfully.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with ID " + id + " not found.");
        }
    }

    // get all appointments for a doctor
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long id) {
        try {
            List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(id);
            return ResponseEntity.ok(appointments);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Get all doctors (limited details)
    @GetMapping("/list")
    public List<DoctorLimitedDTO> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return doctors.stream()
                .map(doctor -> new DoctorLimitedDTO(doctor.getId(), doctor.getName(), doctor.getSpecialization()))
                .collect(Collectors.toList());
    }
    
}