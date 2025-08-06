-- =============================================================================
-- Script de correction pour Oracle Database
-- =============================================================================

-- Se connecter en tant qu'administrateur SYSTEM
-- CONNECT SYSTEM/root@localhost:1521/XEPDB1

-- 1. Créer l'utilisateur RUYA s'il n'existe pas
CREATE USER RUYA IDENTIFIED BY ruya123;

-- 2. Donner les privilèges nécessaires
GRANT CONNECT, RESOURCE, DBA TO RUYA;
GRANT CREATE SESSION TO RUYA;
GRANT UNLIMITED TABLESPACE TO RUYA;
GRANT CREATE TABLE TO RUYA;
GRANT CREATE SEQUENCE TO RUYA;
GRANT CREATE VIEW TO RUYA;
GRANT CREATE PROCEDURE TO RUYA;

-- 3. Déverrouiller le compte s'il est verrouillé
ALTER USER RUYA ACCOUNT UNLOCK;

-- 4. Vérifier le statut du compte
SELECT username, account_status, lock_date, expiry_date 
FROM dba_users 
WHERE username = 'RUYA';

-- 5. Tester la connexion
-- CONNECT RUYA/ruya123@localhost:1521/XEPDB1
-- SELECT 'Connexion réussie!' FROM DUAL;

-- =============================================================================
-- Si vous préférez utiliser SYSTEM directement:
-- =============================================================================
-- 1. Vérifier que SYSTEM a les privilèges nécessaires
SELECT username, account_status FROM dba_users WHERE username = 'SYSTEM';

-- 2. S'assurer que SYSTEM n'est pas verrouillé
ALTER USER SYSTEM ACCOUNT UNLOCK;

-- 3. Tester la connexion SYSTEM
-- CONNECT SYSTEM/root@localhost:1521/XEPDB1
-- SELECT 'Connexion SYSTEM réussie!' FROM DUAL; 