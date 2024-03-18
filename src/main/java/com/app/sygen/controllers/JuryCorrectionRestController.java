package com.app.sygen.controllers;

import com.app.sygen.repositories.DetailsPvUeRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.impl.soap.Detail;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.sygen.entities.DataJuryCorrection;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.Participe;
import com.app.sygen.entities.PvUe;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.ParticipeRepository;
import com.app.sygen.repositories.UeRepository;
import com.app.sygen.services.ProductionPvService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/user")
public class JuryCorrectionRestController {
    @Autowired
    private ParticipeRepository participeRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private UeRepository ueRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private ProductionPvService productionPvService;

    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private DetailsPvUeRepository detailPvUeRepository;
    @PostMapping("/getNote")
    @ResponseBody
    public List<Participe> traitementFormulaire(@RequestParam String matricule, @RequestParam String ue,
            @RequestParam String nom, @RequestParam String eval) {

        List<Participe> participes = participeRepository
                .findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(
                        etudiantRepository.findByMatriculeAndNom(matricule, nom), LocalDate.now().getYear() + "",
                        evaluationRepository.findByTypeEvalAndUe_Code(eval.toLowerCase(), ue));
        if (participes.size() != 0)
            System.out.println(participes.get(0).getNomEtudiant());
        else
            System.out.print("--------{ La liste est vide ! } ------" + matricule + " " + ue + " " + nom + " " + eval);

        if (participes.size() != 0)
            return participes;
        else
            return participes;
    }
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @RolesAllowed("ROLE_USER")
    // @PutMapping("/changeNote")
    @ResponseBody
    public String changeNote(@RequestParam String matricule, @RequestParam String ue, @RequestParam String nom,
            @RequestParam String eval, @RequestParam Float newNote) {
        List<Participe> participes = new ArrayList<Participe>();
        participes = participeRepository.findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(
                etudiantRepository.findByMatriculeAndNom(matricule, nom), LocalDate.now().getYear() + "",
                evaluationRepository.findByTypeEvalAndUe_Code(eval.toLowerCase(), ue));
        if (participes.size() != 0) {
            participes.get(0).setNote(newNote);
            participeRepository.save(participes.get(0));
        } else
            System.out.println("--------->>>>[La liste de recuperation des etudiants est vide {" + matricule + " " + ue
                    + " " + nom + " " + eval + " " + newNote + "} ]<<<<<--------------");

        return "save whit success !!!!";
    }

    @GetMapping("/getPvUe")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPvUe(@RequestParam String ue, @RequestParam String eval,
            @RequestParam int semestre, @RequestParam String filiere) throws Exception {
        
        PvUe pvue = productionPvService.CreatePvUe(semestre, ue);
        if(pvue != null)
            productionPvService.CreateDetaiPvUe(filiere, ue, semestre,pvue );
              
        return productionPvService.makePdfUe(eval, ueRepository.findByCode(ue), filiere, filiere, semestre);
    }

    @GetMapping("/getPvCC")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPvCC(@RequestParam String ue, @RequestParam String eval, int semestre)
            throws Exception {
        String filiere = ueRepository.findByCode(ue).getFiliere().getCode();
        return productionPvService.makePdfCC(eval, ueRepository.findByCode(ue), filiere, semestre);
    }

    @PostMapping("/getAllNote")
    public List<Participe> getAllNote(@RequestParam String ue, @RequestParam String eval) {

        List<Participe> participes = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_Code(eval, ue);
        if (participes.size() != 0) {
            System.out.println("((((((((((((((((()))))))))))))))))" + participes.get(0).getNomEtudiant());
            return participes;

        } else
            return participes;

    }

    @PostMapping("/changeAllMotes")
    public String get_Note(@RequestBody DataJuryCorrection dataJuryCorrection) {

        // participeRepository.saveAll(participes);
        participeRepository.saveAllAndFlush(dataJuryCorrection.getParticipes());
        if(dataJuryCorrection.getListeIdModif() != null)
            System.out.println("(-------[id = "+dataJuryCorrection.getListeIdModif());
        return "donnees modifiees avec sucess !!!";
    }

    @GetMapping("/testju")
    public String test() {
        // Filiere filiere = filiereRepository.findByCode("inf-l3");

        // List<Etudiant> etudiants =
        // List<Etudiant> etudiants = etudiantRepository.findByFiliereAndStatutPaiementIgnoreCaseIsLessThanOrderByNomAsc(filiere, 50000.0);
        // List<Participe> participes = participeRepository.findByEtudiant(etudiants.get(0));
        // List<Etudiant> etudiants = etudiantRepository.findByFiliere(filiere);
        // if (participes.size() == 0)
        //     return "aucun etudiant trouve !!! : " + filiere.getCode();
        // else
        //     return participes.get(1).getNote()+"";
        PvUe pvue = productionPvService.CreatePvUe(1, "inf-321");
        return detailPvUeRepository.findByPvUe(pvue).get(0).getEtudiant().getNom();
        // return productionPvService.CreateDetaiPvUe(filiere.getCode(), "inf-321",1,pvue);    

    }

    

}

