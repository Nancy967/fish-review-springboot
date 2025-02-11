package com.fishreview.api.repository;

import com.fishreview.api.models.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FishRepository extends JpaRepository<Fish, Integer> {
    Optional<Fish> findByType(String type);
}
