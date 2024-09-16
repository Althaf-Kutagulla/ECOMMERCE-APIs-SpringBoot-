package com.althaf.ecommerce.handler;

import com.althaf.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductPurchaseException.class})
    public ResponseEntity<String> handle(ProductPurchaseException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handle(EntityNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex){
        var errors = ex.getBindingResult().getAllErrors();
        var errorsMap = new HashMap<String,String>();
        errors.forEach(e->{
            var errorFeild = ((FieldError) e).getField();
            var errorMessage = e.getDefaultMessage();
            errorsMap.put(errorFeild,errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorsMap));
    }

}
