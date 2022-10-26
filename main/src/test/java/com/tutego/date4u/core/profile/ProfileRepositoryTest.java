package com.tutego.date4u.core.profile;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.persistence.EntityManager;
import org.springframework.test.annotation.Rollback;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfileRepositoryTest {

  private final Logger log = LoggerFactory.getLogger( getClass() );
  @Autowired private ProfileRepository profileRepository;
  @Autowired private DataSource dataSource;
  @Autowired private JdbcTemplate jdbcTemplate;
  //@Autowired private EntityManager entityManager;
  
  @Test
  void injectedComponentsAreNotNull(){
    assertThat(dataSource).isNotNull();
    assertThat(jdbcTemplate).isNotNull();
    //assertThat(entityManager).isNotNull();
    assertThat(profileRepository).isNotNull();
  }
  

  @Test
  void database_contains_no_entities() {
    assertThat( profileRepository.count() ).isEqualTo(20);
  //  assertThat( profileRepository.findAll() ).isEmpty();
  }
  
  // JUnit test for saveEmployee
  @Test
  @Order(1)
  //@Rollback(value = false)
  public void saveProfileTest(){
    
    Profile testProfile =new Profile("Night Dash", LocalDate.now().minusYears(22), 17, 1,
            1, "Through the night", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    
    profileRepository.save(testProfile);
    
    Assertions.assertThat(testProfile.getId()).isGreaterThan(0);
  }
  
  @Test
  @Order(2)
  public void getProfileTest(){
    
    Profile testProfile = profileRepository.findById(1L).get();
    
    Assertions.assertThat(testProfile.getId()).isEqualTo(1L);
    
  }
  
  @Test
  @Order(3)
  public void getListOfProfilesTest(){
    
    Profile testProfile =new Profile("Dawn Sun", LocalDate.now().minusYears(37), 12, 1,
            2, "In the morning or in the evening", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
  
    profileRepository.save(testProfile);
    
    List<Profile> profiles = profileRepository.findAll();
    
    Assertions.assertThat(profiles.size()).isGreaterThan(0);
    
  }
  
  @Test
  @Order(4)
//  @Rollback(value = false)
  public void updateProfileTest(){
    String new_nickname = "Moony Cloud";
    
    Profile employee = profileRepository.findById(1L).get();
    
    employee.setNickname(new_nickname);
    
    Profile profileUpdated =  profileRepository.save(employee);
    
    Assertions.assertThat(profileUpdated.getNickname()).isEqualTo(new_nickname);
    
  }
  
}