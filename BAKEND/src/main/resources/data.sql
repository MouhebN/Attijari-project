- Script de données pour H2 Database
-- Insertion des données de test pour le dashboard CTR/CARTHAGO

-- Données pour la table FICHIERS (codes 30, 31, 32, 33)
INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('30', 150, 45000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('30', 200, 60000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('30', 100, 30000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('31', 120, 36000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('31', 180, 54000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('31', 90, 27000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('32', 80, 24000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('32', 150, 45000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('32', 110, 33000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('33', 200, 60000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('33', 160, 48000.00);

INSERT INTO
    FICHIERS (CODE_VALEUR, NOMBRE, MONTANT)
VALUES ('33', 140, 42000.00);

-- Données pour la table CTR
INSERT INTO
    CTR (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CTR,
        MONTANT_TOTAL_CTR
    )
VALUES (
        '30',
        450,
        135000.00,
        150,
        45000.00
    );

INSERT INTO
    CTR (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CTR,
        MONTANT_TOTAL_CTR
    )
VALUES (
        '31',
        390,
        117000.00,
        120,
        36000.00
    );

INSERT INTO
    CTR (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CTR,
        MONTANT_TOTAL_CTR
    )
VALUES (
        '32',
        340,
        102000.00,
        80,
        24000.00
    );

INSERT INTO
    CTR (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CTR,
        MONTANT_TOTAL_CTR
    )
VALUES (
        '33',
        500,
        150000.00,
        200,
        60000.00
    );

-- Données pour la table CARTHAGO
INSERT INTO
    CARTHAGO (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CONTROLES,
        NB_CHEQUES_VERIFIES,
        NOMBRE_IMAGES
    )
VALUES (
        '30',
        300,
        90000.00,
        250,
        200,
        45
    );

INSERT INTO
    CARTHAGO (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CONTROLES,
        NB_CHEQUES_VERIFIES,
        NOMBRE_IMAGES
    )
VALUES (
        '31',
        280,
        84000.00,
        220,
        180,
        38
    );

INSERT INTO
    CARTHAGO (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CONTROLES,
        NB_CHEQUES_VERIFIES,
        NOMBRE_IMAGES
    )
VALUES (
        '32',
        250,
        75000.00,
        200,
        150,
        32
    );

INSERT INTO
    CARTHAGO (
        CODE_VALEUR,
        NOMBRE,
        MONTANT,
        NB_CHEQUES_CONTROLES,
        NB_CHEQUES_VERIFIES,
        NOMBRE_IMAGES
    )
VALUES (
        '33',
        350,
        105000.00,
        300,
        250,
        52
    );