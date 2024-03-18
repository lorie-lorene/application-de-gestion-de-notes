package com.app.sygen.services.serviceDeliberation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.DetailPvUe;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.PvDeliberation;
import com.app.sygen.repositories.DetailsPvUeRepository;
import com.app.sygen.repositories.PvDeliberationRepository;

import jakarta.transaction.Transactional;

 @Service
// @Transactional
public class EtudiantDeliberation {
       
    
       
    @Autowired
    DetailsPvUeRepository detailsPvUeRepository;
        //Champs primaires
        private List<UeNoteCredit> listInfoNotes = new ArrayList<>();
        private CritereDeliberation critereDeDeliberation;
        //Champs traites
        private Float mgp;
        private Integer nbrEchecSem = 0;
        private Float pourcentageCapitalise = 0.0F;

        //Constructor1
        public void EtudiantDeliberation(){

        }

        @Transactional
        public void etudiantDeliberations(Etudiant etudiant, CritereDeliberation critere){
                System.out.println("\n****package com.app.sygen.services.serviceDeliberation.etudiantDeliberations****\n");
                System.out.println("qwaNumber of pv Deliberation *** " +pvDeliberationRepository.count());
               
                this.mgp = 0.F;
                this.nbrEchecSem = 0;
                this.pourcentageCapitalise = 0.0F;
                this.listInfoNotes.clear();



                this.critereDeDeliberation = critere;
                List<DetailPvUe> details = etudiant.getDetailsPvUe();
                System.out.println("@@@@@@@@@@@@@@@"+ details.size()+ etudiant.getNom());
                for(DetailPvUe detail : details){
                        System.out.println("IdDetailPv *********   @@@@ "+detail.getId());
                        System.out.println("@@@@@@hellor");
                        if(detail.getPvUe().getDate().getYear() == LocalDateTime.now().getYear()){
                                /*
                                 * Recuperation des notes et du credit 
                                 * des etudiant ue pour l'annee en cours
                                 */
                                System.out.println("@@@@@@@@@@Hello world");
                                this.listInfoNotes.add(new UeNoteCredit(detail));
                        }
                }
                calculMgpNbrEchecPourcentageUeCapitalise();
                         
                if(deliberable().booleanValue()){
                    deliberation();    
                }
                
        
        }


        public void calculMgpNbrEchecPourcentageUeCapitalise() {
                System.out.println("package com.app.sygen.services.serviceDeliberation.calculMgpNbrEchecPourcentageUeCapitalise");

                Integer NOTEVALIDE = 50;
                Float sumCreditQuota = 0F;
                Integer sumCredit = 0;
                this.mgp = 0.00F;


                for (UeNoteCredit ueNoteCredit : listInfoNotes) {
                    sumCreditQuota += ueNoteCredit.getQuota() * ueNoteCredit.getCredit();
                    sumCredit += ueNoteCredit.getCredit();
                        
                    if (!ueNoteCredit.getStatus().booleanValue()) {
                        this.nbrEchecSem = this.nbrEchecSem.intValue() + 1;
                    }
                    if(ueNoteCredit.getNote() > NOTEVALIDE){
                        this.pourcentageCapitalise += 100 * 1/listInfoNotes.size();
                    }
                }

                try {
                    this.mgp = sumCreditQuota / sumCredit;
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                    System.out.println("Zero Division error : SumCredit == 0");
                }
                /////////////////////////////
                System.out.println("@@@@mgp == " + mgp);

        }
        

        public Float calculMgp(List<UeNoteCredit> listeInfoNotes) {
            System.out.println("package com.app.sygen.services.serviceDeliberation.calculMgp");
            Float sumCreditQuota = 0F;
            Integer sumCredit = 0;
            this.mgp = 0.00F;
            
            
            
            for (UeNoteCredit ueNoteCredit : listeInfoNotes) {
                sumCreditQuota += ueNoteCredit.getQuota() * ueNoteCredit.getCredit();
                sumCredit += ueNoteCredit.getCredit();
                System.out.println("credit==" + ueNoteCredit.getCredit() +"**quota=="+ ueNoteCredit.getQuota()+"**note=="+ueNoteCredit.getNote());
                System.out.println(sumCreditQuota + "<-sumCreditQuota-sumCredit->" + sumCredit);

            }
            try {
                this.mgp = sumCreditQuota / sumCredit;
            } catch (Exception e) {
                System.out.print(e.getMessage());
                System.out.println("Zero Division error : SumCredit == 0");
            }

            return mgp;
        } 
        
        public Boolean deliberable() {
                System.out.println("package com.app.sygen.services.serviceDeliberation.deliberable");
                System.out.println("\n\n====================Deliberable==================================");
                DonneesDeliberation delData = new DonneesDeliberation();
                delData.setNbEchec(nbrEchecSem);
                delData.setMgp(mgp);
                delData.setPourcentageCapitalise(pourcentageCapitalise);
                Boolean deliberable = critereDeDeliberation.estDeliberable(delData);
                System.out.println("deliberable" + deliberable);
                return deliberable;
        }

        @Transactional
        public void deliberation(){
            System.out.println("package com.app.sygen.services.serviceDeliberation.deliberation");
            System.out.println("aNumber of pv Deliberation *** " +pvDeliberationRepository.count());

            //Liste des notes pour chaque ue utilise pour les modifications pour l'historisation
            List<UeNoteCredit> copieList = new ArrayList<>();
            Float mgp = 0F;

            //Recuperations des notes pour chaque ue avant modifications
            for (UeNoteCredit ueNoteCredit : listInfoNotes) {
                copieList.add(new UeNoteCredit(ueNoteCredit));
            }
            System.out.println("bNumber of pv Deliberation *** " +pvDeliberationRepository.count());

            do {
                Float MINCREDITQUOTA = 300F;

                //Initialisation de la liste des notes pour chaques UE qui generes le moins de mgp.
                List<UeNoteCredit> listUpdate = new ArrayList<>();

                //Recuperation de la liste des ue ou l'etudiant a le moins bien travaille
                //dans la liste listeUpdate
                for (UeNoteCredit ueNoteCredit : copieList) {
                    if (MINCREDITQUOTA > ueNoteCredit.getCredit() * ueNoteCredit.getQuota()) {
                        MINCREDITQUOTA = ueNoteCredit.getCredit() * ueNoteCredit.getQuota();
                        listUpdate.clear();
                        listUpdate.add(ueNoteCredit);
                    } else if (MINCREDITQUOTA == ueNoteCredit.getCredit() * ueNoteCredit.getQuota()) {
                        listUpdate.add(ueNoteCredit);
                    }
                }
            System.out.println("cNumber of pv Deliberation *** " +pvDeliberationRepository.count());

                //Definition de la quantite de point a ajouter a aux notes de la listes **listUpdate**
                Float POINTDELIBERATION = 8F / (listUpdate.size());
                //Ajout de points aux notes de la liste listUpdate
                for (UeNoteCredit ueNoteCredit : listUpdate) {
                    ueNoteCredit.setNote(ueNoteCredit.getNote() + POINTDELIBERATION );// ueNoteCredit.getCredit());
                    ueNoteCredit.quota(ueNoteCredit.getNote());
                }
                //calcule de la mgp de l'etudiant avec prise en compte des ajout de notes
                
                mgp = calculMgp(copieList);
                System.out.println("*****************mgp"+mgp+ "Listupdate"+listUpdate.get(0).getNote()+ "reel"+listInfoNotes.get(0).getNote());
                
            } while(mgp < this.critereDeDeliberation.getMgpMax());

            //Calibrage de la mgp dans l'intevalle [2, 2.5[.
            //Pour eviter les depassemnt le notes modifie sont faites sur les ue qui ont le moins de credit
            if ((mgp - this.critereDeDeliberation.getMgpMax()) > 0.5) {
                do {
                    Integer MINCREDIT = 0;
                    List<UeNoteCredit> listUpdate = new ArrayList<>();
                    for (UeNoteCredit ueNoteCredit : copieList) {
                        if (MINCREDIT.intValue() > ueNoteCredit.getCredit().intValue()) {
                            MINCREDIT = ueNoteCredit.getCredit();
                            listUpdate.clear();
                            listUpdate.add(ueNoteCredit);
                        } else if (MINCREDIT == ueNoteCredit.getCredit() * ueNoteCredit.getQuota()) {
                            listUpdate.add(ueNoteCredit);
                        }
                        Float POINTDELIBERATION = 0.125F;
                        for (UeNoteCredit ueNoteCredit1 : listUpdate) {
                            ueNoteCredit1.setNote(ueNoteCredit1.getNote().floatValue() - POINTDELIBERATION.floatValue());// ueNoteCredit.getCredit());
                            ueNoteCredit1.quota(ueNoteCredit1.getNote());
                        }
                    }
                    mgp = calculMgp(copieList);
                    System.out.println("**************mgp"+ mgp);

                } while ((mgp - this.critereDeDeliberation.getMgpMax()) > 0.5);
            }

            System.out.println("10Number of pv Deliberation *** " +pvDeliberationRepository.count());
         
           historisationNotesDeliberees(this.listInfoNotes, copieList);
           System.out.println("9Number of pv Deliberation *** " +pvDeliberationRepository.count());
            // return  copieList;
        }

        @Autowired
        private PvDeliberationRepository pvDeliberationRepository;

        @Transactional
        public void historisationNotesDeliberees(List<UeNoteCredit> origin, List<UeNoteCredit> modified){
            System.out.println("package com.app.sygen.services.serviceDeliberation.historisationNotesDeliberees");
            System.out.println("8Number of pv Deliberation *** " +pvDeliberationRepository.count());
            // pvDeliberationRepository.deleteAll();
            for(UeNoteCredit ueNoteCredit : modified){
                System.out.println("7Number of pv Deliberation *** " +pvDeliberationRepository.count());
                
                for(UeNoteCredit ueNoteCredit2 :  origin){
                    System.out.println("6Number of pv Deliberation *** " +pvDeliberationRepository.count());

                    if(ueNoteCredit.getIdDetailPv().longValue() == ueNoteCredit2.getIdDetailPv().longValue()
                        && ueNoteCredit.getNote().floatValue() != ueNoteCredit2.getNote().floatValue()){
                            DetailPvUe detail = detailsPvUeRepository.findById(ueNoteCredit2.getIdDetailPv()).get();
                            detail.setNote(ueNoteCredit.getNote());
                            detailsPvUeRepository.save(detail); 
                            //pvDeliberation.setId(detail.getId());
                            // System.out.println("getID *** " + pvDeliberation.getId()+ pvDeliberation.getDetailPvUe().getId());

                            try {
                                  PvDeliberation pvDeliberation = new PvDeliberation(detail, ueNoteCredit2.getNote());
                                  pvDeliberationRepository.save(pvDeliberation);
                                
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("3Number of pv Deliberation *** " +pvDeliberationRepository.count());

                    }
                }
            }
        }

}