package com.app.sygen.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "details_pv_ue")
public class DetailPvUe 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float note;
    
    @ManyToOne
    private PvUe pvUe;
    @ManyToOne
    private Etudiant etudiant;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PvDeliberation> pvDeliberations = new ArrayList<>();
	
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public float getNote() {
		return note;
	}
	public void setNote(float note) {
		this.note = note;
	}
	public PvUe getPvUe() {
		return pvUe;
	}
	public void setPvUe(PvUe pvUe) {
		this.pvUe = pvUe;
	}
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	public List<PvDeliberation> getPvDeliberations() {
		return pvDeliberations;
	}
	public void setPvDeliberations(List<PvDeliberation> pvDeliberations) {
		this.pvDeliberations = pvDeliberations;
	}
	public DetailPvUe(float note, PvUe pvUe, Etudiant etudiant) {
		this.note = note;
		this.pvUe = pvUe;
		this.etudiant = etudiant;
	}
	public DetailPvUe() {
	}

}
