package com.app.sygen.services;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.Participe;
import com.app.sygen.entities.PvCcData;
import com.app.sygen.entities.Ue;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.ParticipeRepository;
import com.app.sygen.repositories.UeRepository;

@Service
public class ParticipeService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ParticipeRepository participeRepository;

    @Autowired
    private UeRepository ueRepository;



    public List<PvCcData> getPvCcData(int semestre,String type,Ue ue){
        
        List<Participe> participes = participeRepository.findByAnneeImportationAndEvaluationOrderByNomEtudiantAsc(LocalDate.now().getYear()+"", evaluationRepository.findByTypeEvalAndUeAndSemestre(type, ueRepository.findByCode(ue.getCode()),semestre));
        
        List<PvCcData> pvsCc = new ArrayList<PvCcData>();

        System.out.println("-------------" + participes.size());
        for (Participe participe : participes) {
            PvCcData pvCc = new PvCcData();

            pvCc.setMatricule(participe.getMatricule());
            pvCc.setNom(participe.getNomEtudiant());
            pvCc.setNote(participe.getNote()+0.0);
            pvsCc.add(pvCc);
        }
        return pvsCc;
    }

    public Participe createParticipeXLSX(Row row, Map<String, Integer> columnIndexMap, String code, String type,
            int noteSur) throws ParseException {
        LocalDate currentDate = LocalDate.now();
        if (type == "EE") {
            Integer anonymat = Integer.parseInt(getColumnValue(row, columnIndexMap, "Anonymat"));
            float note =Float.parseFloat(getColumnValue(row, columnIndexMap, "note"));
            String codes = code;
            String typeE = type;
            Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(typeE, codes);
            if(anonymat != null){
                Participe participe = participeRepository.findByAnonymat(anonymat);
                
                participe.setEvaluation(evaluation);
                participe.setDateImportation(Date.valueOf(currentDate));
                participe.setAnneeImportation( String.valueOf(currentDate.getYear()));
                if (evaluation.getNoteSur() == noteSur) {
                    participe.setNote(note);
                    return participe;

                } else {
                    float notes = (note * evaluation.getNoteSur()) / noteSur;
                    participe.setNote(notes);
                    return participe;}}

                // }//}
            // }
        } else {
            String matricule = getColumnValue(row, columnIndexMap, "Matricule");
            String Nom = getColumnValue(row, columnIndexMap, "Nom");
            String Snote = getColumnValue(row, columnIndexMap, "note");
            LocalDate date = LocalDate.now();
            String anneeEnCours = String.valueOf(date.getYear());
            // String Date = String.valueOf(date.getYear());
            if (!Snote.isEmpty()) {
                float note = Float.parseFloat(Snote);

                String codes = code;
                String typeE = type;
                Etudiant etu = etudiantRepository.findByMatricule(matricule);

                Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(typeE, codes);
                if (evaluation.getNoteSur() == noteSur) {

                    if (Nom != null && matricule != null) {
                        Participe participe = new Participe();
                        participe.setMatricule(matricule);
                        participe.setNomEtudiant(Nom);
                        participe.setNote(note);
                        participe.setEvaluation(evaluation);
                        participe.setEtudiant(etu);
                        participe.setDateImportation(Date.valueOf(currentDate));
                        participe.setAnneeImportation(anneeEnCours);
                        return participe;

                    }
                } else {
                    float notes = (note * evaluation.getNoteSur()) / noteSur;
                    if (Nom != null && matricule != null) {
                        Participe participe = new Participe();
                        participe.setMatricule(matricule);
                        participe.setNomEtudiant(Nom);
                        participe.setNote(notes);
                        participe.setEvaluation(evaluation);
                        participe.setEtudiant(etu);
                        participe.setDateImportation(Date.valueOf(currentDate));
                        participe.setAnneeImportation(anneeEnCours);
                        return participe;

                    }

                }
            } else {
                float note = 0;
                String codes = code;
                String typeE = type;
                Etudiant etu = etudiantRepository.findByMatricule(matricule);

                Evaluation evaluation = evaluationRepository.findByTypeEvalAndUe_Code(typeE, codes);
                if (evaluation.getNoteSur() == noteSur) {

                    if (Nom != null && matricule != null) {
                        Participe participe = new Participe();
                        participe.setMatricule(matricule);
                        participe.setNomEtudiant(Nom);
                        participe.setNote(note);
                        participe.setEvaluation(evaluation);
                        participe.setEtudiant(etu);
                        participe.setDateImportation(Date.valueOf(currentDate));
                        participe.setAnneeImportation(anneeEnCours);
                        return participe;

                    }
                } else {
                    float notes = (note * evaluation.getNoteSur()) / noteSur;
                    if (Nom != null && matricule != null) {
                        Participe participe = new Participe();
                        participe.setMatricule(matricule);
                        participe.setNomEtudiant(Nom);
                        participe.setNote(notes);
                        participe.setEvaluation(evaluation);
                        participe.setEtudiant(etu);
                        participe.setDateImportation(Date.valueOf(currentDate));
                        participe.setAnneeImportation(anneeEnCours);
                        return participe;

                    }
                }
            }
        }
        return null;

    }

    public String getColumnValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                cell.setCellType(CellType.STRING); // Convertir la cellule en type chaîne de caractères
                // cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            }
        }
        return null;
    }

    public Map<String, Integer> getColumnIndexMap(Row headerRow) {
        Map<String, Integer> columnIndexMap = new HashMap<>();
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        int columnIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            columnIndexMap.put(cell.getStringCellValue(), columnIndex);
            columnIndex++;
        }
        return columnIndexMap;
    }
}
