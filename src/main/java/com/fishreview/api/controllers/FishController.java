package com.fishreview.api.controllers;


import com.fishreview.api.dto.FishDto;
import com.fishreview.api.dto.FishResponse;
import com.fishreview.api.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class FishController {

    private FishService fishService;

    @Autowired
    public FishController(FishService fishService) {
        this.fishService = fishService;
    }

    @GetMapping("fish")
    public ResponseEntity<FishResponse> getFishs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(fishService.getAllFish(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("fish/{id}")
    public ResponseEntity<FishDto> fishDetail(@PathVariable int id) {
        return ResponseEntity.ok(fishService.getFishById(id));

    }

    @PostMapping("fish/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FishDto> createFish(@RequestBody FishDto fishDto) {
        return new ResponseEntity<>(fishService.createFish(fishDto), HttpStatus.CREATED);
    }

    @PutMapping("fish/{id}/update")
    public ResponseEntity<FishDto> updateFish(@RequestBody FishDto fishDto, @PathVariable("id") int fishId) {
        FishDto response = fishService.updateFish(fishDto, fishId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("fish/{id}/delete")
    public ResponseEntity<String> deleteFish(@PathVariable("id") int fishId) {
        fishService.deleteFishId(fishId);
        return new ResponseEntity<>("Fish delete", HttpStatus.OK);
    }

}
