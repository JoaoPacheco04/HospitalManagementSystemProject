package com.upt.HospitalManagement.controller;

import com.upt.HospitalManagement.dto.Invoice;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagement.repository.PatientRepository;
import com.upt.HospitalManagement.service.BillingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
	
	

    private final BillingSystem billingSystem;
    
    @Autowired
    private PatientRepository patientRepository;



    @Autowired
    public BillingController(BillingSystem billingSystem) {
        this.billingSystem = billingSystem;
    }

    // Endpoint to generate detailed invoice for a patient
    @GetMapping("/invoice/{patientId}")
    public Invoice generateInvoice(@PathVariable Long patientId) {
        // Fetch patient from database using patientId (for now, use a mock or hardcoded patient)
        Patient patient = findPatientById(patientId); // Replace with actual logic to fetch patient

        return billingSystem.generateDetailedInvoice(patient); // Returns a detailed invoice in JSON
    }

    // Endpoint to calculate the total bill for a patient
    @GetMapping("/total/{patientId}")
    public double calculateTotalBill(@PathVariable Long patientId) {
        // Fetch patient from database using patientId
        Patient patient = findPatientById(patientId); // Replace with actual logic to fetch patient

        return billingSystem.calculateTotalBill(patient); // Returns the total bill as a double
    }
    
    
    // Mock method to simulate fetching patient by ID
    private Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
    }
     
}