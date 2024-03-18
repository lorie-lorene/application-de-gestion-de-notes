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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.sygen.entities.Ue;
import com.app.sygen.repositories.UeRepository;
import com.app.sygen.services.UeService;

@Controller
@RequestMapping("/ue")
public class UeController {
    @Autowired
    UeRepository repo;

    @Autowired
    UeService ueService;

    @GetMapping("/ueList")
    public String listue(Model model){
        List<Ue> listue=repo.findAll();
        model.addAttribute("listue",listue);
        return "ueList";
    }

    @GetMapping("/ueList/new")
    public String shownewForm(Model model){
        model.addAttribute("category",new Ue());

        return "ue_form";
    }
    @PostMapping("/categories/save")
        public String saveue(Ue category){
            repo.save(category);

            return "redirect:/ueList";
        }

    @ModelAttribute("credits")
    public List<Integer> getCredits(){
        List<Integer> credits= new ArrayList<>();
        credits.add(3);
        credits.add(6);
        return credits;
    }

    @PostMapping("/import")
    public String importFIle(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<Ue> importedue = new ArrayList<>();
            // if (file.getOriginalFilename().endsWith(".xlsx")) {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next(); // Lecture de la première ligne (titres des colonnes)
            Map<String, Integer> columnIndexMap = ueService.getColumnIndexMap(headerRow);
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Ue ue = ueService.createUeXLSX(row, columnIndexMap);

                if (ue != null) {
                    // Ue.setUser(user);
                    importedue.add(ue);
                } else {
                    return "redirect:/etudiant/?status=BadStructure";
                }
            }
            workbook.close();

            repo.saveAll(importedue);
            return "redirect:/etudiant/?status=success";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/etudiant/?status=fail";
        }
        // TODO: handle exception
    }
}
