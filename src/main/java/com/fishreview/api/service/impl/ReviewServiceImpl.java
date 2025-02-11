package com.fishreview.api.service.impl;

import com.fishreview.api.exceptions.FishNotFoundException;
import com.fishreview.api.exceptions.ReviewNotFoundException;
import com.fishreview.api.repository.FishRepository;
import com.fishreview.api.repository.ReviewRepository;
import com.fishreview.api.dto.ReviewDto;
import com.fishreview.api.models.Fish;
import com.fishreview.api.models.Review;
import com.fishreview.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private FishRepository fishRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, FishRepository fishRepository) {
        this.reviewRepository = reviewRepository;
        this.fishRepository = fishRepository;
    }

    @Override
    public ReviewDto createReview(int pokemonId, ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);

        Fish fish = fishRepository.findById(pokemonId).orElseThrow(() -> new FishNotFoundException("Pokemon with associated review not found"));

        review.setFish(fish);

        Review newReview = reviewRepository.save(review);

        return mapToDto(newReview);
    }

    @Override
    public List<ReviewDto> getReviewsByPokemonId(int id) {
        List<Review> reviews = reviewRepository.findByPokemonId(id);

        return reviews.stream().map(review -> mapToDto(review)).collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(int reviewId, int pokemonId) {
        Fish fish = fishRepository.findById(pokemonId).orElseThrow(() -> new FishNotFoundException("Pokemon with associated review not found"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associate pokemon not found"));

        if(review.getFish().getId() != fish.getId()) {
            throw new ReviewNotFoundException("This review does not belond to a pokemon");
        }

        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(int pokemonId, int reviewId, ReviewDto reviewDto) {
        Fish fish = fishRepository.findById(pokemonId).orElseThrow(() -> new FishNotFoundException("Pokemon with associated review not found"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associate pokemon not found"));

        if(review.getFish().getId() != fish.getId()) {
            throw new ReviewNotFoundException("This review does not belong to a pokemon");
        }

        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setStars(reviewDto.getStars());

        Review updateReview = reviewRepository.save(review);

        return mapToDto(updateReview);
    }

    @Override
    public void deleteReview(int pokemonId, int reviewId) {
        Fish fish = fishRepository.findById(pokemonId).orElseThrow(() -> new FishNotFoundException("Pokemon with associated review not found"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associate pokemon not found"));

        if(review.getFish().getId() != fish.getId()) {
            throw new ReviewNotFoundException("This review does not belong to a pokemon");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setTitle(review.getTitle());
        reviewDto.setContent(review.getContent());
        reviewDto.setStars(review.getStars());
        return reviewDto;
    }

    private Review mapToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setId(reviewDto.getId());
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setStars(reviewDto.getStars());
        return review;
    }
}
