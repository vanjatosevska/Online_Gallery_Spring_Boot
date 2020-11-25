package com.example.gallery.web;

import com.example.gallery.model.Artist;
import org.springframework.web.bind.annotation.*;

import com.example.gallery.service.ArtistService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistRestController {

    private final ArtistService artistService;

    public ArtistRestController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<Artist> findAll(@RequestParam(required = false) String name) {
        if (name == null) {
            return this.artistService.findAll();
        } else {
            return this.artistService.findAllByName(name);
        }
    }

    @GetMapping("/by-name")
    public List<Artist> findAllByName(@RequestParam String name) {
        return this.artistService.findAllByName(name);
    }

    @GetMapping("/{id}")
    public Artist findById(@PathVariable Long id) {
        return this.artistService.findById(id);
    }

    @PostMapping
    public Artist save(@Valid Artist artist) {
        return this.artistService.save(artist);
    }

    @PutMapping("/{id}")
    public Artist update(@PathVariable Long id, @Valid Artist artist) {
        return this.artistService.update(id, artist);
    }

    @PatchMapping("/{id}")
    public Artist updateName(@PathVariable Long id, @RequestParam String name) {
        return this.artistService.updateName(id, name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.artistService.deleteById(id);
    }


}
