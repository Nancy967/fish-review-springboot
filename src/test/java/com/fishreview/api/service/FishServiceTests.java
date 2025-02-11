package com.fishreview.api.service;

import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.FishResponse;
import com.fishreview.api.models.Fish;
import com.fishreview.api.repository.FishRepository;
import com.fishreview.api.service.impl.FishServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FishServiceTests {

    @Mock
    private FishRepository fishRepository;

    @InjectMocks
    private FishServiceImpl fishService;

    @Test
    public void FishService_CreateFish_ReturnsFishDto() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();
        FishDto fishDto = FishDto.builder().name("pickachu").type("雀鯛").build();

        when(fishRepository.save(Mockito.any(Fish.class))).thenReturn(fish);

        FishDto savedFish = fishService.createFish(fishDto);

        Assertions.assertThat(savedFish).isNotNull();
    }

    @Test
    public void FishService_GetAllFish_ReturnsResponseDto() {
        Page<Fish> fishs = Mockito.mock(Page.class);

        when(fishRepository.findAll(Mockito.any(Pageable.class))).thenReturn(fishs);

        FishResponse saveFish = fishService.getAllFish(1,10);

        Assertions.assertThat(saveFish).isNotNull();
    }
    @Test
    public void FishService_FindById_ReturnFishDto() {
        int fishId = 1;
        Fish fish = Fish.builder().id(1).name("公子小丑").type("雀鯛").type("this is a type").build();
        when(fishRepository.findById(fishId)).thenReturn(Optional.ofNullable(fish));

        FishDto fishReturn = fishService.getFishById(fishId);

        Assertions.assertThat(fishReturn).isNotNull();
    }

    @Test
    public void FishService_UpdateFish_ReturnFishDto() {
        int fishId = 1;
        Fish fish = Fish.builder().id(1).name("公子小丑").type("雀鯛").type("this is a type").build();
        FishDto fishDto = FishDto.builder().id(1).name("公子小丑").type("雀鯛").type("this is a type").build();

        when(fishRepository.findById(fishId)).thenReturn(Optional.ofNullable(fish));
        when(fishRepository.save(fish)).thenReturn(fish);

        FishDto updateReturn = fishService.updateFish(fishDto, fishId);

        Assertions.assertThat(updateReturn).isNotNull();
    }

    @Test
    public void FishService_DeleteFishById_ReturnVoid() {
        int fishId = 1;
        Fish fish = Fish.builder().id(1).name("公子小丑").type("雀鯛").type("this is a type").build();

        when(fishRepository.findById(fishId)).thenReturn(Optional.ofNullable(fish));
        doNothing().when(fishRepository).delete(fish);

        assertAll(() -> fishService.deleteFishId(fishId));
    }
}
