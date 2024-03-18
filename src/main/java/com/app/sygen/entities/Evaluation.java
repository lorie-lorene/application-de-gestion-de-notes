package com.app.sygen.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "evaluations")
public class Evaluation 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private Date dateEval ;
    private int noteSur ; 
    private String typeEval ;
    private int semestre;
    
    @ManyToOne
    private Ue ue; 
    @OneToMany
    private List<Participe> participes;
	
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateEval() {
		return dateEval;
	}
	public void setDateEval(Date dateEval) {
		this.dateEval = dateEval;
	}
	public int getNoteSur() {
		return noteSur;
	}
	public void setNoteSur(int noteSur) {
		this.noteSur = noteSur;
	}
	public String getTypeEval() {
		return typeEval;
	}
	public void setTypeEval(String typeEval) {
		this.typeEval = typeEval;
	}
	public int getSemestre() {
		return semestre;
	}
	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}
	public Ue getUe() {
		return ue;
	}
	public void setUe(Ue ue) {
		this.ue = ue;
	}
	public List<Participe> getParticipes() {
		return participes;
	}
	public void setParticipes(List<Participe> participes) {
		this.participes = participes;
	}
}
