package com.fishreview.api.service.impl;

import com.fishreview.api.exceptions.FishNotFoundException;
import com.fishreview.api.repository.FishRepository;
import com.fishreview.api.service.FishService;
import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.FishResponse;
import com.fishreview.api.models.Fish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FishServiceImpl implements FishService {
    private FishRepository fishRepository;

    @Autowired
    public FishServiceImpl(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    @Override
    public FishDto createFish(FishDto fishDto) {
        Fish fish = new Fish();
        fish.setName(fishDto.getName());
        fish.setType(fishDto.getType());

        Fish newFish = fishRepository.save(fish);

        FishDto pokemonResponse = new FishDto();
        pokemonResponse.setId(newFish.getId());
        pokemonResponse.setName(newFish.getName());
        pokemonResponse.setType(newFish.getType());
        return pokemonResponse;
    }

    @Override
    public FishResponse getAllFish(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Fish> pokemons = fishRepository.findAll(pageable);
        List<Fish> listOfFish = pokemons.getContent();
        List<FishDto> content = listOfFish.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

        FishResponse fishResponse = new FishResponse();
        fishResponse.setContent(content);
        fishResponse.setPageNo(pokemons.getNumber());
        fishResponse.setPageSize(pokemons.getSize());
        fishResponse.setTotalElements(pokemons.getTotalElements());
        fishResponse.setTotalPages(pokemons.getTotalPages());
        fishResponse.setLast(pokemons.isLast());

        return fishResponse;
    }

    @Override
    public FishDto getFishById(int id) {
        Fish fish = fishRepository.findById(id).orElseThrow(() -> new FishNotFoundException("Fish could not be found"));
        return mapToDto(fish);
    }

    @Override
    public FishDto updateFish(FishDto fishDto, int id) {
        Fish fish = fishRepository.findById(id).orElseThrow(() -> new FishNotFoundException("Fish could not be updated"));

        fish.setName(fishDto.getName());
        fish.setType(fishDto.getType());

        Fish updatedFish = fishRepository.save(fish);
        return mapToDto(updatedFish);
    }

    @Override
    public void deleteFishId(int id) {
        Fish fish = fishRepository.findById(id).orElseThrow(() -> new FishNotFoundException("Fish could not be delete"));
        fishRepository.delete(fish);
    }

    private FishDto mapToDto(Fish fish) {
        FishDto fishDto = new FishDto();
        fishDto.setId(fish.getId());
        fishDto.setName(fish.getName());
        fishDto.setType(fish.getType());
        return fishDto;
    }

    private Fish mapToEntity(FishDto fishDto) {
        Fish fish = new Fish();
        fish.setName(fishDto.getName());
        fish.setType(fishDto.getType());
        return fish;
    }
}
