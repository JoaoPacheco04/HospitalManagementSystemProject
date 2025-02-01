package com.upt.HospitalManagementFrontend.RestService;


import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upt.HospitalManagement.dto.DoctorLimitedDTO;
import com.upt.HospitalManagement.dto.Invoice;
import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagement.model.Doctor;
import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagement.model.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Service
public class RestService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api/"; // Adjust if necessary

   // For Patients Menu
    
    
    public String addPatient(Map<String, Object> patientData) {
        String url = BASE_URL + "/patients" + "/add";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, patientData, String.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            // Print the message and don't rethrow, so the program continues
            System.out.println("Error: No medicine found for type: " + e.getResponseBodyAsString());
            return "No medicine found for type: " + e.getResponseBodyAsString();  // Return a message instead of stopping
        } catch (HttpClientErrorException e) {
            System.out.println("Client error occurred: " + e.getMessage());
            return "Client error occurred: " + e.getMessage();
        }
    }
 
    // Method to handle GET request
    public Patient getPatientById(Long patientId) {
        String url = BASE_URL + "/patients" + "/" + patientId;
        try {
            return restTemplate.getForObject(url, Patient.class);  // Returns Patient object
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;  // Handle error gracefully
        }
    }

    // Method to handle PUT request (Update patient)
    public String updatePatient(Long patientId, Map<String, Object> updatedPatientData) {
        String url = BASE_URL + "/patients" + "/update/" + patientId;
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updatedPatientData);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            return response.getBody();  // Returns the response body (String)
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getMessage());
            return "Error occurred while updating patient.";
        }
    }

    // Method to handle DELETE request (Delete patient)
    public String deletePatient(Long patientId) {
        String url = BASE_URL + "/patients" + "/" + patientId;
        try {
            restTemplate.delete(url);  // DELETE does not return a body
            return "Patient deleted successfully";
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getMessage());
            return "Error occurred while deleting patient.";
        }
    }
	
    
    
    // For Pharmacy Menu
    public String addMedicine(Medicine medicine) {
        String url = BASE_URL + "/medicines/add";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Medicine> requestEntity = new HttpEntity<>(medicine, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return "Failed to add medicine: " + ex.getMessage();
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            return "An unexpected error occurred: " + ex.getMessage();
        }
    }
    
    
    // Restock Medicine
    public String restockMedicine(Long id, int quantity) {
        String url = BASE_URL + "/medicines/restock/" + id + "?quantity=" + quantity;
        try {
            restTemplate.put(url, null);
            return "Medicine restocked successfully.";
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return "Failed to restock medicine: " + ex.getMessage();
        }
    }
    
 // Delete Medicine
 
    public String deleteMedicine(Long id) {
        String url = BASE_URL + "/medicines" + "/" + id;
        try {
            restTemplate.delete(url);  // DELETE does not return a body
            return "Medicine deleted successfully";
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getMessage());
            return "Error occurred while deleting Medicine.";
        }
    }
   

    // Get All Medicines
    public List<Medicine> getAllMedicines() {
        String url = BASE_URL + "/medicines";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            String responseBody = response.getBody();

            // Parse the JSON string into a List of Medicine objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<Medicine> medicines = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Medicine.class));
            return medicines;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;  // Handle the error appropriately
        }
    }

    // Get Invoice by Patient ID
    public Invoice getInvoice(Long patientId) {
        String url = BASE_URL + "/billing/invoice/" + patientId;
        try {
            ResponseEntity<Invoice> response = restTemplate.getForEntity(url, Invoice.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return null;  // Return null or handle as per your application's flow
        }
    }
    // For Report Menu 
    
 // Endpoint to get all patients with limited details
    public List<PatientLimitedDTO> getAllPatients() {
        String url = BASE_URL + "/patients/list"; 
        try {
            ResponseEntity<List<PatientLimitedDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PatientLimitedDTO>>() {});
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Log the error and return an empty list or null, depending on preference
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return new ArrayList<>(); // Return an empty list to avoid program termination
        }
    }

    // Endpoint to calculate total earnings of the hospital
    public Map<String, Object> calculateTotalEarnings() {
        String url = BASE_URL + "/hospital/earnings"; 
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {});
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Log the error and return a fallback result
            System.out.println("Error: " + ex.getResponseBodyAsString());
            Map<String, Object> fallbackResult = new HashMap<>();
            fallbackResult.put("message", "Error fetching total earnings.");
            fallbackResult.put("details", ex.getResponseBodyAsString());
            return fallbackResult; // Provide fallback to avoid termination
        }
    }
    
    // For Doctor Menu
    
    public DoctorLimitedDTO addDoctor(DoctorLimitedDTO doctorDTO) {
        String url = BASE_URL + "/doctors/add";

        Map<String, Object> doctorData = new HashMap<>();
        doctorData.put("name", doctorDTO.getName());
        doctorData.put("specialization", doctorDTO.getSpecialization());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(doctorData, headers);

            ResponseEntity<DoctorLimitedDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity, DoctorLimitedDTO.class);

            // Check for successful response
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody(); // Return the DoctorLimitedDTO with updated ID
            } else {
                throw new RuntimeException("Failed to add doctor: " + response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Failed to add doctor: " + ex.getResponseBodyAsString(), ex);
        }
    }
    
    
    public String deleteDoctor(Long doctorId) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the URL for the delete endpoint
        String url = BASE_URL + "/doctors/delete/" + doctorId;

        // Send the DELETE request
        try {
            // Make the HTTP DELETE request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

            // Check the response
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Doctor with ID " + doctorId + " deleted successfully.";
            } else {
                return "Failed to delete doctor with ID " + doctorId + ": " + response.getBody();
            }
        } catch (Exception e) {
            // In case of any error (e.g., no connection to backend)
            return "Error deleting doctor: " + e.getMessage();
        }
    }

    // Get a list of doctors (using limited DTO)
    public List<DoctorLimitedDTO> getAllDoctors() {
        try {
            ResponseEntity<List<Doctor>> response = restTemplate.exchange(
                    BASE_URL + "doctors/list",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Doctor>>() {}
            );

            List<Doctor> doctors = response.getBody();
            return doctors.stream()
                    .map(doctor -> new DoctorLimitedDTO(doctor.getId(), doctor.getName(), doctor.getSpecialization()))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return new ArrayList<>(); // Return empty list in case of an error
        }
    }

    // Check doctor availability
    public boolean checkDoctorAvailability(Long doctorId, String date, String shift) {
        String url = BASE_URL + "/appointments/check-availability?doctorId=" + doctorId + "&date=" + date + "&shift=" + shift;
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return false; // Indicate unavailability in case of error
        }
    }

    // Book an appointment
    public String bookAppointment(Long doctorId, Long patientId, String date, String shift) {
        String url = BASE_URL + "/appointments/book";
        Map<String, Object> request = new HashMap<>();
        request.put("doctorId", doctorId);
        request.put("patientId", patientId);
        request.put("date", date);
        request.put("shift", shift);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return formatAppointmentSuccessMessage(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return "Failed to book appointment: " + ex.getResponseBodyAsString();
        }
    }

    // Get all doctors with appointments
    public List<Doctor> getAllDoctorsWithAppointments() {
        String url = BASE_URL + "/doctors";
        try {
            ResponseEntity<Doctor[]> response = restTemplate.getForEntity(url, Doctor[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error: " + ex.getResponseBodyAsString());
            return new ArrayList<>(); // Return empty list in case of an error
        }
    }

    // Format appointment success message
    private String formatAppointmentSuccessMessage(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> appointmentData = objectMapper.readValue(responseBody, Map.class);
            Map<String, Object> patientData = (Map<String, Object>) appointmentData.get("patient");

            String patientName = patientData != null ? (String) patientData.get("name") : "Unknown Patient";
            String date = (String) appointmentData.get("date");
            String shift = (String) appointmentData.get("shift");

            return String.format(
                "Appointment successfully booked!\n" +
                "Patient: %s\n" +
                "Date: %s\n" +
                "Shift: %s",
                patientName, date, shift
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Appointment booked, but failed to parse response.";
        }
    }
}