package com.tutego.date4u.core.dto;

import com.tutego.date4u.core.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class PageDTO {
    private static final int RESULTS_PER_PAGE = 5;
    private static final String PREV_PAGE = "prev";
    
    private final FilterFormData currentSearchFilter;
    private Long trackId;
    private final List <Long> prevTrackIds = new ArrayList<>();
    private final Integer resultsPerPage;
    private Long totalResults;
    
    private Long displayedResults;
    private List<Profile> items;
    
    public PageDTO() {
        this.resultsPerPage = RESULTS_PER_PAGE;
        this.items = new ArrayList<>();
        this.currentSearchFilter = new FilterFormData();
        this.displayedResults = 0L;
       // this.trackId = 0L;
       // this.prevTrackIds.add(0L);
    }
    
    public void updateTrackId(){
        if(!this.items.isEmpty())
        {
            trackId = this.items.get(this.getItems().size()-1).getId();
        }else{
            trackId = null;
        }
        
    }
    public void updateIDList(String page){
        if( !page.equals(PREV_PAGE) && !this.items.isEmpty())
        {
            this.getPrevTrackIds().add(this.getItems().get(0).getId()-1);
        }
    }
    
    public void resetPrevIDList(){
        this.prevTrackIds.clear();
        
    }
    
    public List<Long> getPrevTrackIds() {
        return prevTrackIds;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }
    public List<Profile> getItems() {
        return items;
    }
    public void setItems(List<Profile> items) {
        this.items = items;
    }
    
   
    public Long getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }
    
    public Long getDisplayedResults() {
        return displayedResults;
    }
    
    public void setDisplayedResults(Long displayedResults) {
        this.displayedResults = displayedResults;
    }
    
    public FilterFormData getCurrentSearchFilter() {
        return this.currentSearchFilter;
    }
    
    public boolean nextPageExist(){
        
        return this.trackId != null;
        //return this.displayedResults < this.totalResults;
    }
    
    public boolean prevPageExist(){
        
        return this.prevTrackIds.size() > 1;
       // return this.displayedResults > this.getResultsPerPage();
    }
    
    
    
    public void setCurrentSearchParams(FilterFormData updatedSearchParams) {
        this.currentSearchFilter.setGender(updatedSearchParams.getGender());
        this.currentSearchFilter.setMaxAge(updatedSearchParams.getMaxAge());
        this.currentSearchFilter.setMinAge(updatedSearchParams.getMinAge());
        this.currentSearchFilter.setMaxHornlength(updatedSearchParams.getMaxHornlength());
        this.currentSearchFilter.setMinHornlength(updatedSearchParams.getMinHornlength());
        
    }
    
    public Long getRequiredID(String page){
        Long result;
        if (PREV_PAGE.equals(page)) {
            result = this.getPrevTrackIds().get(this.getPrevTrackIds().size()-1);
            this.getPrevTrackIds().remove(this.getPrevTrackIds().size()-1);
        } else {
            result = this.trackId;
        }
        return result;
    }
    
    public void updateDisplayedPrevResults(String page){
    
        if(page.equals(PREV_PAGE)) {
            this.setDisplayedResults(this.getDisplayedResults() - this.getItems().size());
        }
    }
    public void updateDisplayedNextResults(String page){
    
        if(! page.equals(PREV_PAGE)) {
            this.setDisplayedResults(this.getDisplayedResults() + this.getItems().size());
        }
    }
    
    
    public void resetDisplayedResults(){
    this.setDisplayedResults(0L);
    }
    
    
    
    

    
}
