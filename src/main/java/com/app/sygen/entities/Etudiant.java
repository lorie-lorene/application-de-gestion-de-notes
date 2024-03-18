package com.app.sygen.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.app.sygen.enums.Sexe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiants")
public class Etudiant 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String matricule;
	private String nom;
	private Date dateNaiss;
	private String lieuNaiss;
	private Sexe sexe;
	private String telephone;
	private String email;
	private String adresse;
	private String diplomeEnEntree;
	private Double statutPaiement;
	
    @ManyToOne
    private Filiere filiere;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DetailPvUe>  detailsPvUe = new ArrayList<>();
    @OneToMany
    private List<HistoriqueFiliere> historiquesFilieres;
    @OneToMany 
    private List<HistoriquePaiement> historiquesPaiements;
    @OneToMany
    private List<Paiement> paiements;
    @OneToMany
    private List<Participe> participes;
	
    
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
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Date getDateNaiss() {
		return dateNaiss;
	}
	public void setDateNaiss(Date dateNaiss) {
		this.dateNaiss = dateNaiss;
	}
	public String getLieuNaiss() {
		return lieuNaiss;
	}
	public void setLieuNaiss(String lieuNaiss) {
		this.lieuNaiss = lieuNaiss;
	}
	public Sexe getSexe() {
		return sexe;
	}
	public void setSexe(Sexe sexe) {
		this.sexe = sexe;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getDiplomeEnEntree() {
		return diplomeEnEntree;
	}
	public void setDiplomeEnEntree(String diplomeEnEntree) {
		this.diplomeEnEntree = diplomeEnEntree;
	}
	public Double getStatutPaiement() {
		return statutPaiement;
	}
	public void setStatutPaiement(Double statutPaiement) {
		this.statutPaiement = statutPaiement;
	}
	public Filiere getFiliere() {
		return filiere;
	}
	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}
	public List<DetailPvUe> getDetailsPvUe() {
		return detailsPvUe;
	}
	public void setDetailsPvUe(List<DetailPvUe> detailsPvUe) {
		this.detailsPvUe = detailsPvUe;
	}
	public List<HistoriqueFiliere> getHistoriquesFilieres() {
		return historiquesFilieres;
	}
	public void setHistoriquesFilieres(List<HistoriqueFiliere> historiquesFilieres) {
		this.historiquesFilieres = historiquesFilieres;
	}
	public List<HistoriquePaiement> getHistoriquesPaiements() {
		return historiquesPaiements;
	}
	public void setHistoriquesPaiements(List<HistoriquePaiement> historiquesPaiements) {
		this.historiquesPaiements = historiquesPaiements;
	}
	public List<Paiement> getPaiements() {
		return paiements;
	}
	public void setPaiements(List<Paiement> paiements) {
		this.paiements = paiements;
	}
	public List<Participe> getParticipes() {
		return participes;
	}
	public void setParticipes(List<Participe> participes) {
		this.participes = participes;
	}
}
