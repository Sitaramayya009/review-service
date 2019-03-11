package com.epam.microservices.reviewservice.service;

import com.epam.microservices.reviewservice.entity.Review;
import com.epam.microservices.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;

	public boolean isReviewtExist(Review review) {
		return false;
	}

	public Review saveorUpdateReview(Review review) {
		Review savedReview = reviewRepository.save(review);
		return savedReview;
	}

	public Review updateReview(Review review) {
		Review updatedReview = reviewRepository.save(review);
		return updatedReview;
	}
	
	public void deleteReview(Long id) {
		reviewRepository.deleteById(id);
	}
	
	public Optional<Review> getReviewById(Long id) {
		return reviewRepository.findById(id);
	}
	public Iterable<Review> getAllReviews() {
		return reviewRepository.findAll();
	}
}
