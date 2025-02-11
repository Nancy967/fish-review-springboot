package com.fishreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishreview.api.controllers.FishController;
import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.FishResponse;
import com.fishreview.api.dto.ReviewDto;
import com.fishreview.api.models.Fish;
import com.fishreview.api.models.Review;
import com.fishreview.api.service.FishService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = FishController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FishControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FishService fishService;

    @Autowired
    private ObjectMapper objectMapper;
    private Fish fish;
    private Review review;
    private ReviewDto reviewDto;
    private FishDto fishDto;

    @BeforeEach
    public void init() {
        fish = Fish.builder().name("公子小丑").type("雀鯛").build();
        fishDto = FishDto.builder().name("公子小丑").type("雀鯛").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("review title").content("test content").stars(5).build();
    }

    @Test
    public void FishController_CreateFish_ReturnCreated() throws Exception {
        given(fishService.createFish(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/fish/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fishDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(fishDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(fishDto.getType())));
    }

    @Test
    public void FishController_GetAllFish_ReturnResponseDto() throws Exception {
        FishResponse responseDto = FishResponse.builder().pageSize(10).last(true).pageNo(1).content(Arrays.asList(fishDto)).build();
        when(fishService.getAllFish(1,10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/api/fish")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo","1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    public void FishController_FishDetail_ReturnFishDto() throws Exception {
        int fishId = 1;
        when(fishService.getFishById(fishId)).thenReturn(fishDto);

        ResultActions response = mockMvc.perform(get("/api/fish/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fishDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(fishDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(fishDto.getType())));
    }

    @Test
    public void FishController_UpdateFish_ReturnFishDto() throws Exception {
        int fishId = 1;
        when(fishService.updateFish(fishDto, fishId)).thenReturn(fishDto);

        ResultActions response = mockMvc.perform(put("/api/fish/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fishDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(fishDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(fishDto.getType())));
    }

    @Test
    public void FishController_DeleteFish_ReturnString() throws Exception {
        int fishId = 1;
        doNothing().when(fishService).deleteFishId(1);

        ResultActions response = mockMvc.perform(delete("/api/fish/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
