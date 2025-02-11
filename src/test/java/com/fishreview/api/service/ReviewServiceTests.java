package com.fishreview.api.service;

import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.ReviewDto;
import com.fishreview.api.models.Fish;
import com.fishreview.api.models.Review;
import com.fishreview.api.repository.FishRepository;
import com.fishreview.api.repository.ReviewRepository;
import com.fishreview.api.service.impl.ReviewServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private FishRepository fishRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Fish fish;
    private Review review;
    private ReviewDto reviewDto;
    private FishDto fishDto;

    @BeforeEach
    public void init() {
        fish = Fish.builder().name("pikachu").type("electric").build();
        fishDto = FishDto.builder().name("pickachu").type("electric").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("review title").content("test content").stars(5).build();
    }

    @Test
    public void ReviewService_CreateReview_ReturnsReviewDto() {
        when(fishRepository.findById(fish.getId())).thenReturn(Optional.of(fish));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(fish.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewsByPokemonId_ReturnReviewDto() {
        int reviewId = 1;
        when(reviewRepository.findByPokemonId(reviewId)).thenReturn(Arrays.asList(review));

        List<ReviewDto> pokemonReturn = reviewService.getReviewsByPokemonId(reviewId);

        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewById_ReturnReviewDto() {
        int reviewId = 1;
        int pokemonId = 1;

        review.setFish(fish);

        when(fishRepository.findById(pokemonId)).thenReturn(Optional.of(fish));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewDto reviewReturn = reviewService.getReviewById(reviewId, pokemonId);

        Assertions.assertThat(reviewReturn).isNotNull();
        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_UpdatePokemon_ReturnReviewDto() {
        int pokemonId = 1;
        int reviewId = 1;
        fish.setReviews(Arrays.asList(review));
        review.setFish(fish);

        when(fishRepository.findById(pokemonId)).thenReturn(Optional.of(fish));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto updateReturn = reviewService.updateReview(pokemonId, reviewId, reviewDto);

        Assertions.assertThat(updateReturn).isNotNull();
    }

    @Test
    public void ReviewService_DeletePokemonById_ReturnVoid() {
        int pokemonId = 1;
        int reviewId = 1;

        fish.setReviews(Arrays.asList(review));
        review.setFish(fish);

        when(fishRepository.findById(pokemonId)).thenReturn(Optional.of(fish));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertAll(() -> reviewService.deleteReview(pokemonId, reviewId));
    }


}
