package com.example.gallery.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class ArtistController {

    @GetMapping("/artists")
    public String getArtistPage(HttpServletResponse res, HttpServletRequest req) {
        return "artists";
    }


}
