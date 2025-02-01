package com.upt.HospitalManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upt.HospitalManagement.model.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalService {

    @Autowired
    private PatientService patientService;

    @Autowired
    private BillingSystem billingSystemService;

    // Method to calculate the total earnings of the hospital and display patient details
    public Map<String, Object> calculateTotalEarnings() {
        double totalEarnings = 0.0;
        List<Map<String, Object>> patientBillDetails = new ArrayList<>();

        // Fetch all patients
        List<Patient> patients = patientService.getAllPatients();

        if (patients == null || patients.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("totalEarnings", totalEarnings);
            response.put("patientBillDetails", patientBillDetails);
            
            return response;
        }

        // Iterate over each patient and calculate their individual bill
        for (Patient patient : patients) {
            double patientBill = billingSystemService.calculateTotalBill(patient); // Call your existing method to get the total bill for the patient
            totalEarnings += patientBill;

            // Add patient details to the list
            Map<String, Object> patientDetail = new HashMap<>();
            patientDetail.put("id", patient.getId());
            patientDetail.put("name", patient.getName());
            patientDetail.put("age", patient.getAge());
            patientDetail.put("contactNumber", patient.getContactNumber());
            patientDetail.put("totalBill", patientBill); // Add totalBill last

            patientBillDetails.add(patientDetail);
        }

        // Return the response with correct structure: patientBillDetails first, totalEarnings last
        Map<String, Object> response = new HashMap<>();
        response.put("totalEarnings", totalEarnings);
        response.put("patientBillDetails", patientBillDetails); // Patient details first
        

        return response;
    }
}