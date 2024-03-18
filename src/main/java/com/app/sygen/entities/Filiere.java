package com.app.sygen.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "filieres")
public class Filiere 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String code;
    private String libelle;
    
    @OneToMany
    private List<Statut> statut;
    @OneToMany
    private List<Etudiant> etudiants;

	@OneToMany
    private List<Ue> ues;
	
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public List<Statut> getStatut() {
		return statut;
	}
	public void setStatut(List<Statut> statut) {
		this.statut = statut;
	}
	public List<Etudiant> getEtudiants() {
		return etudiants;
	}
	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}
	public List<Ue> getUes() {
		return ues;
	}
	public void setUes(List<Ue> ues) {
		this.ues = ues;
	}
}
