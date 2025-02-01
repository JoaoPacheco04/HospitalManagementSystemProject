package com.upt.HospitalManagement.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagement.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicineService medicineService;


    // Get all patients with detailed information
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get a list of patients with limited details 
    public List<PatientLimitedDTO> getPatientsLimitedDetails() {
        List<Patient> patients = patientRepository.findAll();

        // Convert the list of patients to a list of limited DTOs
        return patients.stream()
                .map(patient -> new PatientLimitedDTO(
                        patient.getId(),
                        patient.getName(),
                        patient.getAge(),
                        patient.getContactNumber()))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void addPatient(Patient patient) {
        // Save patient
    	patientRepository.save(patient);
    	System.out.println("Saving Patient: " + patient);

        // Dispense prescribed medicines
        medicineService.dispenseMedicines(patient.getPrescribedMedicines());
    }
    
    public Patient updatePatient(Long id, Map<String, Object> updatedData) {
        // Fetch the existing patient from the database
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update patient fields with new values from the map
        patient.setName((String) updatedData.get("name"));
        patient.setAge((Integer) updatedData.get("age"));
        patient.setContactNumber((String) updatedData.get("contactNumber"));
        patient.setMedicalHistory((String) updatedData.get("medicalHistory"));
        patient.setPrescribedMedicines((Map<String, Integer>) updatedData.get("prescribedMedicines"));
        patient.setSurgeryRequired((Boolean) updatedData.get("isSurgeryRequired"));
        patient.setBedDays((Integer) updatedData.get("bedDays"));
        patient.setSurgeryDays((Integer) updatedData.get("surgeryDays"));

        // Save the updated patient back to the database
        return patientRepository.save(patient);
    }
}