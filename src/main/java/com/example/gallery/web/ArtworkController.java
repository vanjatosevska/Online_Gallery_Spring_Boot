package com.example.gallery.web;

import com.example.gallery.model.Artist;
import com.example.gallery.model.Artwork;
import com.example.gallery.model.exception.ArtworkIsAlreadyInShoppingCartException;
import com.example.gallery.service.ArtistService;
import com.example.gallery.service.ArtworkService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/artworks")
public class ArtworkController {
    private final ArtworkService artworkService;
    private final ArtistService artistService;

    public ArtworkController(ArtworkService artworkService, ArtistService artistService) {
        this.artworkService = artworkService;
        this.artistService = artistService;
    }

    @GetMapping
    public String getArtworks(Model model) {
        List<Artwork> artworks = this.artworkService.findAll();
        model.addAttribute("artworks", artworks);
        return "artworks";
    }

    @GetMapping("/add-new")
    @Secured("ROLE_ADMIN")
    public String addNewArtwork(Model model) {
        List<Artist> artists = this.artistService.findAll();
        model.addAttribute("artists", artists);
        model.addAttribute("artwork", new Artwork());
        return "add-artwork";
    }


    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String editArtwork(Model model, @PathVariable Long id) {
        try {
            Artwork artwork = this.artworkService.findById(id);
            List<Artist> artists = this.artistService.findAll();
            model.addAttribute("artwork", artwork);
            model.addAttribute("artists", artists);
            return "add-artwork";
        } catch ( RuntimeException ex ) {
            return "redirect:/artwork?error=" + ex.getMessage();
        }
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public String saveArtwork(
            @Valid Artwork artwork,
            BindingResult bindingResult,
            @RequestParam MultipartFile image,
            Model model) {
        if (bindingResult.hasErrors()) {
            List<Artist> artists = this.artistService.findAll();
            model.addAttribute("artists", artists);
            return "add-artwork";
        }
        try {
            this.artworkService.saveArtwork(artwork, image);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return "redirect:/artworks";
    }

    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteArtwork(@PathVariable Long id) {
        try {
            this.artworkService.deleteById(id);
        } catch ( ArtworkIsAlreadyInShoppingCartException ex ) {
            return String.format("redirect:/artworks?error=%s", ex.getMessage());
        }
        return "redirect:/artworks";
    }
}
