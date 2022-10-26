package com.tutego.date4u.core.dto;

public class FilterFormData {
    
    public int DEFAULT_GENDER = (int)Gender.ALL.getGender();
    public final int MIN_AGE = 18;
    public final int MAX_AGE = 99;
    public final int MIN_HORNLEN = 0;
    public final int MAX_HORNLEN = 100;
    private int gender;
    
    private int attractedToGender;
    
    private int minAge;
    
    private int maxAge;
    
    private int minHornlength;
    private int maxHornlength;
    
    public FilterFormData(){
        this.gender = DEFAULT_GENDER;
        this.minAge = MIN_AGE;
        this.maxAge = MAX_AGE;
        this.minHornlength = MIN_HORNLEN;
        this.maxHornlength = MAX_HORNLEN;
    
    }
    
    public int getGender() {
        return gender;
    }
    
    public void setGender(int gender) {
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
    
//    public int getAttractedToGender() {
//        return attractedToGender;
//    }
//
//    public void setAttractedToGender(int attractedToGender) {
//        this.attractedToGender = attractedToGender;
//    }
    
   
    
}
