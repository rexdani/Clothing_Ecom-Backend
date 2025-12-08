package com.Ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.model.Contact;

public interface contactRespository  extends JpaRepository<Contact, Long> {

}
