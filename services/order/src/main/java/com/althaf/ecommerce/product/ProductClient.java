package com.althaf.ecommerce.product;

import com.althaf.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;

    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestsBody){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<List<PurchaseRequest>> httpEntity = new HttpEntity<>(requestsBody,headers);
        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<List<PurchaseResponse>>() {
                };
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl+"/purchase",
                HttpMethod.POST,
                httpEntity,
                responseType);
        if(responseEntity.getStatusCode().isError()){
            throw new BusinessException("An error occured while processing the products purchase "+responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }
}
