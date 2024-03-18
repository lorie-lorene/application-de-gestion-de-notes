package com.app.sygen.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class StatutNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean valide;
    @ManyToOne
    private Users user;
    @ManyToOne
    private Evaluation evaluation;
    public Long getId() {
        return id;
    }
    public Evaluation getEvaluation() {
        return evaluation;
    }
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getValide() {
        return valide;
    }
    public void setValide(Boolean valide) {
        this.valide = valide;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }

    
}
