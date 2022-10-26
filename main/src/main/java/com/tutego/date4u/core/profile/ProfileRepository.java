package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;


public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    @Query( nativeQuery = true, value = """
  SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
  FROM profile
  GROUP BY YEAR(lastseen), MONTH(lastseen)""" )
    List<Tuple> findMonthlyProfileCount();
    
    
    Page<Profile> findProfileByBirthdateAndHornlengthAndIdNot(
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    
    Page<Profile> findProfileByBirthdateBetweenAndHornlengthAndIdNot(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    Page<Profile> findProfileByBirthdateAndHornlengthBetweenAndIdNot(
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            @Param("maxHornlength") int maxHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    Page<Profile> findProfileByBirthdateBetweenAndHornlengthBetweenAndIdNot(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            @Param("maxHornlength") int maxHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateAndHornlengthAndIdNot(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateBetweenAndHornlengthAndIdNot(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
  
    Page<Profile> findProfileByGenderAndBirthdateAndHornlengthBetweenAndIdNot(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("maxHornlength") int maxHornlength,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    
    Page<Profile> findProfileByGenderAndBirthdateBetweenAndHornlengthBetweenAndIdNot(
            @Param("gender") long gender,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("maxHornlength") int maxHornlength,
            @Param("minHornlength") int minHornlength,
            @Param("ownId") long ownId,
            Pageable pageable
    );
    
//
//    Page<Profile> findProfileByBirthdateBetweenAndGenderAndIdNot(
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
