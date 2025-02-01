package com.upt.HospitalManagement.controller;

import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagement.repository.MedicineRepository;
import com.upt.HospitalManagement.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PatientRepository patientRepository;

    // 1. View All Medicines
    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // 2. Add New Medicine Types
    @PostMapping("/add")
    public ResponseEntity<String> addMedicine(@RequestBody Medicine medicine) {
        try {
            Medicine savedMedicine = medicineRepository.save(medicine);
            return ResponseEntity.ok("Medicine added successfully with ID: " + savedMedicine.getId());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add medicine: " + ex.getMessage());
        }
    }

    // 3. Restock Medicine Type
    @PutMapping("/restock/{id}")
    public Medicine restockMedicine(@PathVariable Long id, @RequestParam int quantity) {
        return medicineRepository.findById(id).map(medicine -> {
            medicine.setQuantity(medicine.getQuantity() + quantity);
            return medicineRepository.save(medicine);
        }).orElseThrow(() -> new RuntimeException("Medicine not found with id " + id));
    }

    // 5. Delete Medicine
    @DeleteMapping("/{id}")
    public void deleteMedicine(@PathVariable Long id) {
        medicineRepository.deleteById(id);
    }
}