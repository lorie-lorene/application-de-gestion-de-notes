package com.app.sygen.services.serviceDeliberation;

import java.util.Optional;

public class CritereDeliberation{

    private Float mgpMin;
    private Integer nbEchecSem;
    private Float pourcentageCapitalise;
    private Float mgpMax;

    public CritereDeliberation(Optional<Float>mgpMin, Optional<Float>mgpMax, Optional<Integer>nbEchecSem, Optional<Float>pourcentageCapitalise){
        
            this.nbEchecSem = nbEchecSem.orElse(null);
            this.mgpMin = mgpMin.orElse(null);
            this.mgpMax = mgpMax.orElse(null);
            this.pourcentageCapitalise = pourcentageCapitalise.orElse(null);
    }  

    public Boolean estDeliberable(DonneesDeliberation data){

        Boolean deliberable = true;

        //critere1::mgp
        if(mgpMin != null && deliberable){
            if(data.getMgp() > this.mgpMax || data.getMgp() < this.mgpMin){
                deliberable = false;
            }
        }

        //critere2::nombre d'echecs
        if(nbEchecSem != null && deliberable){
            if(data.getNbEchec() > nbEchecSem){
                deliberable = false;
            }
        }

        //critere3::pourcentage d'ue capitalise
        if(pourcentageCapitalise != null && deliberable){
            if(data.getPourcentageCapitalise() < pourcentageCapitalise){
                deliberable = false;
            }
        }


        //critere4::....

        //critere5::...


        return deliberable;
    }














































    public CritereDeliberation(){

    }

    public Float getMgpMin() {
        return mgpMin;
    }

    public void setMgpMin(Float mgpMin) {
        this.mgpMin = mgpMin;
    }

    public Integer getNbEchecSem() {
        return nbEchecSem;
    }

    public void setNbEchecSem(Integer nbEchecSem) {
        this.nbEchecSem = nbEchecSem;
    }

    public Float getPourcentageCapitalise() {
        return pourcentageCapitalise;
    }

    public void setPourcentageCapitalise(Float pourcentageCapitalise) {
        this.pourcentageCapitalise = pourcentageCapitalise;
    }

    public Float getMgpMax() {
        return mgpMax;
    }

    public void setMgpMax(Float mgpMax) {
        this.mgpMax = mgpMax;
    }


}
