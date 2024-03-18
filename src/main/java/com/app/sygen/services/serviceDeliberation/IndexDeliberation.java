package com.app.sygen.services.serviceDeliberation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.Etudiant;
import com.app.sygen.repositories.EtudiantRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
@Configurable
public class IndexDeliberation{

     @Autowired
     EtudiantRepository etudiantRepository;

     @Autowired
     EtudiantDeliberation etudiantDeliberation;
//    public  IndexDeliberation (EtudiantRepository e){
//     this.etudiantRepository = e;
//    }

   public IndexDeliberation(){
    
   }
    public void deliberation(List<CritereDeliberation> blocCriteres, Long filiere){
        System.out.println("package com.app.sygen.services.serviceDeliberation.deliberation");

        List<Etudiant> etudiantsDeFiliere  =  etudiantRepository.findByFiliere(filiere);
      
        for(Etudiant etudiant : etudiantsDeFiliere){
            System.out.println("SSSSSSSSSSSSSSSSSSSSize *****" + etudiant.getDetailsPvUe().size());
            System.out.println("IdDetailPv **** "+etudiant.getDetailsPvUe().get(0).getNote());
            for(CritereDeliberation critere : blocCriteres){

                //  EtudiantDeliberation e = new EtudiantDeliberation();
                 etudiantDeliberation.etudiantDeliberations(etudiant, critere);

            }
        }






    }

}