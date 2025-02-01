package com.upt.HospitalManagement.repository;

import com.upt.HospitalManagement.model.Medicine;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

	Optional<Medicine> findByType(String type); //  Optional is for safety in case we  don't encounter the the type of medicine e want an
													// Saves us from no null exceptions 
} 

