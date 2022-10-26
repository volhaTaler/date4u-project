package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    @Query( nativeQuery = true, value = """
  SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
  FROM profile
  GROUP BY YEAR(lastseen), MONTH(lastseen)""" )
    List<Tuple> findMonthlyProfileCount();
    
    
    Page<Profile> findProfileByBirthdateAndHornlength(
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
    
    Page<Profile> findProfileByBirthdateBetweenAndHornlength(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
    Page<Profile> findProfileByBirthdateAndHornlengthBetween(
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            @Param("maxHornlength") int maxHornlength,
            Pageable pageable
    );
    Page<Profile> findProfileByBirthdateBetweenAndHornlengthBetween(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            @Param("maxHornlength") int maxHornlength,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateAndHornlength(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateBetweenAndHornlength(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
  
    Page<Profile> findProfileByGenderAndBirthdateAndHornlengthBetween(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("maxHornlength") int maxHornlength,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateBetweenAndHornlengthBetween(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("maxHornlength") int maxHornlength,
            @Param("minHornlength") int minHornlength,
            Pageable pageable
    );
    
//
//    Page<Profile> findProfileByBirthdateBetweenAndGender(
//            @Param("startDate")LocalDate startDate,
//            @Param("endDate")LocalDate endDate,
//            @Param("gender") long gender,
//          Pageable pageable
//            );
//    Page<Profile> findProfileByBirthdateBetween(
//            @Param("startDate")LocalDate startDate,
//            @Param("endDate")LocalDate endDate,
//             Pageable pageable
//    );
    
  
}
