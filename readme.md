# TAA - TP4 Keycloak - Maxime Le Gal
Ce projet permet de créer des rendez-vous entre un élève et un professeur. Ces rendez-vous possèdent une durée avec une date de début et de fin. Les données sont stockées dans une base de données mysql grâce à JPA. Dans ce TP, il est possible de manipuler les données en base de données à l'aide de requêtes HTTP Rest. Tous les appels à l'API sont loggés dans la console via un aspect. Toutes les requêtes demandent d'être connecté au service Keycloak en passant un token JWT dans la requête. Certaines requêtes nécessitent un certain niveau de privilèges pour être autorisées.

Le modèle et les DAO utilisés sont les mêmes que dans le TP3 Spring.

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

## Prérequis
### Base de données
Le code actuel utilise une base de données mysql en localhost. La base de données utilisée s'appelle JPA_one.  
Il est possible de modifier cette adresse en modifiant la propriété value de la ligne
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost/JPA_one" />
```
dans `src/main/ressources/META-INF/persistence.xml`

Par défaut, il n'y a pas de données dans la base de données, mais comme dans le TP3, il est possible de rajouter des données en effectuant une requête HTTP `PUT` avec l'URL `localhost:8080/loadSampleData`. Il est préférable, mais pas obligatoire d'appeler cette requête lorsqu'il n'y a pas encore de données enregistrées, sinon il pourrait y avoir un conflit, notamment en cas de conflit sur l'adresse email. Il est nécessaire d'avoir le privilège `ADMIN` pour exécuter cette requête, sinon un code `401` est retourné. En cas d'erreur, la réponse comporte le code `500`, sinon elle comporte le code `201`.

## Keycloak
Pour utiliser cette API Rest, il est nécessaire de mettre en place un serveur Keycloak afin d'obtenir un token JWT pour certifier une connection et un niveau de privilège. Le royaume utilisé dans mon cas est SpringBootRealm et l'utilisateur. Il existe 4 niveaux de privilèges (rôles Keycloak) permettant d'accéder à différentes ressources :
* `ADMIN`
* `TEACHER`
* `STUDENT`
* `USER`

Pour récupérer un token JWT avec le rôle `STUDENT` dans ma configuration (nom du royaume : `springbootrealm`, nom du client : `springbootapp`), il suffit d'utiliser la commande :

```curl
curl --location 'localhost:8080/realms/springbootrealm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=eleve' \
--data-urlencode 'password=eleve' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=springbootapp' \
--data-urlencode 'client_secret=QfK9YeDdVjeB72ZLQs1FG3DNS7bYLRGo' \
--data-urlencode 'scope=openid'
```

Cette commande renvoie notamment, si la connexion est un succès, un `access_token`, qu'il faut insérer dans le header de la requête à l'API après `Authorization: Bearer`. Si le token est valide (correct, non expiré, attribué à un utilisateur avec un niveau de privilège suffisant), la requête s'exécutera, sinon un code `401` est retourné.

### Utilisation de l'API Rest
La partie REST permet de pouvoir accéder à la base de données par le biais de requêtes HTTP. L'API met à disposition un certain nombre d'URL permettant d'accéder aux données, de les filtrer, d'en ajouter de nouvelles, modifier des données existantes ou de les supprimer.

Chaque URL commence par le nom de la ressource à laquelle on veut accéder (eleve, professeur, rdv). Les actions possibles sont sélectionnées en fonction du type de requête HTTP (GET, PUT, DELETE), ainsi que des paramètres passés dans l'URL (id) ou dans le corps de la requête (en XML ou JSON). Les fonctions GET retournent une réponse contenant un corps en XML ou JSON lorsqu'il n'y a pas d'erreur. Sinon un code HTTP est renvoyé indiquant l'erreur.  
Il est possible d'accéder à une description de l'API de type Swagger et de l'essayer en se connectant à l'URL `http://localhost:8084/swagger-ui/index.html` par défaut.

Pour lancer le serveur REST, il suffit d'exécuter la classe `Application` du package `fr.istic` en ayant préalablement mis en route la base de données et Keycloak. Ensuite l'API est disponible à partir de l'url `localhost:8084` par défaut.

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