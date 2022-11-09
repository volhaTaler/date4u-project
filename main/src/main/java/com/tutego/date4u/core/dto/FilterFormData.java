package com.tutego.date4u.core.dto;

import java.util.Objects;

/**
 * This class stores search parameters entered by user and provides them to the backend.
 */
public class FilterFormData {
    
    public static final byte DEFAULT_GENDER = (byte)Gender.ALL.getGender();
    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 99;
    public static final int MIN_HORNLEN = 0;
    public static final int MAX_HORNLEN = 50;
    private byte gender;
    private int attractedToGender;
    private int minAge;
    private int maxAge;
    private int minHornlength;
    private int maxHornlength;
    
    private boolean searchStatus;
    
    
    public FilterFormData(){
        this.gender = DEFAULT_GENDER;
        this.minAge = MIN_AGE;
        this.maxAge = MAX_AGE;
        this.minHornlength = MIN_HORNLEN;
        this.maxHornlength = MAX_HORNLEN;
        
    }
    
    public boolean isSearchStatus() {
        return searchStatus;
    }
    
    public void setSearchStatus(boolean searchStatus) {
        this.searchStatus = searchStatus;
    }
    
    public byte getGender() {
        return gender;
    }
    
    public void setGender(byte gender) {
        this.gender = gender;
    }
    
    public int getMinAge() {
        return minAge;
    }
    
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    
    public int getMaxAge() {
        return maxAge;
    }
    
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
    
    public int getMinHornlength() {
        return minHornlength;
    }
    
    public void setMinHornlength(int minHornlength) {
        this.minHornlength = minHornlength;
    }
    
    public int getMaxHornlength() {
        return maxHornlength;
    }
    
    public void setMaxHornlength(int maxHornlength) {
        this.maxHornlength = maxHornlength;
    }
    
    
    public int getAttractedToGender() {
        return attractedToGender;
    }
    
    public void setAttractedToGender(int attractedToGender) {
        this.attractedToGender = attractedToGender;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterFormData that = (FilterFormData) o;
        return gender == that.gender && attractedToGender == that.attractedToGender &&
                minAge == that.minAge && maxAge == that.maxAge && minHornlength == that.minHornlength &&
                maxHornlength == that.maxHornlength;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(gender, attractedToGender, minAge, maxAge, minHornlength, maxHornlength);
    }
}
