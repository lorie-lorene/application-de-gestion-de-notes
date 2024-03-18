package com.app.sygen.entities;

import java.util.List;

public class DataJuryCorrection {
    private List<Long> listeIdModif;
    private List<Participe> participes;
    public List<Long> getListeIdModif() {
        return listeIdModif;
    }
    public void setListeIdModif(List<Long> listeIdModif) {
        this.listeIdModif = listeIdModif;
    }
    public List<Participe> getParticipes() {
        return participes;
    }
    public void setParticipes(List<Participe> participes) {
        this.participes = participes;
    }

    
}
