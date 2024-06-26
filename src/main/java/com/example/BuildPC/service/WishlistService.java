package com.example.BuildPC.service;

import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.WishlistItem;

import java.util.List;
import java.util.Optional;

public interface WishlistService {

    List<WishlistItem> listWishlistItems(User user);

    void save(WishlistItem wishlistItem);

    void remove(WishlistItem wishlistItem);

    boolean isProductInWishlist(User user, Product product);

    Optional<WishlistItem> findWishlistItem(User user, Product product);
}
