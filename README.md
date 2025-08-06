Encaisse Valeur & Carthago Dashboard
A complete system to compare Encaisse Valeur (EV) and Carthago bank files, check internal consistency, and visualize everything in a modern dashboard.
Backend: Java Spring Boot + Oracle DB
Frontend: Angular (SPA)

-- Features
Import EV (Encaisse Valeur) and Carthago files

Parse Carthago entête/détails (fixed positions)

Store all data in Oracle DB

Automated comparison:

Internal (entête vs détails Carthago)

EV ↔ Carthago

Dashboard:

Concordance rates

Graphs: Pie (répartition dépôts/rejets), Bar (montants)

Anomaly table

-- Project Structure
/backend         # Spring Boot API (Java 17+, Oracle DB)
  /src
  /...
/frontend        # Angular dashboard (ng v16+)
/README.md
🛠Backend (Spring Boot)
Main Entities & Tables
Table	Description
FICHIER	Encaisse Valeur files
CARTHAGO	Raw Carthago files
CARTHAGO_DETAIL	Parsed cheques (per Carthago file)
CTR	Comparison results

See doc for detailed schema & columns.

Main API Endpoints
Endpoint	Method	Description
/api/fichier/upload	POST	Import EV file
/api/carthago/upload	POST	Import Carthago file
/api/ctr	GET	List all comparisons (CTR)
/api/carthago/names	GET	List all Carthago files
/api/carthago/stats/entete	GET	Global entête stats

Caractéristiques techniques
Parsing Carthago : positions fixes pour entête & détail

Calcul concordance :

Interne : compare nombre & montant entête/détails

EV ↔ Carthago : compare EV et parsing Carthago

Gestion des erreurs : upload, parsing, DB

DB Setup
Oracle 19c+ (tables à créer selon le modèle fourni)

Sequences pour auto-increment (IDs)

-- Frontend (Angular)
Dashboard principal :

Stats de concordance (taux global, par EV/Carthago)

Graphiques : camembert, histogramme, etc.

Tableau des anomalies (lignes CTR non-conformes)

Upload fichiers (drag & drop ou formulaire)

API consumption : Axios/HttpClient sur endpoints backend

UI : responsive, moderne, focus sur la lisibilité

Custom theming possible

-- Workflow de traitement
Importer un EV (fichier .csv/.txt) via /api/fichier/upload

Importer un Carthago (fichier .DATA) via /api/carthago/upload

Parsing Carthago :

Extraction entête (positions fixes)

Extraction détails (positions fixes)

Stockage en base (table CARTHAGO_DETAIL)

Comparaison :

Interne Carthago (entête ↔ détails)

EV ↔ Carthago (via table CTR)

Dashboard :

Visualisation stats, anomalies, historique

 Quickstart
Backend

cd backend
# Configure your Oracle DB in src/main/resources/application.properties
./mvnw spring-boot:run
Frontend
cd frontend
npm install
ng serve
# App available at http://localhost:4200
-- Example SQL Inserts

-- Insert an EV file
INSERT INTO FICHIER (TYPE_FICHIER, CODE_VALEUR, SENS, COD_EN, NOM_FICHIER, NATURE_FICHIER, FORMAT_FICHIER, NOMBRE, MONTANT, USER_ID, CREATED_AT)
VALUES ('cheque', '30', 'emis', '21', 'fichier_31_21', 'Chèque', 'env', 30, 13000, 4, SYSDATE);

-- Insert a Carthago file
INSERT INTO CARTHAGO (NOM_FICHIER, DATE_IMPORT, CONTENU)
VALUES ('fichier_31_21.DATA', SYSDATE, 'ENTETE...');

-- Insert Carthago details
INSERT INTO CARTHAGO_DETAIL (CARTHAGO_ID, MONTANT_CHEQUE, ETAT_CHEQUE)
SELECT 1, 500, 'DEPOSE' FROM dual CONNECT BY LEVEL <= 28;
INSERT INTO CARTHAGO_DETAIL (CARTHAGO_ID, MONTANT_CHEQUE, ETAT_CHEQUE)
SELECT 1, 100, 'REJETE' FROM dual CONNECT BY LEVEL <= 2;

-- Insert a comparison result
INSERT INTO CTR (
  FICHIER_ID, CARTHAGO_ID, NOMBRE_TOTAL_CARTHAGO, MONTANT_TOTAL_CARTHAGO, NOMBRE_DEP_CARTHAGO, MONTANT_DEP_CARTHAGO,
  NOMBRE_REJ_CARTHAGO, MONTANT_REJ_CARTHAGO, NOMBRE_FICHIER, MONTANT_FICHIER,
  NOMBRE_OK_ENTETE, MONTANT_OK_ENTETE, NOMBRE_OK, MONTANT_OK, CREATED_AT
) VALUES (
  1, 1, 30, 14200, 28, 14000, 2, 200, 30, 13000, 'Y', 'N', 'Y', 'N', SYSDATE
);
--Dashboard attendu
Taux concordance interne Carthago (% fichiers avec NOMBRE_OK_ENTETE = 'Y' et MONTANT_OK_ENTETE = 'Y')

Taux concordance EV ↔ Carthago (% fichiers avec NOMBRE_OK = 'Y' et MONTANT_OK = 'Y')

Répartition dépôts/rejets (camembert)

Montants dépôts vs rejets (histogramme)

Tableau des anomalies (voir table CTR)

