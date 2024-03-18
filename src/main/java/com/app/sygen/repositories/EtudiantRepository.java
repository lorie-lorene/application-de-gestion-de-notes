package com.app.sygen.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.sygen.entities.Etudiant;
import com.app.sygen.entities.Filiere;

public interface EtudiantRepository extends AppRepository<Etudiant, Long>
{
	/**
	 * Recherche d'un etudiant a partir de son matricule
	 */
	Etudiant findByMatricule(String matricule);

	/**
	 * Recherche d'un etudiant a partir de son id
	 */
	// Etudiant findById(Long id);
	
	/**
	 * Liste des etudiants d'une filiere
	 */
	@Query(value = "SELECT e FROM Etudiant e WHERE e.filiere.id = :filiere", nativeQuery = true)
	List<Etudiant> findByFiliere(@Param("filiere") Long id);
	
	/**
	 * Liste des etudiants d'une filiere
	 */
	List<Etudiant> findByFiliere(Filiere filiere);

    @Query(value = "SELECT SUM(p.Montant) FROM Paiements p JOIN p.etudiant e WHERE e.id = :idEtudiant", nativeQuery = true)
    Double showStatus(@Param("idEtudiant") Long idEtudiant);
	List<Etudiant> findByFiliereAndStatutPaiementGreaterThanEqualOrderByNomAsc(Filiere filiere, Double statutPaiement);
	List<Etudiant> findByFiliereAndStatutPaiementIgnoreCaseIsLessThanOrderByNomAsc(Filiere filiere, Double statutPaiement);
	Etudiant findByFiliereAndMatricule(Filiere filiere, String matricule);

	// List<Etudiant> findByMatriculeOrderByNom(String matricule);
	List<Etudiant> findByNomContaining(String nom);
	List<Etudiant>findByNomContainingIgnoreCase(String nom);
	Etudiant findByNom(String nom);
	// List<Etudiant> findByFiliereAndStatutPaiementGreaterThanEqualOrderByNomAsc(Filiere filiere, Double statutPaiement);
	// List<Etudiant> findByFiliereAndStatutPaiementIgnoreCaseIsLessThanOrderByNomAsc(Filiere filiere, Double statutPaiement);
	// Etudiant findByFiliereAndMatricule(Filiere filiere, String matricule);
	Etudiant findByMatriculeAndNom(String matricule, String nom);
	List<Etudiant> findByMatriculeOrderByNom(String matricule);
}
