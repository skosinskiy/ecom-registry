INSERT INTO users
    ( id, email, first_name, last_name, password, account_expire_date)
VALUES
    ( 1, 'stanislav.kosinski@gmail.com', 'Stanislav', 'Kosinskyi', '$2a$10$LsVsLTHNDaJDu8dDbkGEk.4qDE8zIuiqvQ1Kvo99ET.gd.rqUQZjW', '2100-12-30 00:00:00'),
    ( 2, 'expiredUser@gmail.com', 'Expired', 'User', '$2a$10$LsVsLTHNDaJDu8dDbkGEk.4qDE8zIuiqvQ1Kvo99ET.gd.rqUQZjW', '2000-12-30 00:00:00');

INSERT INTO user_permissions
    ( user_id, permission_id )
VALUES
    ( 1, 0 ),
    ( 1, 1 );