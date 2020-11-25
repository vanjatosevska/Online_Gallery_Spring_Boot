package com.example.gallery.service.impl;

import com.example.gallery.model.Artist;
import com.example.gallery.model.exception.ArtistExceptionNotFound;
import com.example.gallery.repository.ArtistRepository;
import com.example.gallery.service.ArtistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> findAll() {
        return this.artistRepository.findAll();
    }

    @Override
    public List<Artist> findAllByName(String name) {
        return this.artistRepository.findAll();
    }

    @Override
    public Artist findById(Long id) {
        return this.artistRepository.findById(id).orElseThrow(()-> new ArtistExceptionNotFound(id));
    }

    @Override
    public Artist save(Artist artist) {
        return this.artistRepository.save(artist);
    }

    @Override
    public Artist update(Long id, Artist artist) {
        Artist artist1 = this.findById(id);
        artist1.setName(artist.getName());
        artist1.setOrigin(artist.getOrigin());
        return this.artistRepository.save(artist1);
    }

    @Override
    public Artist updateName(Long id, String name) {
        Artist a = this.findById(id);
        a.setName(name);
        return this.artistRepository.save(a);
    }

    @Override
    public void deleteById(Long id) {
      this.artistRepository.deleteById(id);
    }
}
