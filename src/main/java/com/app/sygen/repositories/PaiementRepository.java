package com.app.sygen.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Paiement;

public interface PaiementRepository extends AppRepository<Paiement, Long>
{
	List<Paiement> findByEtudiant(Etudiant etudiant);
}
