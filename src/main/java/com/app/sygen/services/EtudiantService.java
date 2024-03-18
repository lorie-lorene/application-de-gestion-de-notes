package com.app.sygen.services;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.HistoriquePaiement;
import com.app.sygen.entities.Paiement;
import com.app.sygen.enums.Sexe;
import com.app.sygen.repositories.EtudiantRepository;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.repositories.HistoriqueFiliereRepository;
import com.app.sygen.repositories.HistoriquePaiementRepository;
import com.app.sygen.repositories.PaiementRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EtudiantService 
{
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private HistoriqueFiliereRepository historiqueFiliereRepository;
    @Autowired
    private HistoriquePaiementRepository historiquePaiementRepository;
    @Autowired
    private PaiementRepository paiementRepository;
    @Autowired
    private FiliereRepository filiereRepository;

    public void create(Etudiant etudiant)
    {    
        this.etudiantRepository.save(etudiant);
    }

    public Etudiant byId(Long id){
        return this.etudiantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid etudiant Id:" + id));
    }

    public Etudiant getEtudiantById(Long id)
    {
        List<Etudiant> etudiantList = show();
        
        return etudiantList.stream()
        		.filter(etudiant -> etudiant.getId().equals(id))
        		.findFirst()
        		.orElse(null);
    }

    public List<Etudiant> show()
    {
        return etudiantRepository.findAll();
    }
    
    public List<Etudiant> shows()
    {
        Double montant = 0.0;
        
        List<Etudiant> etudiants = etudiantRepository.findAll();
        for(Etudiant etudiant : etudiants) {
            List<Paiement> paiements = paiementRepository.findByEtudiant(etudiant);
            for(Paiement paiement : paiements){
                montant += paiement.getMontant();
                // montant = etudiantRepository.showStatus(etudiant.getId());
                etudiant.setStatutPaiement(montant);
            }
        }

        etudiantRepository.saveAll(etudiants);
        
        return etudiants;
    }

    public void createPaiement(Long id, Paiement paiement)
    {
        // Etudiant etudiant =etudiantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Etudiant not found"));
        Etudiant etudiant=etudiantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invallid etudiant Id:" + id));
        Paiement newPaiement = new Paiement();
        // newPaiement.setId(3L);
        newPaiement.setNomBank(paiement.getNomBank());
        newPaiement.setNumRecu(paiement.getNumRecu());
        newPaiement.setDatePaiement(paiement.getDatePaiement());
        newPaiement.setMontant(paiement.getMontant());
        newPaiement.setEtudiant(etudiant);
        paiementRepository.save(newPaiement);

        HistoriquePaiement historiquePaiement = new HistoriquePaiement();
        historiquePaiement.setEtudiant(etudiant);
        historiquePaiement.setNomBank(paiement.getNomBank());
        historiquePaiement.setDatePaiement(paiement.getDatePaiement());
        historiquePaiement.setNumRecu(paiement.getNumRecu());
        historiquePaiement.setMontant(paiement.getMontant());
        
        historiquePaiementRepository.save(historiquePaiement);
    }

    public Etudiant createEtudiantCSV(String[] record, Map<String, Integer> columnIndexMap) throws ParseException 
    {
        // Map<String, Integer> columnIndexMap = getColumnIndexMap(header);
        String matricule = getColumnValue(record, columnIndexMap, "Matricule");
        String nom = getColumnValue(record, columnIndexMap, "Nom");
        String prenom = getColumnValue(record, columnIndexMap, "Prenom");
        String date = getColumnValue(record, columnIndexMap, "Date_Naiss");
        java.util.Date dates = (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
        Date dateNaiss =new Date(dates.getTime());
        String lieuNaiss = getColumnValue(record, columnIndexMap, "lieu_Naiss");
        String sexe = getColumnValue(record, columnIndexMap, "sexe");
        String telephone = getColumnValue(record, columnIndexMap, "Telephone");
        String email = getColumnValue(record, columnIndexMap, "email");
        String adresse = getColumnValue(record, columnIndexMap, "Adresse");
        String diplomeEnEntree = getColumnValue(record, columnIndexMap, "Diplome En Entree");
        String codeFiliere = getColumnValue(record, columnIndexMap, "code Filiere");
        Double statutPaiement = 0.0;
        Filiere filiere = filiereRepository.findFirstByCode(codeFiliere);
    
        if(filiere == null){
            filiere = new Filiere();
            filiere.setCode(codeFiliere);
            filiereRepository.save(filiere);
        }

        if (nom != null && prenom != null && matricule != null) {
        	Etudiant etudiant = new Etudiant();
        	
        	etudiant.setMatricule(matricule);
        	etudiant.setNom(nom);
        	etudiant.setDateNaiss(dateNaiss);
        	etudiant.setLieuNaiss(lieuNaiss);
        	etudiant.setSexe(Sexe.valueOf(sexe.toUpperCase()));
        	etudiant.setTelephone(telephone);
        	etudiant.setEmail(email);
        	etudiant.setAdresse(adresse);
        	etudiant.setDiplomeEnEntree(diplomeEnEntree);
        	etudiant.setFiliere(filiere);
        	etudiant.setStatutPaiement(statutPaiement);

        	return etudiant;
        }

        return null;

    }

    public String getColumnValue(String[] record, Map<String, Integer> columnIndexMap, String columnName) 
    {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex != null && columnIndex < record.length) {
            return record[columnIndex].trim();
        }
        return null;
    }


    public Map<String, Integer> getColumnIndexMap(String[] header) 
    {
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            columnIndexMap.put(header[i], i);
        }
        return columnIndexMap;
    }

    public Map<String, Integer> getColumnIndexMap(Row headerRow) 
    {
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

    public Etudiant createEtudiantXLSX(Row row, Map<String, Integer> columnIndexMap) throws ParseException 
    {
        String matricule = getColumnValue(row, columnIndexMap, "Matricule");
        String nom = getColumnValue(row, columnIndexMap, "Nom");
        String prenom = getColumnValue(row, columnIndexMap, "Prenom");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // String date = getColumnValue(row, columnIndexMap, "Date_Naiss");
        // java.util.Date dates =format.parse(date);
        // Date dateNaiss=null;
        // String dateString = getColumnValue(row, columnIndexMap, "Date_Naiss");
        // if(dateString != null){
        // //     java.util.Date parsedDate =format.parse(dateString);
        // //  dateNaiss = new java.sql.Date(parsedDate.getTime());
        // // }
        // try {
        //     java.util.Date parsedDate = format.parse(dateString);
        //      dateNaiss = new java.sql.Date(parsedDate.getTime());
        //     // Utiliser la date convertie
        // } catch (ParseException e) {
        //     e.printStackTrace();
        //     // Gérer l'erreur d'analyse de la date
        // }}
        //new Date(2023, 02, 03);//new Date(dates.getTime());
        // String lieuNaiss = getColumnValue(row, columnIndexMap, "lieu_Naiss");
        String sexeValue = getColumnValue(row, columnIndexMap, "sexe");
        String telephone = getColumnValue(row, columnIndexMap, "Telephone");
        
        // String email = getColumnValue(row, columnIndexMap, "email");
        String adresse = getColumnValue(row, columnIndexMap, "Adresse");
        String diplomeEnEntree = getColumnValue(row, columnIndexMap, "Diplome En Entree");
        String codeFiliere = getColumnValue(row, columnIndexMap, "code Filiere");
        String paie = getColumnValue(row, columnIndexMap, "statut paiement");
        Double statutPaiement = Double.parseDouble(paie);// 50000.0;
        Filiere filiere = filiereRepository.findFirstByCode(codeFiliere);
        // System.out.println(sexeValue);
        Sexe sexe = null;
        if(sexeValue != null){
            if (sexeValue.equals(Sexe.MASCULIN.value())) {
                sexe = Sexe.MASCULIN;
            } else if (sexeValue.equals(Sexe.FEMININ.value())) {
                sexe = Sexe.FEMININ;
            } else if (sexeValue.equals(Sexe.AUTRE.value())) {
                sexe = Sexe.AUTRE;
            }}
        
        if(filiere == null){
            filiere = new Filiere();
            filiere.setCode(codeFiliere);
            filiereRepository.save(filiere);
        }

        if (nom != null  && matricule != null && sexeValue!= null && sexe != null) {
            // System.out.println(sexe);
            Etudiant etudiant= new Etudiant();
            etudiant.setMatricule(matricule);
            etudiant.setNom(nom);
            // etudiant.setDateNaiss(dateNaiss);
            // etudiant.setLieuNaiss(lieuNaiss);
            etudiant.setSexe(sexe/*Sexe.valueOf(sexe)*/);
            etudiant.setTelephone(telephone);
            // etudiant.setEmail(email);
            etudiant.setAdresse(adresse);
            etudiant.setDiplomeEnEntree(diplomeEnEntree);
            etudiant.setFiliere(filiere);
            etudiant.setStatutPaiement(statutPaiement);
            System.out.println(etudiant.getNom());
            etudiantRepository.save(etudiant);
            return etudiant;
        }

        return null;
    }

    public String getColumnValue(Row row, Map<String, Integer> columnIndexMap, String columnName) 
    {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                cell.setCellType(CellType.STRING); // Convertir la cellule en type chaîne de caractères
                //cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            }
        }
        return null;
    }

    public void exportExcel(OutputStream outputStream) throws IOException
    {
        List<Etudiant> etudiants = etudiantRepository.findAll();

        // Créer un nouveau classeur Excel
        Workbook workbook = new XSSFWorkbook();

        // Créer une feuille de calcul
        Sheet sheet = workbook.createSheet("person");

        // Créer une ligne pour les en-têtes de colonne
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("numero");
        headerRow.createCell(1).setCellValue("Matricule");
        headerRow.createCell(2).setCellValue("Nom et prenom");
        headerRow.createCell(3).setCellValue("Date Naissance");
        headerRow.createCell(4).setCellValue("Lieu Naissance");
        headerRow.createCell(5).setCellValue("Sexe");
        headerRow.createCell(6).setCellValue("Telephone");
        headerRow.createCell(7).setCellValue("email");
        headerRow.createCell(8).setCellValue("Adresse");
        headerRow.createCell(9).setCellValue("code Filiere");
        headerRow.createCell(10).setCellValue("Diplome En Entree");

        // Ajouter les données de la table
        int rowNum = 1;
        for (Etudiant etudiant : etudiants) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(etudiant.getId());
            row.createCell(1).setCellValue(etudiant.getMatricule());
            row.createCell(2).setCellValue(etudiant.getNom());
            row.createCell(3).setCellValue(etudiant.getDateNaiss());
            row.createCell(4).setCellValue(etudiant.getLieuNaiss());
            row.createCell(5).setCellValue(etudiant.getSexe().value());
            row.createCell(6).setCellValue(etudiant.getTelephone());
            row.createCell(7).setCellValue(etudiant.getEmail());
            row.createCell(8).setCellValue(etudiant.getAdresse());
            row.createCell(9).setCellValue(etudiant.getFiliere().getCode());
            row.createCell(10).setCellValue(etudiant.getDiplomeEnEntree());
        }

        // Écrire le classeur Excel dans un fichier
        try (OutputStream fileOut = outputStream) {
            workbook.write(fileOut);
        }

        // Fermer le classeur Excel
        workbook.close();
    }
}
