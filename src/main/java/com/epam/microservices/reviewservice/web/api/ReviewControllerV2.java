package com.epam.microservices.reviewservice.web.api;

import com.epam.microservices.reviewservice.entity.Review;
import com.epam.microservices.reviewservice.exceptions.ReviewNotFoundException;
import com.epam.microservices.reviewservice.exceptions.ReviewServiceException;
import com.epam.microservices.reviewservice.service.ReviewService;
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
import java.util.List;

@RestController
@RequestMapping("reviews/v2")
public class ReviewControllerV2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("Retrive Reviews List")
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Iterable<Review>> findAllReviews(@PathVariable("productId") long productId) {
        return new ResponseEntity<>(reviewService.retrieveReviews(productId), HttpStatus.OK);
    }

    @ApiOperation("Create Reviews for a Product id")
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> createReview(@PathVariable Long productId,@RequestBody List<Review> reviews) {
        List<Review> savedReviews = reviewService.saveorUpdateReviewForGivenProductId(productId,reviews);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedReviews.get(0).getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @ApiOperation("Update Review for a Product id")
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long productId,@PathVariable Long reviewId,@RequestBody Review review) {
        logger.info("Updating Review with productId {} reviewId {}",productId, reviewId);

        Optional<Review> currentReview = reviewService.getReviewById(reviewId);

        if (currentReview.isPresent()) {
            currentReview.get().setName(review.getName());
            currentReview.get().setDesc(review.getDesc());
            currentReview.get().setRating(review.getRating());
            Review updatedReview = reviewService.saveorUpdateReview(currentReview.get());
            return new ResponseEntity<Review>(updatedReview,HttpStatus.OK);
        }else {
            logger.error("Unable to update. Review with reviewId {} not found for the given productId {}.", reviewId,productId);
            return new ResponseEntity(new ReviewServiceException("Unable to upate. Review with id " + reviewId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}/reviews/{reviewId}")
    @ApiOperation("Delete Review for a Product id")
    public ResponseEntity<?> deleteReview(@PathVariable("productId") long productId,@PathVariable("reviewId") long reviewId) {
        Optional<Review> review = reviewService.getReviewById(reviewId);
        if (!review.isPresent())
            throw new ReviewNotFoundException("Review not found for id " + reviewId);
        reviewService.deleteReviewForGivenProductId(reviewId);
        return new ResponseEntity<Review>(HttpStatus.NO_CONTENT);
    }
}
