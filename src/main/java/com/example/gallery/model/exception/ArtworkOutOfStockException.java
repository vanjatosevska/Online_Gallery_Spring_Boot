package com.example.gallery.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ArtworkOutOfStockException extends RuntimeException {
    public ArtworkOutOfStockException(String name) {
        super(String.format("Artwork %s is out of stock!", name));
    }
}
