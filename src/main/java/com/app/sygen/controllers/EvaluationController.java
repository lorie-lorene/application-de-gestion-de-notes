package com.app.sygen.controllers;

// public class EvaluationController {
    
// }


// package com.app.sygen.controller;
import org.hibernate.internal.util.type.PrimitiveWrapperHelper.IntegerDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.Ue;
import com.app.sygen.repositories.EvaluationRepository;
import com.app.sygen.repositories.UeRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
 public class EvaluationController {
    @Autowired
    private EvaluationRepository exam;

    @Autowired
    private UeRepository ue;

    @GetMapping("/exam/new")
    public String showExamForm(Model model){

        List<Ue> listue=ue.findAll();
        model.addAttribute("listue",listue);
        model.addAttribute("exams",new Evaluation());
        return "examens/index";
    }
    @PostMapping("/exam/save")
    public String saveExam(Evaluation exams){
        exam.save(exams);

        return "redirect:/exam";
    }

    @GetMapping("/exam")
    public String listExam(Model model){
        List<Evaluation> listExam=exam.findAll();
        model.addAttribute("listExam",listExam);
        return "examens/liste_exam";
    }
    @GetMapping("/exam/edit/{idEval}")
    public String editForm(@PathVariable("idEval")Long id, Model model){
        Evaluation exams= exam.findById( id).get(); 
        model.addAttribute("exams", exams);
        List<Ue> listue=ue.findAll();
        model.addAttribute("listue", listue);
        return "examens/index";
    }
    @GetMapping("/exam/delete/{idEval}")
    public String delete(@PathVariable ("idEval") Long id, Model model){
        exam.deleteById(id);
        return "redirect:/exam";
    }
    @ModelAttribute("nombres")
        public List<Integer> getNombres(){
            List<Integer> nombres = new ArrayList<>();
            nombres.add(1);
            nombres.add(2);
            nombres.add(3);
            nombres.add(4);
            return nombres;

        }

    @ModelAttribute("types")
    public List<String > getTypes() {
        List<String> types=new ArrayList<>();
        types.add("EE");
        types.add("CC");
        types.add("TP");
        return types;
    }
    
}