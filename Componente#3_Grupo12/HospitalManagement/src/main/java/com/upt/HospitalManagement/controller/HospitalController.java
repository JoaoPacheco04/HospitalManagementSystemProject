package com.upt.HospitalManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upt.HospitalManagement.service.HospitalService;

import java.util.Map;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    // Endpoint to get the total earnings of the hospital and patient details
    @GetMapping("/earnings")
    public Map<String, Object> getTotalEarnings() {
        return hospitalService.calculateTotalEarnings();
    }
}