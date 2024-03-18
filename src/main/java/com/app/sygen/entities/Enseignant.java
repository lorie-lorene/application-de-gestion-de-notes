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
@Table(name = "enseignants")
public class Enseignant 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	private boolean actif;

	private String email;
   
    @ManyToMany
    private List<Ue> ues;
    @OneToOne
    private Users user;
	
    
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	public List<Ue> getUes() {
		return ues;
	}
	public void setUes(List<Ue> ues) {
		this.ues = ues;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}    
		public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
