package com.example.BuildPC.service.implementation;

import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.WishlistItem;
import com.example.BuildPC.repository.WishlistRepository;
import com.example.BuildPC.service.WishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistItemRepository;

    @Override
    public List<WishlistItem> listWishlistItems(User user) {
        return wishlistItemRepository.findByUser(user);
    }

    @Override
    public void save(WishlistItem wishlistItem) {
        wishlistItemRepository.save(wishlistItem);
    }


    @Override
    @Transactional
    public void remove(WishlistItem wishlistItem) {
        wishlistItemRepository.delete(wishlistItem);
    }

    @Override
    public boolean isProductInWishlist(User user, Product product) {
        return wishlistItemRepository.existsByUserAndProduct(user, product);
    }

    @Override
    public Optional<WishlistItem> findWishlistItem(User user, Product product) {
        return Optional.ofNullable(wishlistItemRepository.findByUserAndProduct(user, product));
    }

}
