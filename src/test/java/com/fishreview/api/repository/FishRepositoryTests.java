package com.fishreview.api.repository;

import com.fishreview.api.models.Fish;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FishRepositoryTests {

    @Autowired
    private FishRepository fishRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {

        //Arrange
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        //Act
        Fish savedFish = fishRepository.save(fish);

        //Assert
        Assertions.assertThat(savedFish).isNotNull();
        Assertions.assertThat(savedFish.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnMoreThenOnePokemon() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();
        Fish fish2 = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        fishRepository.save(fish);
        fishRepository.save(fish2);

        List<Fish> fishList = fishRepository.findAll();

        Assertions.assertThat(fishList).isNotNull();
        Assertions.assertThat(fishList.size()).isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnPokemon() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        fishRepository.save(fish);

        Fish fishList = fishRepository.findById(fish.getId()).get();

        Assertions.assertThat(fishList).isNotNull();
    }

    @Test
    public void PokemonRepository_FindByType_ReturnPokemonNotNull() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        fishRepository.save(fish);

        Fish fishList = fishRepository.findByType(fish.getType()).get();

        Assertions.assertThat(fishList).isNotNull();
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemonNotNull() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        fishRepository.save(fish);

        Fish fishSave = fishRepository.findById(fish.getId()).get();
        fishSave.setType("龍");
        fishSave.setName("糖果龍");

        Fish updatedFish = fishRepository.save(fishSave);

        Assertions.assertThat(updatedFish.getName()).isNotNull();
        Assertions.assertThat(updatedFish.getType()).isNotNull();
    }

    @Test
    public void PokemonRepository_PokemonDelete_ReturnPokemonIsEmpty() {
        Fish fish = Fish.builder()
                .name("公子小丑")
                .type("雀鯛").build();

        fishRepository.save(fish);

        fishRepository.deleteById(fish.getId());
        Optional<Fish> pokemonReturn = fishRepository.findById(fish.getId());

        Assertions.assertThat(pokemonReturn).isEmpty();
    }


}
