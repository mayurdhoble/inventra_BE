package com.business.inventra.controller;

import com.business.inventra.dto.request.*;
import com.business.inventra.dto.response.MobileCheckResponse;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping("/contacts/check-mobile")
    public ResponseEntity<ApiResponse<MobileCheckResponse>> checkMobile(
        @Valid @RequestBody MobileCheckRequest request
    ) {
        MobileCheckResponse response = contactService.checkMobileNumber(request);
        return ResponseEntity.ok(ApiResponse.success("Mobile number check completed", response));
    }

    // Getter and Setter
    public ContactService getContactService() {
        return contactService;
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
} 