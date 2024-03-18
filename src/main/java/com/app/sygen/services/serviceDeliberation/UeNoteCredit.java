package com.app.sygen.services.serviceDeliberation;

import com.app.sygen.entities.DetailPvUe;

public class UeNoteCredit{

    public static final String getIdDetailPv = null;
    private Long IdDetailPv;
    private Boolean status;
    private Float note;
    private Float quota;
    private Integer credit;
    private Integer semestre;
    private Integer annee;


    public UeNoteCredit(DetailPvUe detailPvUe){
        IdDetailPv = detailPvUe.getId(); 
        System.out.println("IdDetailPv **** "+IdDetailPv);
        status(detailPvUe);
        note(detailPvUe);
        credit(detailPvUe);
        semestre(detailPvUe);
        annee(detailPvUe);
    }
    public UeNoteCredit(UeNoteCredit ueNoteCredit){
        this.IdDetailPv = ueNoteCredit.IdDetailPv;
        System.out.println("IdDetailPv **** "+IdDetailPv);
        this.annee = ueNoteCredit.annee;
        this.credit = ueNoteCredit.credit;
        this.note = ueNoteCredit.note;
        this.quota = ueNoteCredit.quota;
        this.credit = ueNoteCredit.credit;
    }

    public UeNoteCredit(){

    }

    public void status(DetailPvUe detailPvUe){
    
        final Integer N = 34 ; 
        if(detailPvUe.getNote() > N){
            this.status = true;
        }
        else{
            this.status = false;
        }
    }

    public void note(DetailPvUe detailPvUe){
        this.note = detailPvUe.getNote();
        this.quota(this.note);
    }

    private void credit(DetailPvUe detailPvUe){
        this.credit = detailPvUe.getPvUe().getUe().getCredit();
    }

    private void semestre(DetailPvUe detailPvUe){
        this.semestre = detailPvUe.getPvUe().getSemestre();
    }

    private void annee(DetailPvUe detailPvUe){
        this.annee = detailPvUe.getPvUe().getDate().getYear();
    }

    public void quota(Float Note){

        if(Note >= 80){this.quota = 4F;}
        else if(Note >= 75){this.quota = 3.70F;}
        else if(Note >= 70){this.quota = 3.30F;}
        else if(Note >= 65){this.quota = 3.00F;}
        else if(Note >= 60){this.quota = 2.70F;}
        else if(Note >= 55){this.quota = 2.30F;}
        else if(Note >= 50){this.quota = 2.00F;}
        else if(Note >= 45){this.quota = 1.70F;}
        else if(Note >= 40){this.quota = 1.30F;}
        else if(Note >= 35){this.quota = 1.00F;}
        else {this.quota = 0.00F;} // Echec
    }


























































    public Long getIdDetailPv() {
        return IdDetailPv;
    }
    public void setIdDetailPv(Long idDetailPv) {
        IdDetailPv = idDetailPv;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Float getNote() {
        return note;
    }
    public void setNote(Float note) {
        this.note = note;
    }
    public Float getQuota() {
        return quota;
    }
    public void setQuota(Float quota) {
        this.quota = quota;
    }
    public Integer getCredit() {
        return credit;
    }
    public void setCredit(Integer credit) {
        this.credit = credit;
    }
    public Integer getSemestre() {
        return semestre;
    }
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
    public Integer getAnnee() {
        return annee;
    }
    public void setAnnee(Integer annee) {
        this.annee = annee;
    }






    
    
}