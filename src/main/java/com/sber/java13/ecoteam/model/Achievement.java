package com.sber.java13.ecoteam.model;

public enum Achievement {
    BRONZE("Бронза"),
    SILVER("Серебро"),
    GOLD("Золото"),
    PLATINUM("Платина"),
    DIAMOND("Алмаз"),
    LEGEND("Легенда");
    private final String textDisplay;
    
    Achievement(String name) {
        this.textDisplay = name;
    }
    
    public String getTextDisplay() {
        return this.textDisplay;
    }
}
