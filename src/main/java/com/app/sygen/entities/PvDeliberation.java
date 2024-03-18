package com.app.sygen.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class PvDeliberation 
{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Float oldNote;
	
    @ManyToOne
    private DetailPvUe detailPvUe;
    
    public PvDeliberation(DetailPvUe detailPvUe, Float oldNote) {
		this.detailPvUe = detailPvUe;
        this.oldNote = oldNote;

    }

   public PvDeliberation(){
	
   }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getOldNote() {
		return oldNote;
	}

	public void setOldNote(Float oldNote) {
		this.oldNote = oldNote;
	}
	public DetailPvUe getDetailPvUe() {
		return detailPvUe;
	}
	public void setDetailPvUe(DetailPvUe detailPvUe) {
		this.detailPvUe = detailPvUe;
	}
}
