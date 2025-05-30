package com.blooddonation.service;

import com.blooddonation.dto.DonorDTO;
import com.blooddonation.model.Donor;

import java.util.List;

public interface DonorService {
    
    DonorDTO registerDonor(DonorDTO donorDTO);
    
    List<DonorDTO> getAllDonors();
    
    DonorDTO getDonorById(Long id);
    
    DonorDTO getDonorByEmail(String email);
    
    List<DonorDTO> searchDonors(String bloodGroup, String location);
    
    DonorDTO updateDonor(Long id, DonorDTO donorDTO);
    
    void deleteDonor(Long id);
    
    boolean isDonorExists(String email);
    
    // Helper methods for conversion
    DonorDTO convertToDTO(Donor donor);
    
    Donor convertToEntity(DonorDTO donorDTO);
}