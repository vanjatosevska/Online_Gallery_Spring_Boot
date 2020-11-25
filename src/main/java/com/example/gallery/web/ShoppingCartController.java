package com.example.gallery.web;

import com.example.gallery.model.ShoppingCart;
import com.example.gallery.service.AuthService;
import com.example.gallery.service.ShoppingCartService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }


    @PostMapping("/{artworkId}/add-artwork")
    @Secured("ROLE_USER")
    public String addArtworkToShoppingCart(@PathVariable Long artworkId, RedirectAttributes redirectAttributes) {
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.addArtworkToShoppingCart(this.authService.getCurrentUserId(), artworkId);
        } catch (RuntimeException ex) {
            return "redirect:/artworks?error=" + ex.getLocalizedMessage();
        }
        redirectAttributes.addFlashAttribute("notification",
                String.format("Artwork successfully added to shopping cart"));
        redirectAttributes.addFlashAttribute("action", "save");
        return "redirect:/artworks";
    }


    @PostMapping("/{artworkId}/remove-artwork")
    @Secured("ROLE_USER")
    public String removeArtworkFromShoppingCart(@PathVariable Long artworkId) {
        ShoppingCart shoppingCart = this.shoppingCartService.removeArtworkFromShoppingCart(this.authService.getCurrentUserId(), artworkId);
        return "redirect:/artworks";
    }
}

