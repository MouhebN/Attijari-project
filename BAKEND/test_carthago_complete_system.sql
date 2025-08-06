-- =============================================================================
-- TEST COMPLET DU SYSTÈME CARTHAGO
-- =============================================================================
-- Script pour tester l'ensemble du système Carthago

-- =============================================================================
-- 1. Vérification de la structure des tables
-- =============================================================================

-- Vérifier que les tables existent
SELECT table_name FROM user_tables WHERE table_name IN ('CARTHAGO', 'CARTHAGO_DETAIL', 'CTR', 'FICHIERS');

-- Vérifier la structure de la table CARTHAGO
DESC CARTHAGO;

-- Vérifier la structure de la table CARTHAGO_DETAIL
DESC CARTHAGO_DETAIL;

-- Vérifier la structure de la table CTR
DESC CTR;

-- =============================================================================
-- 2. Test d'insertion des fichiers Carthago
-- =============================================================================

-- Insérer le premier fichier de test
INSERT INTO CARTHAGO (NOM_FICHIER, DATE_IMPORT, CONTENU) 
VALUES (
    'TEST_CARTHAGO_001.DATA', 
    SYSDATE, 
    '10006174045040114047003423510000000001000000010307680A
200061740000448045050370076771013730000000103076803016122024TT'
);

-- Insérer le deuxième fichier de test
INSERT INTO CARTHAGO (NOM_FICHIER, DATE_IMPORT, CONTENU) 
VALUES (
    'TEST_CARTHAGO_002.DATA', 
    SYSDATE, 
    '10475967041352170064144066730000000006000000008208232A
204759672937872032201300115004647410000000031050003016122024TT
204759676483759071210181101101498640000000028058583016122024TT'
);

-- Vérifier les insertions
SELECT ID, NOM_FICHIER, DATE_IMPORT FROM CARTHAGO WHERE NOM_FICHIER LIKE 'TEST_%';

-- =============================================================================
-- 3. Test du parsing et insertion des détails
-- =============================================================================

-- Parser et insérer les détails pour le premier fichier
INSERT INTO CARTHAGO_DETAIL (CARTHAGO_ID, NUMERO_CHEQUE, MONTANT_CHEQUE, ETAT_CHEQUE, LIGNE_ORIGINE)
SELECT 
    c.ID,
    SUBSTR('200061740000448045050370076771013730000000103076803016122024TT', 1, 20) as NUMERO_CHEQUE,
    TO_NUMBER(SUBSTR('200061740000448045050370076771013730000000103076803016122024TT', 36, 15)) / 100 as MONTANT_CHEQUE,
    CASE 
        WHEN SUBSTR('200061740000448045050370076771013730000000103076803016122024TT', 80, 1) = 'A' THEN 'DEPOSE'
        ELSE 'REJETE'
    END as ETAT_CHEQUE,
    '200061740000448045050370076771013730000000103076803016122024TT' as LIGNE_ORIGINE
FROM CARTHAGO c 
WHERE c.NOM_FICHIER = 'TEST_CARTHAGO_001.DATA';

-- Parser et insérer les détails pour le deuxième fichier
INSERT INTO CARTHAGO_DETAIL (CARTHAGO_ID, NUMERO_CHEQUE, MONTANT_CHEQUE, ETAT_CHEQUE, LIGNE_ORIGINE)
SELECT 
    c.ID,
    SUBSTR('204759672937872032201300115004647410000000031050003016122024TT', 1, 20) as NUMERO_CHEQUE,
    TO_NUMBER(SUBSTR('204759672937872032201300115004647410000000031050003016122024TT', 36, 15)) / 100 as MONTANT_CHEQUE,
    CASE 
        WHEN SUBSTR('204759672937872032201300115004647410000000031050003016122024TT', 80, 1) = 'A' THEN 'DEPOSE'
        ELSE 'REJETE'
    END as ETAT_CHEQUE,
    '204759672937872032201300115004647410000000031050003016122024TT' as LIGNE_ORIGINE
FROM CARTHAGO c 
WHERE c.NOM_FICHIER = 'TEST_CARTHAGO_002.DATA';

INSERT INTO CARTHAGO_DETAIL (CARTHAGO_ID, NUMERO_CHEQUE, MONTANT_CHEQUE, ETAT_CHEQUE, LIGNE_ORIGINE)
SELECT 
    c.ID,
    SUBSTR('204759676483759071210181101101498640000000028058583016122024TT', 1, 20) as NUMERO_CHEQUE,
    TO_NUMBER(SUBSTR('204759676483759071210181101101498640000000028058583016122024TT', 36, 15)) / 100 as MONTANT_CHEQUE,
    CASE 
        WHEN SUBSTR('204759676483759071210181101101498640000000028058583016122024TT', 80, 1) = 'A' THEN 'DEPOSE'
        ELSE 'REJETE'
    END as ETAT_CHEQUE,
    '204759676483759071210181101101498640000000028058583016122024TT' as LIGNE_ORIGINE
FROM CARTHAGO c 
WHERE c.NOM_FICHIER = 'TEST_CARTHAGO_002.DATA';

-- Vérifier les détails parsés
SELECT 
    c.NOM_FICHIER,
    cd.NUMERO_CHEQUE,
    cd.MONTANT_CHEQUE,
    cd.ETAT_CHEQUE,
    cd.LIGNE_ORIGINE
FROM CARTHAGO c
JOIN CARTHAGO_DETAIL cd ON c.ID = cd.CARTHAGO_ID
WHERE c.NOM_FICHIER LIKE 'TEST_%'
ORDER BY c.NOM_FICHIER, cd.NUMERO_CHEQUE;

-- =============================================================================
-- 4. Test des statistiques par fichier
-- =============================================================================

-- Statistiques par fichier
SELECT 
    c.NOM_FICHIER,
    COUNT(cd.ID) as NOMBRE_TOTAL_CHEQUES,
    SUM(cd.MONTANT_CHEQUE) as MONTANT_TOTAL,
    COUNT(CASE WHEN cd.ETAT_CHEQUE = 'DEPOSE' THEN 1 END) as NOMBRE_DEPOSES,
    SUM(CASE WHEN cd.ETAT_CHEQUE = 'DEPOSE' THEN cd.MONTANT_CHEQUE ELSE 0 END) as MONTANT_DEPOSES,
    COUNT(CASE WHEN cd.ETAT_CHEQUE = 'REJETE' THEN 1 END) as NOMBRE_REJETES,
    SUM(CASE WHEN cd.ETAT_CHEQUE = 'REJETE' THEN cd.MONTANT_CHEQUE ELSE 0 END) as MONTANT_REJETES
FROM CARTHAGO c
LEFT JOIN CARTHAGO_DETAIL cd ON c.ID = cd.CARTHAGO_ID
WHERE c.NOM_FICHIER LIKE 'TEST_%'
GROUP BY c.ID, c.NOM_FICHIER, c.DATE_IMPORT;

-- =============================================================================
-- 5. Test de création d'un CTR (si des fichiers Encaisse Valeur existent)
-- =============================================================================

-- Vérifier s'il y a des fichiers Encaisse Valeur
SELECT COUNT(*) as NB_FICHIERS_ENCAISSE FROM FICHIERS;

-- Si des fichiers existent, créer un CTR de test
-- (Cette partie nécessite des données dans la table FICHIERS)

-- =============================================================================
-- 6. Test des vues de reporting
-- =============================================================================

-- Vérifier si les vues existent
SELECT view_name FROM user_views WHERE view_name LIKE 'V_CARTHAGO%' OR view_name LIKE 'V_CTR%';

-- Test de la vue V_CARTHAGO_SUMMARY (si elle existe)
-- SELECT * FROM V_CARTHAGO_SUMMARY WHERE NOM_FICHIER LIKE 'TEST_%';

-- Test de la vue V_CTR_COMPARISON (si elle existe)
-- SELECT * FROM V_CTR_COMPARISON WHERE NOM_FICHIER_CARTHAGO LIKE 'TEST_%';

-- =============================================================================
-- 7. Test des requêtes de dashboard
-- =============================================================================

-- Statistiques globales Carthago
SELECT 
    COUNT(DISTINCT c.ID) as NB_FICHIERS_CARTHAGO,
    COUNT(cd.ID) as NB_TOTAL_CHEQUES,
    SUM(cd.MONTANT_CHEQUE) as MONTANT_TOTAL_CHEQUES,
    COUNT(CASE WHEN cd.ETAT_CHEQUE = 'DEPOSE' THEN 1 END) as NB_CHEQUES_DEPOSES,
    COUNT(CASE WHEN cd.ETAT_CHEQUE = 'REJETE' THEN 1 END) as NB_CHEQUES_REJETES,
    AVG(cd.MONTANT_CHEQUE) as MONTANT_MOYEN_CHEQUE
FROM CARTHAGO c
LEFT JOIN CARTHAGO_DETAIL cd ON c.ID = cd.CARTHAGO_ID
WHERE c.NOM_FICHIER LIKE 'TEST_%';

-- Répartition par état
SELECT 
    cd.ETAT_CHEQUE,
    COUNT(*) as NB_CHEQUES,
    SUM(cd.MONTANT_CHEQUE) as MONTANT_TOTAL,
    AVG(cd.MONTANT_CHEQUE) as MONTANT_MOYEN
FROM CARTHAGO c
JOIN CARTHAGO_DETAIL cd ON c.ID = cd.CARTHAGO_ID
WHERE c.NOM_FICHIER LIKE 'TEST_%'
GROUP BY cd.ETAT_CHEQUE;

-- =============================================================================
-- 8. Nettoyage des données de test
-- =============================================================================

-- Supprimer les données de test (optionnel)
-- DELETE FROM CARTHAGO_DETAIL WHERE CARTHAGO_ID IN (SELECT ID FROM CARTHAGO WHERE NOM_FICHIER LIKE 'TEST_%');
-- DELETE FROM CARTHAGO WHERE NOM_FICHIER LIKE 'TEST_%';

-- =============================================================================
-- SCRIPT TERMINÉ
-- ============================================================================= 