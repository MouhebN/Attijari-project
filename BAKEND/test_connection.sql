-- Script de test de connexion Oracle
-- Ce script teste si la connexion fonctionne

SELECT 'Connexion Oracle reussie !' as message FROM dual;

SELECT username, account_status
FROM dba_users
WHERE
    username = 'RUYA';

SELECT sysdate as current_date FROM dual;

EXIT;