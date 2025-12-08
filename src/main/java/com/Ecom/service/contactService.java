package com.Ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecom.model.Contact;
import com.Ecom.repository.contactRespository;
@Service
public class contactService {
	@Autowired
	private contactRespository contactRespository;
	public void saveContact(Contact contact) {
		contactRespository.save(contact);
	}
	public List<Contact> getAllContacts() {
		return contactRespository.findAll();
		
	}
	

}
