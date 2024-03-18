package com.app.sygen.services.serviceDeliberation;
/**
 * Cette classe regroupe les donnees d'un etudiant qui 
 * permettent de dire si oui ou non il est deliberable.
 */

public class DonneesDeliberation {
   private Float mgp;
   private Integer nbEchec;
   private Float pourcentageCapitalise;

   public Float getMgp() {
       return mgp;
   }
   public Integer getNbEchec() {
       return nbEchec;
   }
   public Float getPourcentageCapitalise() {
       return pourcentageCapitalise;
   }
   public void setMgp(Float mgp) {
       this.mgp = mgp;
   }
   public void setNbEchec(Integer nbEchec) {
       this.nbEchec = nbEchec;
   }
   public void setPourcentageCapitalise(Float pourcentageCapitalise) {
       this.pourcentageCapitalise = pourcentageCapitalise;
   }
}
