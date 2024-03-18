package com.app.sygen.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.Participe;
import com.app.sygen.entities.Filiere;


public interface ParticipeRepository extends AppRepository<Participe, Long>
{
	 Participe findByMatricule(String matricule);

     List<Participe> findByEtudiant(Etudiant etudiant);

     List<Participe> findByEvaluation(Evaluation evaluation);
     List<Participe> findByEvaluationOrderByIdDesc(Evaluation evaluation);
     List<Participe> findByAnneeImportationAndEvaluationOrderByNomEtudiantAsc(String Annee, Evaluation evaluation);
     List<Participe> findByEtudiantAndAnneeImportationAndEvaluationOrderByNomEtudiantAsc(Etudiant etudiant, String annee, Evaluation evaluation);


     List<Participe> findFirstByEvaluationIn(List<Evaluation> evaluations);
     Optional<Participe>  findFirstByEvaluation(Evaluation evaluation);
     // Participe findByEvaluation(Evaluation evaluation);
     List<Participe> findByEvaluation_TypeEvalAndEvaluation_Ue_Code(String TypeEval, String Code);
     List<Participe> findByEvaluation_TypeEvalAndEvaluation_Ue_CodeAndEvaluation_Ue_Filiere_Code(String TypeEval, String Ue_Code, String Filiere_Code);


     Participe findByAnonymat(Integer anonymat);
     Participe findFirstById(int id);

     List<Participe>findByNomEtudiantContainingIgnoreCase(String nom);
     Participe findByNomEtudiant(String nom);
     List<Participe> findByNomEtudiantAndMatricule(String nom, String matricule);
     //int findLastAnonymat();
     @Query("SELECT p.anonymat FROM Participe p WHERE p.id = (SELECT MAX(p2.id) FROM Participe p2)")
     Long findLastAnomymat();
     default Integer generateAnonymat(){
     Long lastAnonymat=findLastAnomymat();
     if(lastAnonymat==null){
          lastAnonymat = 0L;
     }
     Long newAnonymat=lastAnonymat +1;
     // Integer 
          return newAnonymat.intValue();
     }

     // @Query("SELECT * FROM Participe ORDER BY id DESC")
     // List<Participe> findAllOrder();
     

}