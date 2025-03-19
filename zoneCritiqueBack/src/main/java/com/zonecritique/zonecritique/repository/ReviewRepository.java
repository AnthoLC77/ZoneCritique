package com.zonecritique.zonecritique.repository;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.Review;
import com.zonecritique.zonecritique.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser(User user);
    List<Review> findByMedia(Media media);

    List<Review> findByUserId(Long userId);
    List<Review> findByMediaId(Long MediaId);

    Review findByUserIdAndMediaId(Long userId, Long mediaId);

}
