package com.example.gallery.service;

import com.example.gallery.model.ShoppingCart;
import com.example.gallery.model.dto.ChargeRequest;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart findActiveShoppingCartByUsername(String userId);

    List<ShoppingCart> findAllByUsername(String userId);

    ShoppingCart createNewShoppingCart(String userId);

    ShoppingCart addArtworkToShoppingCart(String userId,
                                          Long productId);

    ShoppingCart removeArtworkFromShoppingCart(String userId,
                                               Long productId);

    ShoppingCart getActiveShoppingCart(String userId);

    ShoppingCart cancelActiveShoppingCart(String userId);

    ShoppingCart checkoutShoppingCart(String username, ChargeRequest chargeRequest);

}
