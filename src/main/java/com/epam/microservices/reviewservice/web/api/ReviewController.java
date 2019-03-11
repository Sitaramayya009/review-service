package com.epam.microservices.reviewservice.web.api;

import com.epam.microservices.reviewservice.entity.Review;
import com.epam.microservices.reviewservice.exceptions.ReviewNotFoundException;
import com.epam.microservices.reviewservice.exceptions.ReviewServiceException;
import com.epam.microservices.reviewservice.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("reviews")
@Api(value = "Review Resource", description = "Shows Reviews")
public class ReviewController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReviewService reviewService;

	@ApiOperation("Retrive Reviews List")
	@GetMapping
	public ResponseEntity<Iterable<Review>> findAllReviews() {
		return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
	}

	@ApiOperation("Retrive Review")
	@GetMapping("/{id}")
	public ResponseEntity<Review> findReview(@PathVariable("id") long id) {

		Optional<Review> review =  reviewService.getReviewById(id);
		if (!review.isPresent())
			throw new ReviewNotFoundException("Review not found for id " + id);
		return new ResponseEntity<Review>(review.get(), HttpStatus.OK);
	}

	@ApiOperation("Create Review")
	@PostMapping()
	public ResponseEntity<?> createReview(@RequestBody Review review) {
		Review savedReview = reviewService.saveorUpdateReview(review);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedReview.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@ApiOperation("Update Review")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateReview(@PathVariable Long id,@RequestBody Review review) {
		logger.info("Updating Review with id {}", id);

		Optional<Review> currentReview = reviewService.getReviewById(id);

		if (currentReview.isPresent()) {
			currentReview.get().setName(review.getName());
			currentReview.get().setDesc(review.getDesc());
			Review updatedReview = reviewService.saveorUpdateReview(currentReview.get());
			return new ResponseEntity<Review>(updatedReview,HttpStatus.OK);
		}else {
			logger.error("Unable to update. Review with id {} not found.", id);
			return new ResponseEntity(new ReviewServiceException("Unable to upate. Review with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	@ApiOperation("Delete Review")
	public ResponseEntity<?> deleteReview(@PathVariable("id") long id) {
		Optional<Review> review = reviewService.getReviewById(id);
		if (!review.isPresent())
			throw new ReviewNotFoundException("Review not found for id " + id);
		reviewService.deleteReview(id);
		return new ResponseEntity<Review>(HttpStatus.NO_CONTENT);
	}
}
