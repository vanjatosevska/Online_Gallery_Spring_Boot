package com.example.gallery.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class ArtworkIsAlreadyInShoppingCartException extends RuntimeException{
    public ArtworkIsAlreadyInShoppingCartException(String name) {
        super(String.format("Artowrk %s is alraedy in active shopping cart", name));
    }
}
