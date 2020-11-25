package com.example.gallery.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArtworkNotFoundException extends RuntimeException {
    public ArtworkNotFoundException(Long id) {
        super(String.format("Artwork with id %d not found", id));
    }
}
