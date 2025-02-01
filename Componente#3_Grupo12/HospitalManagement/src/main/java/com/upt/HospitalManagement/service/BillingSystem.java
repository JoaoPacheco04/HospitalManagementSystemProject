package com.upt.HospitalManagement.service;

import com.upt.HospitalManagement.dto.Invoice;
import com.upt.HospitalManagement.dto.MedicineDetail;
import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagement.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BillingSystem {

    private final MedicineRepository medicineRepository;

    @Autowired
    public BillingSystem(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // Method to generate a detailed invoice for a patient (returns data in JSON format)
    public Invoice generateDetailedInvoice(Patient patient) {
        double totalCost = 0.0;
        Invoice invoice = new Invoice();

        invoice.setPatientName(patient.getName());
        invoice.setPatientId(patient.getId());

        // Add prescribed medicines to invoice
        List<MedicineDetail> medicineDetails = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : patient.getPrescribedMedicines().entrySet()) {
            String medicineType = entry.getKey();
            int quantity = entry.getValue();

            // Fetch medicine by type
            Medicine medicine = medicineRepository.findByType(medicineType).orElse(null);

            if (medicine != null) {
                double cost = medicine.getPrice() * quantity;
                totalCost += cost;

                medicineDetails.add(new MedicineDetail(medicineType, quantity, medicine.getPrice(), cost));
            } else {
                medicineDetails.add(new MedicineDetail(medicineType, quantity, 0.0, 0.0));
            }
        }

        invoice.setPrescribedMedicines(medicineDetails);

        // Add additional charges for bed and surgery days
        int bedDays = patient.getBedDays();
        int surgeryDays = patient.getSurgeryDays();
        double bedDayCost = bedDays * 100.0;      // these are hard coded values 
        double surgeryDayCost = surgeryDays * 500.0;

        invoice.setBedDays(bedDays);
        invoice.setBedDayCost(bedDayCost);
        invoice.setSurgeryDays(surgeryDays);
        invoice.setSurgeryDayCost(surgeryDayCost);

        totalCost += bedDayCost + surgeryDayCost;

        invoice.setTotalCost(totalCost);

        return invoice;
    }

    // Method to calculate the total bill for a patient (returns the total cost as a double)
    public double calculateTotalBill(Patient patient) {
        double totalBill = 0.0;

        // Get prescribed medicines
        Map<String, Integer> prescribedMedicines = patient.getPrescribedMedicines();

        // Iterate through each prescribed medicine
        for (Map.Entry<String, Integer> entry : prescribedMedicines.entrySet()) {
            String medicineType = entry.getKey(); // Type of medicine
            int quantity = entry.getValue(); // Quantity prescribed

            // Fetch the medicine price from the database
            Medicine medicine = medicineRepository.findByType(medicineType).orElse(null);

            if (medicine != null) {
                // Calculate total cost for this medicine
                totalBill += medicine.getPrice() * quantity;
            }
        }

        // Additional charges (e.g., bed days, surgery, etc.) can be added here if needed
        int bedDays = patient.getBedDays();
        int surgeryDays = patient.getSurgeryDays();
        double bedDayCost = bedDays * 100.0;
        double surgeryDayCost = surgeryDays * 500.0;

        totalBill += bedDayCost + surgeryDayCost; // Adding extra charges

        return totalBill; // Return the total bill for the patient
    }
}
