package com.example.gallery.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArtistExceptionNotFound extends RuntimeException {
    public ArtistExceptionNotFound(Long id) {
        super(String.format("Artist with id %d is not found",id));
    }
}