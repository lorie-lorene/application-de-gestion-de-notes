package com.app.sygen.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import com.app.sygen.entities.Enseignant;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.Jury;
import com.app.sygen.entities.SeanceDeliberation;
import com.app.sygen.entities.Statut;
import com.app.sygen.entities.Users;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EnseignantRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.JuryRepository;
import com.app.sygen.repositories.SeanceDeliberationRepository;
import com.app.sygen.repositories.StatutRepository;
import com.app.sygen.repositories.UserRepository;
import com.app.sygen.services.serviceDeliberation.CritereDeliberation;
import com.app.sygen.services.serviceDeliberation.Datacontrolleur;
import com.app.sygen.services.serviceDeliberation.IndexDeliberation;
import com.app.sygen.services.serviceDeliberation.ListDeliberation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeliberationController {
    @Autowired
    ListDeliberation listeDeliberation;

    @Autowired
    IndexDeliberation indexDeliberation;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    FiliereRepository filiereRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JuryRepository juryRepository;

    @Autowired
    private StatutRepository statutRepository;

    @Autowired
    private SeanceDeliberationRepository seanceDeliberationRepository;

    @GetMapping("/")
    public String deliberation(Model model ,HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if(user!= null){
         //enseignantRepository.findAll();
        // System.out.println("===="+enseignant);
        
        Jury jury = juryRepository.findByUser(user);
        System.out.println(jury);
        List<Statut> statuts =  statutRepository.findByJurys(jury);
        List<Filiere> fill = new ArrayList<>();
        String cle = (String) model.getAttribute("cle");
        Boolean bool = (Boolean) model.getAttribute("bool");
        Filiere fil = (Filiere) model.getAttribute("fil");
        List<Statut> sList=statutRepository.findByFiliere(fil);
        List<Jury> juries = juryRepository.findByAttributIn(sList);
        Iterable<Jury> enss = juries;
        

        
        System.out.println("444444"+bool);
        for(Statut statut : statuts){
            fill.add(statut.getFiliere());
            System.out.println(statut.getFiliere().getCode());
        }
        // List<Filiere> fil = statutRepository.findFilieresByStatuts(statut);

        Iterable<Filiere> filiere = fill;
        model.addAttribute("filieres", filiere);
        model.addAttribute("cle", cle);
        model.addAttribute("bool", bool);
        model.addAttribute("enss", enss);
        model.addAttribute("user", user);
        return "deliberations/indexDeliberation";
    } else {
        return "redirect:/etudiant/login"; // Rediriger vers la page de connexion en cas d'utilisateur non connecté
    }
    }

    @PostMapping("/metting/create")
    public String createMeeting(HttpSession session,Model model,@RequestParam ("fil") String code){
        String cle="12ff";
        Users user = (Users) session.getAttribute("user");
        Jury jury=juryRepository.findByUser(user);
        SeanceDeliberation seance =new SeanceDeliberation();
        seance.setCreateJury(jury);
        seance.setCle(cle);
        Filiere fil = filiereRepository.findFirstByCode(code);
        seance.setFiliere(fil);
        LocalDate currentDate = LocalDate.now();
        seance.setDay(Date.valueOf(currentDate));
        
        
        seanceDeliberationRepository.save(seance);
        model.addAttribute("cle", cle);
        model.addAttribute("fil", fil);
        return "redirect:/";
    }

    @PostMapping("/metting/connect")
    @ResponseBody
    public Map<String, Object> connectMeeting(HttpSession session ,Model model,@RequestParam ("cle") String cle){
        Users user = (Users) session.getAttribute("user");
        SeanceDeliberation seance = seanceDeliberationRepository.findByCle(cle);
        List<Jury> listSeance =seance.getMembres();
        listSeance.add (juryRepository.findByUser(user));
        seance.setMembres(listSeance);
        isWaiting(cle, model);
        for(Jury s : seance.getMembres()){
        System.out.println("+++++"+s.getUser().getNom());
        }
        Boolean bool=seance.isWaiting();
        seanceDeliberationRepository.save(seance);
        // model.addAttribute("bool", bool);
        Map<String, Object> response = new HashMap<>();
        response.put("bool", bool);

        return response;
        // session.setAttribute("bool", bool);
        // return deliberation(model,session);
        // while(seance.isWaiting()==false){

        // }
        // if(seance.isWaiting() == true){

        // }

    }

    public void isWaiting(String cle,Model model){
        SeanceDeliberation seance = seanceDeliberationRepository.findByCle(cle);
        if(seance.getCreateJury()!= null && seance.getMembres().size() == 2 ){
            seance.setWaiting(false);
            seanceDeliberationRepository.save(seance);
            // model.addAttribute("valeur", "true");
            // return "redirect:/";
        } else {
            seance.setWaiting(true);
            seanceDeliberationRepository.save(seance);
            // model.addAttribute("valeur", "false");
            // return "redirect:/";
        }


    }

    @PostMapping("/criteresDeliberation")
    public String Criteres(@RequestBody Datacontrolleur jsonData, Model model) {
        System.out.println("*****Caught request");
        String codeFiliere = jsonData.getFiliere();
        System.out.println("codeFiliere" + codeFiliere);
        Filiere filiere = filiereRepository.findFirstByCode(codeFiliere);
        List<CritereDeliberation> blocCriteres = jsonData.getTableData();
        indexDeliberation.deliberation(blocCriteres, filiere.getId());
        System.out.println(filiere.getId());
        return "redirect:rapportDeliberation?idFiliere=" + filiere.getId();
    }

    @GetMapping("/rapportDeliberation")
    public String rapportDeliberation(HttpServletRequest request, Model model) {

        // ListDeliberation listDeliberation = new ListDeliberation();
        HashMap<String, List> modelHashMap = listeDeliberation.listDeliberations(
                Long.parseUnsignedLong((String) request.getParameter("idFiliere")), Optional.empty());
        model.addAttribute("listeDesDeliberations", modelHashMap.get("listeDesDeliberations"));
        model.addAttribute("etudiantsDeliberes", modelHashMap.get("etudiantsDeliberes"));
        System.out.println("Rapport Deliberation end");
        return "/deliberations/indexDeliberation";
    }

    @PostMapping("/envoyer-message")
    public String envoyerMessage(Model model,HttpSession session,@RequestParam("nom") String nom) {
        // Recherche de la personne correspondant au nom dans la base de données
        // Personne personne = recherchePersonneParNom(nom);
        Users user = (Users) session.getAttribute("user");
        Enseignant enseignant = enseignantRepository.findByUser(userRepository.findByUsername(nom));
        SeanceDeliberation seance = seanceDeliberationRepository.findByCreateJury(juryRepository.findByUser(user));

        if (enseignant != null) {
            // Récupération de l'email de la enseignant
            String email = enseignant.getEmail();

            // Envoi du message
            String cle = seance.getCle();
            envoyerEmail(email, "cle d'authentification ", cle);

            model.addAttribute("cle", cle);
            return "redirect:/";//new ModelAndView("success");
        } else {
            return "error";
        }
    }

    private void envoyerEmail(String destinataire, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(contenu);
        mailSender.send(message);
    }


    @MessageMapping("/edit")
    @SendTo("/topic/deliberation")
        public List<DeliberationData> edit(List<DeliberationData> data){
        return  data;      
   }


}


class DeliberationData{
   private Integer mgpMin;
   private Integer mgpMax;
   private Integer nbreEchec;
   
   public void setMgpMax(Integer mgpMax) {
       this.mgpMax = mgpMax;
   }
   public void setMgpMin(Integer mgpMin) {
       this.mgpMin = mgpMin;
   }
   public void setNbreEchec(Integer nbreEchec) {
       this.nbreEchec = nbreEchec;
   }
}

