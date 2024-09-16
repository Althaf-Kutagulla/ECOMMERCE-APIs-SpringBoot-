package com.althaf.ecommerce.customer;

import com.althaf.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id())
                .orElseThrow(()-> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No Customer found with id::%s",request.id())
                ));
        mergeCustomer(customer,request);

        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())){
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())){
            customer.setLastName(request.lastName());
        }
        if(StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }

        if(request.address() != null){
            customer.setAddress(request.address());
        }


    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }


    public Boolean existById(String id) {
        return repository.findById(id).isPresent();
    }

    public CustomerResponse findById(String id) {
        return repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(()-> new CustomerNotFoundException(
                        String.format("Customer not found with ID:: %s",id)
                ));
    }

    public void deleteCustomer(String customerId) {
        if(existById(customerId)){
            repository.deleteById(customerId);
        }else {
            throw new CustomerNotFoundException("Customer not found with ID "+ customerId);
        }

    }
}
