package com.app.sygen.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.sygen.entities.Filiere;
// import com.app.sygen.entities.filiere;
import com.app.sygen.repositories.FiliereRepository;
import com.app.sygen.services.FiliereService;

@Controller
@RequestMapping("/filiere")
public class FiliererController {

    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private FiliereService filiereService;

    @PostMapping("/import")
    public String importFIle(@RequestParam("file") MultipartFile file) {
        try {
            List<Filiere> importedfiliere = new ArrayList<>();
            // if (file.getOriginalFilename().endsWith(".xlsx")) {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next(); // Lecture de la première ligne (titres des colonnes)
            Map<String, Integer> columnIndexMap = filiereService.getColumnIndexMap(headerRow);
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Filiere filiere = filiereService.createFiliereXLSX(row, columnIndexMap);

                if (filiere != null) {
                    // filiere.setUser(user);
                    importedfiliere.add(filiere);
                } else {
                    return "redirect:/etudiant/?status=BadStructure";
                }
            }
            workbook.close();

            filiereRepository.saveAll(importedfiliere);
            return "redirect:/etudiant/?status=success";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/etudiant/?status=fail";
        }
        // TODO: handle exception
    }

}
