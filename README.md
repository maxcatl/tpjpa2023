# TAA - TP3 Spring - Maxime Le Gal
Ce projet permet de créer des rendez-vous entre un élève et un professeur. Ces rendez-vous possèdent une durée avec une date de début et de fin. Les données sont stockées dans une base de données mysql grâce à JPA. Dans ce TP, il est possible de manipuler les données en base de données à l'aide de requêtes HTTP Rest. Tous les appels à l'API sont loggés dans la console via un aspect.

Le modèle et les DAO utilisés sont presque les mêmes que lors du TP1 avec l'ajout de champs obligatoires pour les utilisateurs.

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
Contrairement aux précédents TP, un utilisateur doit maintenant obligatoirement posséder un nom, prénom, adresse mail pour pouvoir être enregistré dans la base de données.

### `Eleve`
La classe `Eleve` hérite de `Utilisateur` et donc de tous ses attributs.  
Elle possède en plus un attribut classe(`String`) qui correspond à une classe ou formation.

### `Professeur`
La classe `Professeur` hérite de `Utilisateur` et donc de tous ses attributs.  
Elle possède en plus un attribut spécialité(`String`) qui correspond à une spécialisation de l'enseignant. Cet attribut est désormais obligatoire

### `RDV`
La classe `RDV` correspond virtuellement à un rendez-vous. Ce rendez-vous a comme propriété un élève(`Eleve`), un professeur(`Professeur`), une date de début(`Date`), une date de fin(`Date`) ainsi qu'un lieu(`String`).  
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

Par défaut, il n'y a pas de données dans la base de données, mais il est possible d'en rajouter en exécutant la classe `JpaTest` qui s'utilise comme dans le TP1. L'exécution se passera sans erreurs si les tables viennent d'être créées et sont vides (ou pas encore créées) car sinon les id risquent de différer.


### Utilisation de l'API Rest
La partie REST permet de pouvoir accéder à la base de données par le biais de requêtes HTTP. L'API met à disposition un certain nombre d'URL permettant d'accéder aux données, de les filtrer, d'en ajouter de nouvelles, modifier des données existantes ou de les supprimer.

Chaque URL commence par le nom de la ressource à laquelle on veut accéder (eleve, professeur, rdv). Les actions possibles sont sélectionnées en fonction du type de requête HTTP (GET, PUT, DELETE), ainsi que des paramètres passés dans l'URL (id) ou dans le corps de la requête (en XML ou JSON). Les fonctions GET retournent une réponse contenant un corps en XML ou JSON lorsqu'il n'y a pas d'erreur. Sinon un code HTTP est renvoyé indiquant l'erreur.  
Il est possible d'accéder à une description de l'API de type Swagger et de l'essayer en se connectant à l'URL `http://localhost:8080/swagger-ui/index.html` par défaut.

Pour lancer le serveur REST, il suffit d'exécuter la classe `SpringApplication` du package `fr.istic` en ayant préalablement mis en route la base de données. Ensuite l'API est disponible à partir de l'url `localhost:8080` par défaut.

Concernant la création de rendez-vous, il est possible de ne pas mettre un éléve ou un professeur avec toutes ses valeurs dans la requête, mais plutôt seulement avec son id ou son adresse email. Il est donc possible d'avoir des corps de requêtes comme celles-ci

```json
{
    "id": 1,
    "dateDebut": "2023-11-07T18:04:11.000+00:00",
    "dateFin": "2023-11-07T18:45:11.429+00:00",
    "professeur": {
        "email": "olivier.barais@univ-rennes.fr"
    },
    "eleve": {
        "id": 2
    },
    "lieu": "Istic"
}
```
Dans ce cas, on récupère le professeur par son adresse mail et l'élève par son ID. Dans le cas où l'élève ou le professeur n'est pas trouvé dans la base de données, le programme tentera de le créer. Si un des champs obligatoire est vide, le rendez-vous ne sera pas créer et une erreur sera retournée. S'il manque l'élève ou le professeur, le rendez-vous ne sera pas créé.