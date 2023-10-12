# TAA - TP1 JPA - Maxime Le Gal
Ce projet permet de créer des rendez-vous entre un élève et un professeur. Ces rendez-vous possèdent une durée avec une date de début et de fin. Les données sont stockées dans une base de données mysql grâce à JPA.

## modèle
Le modèle comprend 3 classes concrètes : 
* `Eleve`
* `Professeur`
* `RDV`

Les classes `Eleve` et `Professeur` descendent d'une classe abstraite commune `Utilisateur`.

### `Utilisateur`
La classe `Utilisateur` est une classe abstraite qui correspond virtuellement à un utilisateur. Cet utilisateur possède un nom (`String`), un prénom(`String`) et une adresse mail(`String`).  
Il possède également un Id(`long`) qui est unique.  
L'adresse mail de chaque utilisateur est aussi unique.

### `Eleve`
La classe `Eleve` hérite de `Utilisateur` et donc de tous ses attributs.  
Elle possède en plus un attribut classe(`String`) qui correspond à une classe ou formation.

### `Professeur`
La classe `Professeur` hérite de `Utilisateur` et donc de tous ses attributs.  
Elle possède en plus un attribut spécialité(`String`) qui correspond à une spécialisation de l'enseignant.

### `RDV`
La classe `RDV` correspond virtuellement à un rendez-vous. Ce rendez-vous permet a comme propriété un élève(`Eleve`), un professeur(`Professeur`), une date de début(`Date`), une date de fin(`Date`) ainsi qu'un lieu(`String`).  
`RDV` possède également un attribut duree(`Duration`) qui est calculé en fonction de la date de début et de fin du RDV et qui ne peut pas être modifié directement.  
Il se trouve aussi un attribut Id(`long`) qui est unique.

## Utilisation
### Prérequis
Le code actuel utilise une base de données mysql en localhost. La base de données utilisée s'appelle JPA_one.  
Il est possible de modifier cette adresse en modifiant la propriété value de la ligne 
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost/JPA_one" />
```
dans `src/main/ressources/META-INF/persistence.xml`

### Lancement de la démo prédéfinie
Un ensemble de tests sont prédéfinis effectuant diverses actions comme la création d'entités, la modification de celles-ci, la suppression ainsi que des tests voués à échouer comme la tentative d'ajouter un doublon. Ces tests se trouvent dans le fichier `src/main/java/fr/istic/jpa/JpaTest.java`. Des commentaires se trouvent à l'intérieur du fichier pour expliquer ce que le code fait. Lors de l'execution, les requêtes JPQL sont affichées dans la console.

Il est également possible d'effectuer des tests personnalisés en les implémentant directement dans la fonction `main` du fichier `JpaTest.java`