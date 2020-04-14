INSERT INTO users
    (id, email, first_name, last_name, password, account_expire_date)
VALUES (1, 'stanislav.kosinski@gmail.com', 'Stanislav', 'Kosinskyi',
        '$2a$10$LsVsLTHNDaJDu8dDbkGEk.4qDE8zIuiqvQ1Kvo99ET.gd.rqUQZjW', '2100-12-30 00:00:00'),
       (2, 'expiredUser@gmail.com', 'Expired', 'User', '$2a$10$LsVsLTHNDaJDu8dDbkGEk.4qDE8zIuiqvQ1Kvo99ET.gd.rqUQZjW',
        '2000-12-30 00:00:00'),
       (3, 'Zorikgrigoryan@gmail.com', 'Zokhrap', 'Grigoryan',
        '$2a$10$LsVsLTHNDaJDu8dDbkGEk.4qDE8zIuiqvQ1Kvo99ET.gd.rqUQZjW', '2100-12-30 00:00:00');

INSERT INTO user_permissions
    (user_id, permission_id)
VALUES (1, 0),
       (1, 1),
       (3, 0);

INSERT INTO daily_registry_parse_criteria
    (name, filter_column_name)
VALUES ('EASYPAY', 'Канал продаж'),
       ('EASYPAY SC2A', 'Канал продаж'),
       ('EASYPAYC2A', 'Канал продаж'),
       ('FDY_Alex', 'Канал продаж'),
       ('FDY_Alex SC2A', 'Канал продаж'),
       ('Fondy', 'Канал продаж'),
       ('FondySC2A', 'Канал продаж'),
       ('LPC2A', 'Канал продаж'),
       ('MILOANC2A', 'Канал продаж'),
       ('MONEYVEOC2A', 'Канал продаж'),
       ('MONEYVEOSC2A', 'Канал продаж'),
       ('MOSSTC2A', 'Канал продаж'),
       ('UAPAYOLX_S', 'Канал продаж'),
       ('UAPAYPROM_IN', 'Канал продаж'),
       ('TRANS3_IN C2A', 'Канал продаж'),
       ('TRANS2_IN C2A', 'Канал продаж'),
       ('P2PTRANSF_IN C2A', 'Канал продаж'),
       ('TRANZZO_C2A', 'Канал продаж'),
       ('UAPAYOLX', 'Канал продаж'),
       ('ALFAEASY', 'Канал продаж'),
       ('EasyCash', 'Канал продаж'),
       ('fondycash', 'Канал продаж'),
       ('GM_A2C', 'Канал продаж'),
       ('KIB_A2C', 'Канал продаж'),
       ('MONEYVEOC2С', 'Канал продаж'),
       ('MOSSTA2C', 'Канал продаж'),
       ('UAPAYPROMOUT', 'Канал продаж'),
       ('SMARTICASH', 'Канал продаж'),
       ('TRANS3_OUT A2C', 'Канал продаж'),
       ('TRANS2_OUT A2C', 'Канал продаж'),
       ('TRANSF_OUT A2C', 'Канал продаж'),
       ('TRANZZO_A2C', 'Канал продаж'),
       ('UAPAY_OLX', 'Канал продаж'),
       ('UFCASH2PAY', 'Канал продаж'),
       ('UNLCASH', 'Канал продаж'),
       ('DIRECTPAY', 'Канал продаж'),
       ('P2CARD', 'Канал продаж'),
       ('PAYPOINT', 'Канал продаж'),
       ('monego', 'Канал продаж');

INSERT INTO daily_registry_parse_criteria_filter_value
    (criteria_id, value)
VALUES (1, 'CARD2ANY UA0EASYPAY'),
       (1, 'CARD2ANY UA1EASYPAY'),
       (2, 'CARD2ANY EASYPAY SC2A'),
       (3, 'CARD2ANY EASYPAYC2A'),
       (4, 'CARD2ANY FDY_Alex'),
       (5, 'CARD2ANY FDY_Alex SC2A'),
       (6, 'CARD2ANY FONDY'),
       (7, 'CARD2ANY FONDY SC2A'),
       (8, 'CARD2ANY UA0LPC2A'),
       (8, 'CARD2ANY UA1LPC2A'),
       (9, 'CARD2ANY UA0MILOAN'),
       (9, 'CARD2ANY UA1MILOAN'),
       (10, 'CARD2ANY MONEYVEOC2A'),
       (11, 'CARD2ANY MONEYVEOSC2A'),
       (12, 'CARD2ANY MOSSTC2A'),
       (13, 'CARD2ANY UAPAYOLXS SC2A'),
       (14, 'CARD2ANY UA0UAPAYPROM'),
       (14, 'CARD2ANY UA1UAPAYPROM'),
       (15, 'CARD2ANY TRANS3_IN'),
       (16, 'CARD2ANY UA0TRANS2'),
       (16, 'CARD2ANY UA1TRANS2'),
       (17, 'CARD2ANY UA0P2PTRANSF'),
       (17, 'CARD2ANY UA1P2PTRANSF'),
       (18, 'CARD2ANY TRANZZO_C2A'),
       (19, 'UAPAYOLX'),
       (20, 'CASH2CARD ALFAEASY'),
       (21, 'CASH2CARD A2C EASYCASH'),
       (22, 'CASH2CARD P2PCREDIT'),
       (22, 'CASH2CARD SGROSHI'),
       (23, 'GLOBAL24'),
       (24, 'CASH2CARD KIB'),
       (25, 'CASH2CARD MONEYVEOC2С'),
       (26, 'CASH2CARD MOSSTA2C'),
       (27, 'CASH2CARD PAYPROMOUT'),
       (28, 'CASH2CARD SMARTICASH'),
       (29, 'CASH2CARD TRANS3_OUT'),
       (30, 'CASH2CARD TRANS2_OUT'),
       (31, 'CASH2CARD TRANSF_OUT'),
       (32, 'CASH2CARD TRANZZO_A2C'),
       (33, 'CASH2CARD UAPAY_OLX'),
       (34, 'CASH2CARD UFCASH2PAY'),
       (35, 'CASH2CARD UAUNLCASH'),
       (36, 'CASH2CARD DIRECTPAY'),
       (37, 'CASH2CARD P2CARD'),
       (38, 'CASH2CARD PAYPOINT'),
       (39, 'CASH2CARD IPAY.UA A2C'),
       (39, 'CASH2CARD MONEGOUNI'),
       (39, 'CASH2CARD MONEGOGB'),
       (39, 'CASH2CARD MONEGOOTP'),
       (39, 'CASH2CARD MONEGOKON'),
       (39, 'CASH2CARD MONEGOASV'),
       (39, 'CASH2CARD MONEGOPRV'),
       (39, 'CASH2CARD MONEGOUSB'),
       (39, 'CASH2CARD MONEGOIIB'),
       (39, 'CASH2CARD MONEGOPIV'),
       (39, 'CASH2CARD MONEGOPOL'),
       (39, 'CASH2CARD MONEGOUNE'),
       (39, 'CASH2CARD MONEGOMEG'),
       (39, 'CASH2CARD MONEGOPIR'),
       (39, 'CASH2CARD MONEGOIB'),
       (39, 'CASH2CARD MONEGOCOB'),
       (39, 'CASH2CARD MONEGOEXP'),
       (39, 'CASH2CARD MONEGOOSB'),
       (39, 'CASH2CARD MONEGOKRE'),
       (39, 'CASH2CARD MONEGOMAR'),
       (39, 'CASH2CARD MONEGOIND'),
       (39, 'CASH2CARD MONEGOCIB'),
       (39, 'CASH2CARD MONEGOVER'),
       (39, 'CASH2CARD MONEGOKRI'),
       (39, 'CASH2CARD MONEGOALP');