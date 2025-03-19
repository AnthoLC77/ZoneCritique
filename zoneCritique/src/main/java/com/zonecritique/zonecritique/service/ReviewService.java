package com.zonecritique.zonecritique.service;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.Review;
import com.zonecritique.zonecritique.entity.User;
import com.zonecritique.zonecritique.repository.MediaRepository;
import com.zonecritique.zonecritique.repository.ReviewRepository;
import com.zonecritique.zonecritique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    public Review createReview(Review review) {

        if (review.getUser() == null || review.getMedia() == null) {
            throw new RuntimeException("User or Media is missing in the Review");
        }

        User user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Media media = mediaRepository.findById(review.getMedia().getId())
                .orElseThrow(() -> new RuntimeException("Media not found"));

        Review existingReview = reviewRepository.findByUserIdAndMediaId(user.getId(), media.getId());
        if (existingReview != null) {
            existingReview.setRating(review.getRating());
            return reviewRepository.save(existingReview);
        }

        review.setUser(user);
        review.setMedia(media);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByUser(Long UserId) {
        User user = userRepository.findById(UserId).orElseThrow(()->new RuntimeException("User not found"));

        return reviewRepository.findByUser(user);
    }

    public List<Review> getReviewsByMedia(Long MediaId) {
        Media media = mediaRepository.findById(MediaId).orElseThrow(()->new RuntimeException("Media not found"));

        return reviewRepository.findByMedia(media);
    }

    public void deleteReview(Long userId, Long mediaId) {
        Review review = reviewRepository.findByUserIdAndMediaId(userId, mediaId);
        if (review != null) {
            reviewRepository.delete(review);
        } else {
            throw new RuntimeException("Review not found");
        }
    }
}
