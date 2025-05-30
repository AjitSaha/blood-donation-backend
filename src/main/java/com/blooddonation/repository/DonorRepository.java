package com.blooddonation.repository;

import com.blooddonation.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    
    Optional<Donor> findByEmail(String email);
    
    List<Donor> findByBloodGroup(String bloodGroup);
    
    @Query("SELECT d FROM Donor d WHERE d.bloodGroup = :bloodGroup AND LOWER(d.address) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Donor> findByBloodGroupAndLocation(@Param("bloodGroup") String bloodGroup, @Param("location") String location);
    
    @Query("SELECT d FROM Donor d WHERE LOWER(d.address) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Donor> findByLocation(@Param("location") String location);
    
    boolean existsByEmail(String email);
}