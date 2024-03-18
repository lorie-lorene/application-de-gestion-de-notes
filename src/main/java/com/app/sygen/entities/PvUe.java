package com.app.sygen.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class PvUe 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int semestre;
    private String annee;
    @Column
	@Temporal(TemporalType.DATE)
    private LocalDate date;
    
    @ManyToOne
    private Ue ue;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DetailPvUe> ListDetails = new ArrayList<>();
    
    public PvUe(){

	}
	

    public PvUe(int semestre, LocalDate date) {
		this.semestre = semestre;
		this.date = date;
	}
	
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSemestre() {
		return semestre;
	}
	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Ue getUe() {
		return ue;
	}
	public void setUe(Ue ue) {
		this.ue = ue;
	}
	public List<DetailPvUe> getListDetails() {
		return ListDetails;
	}
	public void setListDetails(List<DetailPvUe> listDetails) {
		ListDetails = listDetails;
	}
	public PvUe(int semestre, String annee, Ue ue) {
		this.semestre = semestre;
		this.annee = annee;
		this.ue = ue;
	}
	
}
