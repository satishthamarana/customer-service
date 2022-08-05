package com.test.customer.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;

@RestController
@RequestMapping("/api/customer/v1")
public class CustomerController {
	
	@Value("${address.client.url}")
	private String addressServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
		
	@PostMapping("/create")
	public Customer createCustomer(@RequestBody Customer customer) throws MalformedURLException {
		customer.setAddress(addressClient());
		return customer;
	}
	
	public Address addressClient() throws MalformedURLException {			
		ResponseEntity<Address> result = restTemplate.getForEntity(addressServiceUrl+"/api/address/v1/get", Address.class);
		return result.getBody();
	}

}
