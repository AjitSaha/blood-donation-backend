package com.blooddonation.controller;

import com.blooddonation.dto.DonorDTO;
import com.blooddonation.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = {"http://localhost:3000", "https://your-frontend-domain.com"})
public class DonorController {
    
    private final DonorService donorService;
    
    @Autowired
    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }
    
    @PostMapping
    public ResponseEntity<DonorDTO> registerDonor(@Valid @RequestBody DonorDTO donorDTO) {
        DonorDTO registeredDonor = donorService.registerDonor(donorDTO);
        return new ResponseEntity<>(registeredDonor, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<DonorDTO>> searchDonors(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String location) {
        List<DonorDTO> donors = donorService.searchDonors(bloodGroup, location);
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable Long id) {
        DonorDTO donor = donorService.getDonorById(id);
        return ResponseEntity.ok(donor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DonorDTO> updateDonor(
            @PathVariable Long id,
            @Valid @RequestBody DonorDTO donorDTO) {
        DonorDTO updatedDonor = donorService.updateDonor(id, donorDTO);
        return ResponseEntity.ok(updatedDonor);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.noContent().build();
    }
}