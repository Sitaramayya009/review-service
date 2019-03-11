package com.epam.microservices.reviewservice.service;

import com.epam.microservices.reviewservice.entity.Review;
import com.epam.microservices.reviewservice.repository.ReviewRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewService.class)
public class ReviewServiceTest {

	@Autowired
	MockMvc mockMvc;

	@InjectMocks
	ReviewService reviewService;

	@MockBean
	ReviewRepository reviewRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(reviewService)
				.build();
	}

	@Test
	public void shouldReturnReviewList() throws Exception {
		Iterable<Review> taskList = Arrays.asList(retriveReviewMockData());
		when(reviewRepository.findAll()).thenReturn(taskList);
		Iterable<Review> result = reviewService.getAllReviews();
		Assert.assertEquals(1, result.spliterator().getExactSizeIfKnown());
	}

	private Review retriveReviewMockData() {
		return new Review(30001L,"Sitaram","Good Quality");
	}

}
