-- =============================================================================
-- TEST CARTHAGO SCHEMA
-- =============================================================================
-- Script de test pour vérifier le bon fonctionnement du schéma Carthago

-- =============================================================================
-- 1. Test d'insertion dans CARTHAGO
-- =============================================================================
INSERT INTO
    CARTHAGO (
        NOM_FICHIER,
        DATE_IMPORT,
        CONTENU
    )
VALUES (
        'test_carthago_001.DATA',
        SYSDATE,
        'LIGNE1:CHQ001:1000.50:DEPOSE
LIGNE2:CHQ002:2500.75:DEPOSE
LIGNE3:CHQ003:500.25:REJETE'
    );

-- Récupérer l'ID généré
SELECT ID, NOM_FICHIER, DATE_IMPORT
FROM CARTHAGO
WHERE
    NOM_FICHIER = 'test_carthago_001.DATA';

-- =============================================================================
-- 2. Test d'insertion dans CARTHAGO_DETAIL
-- =============================================================================
-- Insérer des détails pour le fichier test
INSERT INTO
    CARTHAGO_DETAIL (
        CARTHAGO_ID,
        NUMERO_CHEQUE,
        MONTANT_CHEQUE,
        ETAT_CHEQUE,
        LIGNE_ORIGINE
    )
SELECT c.ID, 'CHQ001', 1000.50, 'DEPOSE', 'LIGNE1:CHQ001:1000.50:DEPOSE'
FROM CARTHAGO c
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA';

INSERT INTO
    CARTHAGO_DETAIL (
        CARTHAGO_ID,
        NUMERO_CHEQUE,
        MONTANT_CHEQUE,
        ETAT_CHEQUE,
        LIGNE_ORIGINE
    )
SELECT c.ID, 'CHQ002', 2500.75, 'DEPOSE', 'LIGNE2:CHQ002:2500.75:DEPOSE'
FROM CARTHAGO c
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA';

INSERT INTO
    CARTHAGO_DETAIL (
        CARTHAGO_ID,
        NUMERO_CHEQUE,
        MONTANT_CHEQUE,
        ETAT_CHEQUE,
        LIGNE_ORIGINE
    )
SELECT c.ID, 'CHQ003', 500.25, 'REJETE', 'LIGNE3:CHQ003:500.25:REJETE'
FROM CARTHAGO c
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA';

-- =============================================================================
-- 3. Test de la vue V_CARTHAGO_SUMMARY
-- =============================================================================
SELECT *
FROM V_CARTHAGO_SUMMARY
WHERE
    NOM_FICHIER = 'test_carthago_001.DATA';

-- =============================================================================
-- 4. Test d'insertion dans CTR (nécessite un fichier existant dans FICHIERS)
-- =============================================================================
-- Vérifier s'il y a des fichiers dans la table FICHIERS
SELECT COUNT(*) as NOMBRE_FICHIERS FROM FICHIERS;

-- Si des fichiers existent, créer un CTR de test
INSERT INTO
    CTR (
        FICHIER_ID,
        CARTHAGO_ID,
        NOMBRE_TOTAL_CARTHAGO,
        MONTANT_TOTAL_CARTHAGO,
        NOMBRE_DEP_CARTHAGO,
        MONTANT_DEP_CARTHAGO,
        NOMBRE_REJ_CARTHAGO,
        MONTANT_REJ_CARTHAGO,
        NOMBRE_FICHIER,
        MONTANT_FICHIER,
        NOMBRE_OK,
        MONTANT_OK
    )
SELECT
    f.ID_FICHIER,
    c.ID,
    3, -- nombre total chèques
    4001.50, -- montant total
    2, -- nombre déposés
    3501.25, -- montant déposés
    1, -- nombre rejetés
    500.25, -- montant rejetés
    f.NOMBRE,
    f.MONTANT,
    CASE
        WHEN f.NOMBRE = 3 THEN 'Y'
        ELSE 'N'
    END,
    CASE
        WHEN f.MONTANT = 4001.50 THEN 'Y'
        ELSE 'N'
    END
FROM CARTHAGO c, FICHIERS f
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA'
    AND ROWNUM = 1;

-- =============================================================================
-- 5. Test de la vue V_CTR_COMPARISON
-- =============================================================================
SELECT *
FROM V_CTR_COMPARISON
WHERE
    CARTHAGO_NOM = 'test_carthago_001.DATA';

-- =============================================================================
-- 6. Requêtes de test supplémentaires
-- =============================================================================

-- Vérifier les détails d'un fichier Carthago
SELECT cd.NUMERO_CHEQUE, cd.MONTANT_CHEQUE, cd.ETAT_CHEQUE, cd.LIGNE_ORIGINE
FROM
    CARTHAGO c
    JOIN CARTHAGO_DETAIL cd ON c.ID = cd.CARTHAGO_ID
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA'
ORDER BY cd.NUMERO_CHEQUE;

-- Statistiques par état
SELECT
    ETAT_CHEQUE,
    COUNT(*) as NOMBRE_CHEQUES,
    SUM(MONTANT_CHEQUE) as MONTANT_TOTAL
FROM
    CARTHAGO_DETAIL cd
    JOIN CARTHAGO c ON cd.CARTHAGO_ID = c.ID
WHERE
    c.NOM_FICHIER = 'test_carthago_001.DATA'
GROUP BY
    ETAT_CHEQUE;

-- =============================================================================
-- 7. Nettoyage des données de test (optionnel)
-- =============================================================================
-- Décommenter les lignes suivantes pour nettoyer les données de test
/*
DELETE FROM CTR WHERE CARTHAGO_ID IN (SELECT ID FROM CARTHAGO WHERE NOM_FICHIER = 'test_carthago_001.DATA');
DELETE FROM CARTHAGO_DETAIL WHERE CARTHAGO_ID IN (SELECT ID FROM CARTHAGO WHERE NOM_FICHIER = 'test_carthago_001.DATA');
DELETE FROM CARTHAGO WHERE NOM_FICHIER = 'test_carthago_001.DATA';
*/

-- =============================================================================
-- SCRIPT DE TEST TERMINÉ
-- =============================================================================