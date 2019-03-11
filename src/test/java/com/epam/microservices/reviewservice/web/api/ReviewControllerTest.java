package com.epam.microservices.reviewservice.web.api;

import com.epam.microservices.reviewservice.entity.Review;
import com.epam.microservices.reviewservice.exceptions.ReviewNotFoundException;
import com.epam.microservices.reviewservice.service.ReviewService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ReviewControllerTest {

	@InjectMocks
	ReviewController reviewController;

	@MockBean
	private ReviewService reviewService;

	MockMvc mockMvc;

	@Before
	public void preTest() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(reviewController)
				.build();

	}

	@Test
	public void shouldRetrieveAllReviews() throws Exception {
		Iterable<Review> reviewsList = Arrays.asList((retriveReviewMockData()));
		Mockito.when(
				reviewService.getAllReviews()).thenReturn(reviewsList);

		RequestBuilder requestBuilder = get("/reviews");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"id\":30001,\"name\":\"Sitaram\",\"desc\":\"Good Quality\"}]";
		System.out.println(result.getResponse().getContentAsString());

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public void shouldRetrieveReviewById() throws Exception {
		Review reviewList = retriveReviewMockData();
		Mockito.when(
				reviewService.getReviewById(30001L)).thenReturn(Optional.of(reviewList));
		mockMvc.perform(get("/reviews/{id}", 30001L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", Matchers.is(30001)))
				.andExpect(jsonPath("$.name", is("Sitaram")));
		verify(reviewService, times(1)).getReviewById(30001L);
	}

	//@Test(expected = ReviewNotFoundException.class)
	@Ignore
	public void shouldNotRetrieveReviewByIdNotFound() throws Exception {
		Review reviewList = retriveReviewMockData();
		Mockito.when(
				reviewService.getReviewById(30001L)).thenReturn(Optional.of(reviewList));
		RequestBuilder requestBuilder = get(
				"/products/{id}", 2L);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		if (result.getResponse().getContentAsString().equals("")) {
			throw new ReviewNotFoundException("Review Not found");
		}
	}


	@Test
	public void shouldDeleteReviewById() throws Exception {
		Review review = retriveReviewMockData();
		Mockito.when(
				reviewService.getReviewById(30001L)).thenReturn(Optional.of(review));

		mockMvc.perform(
				delete("/reviews/{id}", 30001L))
				.andExpect(status().isNoContent());

		verify(reviewService, times(1)).deleteReview(review.getId());
	}

	@Test
	public void shouldNotDeleteReviewByIdInvalid() throws Exception {
		Review review = retriveReviewMockData();
		Mockito.when(
				reviewService.getReviewById(30001L)).thenReturn(Optional.of(review));

		mockMvc.perform(
				delete("/reviews/{id}", 30001L))
				.andExpect(status().isNoContent());

		verify(reviewService, times(1)).deleteReview(review.getId());
	}

	@Test
	public void shouldRetrieveAllReviewsNewWay() throws Exception {
		Iterable<Review> reviewList = Arrays.asList((retriveReviewMockData()));

		Mockito.when(
				reviewService.getAllReviews()).thenReturn(reviewList);

		MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get("/reviews").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(results.getResponse());
		verify(reviewService).getAllReviews();

	}

	private Review retriveReviewMockData() {
		return new Review(30001L,"Sitaram","Good Quality");
	}
}
