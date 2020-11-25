package com.example.gallery.service;

import com.example.gallery.model.Artwork;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtworkService {
    List<Artwork> findAll();
    List<Artwork> findAllByArtistId(Long artistId);
    List<Artwork> findAllSortedByPrice(boolean asc);
    Artwork findById(Long id);
    Artwork saveArtwork(Long id, String name, String style, Float price, Integer quantity, Long artistId);
    Artwork saveArtwork(Artwork artwork, MultipartFile image) throws IOException;
    Artwork editArtwork(Long id, Artwork artwork, MultipartFile image) throws IOException;
    void deleteById(Long id);
}
