package com.zonecritique.zonecritique.controller;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.Review;
import com.zonecritique.zonecritique.entity.TypeDeMedia;
import com.zonecritique.zonecritique.repository.ReviewRepository;
import com.zonecritique.zonecritique.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<?> addReview(@RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création de la review");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReview(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Integer>>getUserStats(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);

        int totalReviews = reviews.size();
        int films = (int) reviews.stream().filter(r -> r.getMedia().getType() == TypeDeMedia.FILM).count();
        int jeuxVideos = (int) reviews.stream().filter(r -> r.getMedia().getType() == TypeDeMedia.JEU_VIDEO).count();
        int bd = (int) reviews.stream().filter(r -> r.getMedia().getType() == TypeDeMedia.BD).count();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("Total", totalReviews);
        stats.put("films", films);
        stats.put("jeuxVideos", jeuxVideos);
        stats.put("bd", bd);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("media/{mediaId}")
    public ResponseEntity<List<Review>> getMediaReview(@PathVariable Long mediaId) {
        return ResponseEntity.ok(reviewService.getReviewsByMedia(mediaId));
    }

    @PutMapping
    public ResponseEntity<Review> updateReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review)); // Comme `createReview` met à jour si besoin
    }

    @DeleteMapping
    public ResponseEntity<String> deleteReview(@RequestParam Long userId, @RequestParam Long mediaId) {
        reviewService.deleteReview(userId, mediaId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
