# Projet : API Foot Management (Scala) - M2 Programmation Modulaire

Ce projet consiste à concevoir et implémenter une API REST de gestion de football
(équipes, joueurs et matchs) en **Scala**, en s’appuyant sur une **spécification OpenAPI**
et une architecture **modulaire**.

L’API est générée automatiquement à partir d’OpenAPI, puis complétée par une
implémentation métier utilisant **Apache Pekko HTTP** et des **acteurs typed**.

---

## Objectifs pédagogiques

- Concevoir une API REST à partir d’OpenAPI
- Générer automatiquement un serveur Scala
- Implémenter la logique métier de manière modulaire
- Utiliser les acteurs (Pekko Typed) pour la gestion de l’état
- Sérialiser / désérialiser des données JSON
- Structurer proprement un projet Scala avec sbt

---

## Technologies utilisées

- **Scala 2.12**
- **sbt**
- **Apache Pekko** (Actor Typed, HTTP)
- **Spray JSON**
- **OpenAPI Generator**
- **curl** (tests des endpoints)

---

## Structure du projet

```

M2-API-foot-management-Scala/
├── football-api.yaml        # Spécification OpenAPI
├── impl/                    # Implémentation métier
├── output_directory/        # Code serveur généré OpenAPI
├── README.md                # Documentation du projet
├── sequence-diagram.puml   # Diagramme de séquence UML

```

### Détails des dossiers

- **`football-api.yaml`**  
  Spécification OpenAPI décrivant les endpoints REST.

- **`output_directory/`**  
  Code serveur généré automatiquement à partir de la spécification OpenAPI
  (routes, modèles, contrôleurs, acteurs).

- **`impl/`**  
  Implémentation métier :
  - logique applicative
  - marshallers JSON
  - intégration avec le serveur généré

---

## Compilation

## Makefile

Depuis la racine du projet :

```bash
make clean     # nettoie les fichiers générés
make compile   # compile output_directory puis impl
make run       # lance le serveur (port 8080)


OU

### 1. Compilation du code généré
```bash
cd output_directory
sbt clean compile
```

### 2. Compilation de l’implémentation

```bash
cd ../impl
sbt clean compile
```

---

## Exécution du serveur

```bash
cd impl
sbt run
```

Le serveur démarre sur :

```
http://localhost:8080
```

> La route `/` n’est pas définie volontairement.
> Les endpoints sont accessibles via `/teams`, `/players` et `/matches`.


---

## Endpoints de l’API

### Teams

* `GET /teams` : liste des équipes
* `POST /teams` : créer une équipe
* `DELETE /teams/{id}` : supprimer une équipe

Exemple :

```bash
curl -X POST http://localhost:8080/teams \
  -H "Content-Type: application/json" \
  -d '{"id":"t1","name":"PSG","city":"Paris"}'
```

---

### Players

* `GET /players` : liste des joueurs
* `POST /players` : créer un joueur (le `teamId` doit exister)
* `DELETE /players/{id}` : supprimer un joueur

### Exemples :

1. Vérif serveur + listes vides :

```bash
curl -i http://localhost:8080/teams
curl -i http://localhost:8080/players
curl -i http://localhost:8080/matches
```

2. Création équipe :

```bash
curl -i -X POST http://localhost:8080/teams \
  -H "Content-Type: application/json" \
  -d '{"id":"t1","name":"PSG","city":"Paris"}'
```

3. Vérification liste :

```bash
curl -i http://localhost:8080/teams
```

4. (Optionnel) Création joueur :

```bash
curl -i -X POST http://localhost:8080/players \
  -H "Content-Type: application/json" \
  -d '{"id":"p1","name":"Mbappe","position":"ATT","teamId":"t1"}'
```

**DELETE n’est pas implémenté dans `Api.scala`** 

---

### Matches

* `GET /matches` : liste des matchs
* `POST /matches` : créer un match
* `DELETE /matches/{id}` : supprimer un match

---

## Génération OpenAPI (optionnel)

Le code serveur peut être régénéré à partir de la spécification :

```bash
java -jar openapi-generator-cli.jar generate \
  -i football-api.yaml \
  -g scala-akka-http-server \
  -o output_directory
```

---

## Remarques

* Les dossiers `target`, `.bsp`, `.metals` et fichiers temporaires sont générés
  automatiquement et exclus du versionnement via `.gitignore`.
* Le dossier `output_directory` contient du **code généré**, conservé volontairement
  pour illustrer l’intégration OpenAPI.

---

## Projet académique

Master 2 — Programmation modulaire
API Foot Management en Scala

### Fait par :
Abdessamad 
Abdelhakim Mennina
Soumia Meddas
Nivetha Vijayatharan
Myriam Milha