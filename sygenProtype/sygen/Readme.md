# Système de Gestion des Notes (SyGeN) - Université de Yaoundé I

Ce dépôt est destiné à accueillir le code source de l'application de gestion des notes de l'Université de Yaoundé I par l'équipe de M Aboubakar Bouba.

## Equipes
* Superviseur: Mr Aboubakar Bouba
* Intégrateur (Lead Dev): Dimitri Sitchet
* Développeurs
    * Cherif
    * Jerry
	* Lorene
 	* Anna
  * Balekamen  
   
## Stack
Ce projet est développé avec le framework Spring Boot 3.1, utilise Thymeleaf comme moteur de template et tourne sur une base de données MySql

## Installation
Commencez par cloner le projet sur votre PC
```bash
git clone https://github.com/mr-about-team/sygen.git
```
Rendez-vous sur la branche correspondant à votre module (exemple: `git checkout -b corrections`)
**Vous ne ferez des commits et push que sur votre branche**

## Cycle de développement
Avant de commencer à travailler, chacun doit se rassurer au préalable d'avoir récupérer les travaux de ses collaborateurs afin que toute l'équipe ai la même base de code. De ce fait, le workflow quotidien doit être le suivant
```bash
git checkout {{branch}} // branch est votre branche
git pull origin devs
git merge devs {{branch}}
// commit 1
// commit 2
git push origin {{branch}}
```
Une fois le push fait, le développeur devra faire une **pull request** sur GitHub afin qu'on puisse fusionner ses travaux.
Chaque soir (18h), le Lead Dev fusionnera toutes les differentes branches ayant soumis des *pull request* au sein de la branche **devs** et testera en locale. Si tout est ok, les travaux seront mis sur la branche principale (main) pour déploiement 

## Guide de développement
LES REGLES SUIVANTES DOIVENT SCRUPULEUSEMENT ETRE RESPECTEES

1. Chaque responsable de module doit créer une branche correspondant à son module pour effectuer son travail.
Par exemple, Jerry qui s'occupe des délibération doit avoir une branche **deliberation** dans laquelle il fera ses modifications en rapport avec son module  `git checkout -b deliberation`
2. Aucun push ne doit être fait sur la branche principale. Les différents développeurs devront faire des push sur leurs branches et faire une **pull request** sur la branche **dev**  
3. Les commits sur un nombre important de fichiers sont proscits. Ils doivent être ponctuels et spécifique à une activité précise. Par ailleurs les commits doivent être fait avec la syntaxe suivante **{{badge}}: description**. La description du badge doivent être précise et en rapport avec l'action effectuée. {{badge}} est un terme générique qui peut avoir les valeurs suivantes:
* **feat** : Utilisé lorsqu'une nouvelle fonctionnalité a été ajoutée
* **fix** : Utilisé lorsqu'un bug a été corrigé
* **patch** : Utilisé lorsqu'on a apporté une optimisation d'une fonction sans ajout de fonctionnalité ou correction de bug et sans que sa n'impacte sur le reste du code (lors du remplacement d'un if/else par une condition ternaire par exemple)
* **chore** : Utilisé lorsque la structure globale d'un grand ensemble a été fortement modifiée
* **cs-fix** : Utilisé lorsqu'on fait une correction du style de code (par exemple quand on change **ma_variable** par **maVariable**)
* **docs**: Utilisé quand on fait une modification sur le guide d'utilisation (documentation). Un wiki sera créer sur ce dépôt de chaque responsable documentera chacune des fonctionnalités de son module.
Vous pouvez vous referez à la page des commits (https://github.com/mr-about-team/sygen/commits/devs) pour avoir un aperçu 
4. Les règles de codage suivantes doivent être respecter pour une meilleure intégration.
* Le nom de classe doivent être en *pascal case* et doivent être sufixé de *Controller* c'est-à-dire **MaClasseController**
* Le nom des propriétés et méthodes doivent être en *camel case* c'est-à-dire **maVariable** ou **maMethode**
* **L'utilisation de lombock est proscrit**. Toutes les entités (modèles) doivent définir tous les getters et setters. Les constructeurs paramétrés sont également proscris.
* L'utilisation de l'ioc est recommandée c'est-à-dire l'utilisation de **@Autowired** de ce fait, pas besoin de créer un constructeur qui injecte un repository par exemple
* Les traitements ne se font pas directement au niveau des contrôleurs mais dans les services. Les contrôleurs appellent les services appropriés pour les traitements complexes.
* Pour ce qui est des routes, chaque contrôleur doit avoir un chemin de base et les méthodes doivent avoir des routes nommées conformement aux spécifications REST. Les méthodes doivent être documentées (au moins le rôle de chaque méthode doit être mis). Prenons par exemple le cas du contrôleur *etudiant*, on aura alors un truc du genre:
```java 
package com.app.sygen;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController 
{
    @Autowired 
	private EtudiantService etudiantService;


	/**
	 * Liste des etudiants
	 */
	@GetMapping("/")
	public String index(Model model)
	{
		// code
	}

	/**
	 * Details d'un etudiant
	 */
	@GetMapping("/{id}")
	public String show(@PathVariable("id") final Long id, Model model)
	{
		// code
	}

	/**
	 * Formulaire de creation d'etudiant
	 */
	@GetMapping("/create")
	public String create(Model model)
	{
		// code
	}

	/**
	 * Traitement de la creation d'etudiant
	 */
	@PostMapping("/create")
	public String save(Model model)
	{
		// code
	}

	/**
	 * Formulaire de la modification d'etudiant
	 */
	@GetMapping("/edit/{id}")
	public String create(@PathVariable("id") final Long id, Model model)
	{
		// code
	}

	/**
	 * Traitement de la modification d'etudiant
	 */
	@PostMapping("/edit/{id}")
	public String update(Model model)
	{
		// code
	}

	/**
	 * Suppression d'etudiant
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") final Long id, Model model)
	{
		// code
	}
}
```

* Au niveau des vues, le dossier *template*, doit être structurer de cette manière:
    - layout: c'est le dossier pour le gabarit de la page (on a un fichier **default.html** pour le gabarit global, **header.html**, **footer.html**, et **sidebar.html** pour le menu)
    - auth: C'est le dossier des pages d'authentification (connexion, mot de passe oublié, inscription) 
    - {{pages}}: nom générique représentant une entité. par exemple, on aura un dossier *enseignant*, *etudiant*, *filiere* etc. Chaqu'un de ces dossiers doivent avoir au moins 4 fichiers (**index.html** pour la liste d'éléments (exemple liste des enseignants), **show.html** pour afficher les détails d'un élément, **create.html** pour le formulaire de création d'un élément, **edit.html** pour le formulaire d'édition d'un élément)
