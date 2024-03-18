package com.app.sygen.services.serviceDeliberation;


public class TableDeliberation{

    private String codeUe;
    private String nom;
    private String matricule;
    private Float oldNote;
    private Float actuNote;
   
    public String getCodeUe() {
        return codeUe;
    }
    public void setCodeUe(String codeUe) {
        this.codeUe = codeUe;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public Float getOldNote() {
        return oldNote;
    }
    public void setOldNote(Float oldNote) {
        this.oldNote = oldNote;
    }
    public Float getActuNote() {
        return actuNote;
    }
    public void setActuNote(Float float1) {
        this.actuNote = float1;
    }
}