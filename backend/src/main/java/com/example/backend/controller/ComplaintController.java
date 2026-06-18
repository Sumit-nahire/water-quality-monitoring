package com.example.backend.controller;

import com.example.backend.entity.Complaint;
import com.example.backend.service.ComplaintService;
import com.example.backend.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private EmailService emailService;
    private String email;
    private String status;

    @PostMapping("/add")
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    @GetMapping("/getAll")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @PutMapping("/{id}")
    public Complaint updateComplaint(@PathVariable Long id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }
    @PutMapping("/accept/{id}")
    public Complaint acceptComplaint(@PathVariable Long id) {

        Complaint complaint = complaintService.getComplaintById(id);

        complaint.setStatus("ACCEPTED");
        complaintService.updateComplaint(id, complaint);

        emailService.sendMail(
        	    complaint.getEmailAddress(),
        	    "Complaint Accepted",
        	    "Your complaint has been accepted and action will be taken soon."
        	);

        return complaint;
    }
    @PutMapping("/reject/{id}")
    public Complaint rejectComplaint(@PathVariable Long id) {

        Complaint complaint = complaintService.getComplaintById(id);

        complaint.setStatus("REJECTED");
        complaintService.updateComplaint(id, complaint);

        emailService.sendMail(
                complaint.getEmailAddress(),
                "Complaint Rejected",
                "Your complaint has been rejected."
        );

        return complaint;
    }
}
