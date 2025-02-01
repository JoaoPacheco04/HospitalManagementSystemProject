package com.upt.HospitalManagement.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagement.repository.MedicineRepository;

import jakarta.transaction.Transactional;

@Service
public class MedicineService {
	 @Autowired
	    private MedicineRepository medicineRepository;
	 	
	 
	 // This method to dispense medicines when a patient is added and the medicines are removed from stock 
	    @Transactional
	    public void dispenseMedicines(Map<String, Integer> prescribedMedicines) {
	        for (Map.Entry<String, Integer> entry : prescribedMedicines.entrySet()) {
	            String medicineType = entry.getKey();
	            int quantityToDispense = entry.getValue();

	            // Fetch medicine from DB
	            Medicine medicine = medicineRepository.findByType(medicineType)
	                    .orElseThrow(() -> new IllegalArgumentException("No medicine found for type: " + medicineType));

	            if (medicine.getQuantity() >= quantityToDispense) {
	                // Reduce stock and save
	                medicine.setQuantity(medicine.getQuantity() - quantityToDispense);
	                medicineRepository.save(medicine);
	            } else {
	                throw new IllegalStateException("Not enough stock for " + medicineType);
	            }
	        }
	    }
}
