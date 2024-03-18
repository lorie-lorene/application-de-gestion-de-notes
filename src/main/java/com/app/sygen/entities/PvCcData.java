package com.app.sygen.entities;

public class PvCcData {
    private String matricule;
    private String nom;
    private Double note;
    private String observation;
    public PvCcData() {
    }
    public PvCcData(String matricule, String nom, Double note, String observation) {
        this.matricule = matricule;
        this.nom = nom;
        this.note = note;
        this.observation = observation;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Double getNote() {
        return note;
    }
    public void setNote(Double note) {
        this.note = note;
    }
    public String getObservation() {
        return observation;
    }
    public void setObservation(String observation) {
        this.observation = observation;
    }

    
}
