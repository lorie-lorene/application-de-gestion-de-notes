package com.app.sygen.repositories;

import com.app.sygen.entities.SeanceDeliberation;
import com.app.sygen.entities.Statut;
import com.app.sygen.entities.Filiere;
import com.app.sygen.entities.Jury;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StatutRepository extends AppRepository<Statut, Long>{
    List<Statut> findByJurys(Jury jurys);
    // List<Filiere> findDistinctFiliereByStatutIn(List<Statut> statuts);
    @Query("SELECT DISTINCT s.filiere FROM Statut s WHERE s.statut IN :statuts")
    List<Filiere> findFilieresByStatuts(@Param("statuts") List<Statut> statuts);

    List<Statut> findByFiliere(Filiere filiere);
    List<Statut> findByJurysIn(List<Jury> jurys);
}
