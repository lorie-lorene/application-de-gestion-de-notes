package com.app.sygen.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;

// import com.ite/tpdf.text.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class SeanceDeliberation {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Jury createJury;

    @ManyToMany
    private List<Jury> membres;

    private String cle;

    private Date day;

    private Boolean isWaiting;

    @ManyToOne
    private Filiere filiere;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Jury getCreateJury() {
        return createJury;
    }

    public void setCreateJury(Jury createJury) {
        this.createJury = createJury;
    }

    public List<Jury> getMembres() {
        return membres;
    }

    public void setMembres(List<Jury> membres) {
        this.membres = membres;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(Boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    
}
