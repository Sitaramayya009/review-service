package com.epam.microservices.reviewservice.repository;

import com.epam.microservices.reviewservice.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long>{
    List<Review> findByProductId(Long productId);
    Review findByIdAndProductId(Long id,Long productId);
}
