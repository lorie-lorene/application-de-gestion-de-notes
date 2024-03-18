package com.app.sygen.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "paiements")
public class Paiement 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String nomBank;
    private Date datePaiement;
    private String numRecu;
    private Double montant;
    
    @ManyToOne
    private Etudiant etudiant;

    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomBank() {
		return nomBank;
	}
	public void setNomBank(String nomBank) {
		this.nomBank = nomBank;
	}
	public Date getDatePaiement() {
		return datePaiement;
	}
	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}
	public String getNumRecu() {
		return numRecu;
	}
	public void setNumRecu(String numRecu) {
		this.numRecu = numRecu;
	}
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
}