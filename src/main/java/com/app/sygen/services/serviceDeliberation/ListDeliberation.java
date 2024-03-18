package com.app.sygen.services.serviceDeliberation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.PvDeliberation;
import com.app.sygen.repositories.PvDeliberationRepository;

@Service
public class ListDeliberation{
    @Autowired
    PvDeliberationRepository pvDeliberationRepository;
   
    public HashMap<String, List> listDeliberations(Long idFiliere, Optional<Integer> year){
        HashMap<String, List> model = new HashMap<>();
        List<PvDeliberation> pvdeliberation = pvDeliberationRepository.findAll();
        List<TableDeliberation> ListDesDeliberations = new ArrayList<>();
        Set<String> etudiantsDeliberes = new HashSet<>();
        // Integer anneeEncours = LocalDate.now().getYear();

        for(PvDeliberation pv : pvdeliberation){
            Long codeFiliere  = pv.getDetailPvUe().getEtudiant().getFiliere().getId();
            Long parameter = idFiliere; 
            Integer anneeEnCours = pv.getDetailPvUe().getPvUe().getDate().getYear();
            Integer annee = year.orElse(LocalDate.now().getYear());
            
            if(Objects.equals(codeFiliere,parameter) && Objects.equals(anneeEnCours, annee)){
                TableDeliberation tb = new TableDeliberation();
                tb.setMatricule(pv.getDetailPvUe().getEtudiant().getMatricule());
                tb.setNom(pv.getDetailPvUe().getEtudiant().getNom());
                etudiantsDeliberes.add(pv.getDetailPvUe().getEtudiant().getNom());
                tb.setCodeUe(pv.getDetailPvUe().getPvUe().getUe().getCode());
                tb.setActuNote(pv.getDetailPvUe().getNote());
                tb.setOldNote(pv.getOldNote());
                ListDesDeliberations.add(tb);
            }
        }
        System.out.println("size of liste deliberation ****" + ListDesDeliberations.size());
        compareByUe comparator = new compareByUe();
        Collections.sort(ListDesDeliberations, comparator);
        List<String> etudiantDeliberesList = new ArrayList<String> (etudiantsDeliberes);
        model.put("listeDesDeliberations", ListDesDeliberations);
        model.put("etudiantsDeliberes", etudiantDeliberesList);
        return model;
    }
    
}