package com.upt.HospitalManagement.repository;

import com.upt.HospitalManagement.model.Patient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	Optional<Patient> findById(Long id);
}

