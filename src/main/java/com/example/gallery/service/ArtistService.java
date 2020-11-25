package com.example.gallery.service;

import com.example.gallery.model.Artist;

import java.util.List;

public interface ArtistService {
    List<Artist> findAll();
    List<Artist> findAllByName(String name);
    Artist findById(Long id);
    Artist save(Artist artist);
    Artist update(Long id, Artist artist);
    Artist updateName(Long id, String name);
    void deleteById(Long id);
}
