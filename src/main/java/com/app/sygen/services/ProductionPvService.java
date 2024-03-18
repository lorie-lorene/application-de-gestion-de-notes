package com.app.sygen.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.app.sygen.entities.DetailPvUe;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.Participe;
import com.app.sygen.entities.PvCcData;
import com.app.sygen.entities.PvUe;
import com.app.sygen.entities.PvUeData;
import com.app.sygen.entities.Ue;
import com.app.sygen.repositories.DetailsPvUeRepository;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.ParticipeRepository;
import com.app.sygen.repositories.PvUeRepository;
import com.app.sygen.repositories.UeRepository;

@Service
public class ProductionPvService {
    @Autowired
    private ParticipeRepository participeRepository;
    @Autowired
    private ParticipeService participeService;

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private UeRepository ueRepository;
    @Autowired
    private FiliereRepository filNivRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PvUeRepository pvUeRepository;
    @Autowired
    private DetailsPvUeRepository detailPvUeRepository;

    public ResponseEntity<ByteArrayResource> makePdfCC(String typeEval, Ue ue, String filiere, int semestre) throws Exception {
        
        List<PvCcData> pvsCc = participeService.getPvCcData(semestre,typeEval,ue);

        System.out.println("----------->######"+pvsCc.size());
        String dataRow = "";
        // production du pdf
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            
            Evaluation evaluation = evaluationRepository.findByTypeEvalAndUeAndSemestre(typeEval, ue, semestre);
            int k = 0;
            int r = 0;
            for(int i = 0; i < pvsCc.size(); i++){
                
                // for(int j = 0; j < 60; j++){ // boucle pour effectuer le test elle permet de multiplier les donnees
                    dataRow += makePvCcBody(pvsCc.get(i), i+1);
                    k++;
                  
                if(k == 32 && r == 0){
                      

                    String content = makePvccCodeHead(dataRow, evaluation, filiere);
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.createPDF(outputStream, false);
                    dataRow = "";
                    r = 1;
                    k = 1;
                }
               else if(k % 43 == 0){
                    String content = makePvccCodeEntete(dataRow, evaluation, filiere);                    
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.writeNextDocument();
                    dataRow = "";
                }
                else if(pvsCc.size() < 32 && k == pvsCc.size()){
                    String content = makePvccCodeHead(dataRow, evaluation, filiere);
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    
                    renderer.createPDF(outputStream, false);
                    dataRow = "";
                }
                
            // }
            }
            

            
            renderer.finishPDF();
            byte[] pdfBytes = outputStream.toByteArray();


        
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=two-pages.pdf");
    
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new ByteArrayResource(pdfBytes));
      
            


           
        }
    }


// #####################################################################################################
    // ------>>>>>>>>> construction des lignes du tableau du pvcc <<<<-------
    public String makePvCcBody(PvCcData pvCc, int num){

        // List<PvCcData> pvsCc = participeService.getPvCcData();
        String htmlCorp = "";
        htmlCorp = htmlCorp + "<tr>";
        htmlCorp = htmlCorp + "<td>"+ num +"</td>"; // attribution d'un numero a l'etudiant
            
        htmlCorp = htmlCorp + "<td>"+ pvCc.getMatricule().toUpperCase()+"</td>"; // matricule
        htmlCorp = htmlCorp + "<td>"+ pvCc.getNom() +"</td>"; // nom et prenom
        if(pvCc.getNote() != null) // si sa note n'est pas null on l'attribut une note
            htmlCorp = htmlCorp + "<td>"+ pvCc.getNote() +"</td>";
        else
            htmlCorp = htmlCorp + "<td>"+ " " +"</td>";
        // if(etudiantService.geEtudiantByMatricule(pvCc.getMatricule()).isEmpty())
        if(etudiantRepository.findByMatricule(pvCc.getMatricule()) == null)
            htmlCorp = htmlCorp + "<td>"+"Etudiant non autoris\u00E9" +"</td>";
        else
            htmlCorp = htmlCorp + "<td>"+"ok" +"</td>";
        htmlCorp = htmlCorp + "</tr>";
        
        return htmlCorp;
    }

// ########################################################################################################
    // ----------->>>>>>#@> production du code html pour la creation du pdf <<<<-----------
    public String makePvccCodeHead(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                // "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(18, 172, 232);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-62px;\n" + //
                    "             margin-right:0;\n" + //  
                   
                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:18%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
          
                                "display:inline-block;\n" + //            
              

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            width: 0%;\n" + //
                    "            display:inline-block;\n"+//           
                    "            margin-left:8%;\n"+//           

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                    // "    <header>\n" + //
                    // "        <h1> SYGEN </h1>\n" + //
                    // "    </header>\n" + //
                    "    <main style='color:rgb(24, 23, 23);'>\n" + //
                    "        <div id=\"block-info\" style='margin:0; padding:0;'>\n" + //
                    "            <div class=\"info-uv-1\" >\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; padding-left:0; font-size: 12px; font-weight: bold;'>REPUBLIQUE DU CAMEROUN</p>\n"+//
                    "                <p style='margin:8px;'>Paix -Travail - Patrie</p>\n"+//
                    "                <p style='margin:8px; font-size: 12px; font-weight: bold;' >UNIVERSITE DE YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px;'>FACULTE DES SCIENCES</p>\n" + //
                    "                <p style='margin:8px;'> BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p>\n" + //
                    "                <p style='margin:8px;'> Tel : 222 234 496 / Email:</p>\n" + //
                    "                <p style='margin:8px;'>diplome@facsciences.uy1.cm</p>\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "            <div id=\"logo-uv\">\n" + //
                                    "<img src=\"src/main/resources/static/img/Blason_univ_Yaound\u00E9_1.png\" style=\"height: 90px;\"/>\n" + //
                    "            </div>\n" + //
                    "            <div class=\"info-uv-2\" >\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; font-size: 12px; font-weight: bold;'>REPUBLIQUE DU CAMEROUN</p>\n"+//
                    "                <p style='margin:8px;'>Paix -Travail - Patrie</p  >\n"+//
                    "                <p style='margin:8px; font-size: 12px; font-weight: bold;' >UNIVERSITE DE YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px;'>FACULTE DES SCIENCES</p>\n" + //
                    "                <p style='margin:8px;'> BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p >\n" + //
                    "                <p style='margin:8px;'> Tel : 222 234 496 / Email:</p >\n" + //
                    "                <p style='margin:8px;'>diplome@facsciences.uy1.cm</p  >\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "        </div>\n" + //
                    "        <div id=\"info-pv\">\n" + //
                    "            <p style='margin:3px; font-size: 25px; font-weight: bold;'>PROCES VERBAL DE L'UNITE D'ENSEIGNEMENT</p>\n" + //
                    "            <p style='margin:3px; font-size: 18px; font-weight: 400;'>"+evaluation.getUe().getCode()+" "+evaluation.getUe().getIntitule()+"</p>\n" + //
                    "            <p style='margin:3px; font-size: 15px; font-weight: 500;'>"+ evaluation.getTypeEval()+"</p>\n" + //
                    "            <p style='margin:3px; font-size: 13px; font-weight: 400;'>FILIERE : INE - "+filiere.toUpperCase()+"</p>\n" + //
                    "            <p style='margin:3px; font-size: 13px; font-weight: 400;'>SPECIALITE : -</p>\n" + //
                    "        </div>\n" + //
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Note </td>\n" + //
                    "                <td> Observation </td>\n" + //
                    "            </tr>\n" + //
                                htmlCorp +

                    "        </table>\n"+

                    "    </main>\n" + //
                    "</body>\n" + //
                    "\n" + //
                    "</html>";

        return contentPdf;            
    }
//############################################################################################
    public String makePvccCodeEntete(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                // "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(18, 172, 232);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    // "\n" + //
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-62px;\n" + //
                    "             margin-right:0;\n" + //  
                    // "             padding-right:-20%;\n" + //                              

                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:18%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                                "display:inline-block;\n" + //            
                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            width: 0%;\n" + //
                    "            display:inline-block;\n"+//           
                    "            margin-left:8%;\n"+//           

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                  
                    "    <main style='color:rgb(24, 23, 23);'>\n" + //
                    
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Note </td>\n" + //
                    "                <td> Observation </td>\n" + //
                    "            </tr>\n" + //
                                htmlCorp +

                    "        </table>\n"+

                    "    </main>\n" + //
                    "</body>\n" + //
                    "\n" + //
                    "</html>";
        return contentPdf;
    }


// #########################################################################################

    public ResponseEntity<ByteArrayResource> makePdfUe(String typeEval, Ue ue, String filiere, String niveau, int semestre) throws Exception {
        List<PvUeData> pvUeDatas = sortStudent(false, filiere, ue.getCode(), semestre);
        List<PvUeData> pvUeDatasPb = sortStudent(true, filiere, ue.getCode(), semestre);
        String dataRow = "";
        // production du pdf
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            
            Evaluation evaluation = evaluationRepository.findByTypeEvalAndUeAndSemestre(typeEval, ue, semestre);
            int k = 0;
            int r = 0;
            int s = 0;
            int u = 0;

            // |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            // -------------<<<<<<<<<< Rapport a  afficher >>>>>>>>>--------------            
            for(int i = 0; i < pvUeDatas.size(); i++){
                
                // for(int j = 0; j < 10; j++){ // boucle pour effectuer le test elle permet de multiplier les donnees
                    dataRow += makeBodyPvUe(pvUeDatas.get(i), i+1, niveau,false);
                    k++;
                  
                if(k == 19 && r == 0){
                      

                    String content = PvUeCode(dataRow, evaluation, filiere);
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.createPDF(outputStream, false);
                    dataRow = "";
                    r = 1;
                    k = 1;
                }
               else if(k % 29 == 0){
                    String content = PvUeCodeBody(dataRow, evaluation, filiere);                    
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.writeNextDocument();
                    dataRow = "";
                }
                else if(pvUeDatas.size() < 19){
                    if(pvUeDatas.size() == k){
                        String content = PvUeCode(dataRow, evaluation, filiere);
                        renderer.setDocumentFromString(content);
                        renderer.layout();
                        renderer.createPDF(outputStream, false);
                        dataRow = "";
                        r = 1;
                        k = 1;
                    }    
                }
                else{
                    String content = PvUeCodeBody(dataRow, evaluation, filiere);                    
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.writeNextDocument();
                    dataRow = "";
                }
                
            // }
            }

            // |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            // -------------<<<<<<<<<< Rapport a ne pas afficher >>>>>>>>>--------------
            dataRow = "";
            for(int i = 0; i < pvUeDatasPb.size(); i++){
                
                // for(int j = 0; j < 50; j++){ // boucle pour effectuer le test elle permet de multiplier les donnees
                    u++;
                    dataRow += makeBodyPvUe(pvUeDatasPb.get(i), i+1 ,niveau ,true);
                  
                if(u == 18 && s == 0){
                      

                    String content = PvUePbCodeHead(dataRow, evaluation, filiere);
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    // renderer.createPDF(outputStream, false);
                    renderer.writeNextDocument();
                    dataRow = "";
                    s = 1;
                    u = 1;
                }
               else if(u % 29 == 0){
                    String content = PvUePbCodeBody(dataRow, evaluation, filiere);                    
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.writeNextDocument();
                    dataRow = "";
                }
                else if(pvUeDatasPb.size() < 19){
                    if(pvUeDatasPb.size() == u){
                        String content = PvUePbCodeHead(dataRow, evaluation, filiere);
                        renderer.setDocumentFromString(content);
                        renderer.layout();
                        renderer.writeNextDocument();
                        dataRow = "";
                        s = 1;
                        u= 1;
                    }    
                }
                else{
                    String content = PvUeCodeBody(dataRow, evaluation, filiere);                    
                    renderer.setDocumentFromString(content);
                    renderer.layout();
                    renderer.writeNextDocument();
                    dataRow = "";
                }
                
            // }
            }
            

            
            renderer.finishPDF();
            byte[] pdfBytes = outputStream.toByteArray();


        
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=two-pages.pdf");
    
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new ByteArrayResource(pdfBytes));
      
            


           
        }
    }

// ##################<<<<<<< PRODUCTION DU PV UE >>>>>>>>>##########################

    // ################### fonction qui recupere les etudiants ayant payer et ceux n'ayant pas payer
    public List<PvUeData> findGoodAndBadStudents(Boolean paie, String filiere, String codeUe, int semestre){
        List<Etudiant> etudiants = new ArrayList<Etudiant>();
        if(paie == true){ // on recupere les etudiant qui on payer la deuxieme tranche
            if(semestre == 1)
                etudiants = etudiantRepository.findByFiliereAndStatutPaiementGreaterThanEqualOrderByNomAsc(filNivRepository.findByCode(filiere), 25000.0);
            if(semestre == 2)
                etudiants = etudiantRepository.findByFiliereAndStatutPaiementGreaterThanEqualOrderByNomAsc(filNivRepository.findByCode(filiere), 50000.0);

        }
        else{ // on recupere les etudiants qui n'on pas encore payer la deuxieme tranche
            if(semestre == 1)
                etudiants = etudiantRepository.findByFiliereAndStatutPaiementIgnoreCaseIsLessThanOrderByNomAsc(filNivRepository.findByCode(filiere), 25000.0);
            if(semestre == 2)
                etudiants = etudiantRepository.findByFiliereAndStatutPaiementIgnoreCaseIsLessThanOrderByNomAsc(filNivRepository.findByCode(filiere), 50000.0);
        }

        List<PvUeData> pvUeDatas = new ArrayList<PvUeData>();
        
        for (int i = 0; i < etudiants.size(); i++) { // on parcour la liste des etudiants 
            int t = 0;
            PvUeData pvUeData = new PvUeData();
            // on recupere les information sur leur note de tp, cc et sn
            List<Participe> participesCc = participeRepository.findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(etudiants.get(i), LocalDate.now().getYear()+"", evaluationRepository.findByTypeEvalAndUeAndSemestre("cc", ueRepository.findByCode(codeUe), semestre));
            List<Participe> participesTp = participeRepository.findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(etudiants.get(i), LocalDate.now().getYear()+"", evaluationRepository.findByTypeEvalAndUeAndSemestre("tp", ueRepository.findByCode(codeUe), semestre));
            List<Participe> participesSn = participeRepository.findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(etudiants.get(i), LocalDate.now().getYear()+"", evaluationRepository.findByTypeEvalAndUeAndSemestre("sn", ueRepository.findByCode(codeUe), semestre));
            
            // recuperation du nom matricule et filiere de l'etudiant
            pvUeData.setNom(etudiants.get(i).getNom());
            pvUeData.setMatricule(etudiants.get(i).getMatricule());
            pvUeData.setNiveau(etudiants.get(i).getFiliere().getCode());
            
            // |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            // ---------- <<< recuration de la note de cc de l'etudiant >>>>>------------
            if((participesSn.size() > 1 || paie == false) && t == 0){
                pvUeData.setObservation(null);
                if(participesCc.size() > 1 && t == 0)
                    pvUeData.setObservation("ETUDIANT AYANT PLUSIEURS ENTETE<br/>");
                if( t == 0 && paie == false)
                    pvUeData.setObservation(pvUeData.getObservation()+" ABSENCE DE PAIEMENT");
                t = 1;
            }    
            else{
                
                if(participesCc.size() == 0){
                    pvUeData.setNoteCc(null);
                    pvUeData.setAnnonymaCc(null);
                }        
                else{
                    pvUeData.setNoteCc(participesCc.get(0).getNote()+0.0);
                    pvUeData.setAnnonymaCc(participesCc.get(0).getAnonymat());
                } 
            }   
                                                                                    
            // |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            // ---------- <<< recuration de la note de tp de l'etudiant >>>>>------------
            if((participesSn.size() > 1 || paie == false) && t == 0){
                pvUeData.setObservation("");
                if(participesTp.size() > 1 && t == 0)
                    pvUeData.setObservation("ETUDIANT AYANT PLUSIEURS ENTETE<br/>");
                if( t == 0 && paie == false)
                    pvUeData.setObservation(pvUeData.getObservation()+" ABSENCE DE PAIEMENT");
                t = 1;
            }     
            else{
                if(participesTp.size() == 0){
                           pvUeData.setNoteTp(null);
                    pvUeData.setAnnonymaTp(null);
                }        
                else{
                    pvUeData.setNoteTp(participesTp.get(0).getNote()+0.0);
                    pvUeData.setAnnonymaTp(participesSn.get(0).getAnonymat());
                }    
            }

             // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            // ---------- <<< recuration de la note de tp de l'etudiant >>>>>------------
            if((participesSn.size() > 1 || paie == false) && t == 0){
                pvUeData.setObservation("");
                if(participesSn.size() > 1 && t == 0)
                    pvUeData.setObservation("ETUDIANT AYANT PLUSIEURS ENTETE<br/>");
                if( t == 0 && paie == false)
                    pvUeData.setObservation(pvUeData.getObservation()+" ABSENCE DE PAIEMENT");
                t = 1;
            }     
            else{
                if(participesTp.size() == 0){
                    pvUeData.setNoteSn(null);
                    pvUeData.setAnnonymaSn(null);
                }        
                else{
                    pvUeData.setNoteSn(participesSn.get(0).getNote()+0.0);
                    pvUeData.setAnnonymaSn(participesSn.get(0).getAnonymat());
                } 
            }

            pvUeDatas.add(pvUeData);
            
        }
        


        // return etudiants.get(1).getNom();
        return pvUeDatas;
    }

    // ################## fonction qui classe les etudiants (ceux qui ont des problemes de ceux qui n'en ont pas) ##############
    public List<PvUeData> sortStudent(boolean pb, String filiere, String codeUe, int semestre){
        // filiere = "inf-l2";
        List<PvUeData> badStudent = findGoodAndBadStudents(false, filiere, codeUe, semestre);
        List<PvUeData> studentOk = findGoodAndBadStudents(true, filiere, codeUe, semestre);
        List<PvUeData> goodStudent = new ArrayList<PvUeData>();
        for (PvUeData student : studentOk) {
            if(student.getObservation() != null)
                badStudent.add(student);
            else
                goodStudent.add(student);    
        }

        Collections.sort(badStudent);
        Collections.sort(goodStudent);
        List<PvUeData> pvUeDatas2 = new ArrayList<PvUeData>();
        if(pb == true)
            pvUeDatas2 = badStudent;
        else
            pvUeDatas2 = goodStudent;

        for (PvUeData pvUeData : pvUeDatas2) {
            if(pvUeData.getNoteCc() == null || pvUeData.getNoteTp() == null || pvUeData.getNoteSn() == null){
                pvUeData.setTotal(null);
                pvUeData.setDecision("El");
                pvUeData.setMention(null);
            }
            else{
                Double total = pvUeData.getNoteCc() + pvUeData.getNoteTp() + pvUeData.getNoteSn();
                pvUeData.setTotal(total);
                pvUeData.setDecision(decision(total));
                pvUeData.setMention(mention(total));
            }

        }


        if(pb == true)
            return badStudent;
        else
            return goodStudent;

        // return badStudent.get(3).getNom();
    }

    // ################# methode permettant de corp du pdf du pv Ue ################
    public String makeBodyPvUe(PvUeData pvUeData, int num, String niveau, boolean pb){

        String htmlCorp = "";
        htmlCorp += "<tr>";
        htmlCorp += "<td>"+ num +"</td>"; // attribution d'un numero a l'etudiant
        htmlCorp += "<td>"+ pvUeData.getMatricule() +"</td>";
        htmlCorp += "<td>"+ pvUeData.getNom() +"</td>";
        htmlCorp += "<td>"+ niveau +"</td>";
        if(pvUeData.getAnnonymaCc() != null)
            htmlCorp += "<td>"+ pvUeData.getAnnonymaCc() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";
        
        if(pvUeData.getNoteCc() != null)    
            htmlCorp += "<td>"+ pvUeData.getNoteCc() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";


        if(pvUeData.getAnnonymaTp() != null)
            htmlCorp += "<td>"+ pvUeData.getAnnonymaTp() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";

        if(pvUeData.getNoteTp() != null)    
            htmlCorp += "<td>"+ pvUeData.getNoteTp() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";
            
        if(pvUeData.getAnnonymaSn() != null)
            htmlCorp += "<td>"+ pvUeData.getAnnonymaSn() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";

        if(pvUeData.getNoteSn() != null)    
            htmlCorp += "<td>"+ pvUeData.getNoteSn() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";
        
            if(pvUeData.getTotal() != null)
            htmlCorp += "<td>"+ pvUeData.getTotal() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";

        if(pvUeData.getDecision() != null)
            htmlCorp += "<td>"+ pvUeData.getDecision() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";

        if(pvUeData.getMention() != null)
            htmlCorp += "<td>"+ pvUeData.getMention() +"</td>";
        else
            htmlCorp += "<td>"+ " " +"</td>";

        if(pvUeData.getObservation() != null)
            htmlCorp += "<td>"+ pvUeData.getObservation() +"</td>";
        htmlCorp +="</tr>";    
        return htmlCorp;
    }


    // ################## methode pour produire le code de l'entete du pdf ################
    public String PvUeCode(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html>\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                    // "        @page{ \n" + //
                    "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(2, 142, 152);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    // "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    // "\n" + //
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-97px;\n" + //
                    "             margin-right:0;\n" + //  
                    // "             padding-right:-20%;\n" + //                              

                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:12%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                                "display:inline-block;\n" + //            
                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            margin-left: 10%;\n" + //
                    "            display:inline-block;\n"+//            

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                    // "    <header>\n" + //
                    // "        <h1> SYGEN </h1>\n" + //
                    // "    </header>\n" + //
                    "    <main>\n" + //
                    "        <div id=\"block-info\" style='margin:0; padding:0;'>\n" + //
                    "            <div class=\"info-uv-1\">\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;' >UNIVERSITE DE YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>FACULTE DES SCIENCESFACULTE</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'> BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'> Tel : 222 234 496 / Email:</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>diplome@facsciences.uy1.cm</p>\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "            <div id=\"logo-uv\">\n" + //
                                    "<img src=\"src/main/resources/static/img/Blason_univ_Yaound\u00E9_1.png\" style=\"height: 90px;\"/>\n" + //

                                    // "<img src=\"src/main/resources/static/img/Blason_univ_Yaound\\u00E9_1.png\" style=\"height: 90px;\"/>\n" + //
                    "            </div>\n" + //
                    "            <div class=\"info-uv-2\">\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>UNIVERSITY OF YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>FACULTE DES SCIENCES</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>Tel : 222 234 496 / Email:</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>diplome@facsciences.uy1.cm</p>\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "        </div>\n" + //
                    "        <div id=\"info-pv\">\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:20px;'>PROCES VERBAL DE L'UNITE D'ENSEIGNEMENT</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>"+evaluation.getUe().getCode()+" "+ evaluation.getUe().getIntitule()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>"+ evaluation.getTypeEval()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>FILIERE : INE - "+ filiere.toUpperCase()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>SPECIALITE : -</p>\n" + //
                    "        </div>\n" + //
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Niveau</td>\n" + //
                    "                <td>ANO_CC</td>\n" + //
                    "                <td>CC/20</td>\n" + //
                    "                <td>ANO_EE</td>\n" + //
                    "                <td>EE/50</td>\n" + //
                    "                <td>ANO_EP</td>\n" + //
                    "                <td>EP/50</td>\n" + //
                    "                <td>Total/100</td>\n" + //
                    "                <td>DEC</td>\n" + //
                    "                <td>Mention</td>\n" + //
                    "            </tr>\n" + //
                    htmlCorp+ //
                    "        </table>\n" + //
                    "    </main>\n" + //

                    // rapport a ne pas afficher du process verbale
                    // htmlCorp1+ //
                    "</body>\n" + //
                    "\n" + //
                    "</html>";
        return contentPdf;            
    }


    // ################## methode pour produire le code du corp du pdf ################
    public String PvUeCodeBody(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html>\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                    // "        @page{ \n" + //
                    "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(2, 142, 152);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    // "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    // "\n" + //
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-97px;\n" + //
                    "             margin-right:0;\n" + //  
                    // "             padding-right:-20%;\n" + //                              

                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:12%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                                "display:inline-block;\n" + //            
                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            margin-left: 10%;\n" + //
                    "            display:inline-block;\n"+//            

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                    // "    <header>\n" + //
                    // "        <h1> SYGEN </h1>\n" + //
                    // "    </header>\n" + //
                    "    <main>\n" + //
                    
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Niveau</td>\n" + //
                    "                <td>ANO_CC</td>\n" + //
                    "                <td>CC/20</td>\n" + //
                    "                <td>ANO_EE</td>\n" + //
                    "                <td>EE/50</td>\n" + //
                    "                <td>ANO_EP</td>\n" + //
                    "                <td>EP/50</td>\n" + //
                    "                <td>Total/100</td>\n" + //
                    "                <td>DEC</td>\n" + //
                    "                <td>Mention</td>\n" + //
                    "            </tr>\n" + //
                    htmlCorp+ //
                    "        </table>\n" + //
                    "    </main>\n" + //

                    // rapport a ne pas afficher du process verbale
                    // htmlCorp1+ //
                    "</body>\n" + //
                    "\n" + //
                    "</html>";
        return contentPdf;            
    }


    // ################### methode pour produir l'entete de pdf a ne pas a afficher ###########
    public String PvUePbCodeHead(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html>\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                    // "        @page{ \n" + //
                    "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(2, 142, 152);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    // "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    // "\n" + //
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-97px;\n" + //
                    "             margin-right:0;\n" + //  
                    // "             padding-right:-20%;\n" + //                              

                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:12%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                                "display:inline-block;\n" + //            
                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            margin-left: 10%;\n" + //
                    "            display:inline-block;\n"+//            

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                    // "    <header>\n" + //
                    // "        <h1> SYGEN </h1>\n" + //
                    // "    </header>\n" + //
                    "    <main>\n" + //
                    "        <div id=\"block-info\" style='margin:0; padding:0;'>\n" + //
                    "            <div class=\"info-uv-1\">\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;' >UNIVERSITE DE YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>FACULTE DES SCIENCESFACULTE</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'> BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'> Tel : 222 234 496 / Email:</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>diplome@facsciences.uy1.cm</p>\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "            <div id=\"logo-uv\">\n" + //
                                    "<img src=\"src/main/resources/static/img/Blason_univ_Yaound\u00E9_1.png\" style=\"height: 90px;\"/>\n" + //

                                    // "<img src=\"src/main/resources/static/img/Blason_univ_Yaound\\u00E9_1.png\" style=\"height: 90px;\"/>\n" + //
                    "            </div>\n" + //
                    "            <div class=\"info-uv-2\">\n" + //
                    // "                <p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>UNIVERSITY OF YAOUNDE I</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>FACULTE DES SCIENCES</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>BP/P.O.Box 812 Yaound\u00E9-CAMEROUN /</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>Tel : 222 234 496 / Email:</p>\n" + //
                    "                <p style='margin:8px; font-weight:bold; font-size:15px;'>diplome@facsciences.uy1.cm</p>\n" + //
                    // "                </p>\n" + //
                    "            </div>\n" + //
                    "        </div>\n" + //
                    "        <div id=\"info-pv\">\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:20px;'>PROCES VERBAL DE L'UNITE D'ENSEIGNEMENT</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>"+evaluation.getUe().getCode()+" "+ evaluation.getUe().getIntitule()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>"+ evaluation.getTypeEval()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>FILIERE : INE - "+ filiere.toUpperCase()+"</p>\n" + //
                    "            <p style='margin:3px; font-weight:bold; font-size:15px;'>SPECIALITE : -</p>\n" + //
                    "        </div>\n" + //
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Niveau</td>\n" + //
                    "                <td>ANO_CC</td>\n" + //
                    "                <td>CC/20</td>\n" + //
                    "                <td>ANO_EE</td>\n" + //
                    "                <td>EE/50</td>\n" + //
                    "                <td>ANO_EP</td>\n" + //
                    "                <td>EP/50</td>\n" + //
                    "                <td>Total/100</td>\n" + //
                    "                <td>DEC</td>\n" + //
                    "                <td>Mention</td>\n" + //
                    "                <td>OBSERVATION</td>\n" + //
                    "            </tr>\n" + //
                    htmlCorp+ //
                    "        </table>\n" + //
                    "    </main>\n" + //

                    // rapport a ne pas afficher du process verbale
                    // htmlCorp1+ //
                    "</body>\n" + //
                    "\n" + //
                    "</html>";
        return contentPdf;
    }


      // ################### methode pour produir l'entete de pdf a ne pas a afficher ###########
    public String PvUePbCodeBody(String htmlCorp, Evaluation evaluation, String filiere){
        String contentPdf = "\n" + //
                    "<html>\n" + //
                    "\n" + //
                    "<head style='background-color:yellow;'>\n" + //
                    " \n" + //
                    "    <title>Document</title>\n" + //
                    "    <style>\n" + //
                    // "        @page{ \n" + //
                    "       @page{size:landscape;}\n"+ //
                    "        main {\n" + //

                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #block-info {\n" + //
                    "            width: 110%;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        header {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table {\n" + //
                    "            border-collapse: collapse;\n" + //
                    "            font-weight: bold;\n" + //
                    // "            margin-left: 15%;\n" + //
                    "            width:100%;\n" + //

                    "        }\n" + //
                    "\n" + //
                    "        td {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        table,\n" + //
                    "        td {\n" + //
                    "            border: solid 2px rgb(2, 142, 152);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        #head-pv {\n" + //
                    // "            font-size: 18px;\n" + //
                    "        }\n" + //
                    "         p {\n" + //
                    "            font-size: 10px;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #info-pv {\n" + //
                    "            text-align: center;\n" + //
                    "        }\n" + //
                    // "\n" + //
                    "        .info-uv-1 {\n" + //
                    "            text-align: center;\n" + //
                    "            display:inline-block;\n"+//  
                    "             margin-left:-97px;\n" + //
                    "             margin-right:0;\n" + //  
                    // "             padding-right:-20%;\n" + //                              

                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //
                    "        .info-uv-2 {\n" + //
                    "            text-align: center;\n" + //
                    "             margin-left:12%;\n" + //
                    "             margin-right:-10px;\n" + //
                    "             padding:0;\n" + //
                    // "            display: flex;\n" + //
                    // "            flex-direction: column;\n" + //
                                "display:inline-block;\n" + //            
                    // "            border:solid 3px blue;\n"+//                  
                    // "            color:blue;\n"+//

                    "            width: 40%;\n" + //
                    "        }\n" + //                    
                    "\n" + //
                    "        #logo-uv {\n" + //
                    "            margin-left: 10%;\n" + //
                    "            display:inline-block;\n"+//            

                    "        }\n" + //
                    "    </style>\n" + //
                    "</head>\n" + //
                    "\n" + //
                    "<body style='margin-top:0px; padding-top:0px; '>\n" + //
                    // "    <header>\n" + //
                    // "        <h1> SYGEN </h1>\n" + //
                    // "    </header>\n" + //
                    "    <main>\n" + //
                    
                    "        <table>\n" + //
                    "            <tr id='head-pv'>\n" + //
                    "                <td>Num</td>\n" + //
                    "                <td> Matricul </td>\n" + //
                    "                <td> Nom et prenom </td>\n" + //
                    "                <td>Niveau</td>\n" + //
                    "                <td>ANO_CC</td>\n" + //
                    "                <td>CC/20</td>\n" + //
                    "                <td>ANO_EE</td>\n" + //
                    "                <td>EE/50</td>\n" + //
                    "                <td>ANO_EP</td>\n" + //
                    "                <td>EP/50</td>\n" + //
                    "                <td>Total/100</td>\n" + //
                    "                <td>DEC</td>\n" + //
                    "                <td>Mention</td>\n" + //
                    "                <td>OBSERVATION</td>\n" + //
                    "            </tr>\n" + //
                    htmlCorp+ //
                    "        </table>\n" + //
                    "    </main>\n" + //

                    "</body>\n" + //
                    "\n" + //
                    "</html>";
        return contentPdf;
    }

    






    // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    // ################# methode permettant d'obtenir la decision ############
    public String decision(Double total) {
        String decision = "";
        if(total == null)
            decision = "El";
        else if(total < 35)    
            decision = "Echec";
        else if(total >= 35 && total <50)
            decision = "CANT";
        else if(total >= 50)
            decision = "CA";
        return decision;    
    }
    // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
// ##################### methode permettant d'obtenir la mention #######################
    public String mention(Double note){
        String mention = "";
        if(note < 30)
            mention = "F";
        else if(note >= 30 && note < 35)
            mention = "E";
        else if(note >= 35 && note < 39)
            mention = "D";
        else if(note >= 40 && note < 44)
            mention = "D+";
        else if(note >= 45 && note < 49)
            mention = "C-";
        else if(note >= 50 && note < 54)
            mention = "C";
        else if(note >= 55 && note < 59)
            mention = "C+";
        else if(note >= 60 && note < 64)
            mention = "B-";
        else if(note >= 65 && note < 69)
            mention = "B";
        else if(note >= 70 && note < 74)
            mention = "B+";
        else if(note >= 75 && note < 79)
            mention = "A-";
        else if(note >= 80)
            mention = "A";    
        return mention;    
    }

    // ##################################################################################
    // |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    // ############# sauvegarde des information relative a l'ue dans la table pvUe ###########//#endregion

    public PvUe CreatePvUe(int semestre, String codeUe) {

        if(ueRepository.findByCode(codeUe) != null){
            System.out.println("cette ue n'existe pas !");
            return null;
        }
        else{
            Ue ue = ueRepository.findByCode(codeUe);            
            if(pvUeRepository.findByAnneeAndSemestreAndUe(LocalDate.now().getYear()+"", semestre, ue) == null){
            PvUe pvUe = new PvUe(semestre, LocalDate.now().getYear()+"", ue);
            
                return pvUeRepository.save(pvUe);
            }
            else{
                System.out.println("ce pv a deja ete creee !!!");
                return pvUeRepository.findByUe(ue);
            }
        }
        
    }


    public String CreateDetaiPvUe(String filiere, String codeUe, int semestre, PvUe pvUe) {
        List<PvUeData> pvUeDatas1 = new ArrayList<PvUeData>();
        List<PvUeData> pvUeDatas2 = new ArrayList<PvUeData>();

        pvUeDatas1 = sortStudent(false, filiere,pvUe.getUe().getCode(),semestre);
        // DetailPvUe detailPvUe = detailPvUeRepository.findByEtudiantAndPvUe(etudiantRepository.findByMatricule(pvUeDatas1.get(0).getMatricule()).get(0), pvUeRepository.findByAnneeAndSemestreAndUe(LocalDate.now().getYear()+"", semestre, ueRepository.findByCodeUE(codeUe)));
        
        for (PvUeData pvUeData : pvUeDatas1) {
            DetailPvUe detailPvUe = detailPvUeRepository.findByEtudiantAndPvUe(etudiantRepository.findByMatriculeOrderByNom(pvUeDatas1.get(0).getMatricule()).get(0), pvUeRepository.findByAnneeAndSemestreAndUe(LocalDate.now().getYear()+"", semestre, ueRepository.findByCode(codeUe)));
            if(detailPvUe != null ){
                System.out.println("Alredy exist !! ");
                
            }
            else{
                DetailPvUe detailPvUe2 = new DetailPvUe(pvUeData.getTotal().floatValue(), pvUe, etudiantRepository.findByFiliereAndMatricule(filNivRepository.findByCode(filiere), pvUeData.getMatricule()));
                detailPvUeRepository.save(detailPvUe2);
            }
        }

        for (PvUeData pvUeData : pvUeDatas2) {
            DetailPvUe detailPvUe = detailPvUeRepository.findByEtudiantAndPvUe(etudiantRepository.findByMatriculeOrderByNom(pvUeDatas1.get(0).getMatricule()).get(0), pvUeRepository.findByAnneeAndSemestreAndUe(LocalDate.now().getYear()+"", semestre, ueRepository.findByCode(codeUe)));
            if(detailPvUe != null ){
                System.out.println("Alredy exist !! ");
                
            }
            else{
                DetailPvUe detailPvUe2 = new DetailPvUe(pvUeData.getTotal().floatValue(), pvUe, etudiantRepository.findByFiliereAndMatricule(filNivRepository.findByCode(filiere), pvUeData.getMatricule()));                
                detailPvUeRepository.save(detailPvUe2);
            }
        }
        
        return "ok";   
    }

}

