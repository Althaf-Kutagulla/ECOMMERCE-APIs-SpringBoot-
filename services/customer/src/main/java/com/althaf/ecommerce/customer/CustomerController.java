package com.althaf.ecommerce.customer;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok(service.createCustomer(request));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
        service.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> findAllCustomers(){
        return ResponseEntity.ok(service.findAllCustomers());
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existById(@PathVariable("customer-id") String id){
        return ResponseEntity.ok(service.existById(id));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable("customer-id") String id){
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteById(@PathVariable("customer-id") String customerId){
        service.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }

}
