package com.epam.microservices.reviewservice.repository;

import com.epam.microservices.reviewservice.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long>{

}
