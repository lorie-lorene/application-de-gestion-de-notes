package com.app.sygen.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
// import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.app.sygen.entities.Enseignant;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.Jury;
import com.app.sygen.entities.Paiement;
import com.app.sygen.entities.Ue;
import com.app.sygen.entities.Users;
import com.app.sygen.repositories.EnseignantRepository;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.JuryRepository;
import com.app.sygen.repositories.UeRepository;
import com.app.sygen.repositories.UserRepository;
// import com.app.sygen.repositories.EtudiantRepository;
// import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.services.EtudiantService;
import com.opencsv.CSVReader;

import jakarta.servlet.http.HttpSession;

// import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController 
{
	@Autowired
    private EtudiantService etudiantService;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private UeRepository ueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JuryRepository juryRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login"; // Le nom de la vue pour la page de connexion
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        Users user = userRepository.findByLoginAndPassword(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/index-import"; // Rediriger vers la page affichant les matières
        } else {
            return "user/login"; // Rediriger vers la page de connexion en cas d'informations de connexion incorrectes
        }
    }


    @GetMapping("/")
    public String index(HttpSession session,Model model)
    {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
        Iterable<Etudiant> listetudiant = etudiantService.shows();
        Iterable<Filiere> listfiliere = filiereRepository.findAll();
        Iterable<Ue> listue = ueRepository.findAll();
        Iterable<Jury> listjury = juryRepository.findAll();
        Iterable<Enseignant> listenseignant = enseignantRepository.findAll();
        model.addAttribute("etudiant", listetudiant);
        model.addAttribute("enseignant", listenseignant);
        model.addAttribute("ue", listue);
        model.addAttribute("jury", listjury);
        model.addAttribute("filiere", listfiliere);
        model.addAttribute("user", user);
        model.addAttribute("paiement", new Paiement()); 
        
        return "etudiants/index";
        } else {
            return "redirect:/etudiant/login"; // Rediriger vers la page de connexion en cas d'utilisateur non connecté
        }
    }

    @GetMapping("/{id}")
    public String addPaiement(@PathVariable("id") Long id,Model model){
        Optional<Etudiant> etudiant=etudiantRepository.findById(id);
        model.addAttribute("etudiant", etudiant); 
        model.addAttribute("paiement", new Paiement()); 
        return "AddPaiement";
    }
    
    @GetMapping("/index")
    public List<Etudiant> show()
    {
        return this.etudiantService.show();

    }
    
    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("etudiant", new Etudiant());
        
        return "/etudiant/create";
    }

    @PostMapping("/create")
    public RedirectView processCreate(@ModelAttribute("etudiant") Etudiant etudiant, RedirectAttributes redir)
    {			
		RedirectView redirectView;
		
		try {
			this.etudiantService.create(etudiant);
			redirectView = new RedirectView("/etudiant/index", true);
			redir.addFlashAttribute("success", "Etudiant créé avec succès");
		}
		catch(Exception e) {
			redirectView = new RedirectView("/etudiant/create", true);
			redir.addFlashAttribute("error", e.getMessage());
		}
		
		return redirectView;
    }

    @PostMapping("/savePaiement/{id}")
    public String savePaiement(@PathVariable("id") Long id,@ModelAttribute("paiement") Paiement paiement){
        etudiantService.createPaiement(id, paiement);
        return "redirect:/etudiant/";
    }

    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            List<Etudiant> importedPersons = new ArrayList<>();

            if (file.getOriginalFilename().endsWith(".csv")) {
                // Traitement pour les fichiers CSV
                Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVReader csvReader = new CSVReader(reader);
                String[] header = csvReader.readNext(); // Lecture de la première ligne (titres des colonnes)
                Map<String, Integer> columnIndexMap = etudiantService.getColumnIndexMap(header);

                String[] nextRecord;

                while ((nextRecord = csvReader.readNext()) != null) {
                    // Vérification et création d'une personne pour chaque ligne du fichier CSV
                    Etudiant etudiant = etudiantService.createEtudiantCSV(nextRecord, columnIndexMap);
                        if (etudiant != null) {
                            importedPersons.add(etudiant);
                        
                    } else {
                        return ResponseEntity.badRequest().body("Le format des donnees dans le fichier CSV est incorrect.");
                    }
                }

                csvReader.close();
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                // Traitement pour les fichiers XLSX
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                Row headerRow = rowIterator.next(); // Lecture de la première ligne (titres des colonnes)
                Map<String, Integer> columnIndexMap = etudiantService.getColumnIndexMap(headerRow);

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Etudiant etudiant = etudiantService.createEtudiantXLSX(row,columnIndexMap);
                    // System.out.println(etudiant);
                        if(etudiant != null){                        
                        importedPersons.add(etudiant);
                    } else {
                        return ResponseEntity.badRequest().body("Le format des donnees dans le fichier XLSX est incorrect.");
                    }
                }

                workbook.close();
            } else {
                return ResponseEntity.badRequest().body("Le format de fichier n'est pas pris en charge. Veuillez utiliser un fichier CSV ou XLSX.");
            }

            // Enregistrer les personnes importées dans la table Person
            etudiantRepository.saveAll(importedPersons);

            // Retourner la liste des personnes importées
            return ResponseEntity.ok(importedPersons);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de l'importation du fichier.");
        }
    }
}
