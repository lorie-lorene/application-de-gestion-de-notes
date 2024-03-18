package com.app.sygen.controllers;

import com.app.sygen.repositories.HistoriqueNotesRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.app.sygen.entities.Enseignant;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.HistoriqueNote;
import com.app.sygen.entities.Participe;
import com.app.sygen.entities.StatutNote;
import com.app.sygen.entities.Ue;
import com.app.sygen.entities.Users;
import com.app.sygen.entities.data.Data;
import com.app.sygen.repositories.EnseignantRepository;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.ParticipeRepository;
import com.app.sygen.repositories.StatutNoteRepository;
import com.app.sygen.repositories.UserRepository;
// import com.app.sygen.repositories.UserRepository;
import com.app.sygen.services.ParticipeService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
// @RequestMapping("/participe")
public class ParticipeController {
    @Autowired
    private ParticipeRepository participeRepository;

    @Autowired
    private ParticipeService participeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatutNoteRepository statutNoteRepository;

    @Autowired
    private HistoriqueNotesRepository HistoriqueNoteRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;
    // @Autowired 
	// private ParticipeRepository participe;
	@Autowired 
	private EtudiantRepository etudiantRepository;

    @Autowired 
	private FiliereRepository filiereRepository;
	
	@GetMapping("/formulaire")
	public String formulaire(Model model){
	model.addAttribute("participes",new Participe());
	return "examens/anonymat";
	}

    // @GetMapping("/admin")
    public Users showadmin(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        return user;
    }

    @PostMapping("/import")
    public String importFile(HttpSession session,@RequestParam("code") String code, @RequestParam("typeEval") String typeEval,
            @RequestParam("noteSur") int noteSur, @RequestParam("file") MultipartFile file, Model model) {
        try {
            List<Participe> importedPersons = new ArrayList<>();
            // Users user = userRepository.findByLogin("bosley-12");
            Users user = (Users) session.getAttribute("user");
            System.out.println(user.getLogin());
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                Row headerRow = rowIterator.next(); // Lecture de la première ligne (titres des colonnes)
                Map<String, Integer> columnIndexMap = participeService.getColumnIndexMap(headerRow);

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Participe participe = participeService.createParticipeXLSX(row, columnIndexMap, code, typeEval,
                            noteSur);

                    if (participe != null) {
                        participe.setUser(user);
                        importedPersons.add(participe);
                    } else {
                        return "redirect:/index-import?status=bad";
                    }
                }

                workbook.close();
            } else {
                return "redirect:/index-import?status=bad";
            }

            // Enregistrer les personnes importées dans la table Person
            participeRepository.saveAll(importedPersons);
            
            

            return "redirect:/index-import?status=success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("file", file);
            return "redirect:/index-import?status=fail";
        }
    }

    // public String couper(Evaluation evaluation){

    // }

    @GetMapping("/index-import")
    public String ListUe(HttpSession session,Model model ) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<Evaluation> evals = evaluationRepository.findAll();
            StatutNote statut;
            for(Evaluation eval:evals){
                statut=statutNoteRepository.findFirstByEvaluation(eval);
                if(statut == null){
                    StatutNote statuts =new StatutNote();
                    statuts.setEvaluation(eval);
                    // statuts.setUser(user);
                    statuts.setValide(false);
                    statutNoteRepository.save(statuts);
                }
            }
        // Users users = (Users) session.getAttribute("user");
        // Users user = userRepository.findByLogin("Tapamo");
        List<Ue> Ues = enseignantRepository.findByUser(user).getUes();
        
        List<Evaluation> evaluations = evaluationRepository.findByUe(Ues);
        // System.out.println(evaluations);
        // List<Participe> participe = participeRepository.findFirstByEvaluationIn(evaluations);
        List<Integer> liste = new ArrayList<>();
        for (Evaluation obj : evaluations) {
            Optional<Participe> participeOptional = participeRepository.findFirstByEvaluation(obj);
            // System.out.println(participeOptional);
            int valeur = participeOptional.isPresent() ? 1 : 0;
            liste.add(valeur);
            String val = obj.getUe().getFiliere().getCode().split("-")[0];
            obj.getUe().getFiliere().setCode(val);
        }
        List<StatutNote> statutNote = statutNoteRepository.findAll();

        List<Participe> listeParticipe = (List<Participe>) model.getAttribute("participes");
        // StatutNote statut = (List<Participe>) model.getAttribute("participes");

        Iterable<Evaluation> evaluation=evaluations;
        model.addAttribute("participes", listeParticipe);
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("list", liste);
        model.addAttribute("user", user);
        model.addAttribute("status", statutNote);
        return "import en masse/index";

    } else {
        return "redirect:/etudiant/login"; // Rediriger vers la page de connexion en cas d'utilisateur non connecté
    }

    }

    @GetMapping("/afficher")
    public String afficher (HttpSession session,@RequestParam("code") String code, @RequestParam("typeEval") String typeEval ,@RequestParam("Fcode") String Fcode,Model model){
        // List<Participe> participes = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_Code(typeEval,code);
        // Evaluation evaluations = evaluationRepository.findByTypeEvalAndUe_Code(typeEval, code);
        List<Participe> participes = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_CodeAndEvaluation_Ue_Filiere_Code(typeEval, code, Fcode);        // System.out.println("----------->######"+participes);
        // System.out.println("----------->######"+participes.size());
        if (participes.isEmpty()){
            // System.out.println("----------->######"+participes.size());
            Filiere filniv = filiereRepository.findFirstByCode(Fcode);
            List<Etudiant> etu =etudiantRepository.findByFiliere(filniv) ;
            Iterable<Etudiant> etus = etu;
            model.addAttribute("persons", etus);
            
        } else {
            // System.out.println("+++++++++>######"+participes.size());
            Iterable<Participe> participe = participes;
            model.addAttribute("personns", participe);
            // participeRepository.deleteAll(participes);
        }
        // System.out.println("----------->######"+model);
        return ListUe(session,model);
    }


    @PostMapping("/updateAll")
    // @ResponseBody
    public String updateAllPersons(HttpSession session,@RequestBody List<Data> pnews,Model model) {
        System.out.println(pnews.size());
        // try {
            Users user = (Users) session.getAttribute("user");
            for (Data pnew : pnews) {
                // System.out.println(pnew.getNom());
                // System.out.println(pnew.getNote());
                if(pnew.getId()!=null){
                Participe participe = participeRepository.findById(pnew.getId()).orElse(null);
                // System.out.println(participe.getNomEtudiant());
                if (participe != null) {
                    // System.out.println(pnew.getNom());
                
                    Etudiant etudiant = etudiantRepository.findByMatricule(pnew.getMatricule());
                    Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(pnew.getTypeEval(), pnew.getCode());
                    Float note1 = pnew.getNote();
                    Float note2 = participe.getNote();
                    String nom1 = pnew.getNom();
                    String nom2 = participe.getNomEtudiant();
                    String mat1 = pnew.getMatricule();
                    String mat2 = participe.getMatricule();
                    
                    // System.out.println(note1 + "--------"+ note2);
                    if( Float.compare(note1, note2) != 0 || nom1 != null && !nom1.equals(nom2) || mat1 != null && !mat1.equals(mat2)){
                        System.out.println("11111111111"+pnew.getNote());
                        System.out.println("------------"+participe.getNote());
                        HistoriqueNote historiqueNote = new HistoriqueNote();
                        historiqueNote.setEvaluation(evaluation);
                        historiqueNote.setNewMatricule(pnew.getMatricule());
                        historiqueNote.setOldMatricule(participe.getMatricule());
                        historiqueNote.setOldNom(participe.getNomEtudiant());
                        historiqueNote.setNewNom(pnew.getNom());
                        historiqueNote.setOldNote(participe.getNote());
                        historiqueNote.setNewNote(pnew.getNote());
                        historiqueNote.setUser(user);
                        historiqueNote.setDate(Date.valueOf(LocalDate.now()));
                        HistoriqueNoteRepository.save(historiqueNote);
                    }
                    participe.setEtudiant(etudiant);
                    participe.setEvaluation(evaluation);
                    participe.setNote(pnew.getNote());
                    participeRepository.save(participe);
                    
                } else {
                    System.out.println("2222222222"+pnew.getNote());
                    LocalDate currentDate = LocalDate.now();
                    Participe p = new Participe();
                    p.setDateImportation(Date.valueOf(currentDate));
                    p.setAnneeImportation( String.valueOf(currentDate.getYear()));
                    Etudiant etudiant = etudiantRepository.findByMatricule(pnew.getMatricule());
                    Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(pnew.getTypeEval(), pnew.getCode());
                    p.setEtudiant(etudiant);
                    p.setMatricule(pnew.getMatricule());
                    p.setEvaluation(evaluation);
                    p.setNote(pnew.getNote());
                    p.setNomEtudiant(pnew.getNom());
                    p.setUser(user);
                    participeRepository.save(p);
                }} else {
                    System.out.println("33333333333"+pnew.getNote());
                    LocalDate currentDate = LocalDate.now();
                    Participe p = new Participe();
                    p.setDateImportation(Date.valueOf(currentDate));
                    p.setAnneeImportation( String.valueOf(currentDate.getYear()));
                    Etudiant etudiant = etudiantRepository.findByMatricule(pnew.getMatricule());
                    Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(pnew.getTypeEval(), pnew.getCode());
                    p.setEtudiant(etudiant);
                    p.setEvaluation(evaluation);
                    p.setNote(pnew.getNote());
                    p.setMatricule(pnew.getMatricule());
                    p.setNomEtudiant(pnew.getNom());
                    p.setUser(user);
                    participeRepository.save(p); 
                }
            }
            return ListUe(session, model);
    }
   
    @PostMapping("/delete")
    public String delete (@RequestParam("code") String code, @RequestParam("typeEval") String typeEval,@RequestParam("Fcode") String Fcode){
        List<Participe> participes = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_CodeAndEvaluation_Ue_Filiere_Code(typeEval, code, Fcode);//findByEvaluation(evaluations);
        participeRepository.deleteAll(participes);
        return "redirect:/index-import";
    }

    @GetMapping("/shows-data")
    public String data(HttpSession session,@RequestParam("code") String code, @RequestParam("typeEval") String typeEval ,@RequestParam("Fcode") String Fcode,Model model){
        // Evaluation evaluations = evaluationRepository.findByTypeEvalAndUe_Code(typeEval, code);
List<Participe> participes = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_CodeAndEvaluation_Ue_Filiere_Code(typeEval, code, Fcode);        // System.out.println("----------->######"+participes);
        model.addAttribute("participes", participes);
         return ListUe(session,model);
    }

    @PostMapping("/valide")
    public String vali(HttpSession session,@RequestParam("code") String code, @RequestParam("typeEval") String typeEval ,@RequestParam("Fcode") String Fcode,Model model){
        Evaluation evaluations = evaluationRepository.findByTypeEvalAndUe_CodeAndUe_Filiere_Code(typeEval, code, Fcode);
        Users user = (Users) session.getAttribute("user");
        StatutNote statut =statutNoteRepository.findFirstByEvaluation(evaluations);//new StatutNote();
        // statut.setEvaluation(evaluations);
        statut.setUser(user);
        statut.setValide(true);
        statutNoteRepository.save(statut);
        // StatutNote statutNote = statutNoteRepository.findFirstByEvaluation(evaluations);
        // model.addAttribute("statut", statutNote);
        return ListUe(session,model);
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> downloadExcel(HttpServletResponse response,@RequestParam("code") String code, @RequestParam("typeEval") String typeEval ,@RequestParam("Fcode") String Fcode) throws IOException {
        // Créer un nouveau classeur Excel
        //  System.out.println("----------->######"+code);
        // System.out.println("----------->######"+typeEval);
        // System.out.println("----------->######"+Fcode);
        List<Participe> data = participeRepository.findByEvaluation_TypeEvalAndEvaluation_Ue_CodeAndEvaluation_Ue_Filiere_Code(typeEval, code, Fcode);        // System.out.println("----------->######"+participes);
        Evaluation eval = evaluationRepository.findByTypeEvalAndUe_CodeAndUe_Filiere_Code(typeEval,code,Fcode);
        Participe participe = participeRepository.findFirstByEvaluation(eval).orElse(null);
        Workbook workbook = new XSSFWorkbook();

        // Créer une feuille dans le classeur
        Sheet sheet = workbook.createSheet("Données");

        // Récupérer les données de votre table et les insérer dans la feuille
        // Remplacez cette partie par votre logique pour récupérer les données de votre table
        // et les insérer dans la feuille Excel
        // Par exemple, vous pouvez utiliser une boucle pour parcourir les données et les insérer dans les cellules
        if(typeEval == "EE"){
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Anonymat");
            // headerRow.createCell(1).setCellValue("Nom");
            headerRow.createCell(2).setCellValue("note");
            // Ajoutez des cellules pour chaque colonne supplémentaire

            // Remplissage des données dans la feuille
            int rowNum = 1;
            for (Participe entity : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getMatricule());
                // row.createCell(1).setCellValue(entity.getNomEtudiant());
                row.createCell(2).setCellValue(entity.getNote());
                // Ajoutez des cellules pour chaque colonne supplémentaire
            }
        } else {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Matricule");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("note");
        // Ajoutez des cellules pour chaque colonne supplémentaire

        // Remplissage des données dans la feuille
        int rowNum = 1;
        for (Participe entity : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entity.getMatricule());
            row.createCell(1).setCellValue(entity.getNomEtudiant());
            row.createCell(2).setCellValue(entity.getNote());
            // Ajoutez des cellules pour chaque colonne supplémentaire
        }}

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        Date dat = eval.getDateEval();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String datt=dateFormat.format(dat);
        String filename = eval.getUe().getCode()+"_"+eval.getTypeEval()+"_S"+eval.getSemestre()+"_"+datt+"_"+eval.getNoteSur()+"_"+eval.getUe().getFiliere().getCode().split("-")[0]+".xlsx";
        byte[] excelBytes = outputStream.toByteArray();
        outputStream.close();
        workbook.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }





    // @PostMapping("/enregistrer")
	// public String getEnregistrer(@ModelAttribute ("participes") Participe participes){
    //     // Etudiant etudiant = participes.getEtudiant();
    //     String matricule = participes.getMatricule();
    //     Etudiant etudiant = etudiantRepository.findByMatricule(matricule);
    //     participes.setEtudiant(etudiant);
	// 	Integer anonymat=participeRepository.generateAnonymat();
	// 	participes.setAnonymat(anonymat);
	// 	participeRepository.save(participes);
	// 	return"redirect:/formulaire";
	// }
	
	// @GetMapping("/suggestions")
	// @ResponseBody
	// public List<String> suggestions(@RequestParam("nom") String nom){
	// 	List<Etudiant> suggestions=etudiantRepository.findByNomContaining(nom);
	// 	List<String> noms=new ArrayList<>();
	// 		for (Etudiant etudiants:suggestions){
	// 		noms.add(etudiants.getNom());
	// 		}
	// 		return noms;

	// 	//testing#####################################################################################""
    //     }

        @PostMapping("/enregistrer")

	public String getEnregistrer(@ModelAttribute ("participes") Participe participes){
        String matricule=participes.getMatricule();
      // Integer id_filiere=participes.getId_Filiere();
        //Etudiant etu=etudiant.findByMatriculeAndId_Filiere(matricule,id_filiere);
        Etudiant etu =etudiantRepository.findByMatricule(matricule);
        participes.setEtudiant(etu);
        if(participes.getAnonymat()==null){
        Integer anonymat=participeRepository.generateAnonymat();
		participes.setAnonymat(anonymat);
		participeRepository.save(participes);
     } else {
        participeRepository.save(participes);
     }

		return "redirect:/etudiant";
	}
	
	@GetMapping("/suggestions")
	@ResponseBody
	public List<String> suggestions(@RequestParam("nom") String nom){
		List<Etudiant> suggestions=etudiantRepository.findByNomContaining(nom);
		List<String> noms=new ArrayList<>();
			for (Etudiant etudiants:suggestions){
			noms.add(etudiants.getNom());
			}
			return noms;
	}




    @GetMapping("/complete")
    @ResponseBody
    public String remplirMatricule(@RequestParam("nom") String nom){
        Etudiant etu=etudiantRepository.findByNom(nom);
        if(etu!=null){
        return etu.getMatricule();
        }
        else{
        return "";
        }
    }





@GetMapping("/etudiant")
    public String listetudiant(HttpSession session,Model model){//},@RequestParam("code") String code, @RequestParam("typeEval") String typeEval ,@RequestParam("Fcode") String Fcode){

        Users user = (Users) session.getAttribute("user");
        if(user!=null){
        List<Participe> listetudiant=participeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<Etudiant> fil=etudiantRepository.findAll();
        model.addAttribute("participes",new Participe());
        //model.addAttribute("fil",fil);
        //List<Participe> listetudiant=participe.findAllOrderByLIFO();
        model.addAttribute("listetudiant",listetudiant);
        // Participe parti = (Participe) model.getAttribute("participess");
        Participe parti = (Participe) model.getAttribute("participess");
        // System.out.println(parti.getNomEtudiant());
        model.addAttribute("participess", parti);
        model.addAttribute("user", user);
        return "list_form";
        } else {
            return "redirect:/etudiant/login"; // Rediriger vers la page de connexion en cas d'utilisateur non connecté
        }
    }

    @GetMapping("/etudiant/edit/{idP}")
    public String editForm(HttpSession session,@PathVariable("idP")Long id, Model model){
        Participe participes= participeRepository.findById( id).get();
        model.addAttribute("participess", participes);
        System.out.println(participes);//.getNomEtudiant());
        System.out.println(model);
       // List<Etudiant> listue=ue.findAll();
        //model.addAttribute("listue", listue);
        return listetudiant(session,model);
    }
    @GetMapping("/etudiant/delete/{idP}")
    public String delete(@PathVariable ("idP") Long idP, Model model){
        participeRepository.deleteById(idP);
        return "redirect:/etudiant";
    }






        @GetMapping("/iiii")
    public String juryCorr(){
        return "indexCorr";
    }


    @GetMapping("/user/test")
    public String testcrr(){
        return "test";
    }

    

    @GetMapping("/login")
    public String formLogin1(){
        return "login";

        
    }

    @GetMapping("/logout1")
    public String formLogout(){
        return "logout1";
    }

    @PostMapping("/account")
    public RedirectView account(@RequestParam String username, @RequestParam String password){
        System.out.println("ok");
        return new RedirectView("/home");
    }




}
