package com.app.sygen.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
//@Table(name = "jury")
public class Jury 
{
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long id;

    @ManyToMany
    private List<Statut> attribut;
    @OneToOne
    private Users user;
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Statut> getAttribut() {
		return attribut;
	}
	public void setAttribut(List<Statut> attribut) {
		this.attribut = attribut;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}
