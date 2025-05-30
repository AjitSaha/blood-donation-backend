package com.blooddonation.service;

import com.blooddonation.dto.DonorDTO;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.model.Donor;
import com.blooddonation.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorServiceImpl implements DonorService {
    
    private final DonorRepository donorRepository;
    
    @Autowired
    public DonorServiceImpl(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }
    
    @Override
    public DonorDTO registerDonor(DonorDTO donorDTO) {
        if (donorRepository.existsByEmail(donorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        Donor donor = convertToEntity(donorDTO);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDTO(savedDonor);
    }
    
    @Override
    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DonorDTO getDonorById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        return convertToDTO(donor);
    }
    
    @Override
    public DonorDTO getDonorByEmail(String email) {
        Donor donor = donorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with email: " + email));
        return convertToDTO(donor);
    }
    
    @Override
    public List<DonorDTO> searchDonors(String bloodGroup, String location) {
        List<Donor> donors;
        
        if (bloodGroup != null && !bloodGroup.isEmpty() && location != null && !location.isEmpty()) {
            donors = donorRepository.findByBloodGroupAndLocation(bloodGroup, location);
        } else if (bloodGroup != null && !bloodGroup.isEmpty()) {
            donors = donorRepository.findByBloodGroup(bloodGroup);
        } else if (location != null && !location.isEmpty()) {
            donors = donorRepository.findByLocation(location);
        } else {
            donors = donorRepository.findAll();
        }
        
        return donors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DonorDTO updateDonor(Long id, DonorDTO donorDTO) {
        Donor existingDonor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        
        // Check if email is being changed and if it already exists
        if (!existingDonor.getEmail().equals(donorDTO.getEmail()) && 
            donorRepository.existsByEmail(donorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        existingDonor.setName(donorDTO.getName());
        existingDonor.setAge(donorDTO.getAge());
        existingDonor.setPhone(donorDTO.getPhone());
        existingDonor.setBloodGroup(donorDTO.getBloodGroup());
        existingDonor.setAddress(donorDTO.getAddress());
        existingDonor.setEmail(donorDTO.getEmail());
        existingDonor.setGender(donorDTO.getGender());
        
        Donor updatedDonor = donorRepository.save(existingDonor);
        return convertToDTO(updatedDonor);
    }
    
    @Override
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }
    
    @Override
    public boolean isDonorExists(String email) {
        return donorRepository.existsByEmail(email);
    }
    
    @Override
    public DonorDTO convertToDTO(Donor donor) {
        return new DonorDTO(
                donor.getId(),
                donor.getName(),
                donor.getAge(),
                donor.getPhone(),
                donor.getBloodGroup(),
                donor.getAddress(),
                donor.getEmail(),
                donor.getGender()
        );
    }
    
    @Override
    public Donor convertToEntity(DonorDTO donorDTO) {
        Donor donor = new Donor();
        donor.setId(donorDTO.getId());
        donor.setName(donorDTO.getName());
        donor.setAge(donorDTO.getAge());
        donor.setPhone(donorDTO.getPhone());
        donor.setBloodGroup(donorDTO.getBloodGroup());
        donor.setAddress(donorDTO.getAddress());
        donor.setEmail(donorDTO.getEmail());
        donor.setGender(donorDTO.getGender());
        return donor;
    }
}