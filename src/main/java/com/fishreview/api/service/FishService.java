package com.fishreview.api.service;

import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.FishResponse;

public interface FishService {
    FishDto createFish(FishDto fishDto);
    FishResponse getAllFish(int pageNo, int pageSize);
    FishDto getFishById(int id);
    FishDto updateFish(FishDto fishDto, int id);
    void deleteFishId(int id);
}
