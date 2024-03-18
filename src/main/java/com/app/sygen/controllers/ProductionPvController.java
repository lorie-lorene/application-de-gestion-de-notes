// package com.app.sygen.controllers;

// import java.time.LocalDate;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.ByteArrayResource;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.app.sygen.entities.Etudiant;
// import com.app.sygen.entities.Filiere;
// import com.app.sygen.entities.Paiement;
// import com.app.sygen.entities.Participe;
// import com.app.sygen.entities.Ue;
// import com.app.sygen.repositories.EtudiantRepository;
// import com.app.sygen.repositories.EvaluationRepository;
// import com.app.sygen.repositories.FiliereRepository;
// import com.app.sygen.repositories.ParticipeRepository;
// import com.app.sygen.repositories.UeRepository;
// import com.app.sygen.services.ProductionPvService;

// @Controller
// @RestController
// // @RequestMapping(path = "doc")
// public class ProductionPvController {
//     @Autowired
//     private ProductionPvService productionPvService;
//     @Autowired
//     private UeRepository ueRepository;
//     @Autowired
//     private EtudiantRepository etudiantRepository;
//     @Autowired
//     private FiliereRepository filiereRepository;
//     @Autowired
//     private ParticipeRepository participeRepository;
//     @Autowired
//     private EvaluationRepository evaluationRepository;
    
//     @GetMapping("/pv")
//     public ResponseEntity<ByteArrayResource> getPv(@RequestParam("code") String code, @RequestParam("typeEval") String typeEval) throws Exception{
//         // return productionPvService.makePdfCC("cc",ueRepository.findByCode("inf-242") , "inf-l3");
//         Ue ue = ueRepository.findByCode(code);
//         Filiere filiere = filiereRepository.findFirstByUes(ue);
//         String codeF = filiere.getCode();
//         if(typeEval == "CC"){
//             return productionPvService.makePdfCC(typeEval, ue, codeF);
//          }// else if (typeEval == "TP"){
//         //     return productionPvService.makePdf
//          else if (typeEval == "EE"){
//         return productionPvService.makePdfUe(typeEval, ueRepository.findByCode(code), codeF, codeF);
//     }
//     return null;
// }


    
// }
