package com.app.sygen.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historiques_notes")
public class HistoriqueNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oldMatricule;
    private String newMatricule;
    private String oldNom;
    private String newNom;
    private Float OldNote;
    private Float newNote;
    @ManyToOne
    private Users user ;

    private Date date;
    @ManyToOne 
    private Evaluation evaluation;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOldMatricule() {
        return oldMatricule;
    }
    public void setOldMatricule(String oldMatricule) {
        this.oldMatricule = oldMatricule;
    }
    public String getNewMatricule() {
        return newMatricule;
    }
    public void setNewMatricule(String newMatricule) {
        this.newMatricule = newMatricule;
    }
    public String getOldNom() {
        return oldNom;
    }
    public void setOldNom(String oldNom) {
        this.oldNom = oldNom;
    }
    public String getNewNom() {
        return newNom;
    }
    public void setNewNom(String newNom) {
        this.newNom = newNom;
    }
    public Float getOldNote() {
        return OldNote;
    }
    public void setOldNote(Float oldNote) {
        OldNote = oldNote;
    }
    public Float getNewNote() {
        return newNote;
    }
    public void setNewNote(Float newNote) {
        this.newNote = newNote;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Evaluation getEvaluation() {
        return evaluation;
    }
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }



    
    
}
