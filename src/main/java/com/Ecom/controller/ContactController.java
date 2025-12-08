package com.Ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.ProductDTO;
import com.Ecom.model.Contact;
import com.Ecom.service.contactService;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
public class ContactController {
	@Autowired
	private contactService contactService;
	@PostMapping
	public String saveContact(@RequestBody Contact contact) {
		contactService.saveContact(contact);
		return "Request form submitted successfully";
	}
	@GetMapping
	public ResponseEntity<List<Contact>> getAllContact() {
		return ResponseEntity.ok(contactService.getAllContacts());
	}

}
