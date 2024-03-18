package com.app.sygen.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.Jury;
import com.app.sygen.entities.Statut;
import com.app.sygen.entities.Users;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.JuryRepository;
import com.app.sygen.repositories.ParticipeRepository;
import com.app.sygen.repositories.StatutRepository;
import com.app.sygen.repositories.UeRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/correction")
public class JuryCorrectionController {
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private ParticipeRepository participeRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private UeRepository ueRepository;
    @Autowired
    private JuryRepository juryRepository;

    @Autowired
    private StatutRepository statutRepository;

    
    @GetMapping("/juryCorr")
    public String juryCorr(Model model ,HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if(user!= null){
            Jury jury = juryRepository.findByUser(user);
        System.out.println(jury);
        List<Statut> statuts =  statutRepository.findByJurys(jury);
        List<Filiere> fill = new ArrayList<>();
            // System.out.println("444444"+bool);
        for(Statut statut : statuts){
            fill.add(statut.getFiliere());
            System.out.println(statut.getFiliere().getCode());
        }
        // List<Filiere> fil = statutRepository.findFilieresByStatuts(statut);

        Iterable<Filiere> filiere = fill;
        model.addAttribute("filieres", filiere);
            model.addAttribute("user", user);
        return "indexCorr";
        } else {
            return "redirect:/etudiant/login"; // Rediriger vers la page de connexion en cas d'utilisateur non connecté
        }
    }


    // @GetMapping("/user/test")
    // public String testcrr(){
    //     return "test";
    // }

    

    // @GetMapping("/login")
    // public String formLogin1(){
    //     return "login";

        
    // }

    // @GetMapping("/logout1")
    // public String formLogout(){
    //     return "logout1";
    // }

    // @PostMapping("/account")
    // public RedirectView account(@RequestParam String username, @RequestParam String password){
    //     System.out.println("ok");
    //     return new RedirectView("/home");
    // }
    

    
}
