package com.app.sygen.entities;

public class PvUeData implements Comparable<PvUeData>{
    
    private String matricule;
    private String nom;
    private String niveau;
    private Integer annonymaCc;
    private Double noteCc;
    private Integer annonymaSn;
    private Double noteSn;
    private Integer annonymaTp;
    private Double noteTp;
    private Double total;
    private String decision;
    private String mention;
    private String observation;
    public PvUeData(String matricule, String nom, String niveau, int annonymaCc, Double noteCc, int annonymaSn,
            Double noteSn, int annonymaTp, Double noteTp, Double total, String decision, String mention,
            String observation) {
        this.matricule = matricule;
        this.nom = nom;
        this.niveau = niveau;
        this.annonymaCc = annonymaCc;
        this.noteCc = noteCc;
        this.annonymaSn = annonymaSn;
        this.noteSn = noteSn;
        this.annonymaTp = annonymaTp;
        this.noteTp = noteTp;
        this.total = total;
        this.decision = decision;
        this.mention = mention;
        this.observation = observation;
    }
    public PvUeData() {
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
    public String getNiveau() {
        return niveau;
    }
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    public Integer getAnnonymaCc() {
        return annonymaCc;
    }
    public void setAnnonymaCc(Integer annonymaCc) {
        this.annonymaCc = annonymaCc;
    }
    public Double getNoteCc() {
        return noteCc;
    }
    public void setNoteCc(Double noteCc) {
        this.noteCc = noteCc;
    }
    public Integer getAnnonymaSn() {
        return annonymaSn;
    }
    public void setAnnonymaSn(Integer annonymaSn) {
        this.annonymaSn = annonymaSn;
    }
    public Double getNoteSn() {
        return noteSn;
    }
    public void setNoteSn(Double noteSn) {
        this.noteSn = noteSn;
    }
    public Integer getAnnonymaTp() {
        return annonymaTp;
    }
    public void setAnnonymaTp(Integer annonymaTp) {
        this.annonymaTp = annonymaTp;
    }
    public Double getNoteTp() {
        return noteTp;
    }
    public void setNoteTp(Double noteTp) {
        this.noteTp = noteTp;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public String getDecision() {
        return decision;
    }
    public void setDecision(String decision) {
        this.decision = decision;
    }
    public String getMention() {
        return mention;
    }
    public void setMention(String mention) {
        this.mention = mention;
    }
    public String getObservation() {
        return observation;
    }
    public void setObservation(String observation) {
        this.observation = observation;
    }
    @Override
    public int compareTo(PvUeData pvUeData) {
        return this.nom.compareToIgnoreCase(pvUeData.nom) ;
    }

    
    
}
