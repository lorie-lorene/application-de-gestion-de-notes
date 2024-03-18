package com.app.sygen.enums;

public enum Sexe 
{
	MASCULIN("M"),
	FEMININ("F"),
	AUTRE("A");

	Sexe(String key) {
	    this.value = key;
    }
    
    private String value;
    
    public String value() {
        return value;
    }
}
