package com.upt.HospitalManagement.controller;

import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagement.repository.PatientRepository;
import com.upt.HospitalManagement.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientService patientService;

    // Get All Patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
 // Get all patients with limited details
    @GetMapping("/list")
    public List<PatientLimitedDTO> getPatientsLimitedDetails() {
        return patientService.getPatientsLimitedDetails();
    }

    // Get Patient by ID
    @GetMapping("/{id}")
    public Optional<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        try {
            patientService.addPatient(patient);
            return ResponseEntity.ok("Patient added and medicines dispensed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // PUT endpoint to update patient details
    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Map<String, Object> updatedData) {
        Patient updatedPatient = patientService.updatePatient(id, updatedData);
        return ResponseEntity.ok(updatedPatient);
    }

    // Delete Patient
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
    }
}
