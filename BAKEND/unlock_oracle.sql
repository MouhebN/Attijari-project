-- Script pour déverrouiller et configurer le compte RUYA dans Oracle 21
-- Exécuter ce script en tant qu'administrateur (SYSTEM)

-- Se connecter en tant que SYSTEM (exécuter manuellement)
-- CONNECT SYSTEM/root@localhost:1521/XEPDB1

-- Déverrouiller le compte RUYA
ALTER USER RUYA ACCOUNT UNLOCK;

-- Réinitialiser le mot de passe
ALTER USER RUYA IDENTIFIED BY ruya123;

-- Donner les privilèges nécessaires
GRANT CONNECT, RESOURCE, DBA TO RUYA;
GRANT CREATE SESSION TO RUYA;
GRANT UNLIMITED TABLESPACE TO RUYA;
GRANT CREATE TABLE TO RUYA;
GRANT CREATE SEQUENCE TO RUYA;
GRANT CREATE VIEW TO RUYA;
GRANT CREATE PROCEDURE TO RUYA;
GRANT CREATE TRIGGER TO RUYA;

-- Vérifier que le compte est déverrouillé
SELECT username, account_status, lock_date, expiry_date 
FROM dba_users 
WHERE username = 'RUYA';

-- Vérifier les privilèges
SELECT * FROM dba_sys_privs WHERE grantee = 'RUYA';

-- Créer un tablespace pour RUYA si nécessaire
-- CREATE TABLESPACE RUYA_DATA
-- DATAFILE 'ruya_data.dbf' SIZE 100M
-- AUTOEXTEND ON NEXT 10M MAXSIZE 1G;

-- Attribuer le tablespace par défaut
-- ALTER USER RUYA DEFAULT TABLESPACE RUYA_DATA;

COMMIT;

-- Message de confirmation
PROMPT ========================================
PROMPT Compte RUYA déverrouillé et configuré !
PROMPT ========================================
PROMPT 
PROMPT Vous pouvez maintenant :
PROMPT 1. Tester la connexion : sqlplus RUYA/ruya123@localhost:1521/XEPDB1
PROMPT 2. Démarrer l'application Spring Boot
PROMPT 3. Vérifier les logs pour confirmer la connexion
PROMPT 