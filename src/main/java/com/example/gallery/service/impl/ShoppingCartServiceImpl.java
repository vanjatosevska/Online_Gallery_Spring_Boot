package com.example.gallery.service.impl;

import com.example.gallery.model.Artwork;
import com.example.gallery.model.ShoppingCart;
import com.example.gallery.model.User;
import com.example.gallery.model.dto.ChargeRequest;
import com.example.gallery.model.enumerations.CartStatus;
import com.example.gallery.model.exception.*;
import com.example.gallery.repository.ShoppingCartRepository;
import com.example.gallery.service.PaymentService;
import com.example.gallery.service.*;
import com.example.gallery.service.ShoppingCartService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserService userService;
    private final ArtworkService artworkService;
    private final PaymentService paymentService;

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(UserService userService,
                                   ArtworkService artworkService,
                                   PaymentService paymentService,
                                   ShoppingCartRepository shoppingCartRepository) {
        this.userService = userService;
        this.artworkService = artworkService;
        this.paymentService = paymentService;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String userId) {
        return this.shoppingCartRepository.findAllByUserUsername(userId);
    }

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
        User user = this.userService.findById(userId);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(
                user.getUsername(),
                CartStatus.CREATED
        )) {
            throw new ShoppingCartIsAlreadyCreatedException(userId);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart addArtworkToShoppingCart(String username, Long artworkId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Artwork artwork = this.artworkService.findById(artworkId);
        for (Artwork a : shoppingCart.getArtworks()) {
            if (a.getId().equals(artworkId)) {
                throw new ArtworkIsAlreadyInShoppingCartException(artwork.getName());
            }
        }
        shoppingCart.getArtworks().add(artwork);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart removeArtworkFromShoppingCart(String username, Long artworkId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        shoppingCart.setArtworks(
                shoppingCart.getArtworks()
                        .stream()
                        .filter(artwork -> !artwork.getId().equals(artworkId))
                        .collect(Collectors.toList())
        );
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        return this.shoppingCartRepository
                .findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User user = this.userService.findById(username);
                    shoppingCart.setUser(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }

    @Override
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }


    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String username, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(username));

        List<Artwork> artworks = shoppingCart.getArtworks();
        float price = 0;

        for (Artwork artwork : artworks) {
            if (artwork.getQuantity() <= 0) {
                throw new ArtworkOutOfStockException(artwork.getName());
            }
            artwork.setQuantity(artwork.getQuantity() - 1);
            price += artwork.getPrice();
        }
        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (InvalidRequestException e) {
            throw new TransactionFailedException(username, e.getMessage());
        }
        //////??????????????
        catch (StripeException e) {
            e.printStackTrace();
        }

        shoppingCart.setArtworks(artworks);
        shoppingCart.setStatus(CartStatus.FINISHED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

}
