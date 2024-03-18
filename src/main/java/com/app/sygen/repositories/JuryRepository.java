package com.app.sygen.repositories;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Jury;
import com.app.sygen.entities.Users;
import java.util.List;
import com.app.sygen.entities.Statut;


@Repository
public interface JuryRepository extends AppRepository<Jury, Long> {

    Jury findByUser(Users user);
    // List<Jury> findByAttribut(List<Statut> attribut);
    List<Jury> findByAttributIn(List<Statut> attributs);

}
