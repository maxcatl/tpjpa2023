# TAA - TP2 Servlet & REST - Maxime Le Gal
Ce projet permet de créer des rendez-vous entre un élève et un professeur. Ces rendez-vous possèdent une durée avec une date de début et de fin. Les données sont stockées dans une base de données mysql grâce à JPA. Dans ce TP, il est possible de manipuler les données en base de données depuis des pages webs ou des requêtes HTTP.

Ce TP est composé de deux parties :
- Une première partie sur les servlets ;
- Une deuxième partie avec une API Rest.

Le modèle et les DAO utilisés sont les mêmes que lors du TP1.

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

Par défaut, il n'y a pas de données dans la base de données, mais il est possible d'en rajouter en exécutant la classe `JpaTest` qui s'utilise comme dans le TP1.

### 1. Partie servlets
La partie servlet permet de pouvoir accéder à une interface web permettant d'intéragir avec la base de données en lecture et en écriture. Pour pouvoir accéder au serveur web, il faut exécuter `jetty:run`, ce qui va lancer le serveur web, puis se rendre sur le lien [`localhost:8080/`](http://localhost:8080) qui mène à la page d'accueil du site web. Trois liens sont alors disponibles pour intéragir avec la base.

- Le premier permet d'ajouter un professeur ou un élève à la base de données en saisissant ses informations dans un formulaire.
- Le deuxième permet de créer un rendez-vous, en saisissant un élève, un professeur, une date de début et de fin et un lieu. S'il n'y a pas de professeur ou d'élève disponible dans la base, il ne sera pas possible de saisir un rendez-vous.
- Le troisième permet d'obtenir des ressources de la base (élève,  professeur, rendez-vous). Pour chaque ressource, il est possible d'obtenir la liste de tous les éléments de cette ressource, ou d'obtenir un élément spécifique à l'aide de son id.

### 2. Partie REST api
La partie REST permet de pouvoir accéder à la base de données par le biais de requêtes HTTP. L'API met à disposition un certain nombre d'URL permettant d'accéder aux données, de les filtrer, d'en ajouter de nouvelles, modifier des données existantes ou de les supprimer.

Chaque URL commence par le nom de la ressource à laquelle on veut accéder (eleve, professeur, rdv). Les actions possibles sont sélectionnées en fonction du type de requête HTTP (GET, PUT, DELETE), ainsi que des paramètres passés dans l'URL (id) ou dans le corps de la requête (en XML ou JSON). Les fonctions GET retournent une réponse contenant un corps en XML ou JSON lorsqu'il n'y a pas d'erreur. Sinon un code HTTP est renvoyé indiquant l'erreur.  
Il est possible d'accéder à une description de l'API de type Swagger et de l'essayer en se connectant à l'URL `localhost:8080/api` par défaut.

Pour lancer le serveur REST, il suffit d'exécuter la classe `RestServer` du package `fr.istic.rest` en ayant préalablement mis en route la base de données. Ensuite l'API est disponible à partir de l'url `localhost:8080` par défaut.