package com.app.sygen.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "participes")
public class Participe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String matricule;
	private float note;
	
	@Column(columnDefinition = "integer")
	private Integer anonymat;
	private String nomEtudiant;
	private Date dateImportation;
	private String anneeImportation;

	@ManyToOne
	private Users user;
	@ManyToOne
	public Evaluation evaluation;
	@ManyToOne
	private Etudiant etudiant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getAnneeImportation() {
		return anneeImportation;
	}

	public void setAnneeImportation(String anneeImportation) {
		this.anneeImportation = anneeImportation;
	}

	public Float getNote() {
		return note;
	}

	public void setNote(Float note) {
		this.note = note;
	}

	public Integer getAnonymat() {
		return anonymat;
	}

	public void setAnonymat(Integer anonymat) {
		this.anonymat = anonymat;
	}

	public String getNomEtudiant() {
		return nomEtudiant;
	}

	public void setNomEtudiant(String nomEtudiant) {
		this.nomEtudiant = nomEtudiant;
	}

	public Date getDateImportation() {
		return dateImportation;
	}

	public void setDateImportation(Date dateImportation) {
		this.dateImportation = dateImportation;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
}
