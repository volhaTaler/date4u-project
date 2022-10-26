package com.tutego.date4u.core.dto;

public enum Gender {
    
    OTHER(0),
    MALE(1),
    FEMALE(2),
    ALL(3);
    
    
    public final long gender;
    private Gender(long gender){
        this.gender = gender;
    }
    
    public long getGender() {
        return gender;
    }
    
    public static long[] getAllGenders(){
        long[] genders = new long[Gender.values().length];
        genders[0] = Gender.OTHER.gender;
        genders[1] = Gender.MALE.gender;
        genders[2] = FEMALE.gender;
        return genders;
    }
    
    public static String getGenderString(int gender){
        
        switch(gender){
            case  0: return OTHER.toString();
            case 1: return MALE.toString();
            case 2: return FEMALE.toString();
        }
        return ALL.toString();
    }
    
    
}
