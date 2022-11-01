package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    
    @Query( nativeQuery = true, value = """
  SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
  FROM profile
  GROUP BY YEAR(lastseen), MONTH(lastseen)""" )
    List<Tuple> findMonthlyProfileCount();
    
    
    
    @Query(value = """
         SELECT p FROM Profile as p WHERE
         (p.id <> :ownId) and
         (:nickname is null or p.nickname like %:nickname% ) and
         (p.hornlength >= :minHornlength) and
         (p.hornlength <= :maxHornlength) and
         (p.birthdate between :maxAgeAsDate and :minAgeAsDate ) and
         (:gender is null or p.gender = :gender )
         """
    )
    Page<Profile> findAllProfilesBySearchParams(
            long ownId,
            String nickname,
            int minHornlength,
            int maxHornlength,
            LocalDate minAgeAsDate,
            LocalDate maxAgeAsDate,
            Byte gender,
            Pageable pageable);
    
    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.profilesThatILike WHERE p.id = (:id)")
    public Profile findByIdAndFetchLikee(@Param("id") Long id);
    
    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.profilesThatLikeMe WHERE p.id = (:id)")
    public Profile findByIdAndFetchLiker(@Param("id") Long id);

    
  
}
