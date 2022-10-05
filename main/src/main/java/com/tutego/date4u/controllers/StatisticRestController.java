package com.tutego.date4u.controllers;

import com.tutego.date4u.core.statistics.LastSeenStatistics;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
public class StatisticRestController {
    
    private final ProfileRepository profiles;
    
    
    public StatisticRestController( ProfileRepository profiles ) {
        this.profiles = profiles;
    }
    
    @RequestMapping( "/api/stat/total" )
    // long als Rückgabe ist auch in Ordnung
    public String totalNumberOfRegisteredUnicorns() {
        return String.valueOf( profiles.count() );
    }
    
    @GetMapping( "/api/stat/last-seen" )
    public LastSeenStatistics lastSeenStatistics(@RequestParam("start") Optional<String> startOpt, @RequestParam("end") Optional<String> endOpt) {
        YearMonth start = startOpt.isEmpty()? YearMonth.now().minusYears( 2 ) :YearMonth.parse(startOpt.get());
        YearMonth end = endOpt.isEmpty()? YearMonth.now() :YearMonth.parse(endOpt.get());
        
        
        return new LastSeenStatistics(
                                profiles.findMonthlyProfileCount().stream().map(
                                        tuple -> {
                                            return new LastSeenStatistics.Data(start,
                                                    (int) start.until(end, ChronoUnit.MONTHS));
                                        } ).toList());


    }
    
}
