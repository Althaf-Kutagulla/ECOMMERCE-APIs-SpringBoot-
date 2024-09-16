package com.althaf.ecommerce.product;

import com.althaf.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    public Integer createProduct(ProductRequest request) {
        var product = repository.save(mapper.toProduct(request));
        return product.getId();
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Product not found with ID::%s",productId)));
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException(String.format("One or more products are not exists"));
        }

        var storesRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for(int i =0; i<storedProducts.size();i++){
            var product = storedProducts.get(i);
            var prodcutRequest = storesRequest.get(i);
            if(product.getAvailableQuantity() < prodcutRequest.quantity()){
                throw new ProductPurchaseException(String.format("Insufficient stock quantity for the Productid::%s",prodcutRequest.productId()));
            }
            var newAvailableQuantity = product.getAvailableQuantity() - prodcutRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product,prodcutRequest.quantity()));
        }

        return purchasedProducts;
    }


    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
