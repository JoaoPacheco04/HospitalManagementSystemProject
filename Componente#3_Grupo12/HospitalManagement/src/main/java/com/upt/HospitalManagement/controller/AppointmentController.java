package com.upt.HospitalManagement.controller;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.upt.HospitalManagement.dto.AppointmentRequest;
import com.upt.HospitalManagement.model.Appointment;
import com.upt.HospitalManagement.model.Shift;
import com.upt.HospitalManagement.service.AppointmentService;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkDoctorAvailability(
            @RequestParam Long doctorId,
            @RequestParam String date, // "yyyy-MM-dd"
            @RequestParam Shift shift) {
        boolean isAvailable = appointmentService.isDoctorAvailable(doctorId, LocalDate.parse(date), shift);
        return ResponseEntity.ok(isAvailable);
    }
    

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        try {
            Appointment appointment = appointmentService.bookAppointment(
                appointmentRequest.getDoctorId(),
                appointmentRequest.getPatientId(),
                appointmentRequest.getDate(),
                appointmentRequest.getShift()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}