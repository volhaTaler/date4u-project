package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    @Query( nativeQuery = true, value = """
  SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
  FROM profile
  GROUP BY YEAR(lastseen), MONTH(lastseen)""" )
    List<Tuple> findMonthlyProfileCount();
   
   Optional<Profile> findByNickname(String name);
  // @Query(value = "SELECT * FROM profiles")
    List<Profile>findByGender(int gender);
  
}
