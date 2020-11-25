package com.example.gallery.service.impl;

import com.example.gallery.model.Artwork;
import com.example.gallery.model.Artist;
import com.example.gallery.model.exception.ArtworkIsAlreadyInShoppingCartException;
import com.example.gallery.model.exception.ArtworkNotFoundException;
import com.example.gallery.repository.ArtworkRepository;
import com.example.gallery.service.ArtworkService;
import com.example.gallery.service.ArtistService;
import org.hibernate.boot.jaxb.hbm.internal.CacheAccessTypeConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final ArtistService artistService;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository, ArtistService artistService) {
        this.artworkRepository = artworkRepository;
        this.artistService = artistService;
    }

    @Override
    public List<Artwork> findAll() {
        return this.artworkRepository.findAll();
    }

    @Override
    public List<Artwork> findAllByArtistId(Long artistId) {
        return null;
    }

    @Override
    public List<Artwork> findAllSortedByPrice(boolean asc) {
        return null;
    }

    @Override
    public Artwork findById(Long id) {
        return this.artworkRepository.findById(id)
                .orElseThrow(() -> new ArtworkNotFoundException(id));
    }

    @Override
    public Artwork saveArtwork(Long id, String name, String style, Float price, Integer quantity, Long artistId) {
        Artist artist = this.artistService.findById(artistId);
        Artwork artwork = new Artwork(id, name, style, price, quantity, artist);
        return this.artworkRepository.save(artwork);
    }

    @Override
    public Artwork saveArtwork(Artwork artwork, MultipartFile image) throws IOException {
        Artist artist = this.artistService.findById(artwork.getArtist().getId());
        artwork.setArtist(artist);
        if (image != null || !image.getName().isEmpty()){
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s",
                    image.getContentType(),
                    Base64.getEncoder().encodeToString(bytes));
            artwork.setImageBase64(base64Image);
        }
        return this.artworkRepository.save(artwork);
    }

    @Override
    public Artwork editArtwork(Long id, Artwork artwork, MultipartFile image) throws IOException {
        Artwork artwork1 = this.findById(id);
        Artist artist = this.artistService.findById(artwork1.getArtist().getId());
        artwork1.setArtist(artist);
        artwork1.setId(artwork.getId());
        artwork1.setQuantity(artwork.getQuantity());
        artwork1.setName(artwork.getName());
        if(image!=null && !image.getName().isEmpty()){
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s",image.getContentType(),
                    Base64.getEncoder().encodeToString(bytes));
            artwork1.setImageBase64(base64Image);
        }
        return this.artworkRepository.save(artwork1);
    }


    @Override
    public void deleteById(Long id) throws ArtworkIsAlreadyInShoppingCartException {
        Artwork artwork = this.findById(id);
        if (artwork.getShoppingCarts().size() > 0) {
            //avoid deleting product that is already in shopping cart!
            throw new ArtworkIsAlreadyInShoppingCartException(artwork.getName());
        }

        this.artworkRepository.deleteById(id);
    }
}
