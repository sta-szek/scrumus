------------------------------------------------- TABELE UŻYTKOWNIKÓW -------------------------------------------------

-- DEVELOPER TABLE                                                                                ADMIN   SM    TEAM    PO
INSERT INTO developer VALUES (1, 'jano@email.pl', 'Jan', 'Nowak', 'jano', NULL);              --    -     -      1      -
INSERT INTO developer VALUES (2, 'szgo@email.pl', 'Szymon', 'Górnik', 'szgo', NULL);          --    -     -      2      -
INSERT INTO developer VALUES (3, 'alto@email.pl', 'Alfred', 'Tomczak', 'alto', NULL);         --    -     -      1      -
INSERT INTO developer VALUES (4, 'domo@email.pl', 'Donata', 'Momot', 'domo', NULL);           --    -     -      2      -
INSERT INTO developer VALUES (5, 'arfa@email.pl', 'Arseniusz', 'Fabisiak', 'arfa', NULL);     --    Y     -      3      -
INSERT INTO developer VALUES (6, 'kaja@email.pl', 'Kacper', 'Janas', 'kaja', NULL);           --    -     2      2      -
INSERT INTO developer VALUES (7, 'wafr@email.pl', 'Warcisław', 'Frydrych', 'wafr', NULL);     --    Y     -      1,3    -
INSERT INTO developer VALUES (8, 'miwi@email.pl', 'Miłosz', 'Wielgus', 'miwi', NULL);         --    -     -      2      -
INSERT INTO developer VALUES (9, 'trba@email.pl', 'Trzebowit', 'Bartosiak', 'trba', NULL);    --    -     -      1      -
INSERT INTO developer VALUES (10, 'miwa@email.pl', 'Mirella', 'Wachowiak', 'miwa', NULL);     --    -     -      2      1
INSERT INTO developer VALUES (11, 'grjo@email.pl', 'Grzesiak', 'Job', 'grjo', NULL);          --    -     -      1      -
INSERT INTO developer VALUES (12, 'anra@email.pl', 'Angelus', 'Rączka', 'anra', NULL);        --    -     -      2      -
INSERT INTO developer VALUES (13, 'cyru@email.pl', 'Cyryl', 'Rusin', 'cyru', NULL);           --    -     -      1      -
INSERT INTO developer VALUES (14, 'jafr@email.pl', 'Jazon', 'Frąckowiak', 'jafr', NULL);      --    -     1      1      -
INSERT INTO developer VALUES (15, 'grli@email.pl', 'Grażyna', 'Lipowski', 'grli', NULL);      --    -     -      1      2

-- ADMINISTRATORZY
INSERT INTO admin VALUES (1, 5);
INSERT INTO admin VALUES (2, 7);

-- SCRUM MASTERZY
INSERT INTO scrum_master VALUES (1, 6);
INSERT INTO scrum_master VALUES (2, 14);

-- HASŁA
INSERT INTO password VALUES (1, '202cb962ac59075b964b07152d234b70', 1);
INSERT INTO password VALUES (2, '202cb962ac59075b964b07152d234b70', 2);
INSERT INTO password VALUES (3, '202cb962ac59075b964b07152d234b70', 3);
INSERT INTO password VALUES (4, '202cb962ac59075b964b07152d234b70', 4);
INSERT INTO password VALUES (5, '202cb962ac59075b964b07152d234b70', 5);
INSERT INTO password VALUES (6, '202cb962ac59075b964b07152d234b70', 6);
INSERT INTO password VALUES (7, '202cb962ac59075b964b07152d234b70', 7);
INSERT INTO password VALUES (8, '202cb962ac59075b964b07152d234b70', 8);
INSERT INTO password VALUES (9, '202cb962ac59075b964b07152d234b70', 9);
INSERT INTO password VALUES (10, '202cb962ac59075b964b07152d234b70', 10);
INSERT INTO password VALUES (11, '202cb962ac59075b964b07152d234b70', 11);
INSERT INTO password VALUES (12, '202cb962ac59075b964b07152d234b70', 12);
INSERT INTO password VALUES (13, '202cb962ac59075b964b07152d234b70', 13);
INSERT INTO password VALUES (14, '202cb962ac59075b964b07152d234b70', 14);
INSERT INTO password VALUES (15, '202cb962ac59075b964b07152d234b70', 15);

-- TEAMY
INSERT INTO team VALUES (1, 'Fumfle');
INSERT INTO team VALUES (2, 'Nikifory');
INSERT INTO team VALUES (3, 'Administratorzy');

-- DEVELOPER TEAM
INSERT INTO team_developer VALUES (1, 1);
INSERT INTO team_developer VALUES (2, 2);
INSERT INTO team_developer VALUES (1, 3);
INSERT INTO team_developer VALUES (2, 4);
INSERT INTO team_developer VALUES (3, 5);
INSERT INTO team_developer VALUES (2, 6);
INSERT INTO team_developer VALUES (1, 7);
INSERT INTO team_developer VALUES (3, 7);
INSERT INTO team_developer VALUES (2, 8);
INSERT INTO team_developer VALUES (1, 9);
INSERT INTO team_developer VALUES (2, 10);
INSERT INTO team_developer VALUES (1, 11);
INSERT INTO team_developer VALUES (2, 12);
INSERT INTO team_developer VALUES (1, 13);
INSERT INTO team_developer VALUES (1, 14);
INSERT INTO team_developer VALUES (1, 15);

-- BACKLOG
INSERT INTO backlog VALUES (1);
INSERT INTO backlog VALUES (2);

-- PROJEKTY
INSERT INTO project VALUES ('reco', '2015-11-17 12:21:13 ', 'Aplikacja umożliwaiając zdalne połączenie się z komputerem', 'Opis aplikacji umożliwiającej łączenie się z komputerem', 'Remote connector', 1);
INSERT INTO project VALUES ('pote', '2016-01-15 12:21:13 ', 'Framework do testowania obiektów POJO', 'Opis frameworku do testowania obiektów POJO', 'Pojo tester', 2);

-- PRODUCT OWNERZY
INSERT INTO product_owner VALUES (1, 10, 'reco');
INSERT INTO product_owner VALUES (2, 15, 'pote');

--RETROSPEKTYWY
INSERT INTO retrospective VALUES (1, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (2, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (3, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (4, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (5, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (6, 'Opis retrospektywy 1');

-- SPRINTY
INSERT INTO sprint VALUES (1, 'Implementacja story 1-5', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 'reco', 1);
INSERT INTO sprint VALUES (2, 'Testy systemowe', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 'reco', 2);
INSERT INTO sprint VALUES (3, 'Release wersji 1.0', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 'reco', 3);
INSERT INTO sprint VALUES (4, 'Rozpoznanie środowisk', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 'pote', 4);
INSERT INTO sprint VALUES (5, 'Implementacja', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 'pote', 5);
INSERT INTO sprint VALUES (6, 'Testy systemowe', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 'pote', 6);

-- TEAMY DO PROJEKTU
INSERT INTO project_team VALUES ('reco', 1);
INSERT INTO project_team VALUES ('pote', 2);

-- SCRUM MASTERZY DO TEAMU
INSERT INTO scrum_master_team VALUES (1, 2);
INSERT INTO scrum_master_team VALUES (2, 1);

-- STORY
INSERT INTO story VALUES (1, 'Definition of Done dla story', 'Optional name1', '1', 1);
INSERT INTO story VALUES (2, 'Definition of Done dla story', 'Optional name2', '2', 2);
INSERT INTO story VALUES (3, 'Definition of Done dla story', 'Optional name3', '3', 3);
INSERT INTO story VALUES (4, 'Definition of Done dla story', 'Optional name4', '5', 4);
INSERT INTO story VALUES (5, 'Definition of Done dla story', 'Optional name5', '8', 5);
INSERT INTO story VALUES (6, 'Definition of Done dla story', 'Optional name6', '13', 6);
INSERT INTO story VALUES (7, 'Definition of Done dla story', 'Optional name7', '1', 1);
INSERT INTO story VALUES (8, 'Definition of Done dla story', 'Optional name8', '2', 2);
INSERT INTO story VALUES (9, 'Definition of Done dla story', 'Optional name9', '3', 3);

-- RODZAJE ISSUE
INSERT INTO issue_type VALUES (1, 'Task');
INSERT INTO issue_type VALUES (2, 'Feature');
INSERT INTO issue_type VALUES (3, 'Bug');
INSERT INTO issue_type VALUES (4, 'Improvement');

-- PRIORYTETY
INSERT INTO priority VALUES (1, 'Wysoki');
INSERT INTO priority VALUES (2, 'Średni');
INSERT INTO priority VALUES (3, 'Niski');
INSERT INTO priority VALUES (4, 'Mały');

-- STANY
INSERT INTO state VALUES (1, 'TO DO');
INSERT INTO state VALUES (2, 'IN PROGRES');
INSERT INTO state VALUES (3, 'DONE');

-- ISSUES
INSERT INTO issue VALUES (1, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-1', 'nazwa', NULL, 1, 1, 1);
INSERT INTO issue VALUES (2, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-2', 'nazwa', 1, 2, 1, 1);
INSERT INTO issue VALUES (3, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-3', 'nazwa', NULL, 3, 1, 11);
INSERT INTO issue VALUES (4, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-4', 'nazwa', 2, 4, 1, 1);
INSERT INTO issue VALUES (5, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-5', 'nazwa', 3, 1, 1, 10);
INSERT INTO issue VALUES (6, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-6', 'nazwa', 4, 2, 1, 1);
INSERT INTO issue VALUES (7, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-7', 'nazwa', 5, 3, 1, 12);
INSERT INTO issue VALUES (8, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-8', 'nazwa', 6, 4, 1, 13);
INSERT INTO issue VALUES (9, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-9', 'nazwa', 7, 1, 1, 14);
INSERT INTO issue VALUES (10, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-10', 'nazwa', 8, 2, 1, 1);
INSERT INTO issue VALUES (11, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-11', 'nazwa', 9, 3, 1, 8);
INSERT INTO issue VALUES (12, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-12', 'nazwa', 10, 4, 1, 7);
INSERT INTO issue VALUES (13, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-13', 'nazwa', 11, 1, 1, 6);
INSERT INTO issue VALUES (14, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-14', 'nazwa', 2, 2, 1, 5);
INSERT INTO issue VALUES (15, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-15', 'nazwa', 11, 3, 1, 4);
INSERT INTO issue VALUES (16, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-16', 'nazwa', 3, 4, 1, 3);
INSERT INTO issue VALUES (17, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-17', 'nazwa', 1, 1, 1, 2);
INSERT INTO issue VALUES (18, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-18', 'nazwa', 12, 2, 1, 1);
INSERT INTO issue VALUES (19, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-19', 'nazwa', NULL, 3, 1, 1);
INSERT INTO issue VALUES (20, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-20', 'nazwa', 13, 4, 1, 1);
INSERT INTO issue VALUES (21, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-21', 'nazwa', NULL, 1, 1, 15);
INSERT INTO issue VALUES (22, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote-22', 'nazwa', 13, 2, 1, 1);

-- KOMENTARZE
INSERT INTO comment VALUES (1, 'Komentarz 1', '2015-02-02 12:00:00', 1);
INSERT INTO comment VALUES (2, 'Komentarz 2', '2015-02-02 12:00:00', 2);
INSERT INTO comment VALUES (3, 'Komentarz 3', '2015-02-02 12:00:00', 3);
INSERT INTO comment VALUES (4, 'Komentarz 4', '2015-02-02 12:00:00', 4);
INSERT INTO comment VALUES (5, 'Komentarz 5', '2015-02-02 12:00:00', 5);
INSERT INTO comment VALUES (6, 'Komentarz 6', '2015-02-02 12:00:00', 6);
INSERT INTO comment VALUES (7, 'Komentarz 7', '2015-02-02 12:00:00', 7);
INSERT INTO comment VALUES (8, 'Komentarz 8', '2015-02-02 12:00:00', 8);
INSERT INTO comment VALUES (9, 'Komentarz 9', '2015-02-02 12:00:00', 9);
INSERT INTO comment VALUES (10, 'Komentarz 10', '2015-02-02 12:00:00', 10);
INSERT INTO comment VALUES (11, 'Komentarz 11', '2015-02-02 12:00:00', 11);
INSERT INTO comment VALUES (12, 'Komentarz 12', '2015-02-02 12:00:00', 12);
INSERT INTO comment VALUES (13, 'Komentarz 13', '2015-02-02 12:00:00', 13);
INSERT INTO comment VALUES (14, 'Komentarz 14', '2015-02-02 12:00:00', 14);

-- KOMENTARZE DO ISSE
INSERT INTO issue_comment VALUES (1, 1);
INSERT INTO issue_comment VALUES (2, 2);
INSERT INTO issue_comment VALUES (3, 3);
INSERT INTO issue_comment VALUES (4, 4);
INSERT INTO issue_comment VALUES (5, 5);
INSERT INTO issue_comment VALUES (6, 6);
INSERT INTO issue_comment VALUES (7, 7);
INSERT INTO issue_comment VALUES (8, 8);
INSERT INTO issue_comment VALUES (9, 9);

-- KOMENTARZE DO RETROSPEKTYWY
INSERT INTO retrospective_comment VALUES (1, 10);
INSERT INTO retrospective_comment VALUES (2, 11);
INSERT INTO retrospective_comment VALUES (3, 12);
INSERT INTO retrospective_comment VALUES (4, 13);
INSERT INTO retrospective_comment VALUES (5, 14);

-- ISSUE DO BACKLOGA
INSERT INTO backlog_issue VALUES (1, 1);
INSERT INTO backlog_issue VALUES (1, 2);
INSERT INTO backlog_issue VALUES (1, 3);
INSERT INTO backlog_issue VALUES (2, 4);
INSERT INTO backlog_issue VALUES (2, 5);
INSERT INTO backlog_issue VALUES (2, 6);
INSERT INTO backlog_issue VALUES (2, 7);

-- ISSUE DO STORY
INSERT INTO story_issue VALUES (1, 8);
INSERT INTO story_issue VALUES (2, 9);
INSERT INTO story_issue VALUES (3, 10);
INSERT INTO story_issue VALUES (4, 11);
INSERT INTO story_issue VALUES (5, 12);
INSERT INTO story_issue VALUES (6, 13);
INSERT INTO story_issue VALUES (7, 14);
INSERT INTO story_issue VALUES (8, 15);
INSERT INTO story_issue VALUES (9, 16);
INSERT INTO story_issue VALUES (1, 17);
INSERT INTO story_issue VALUES (2, 18);
INSERT INTO story_issue VALUES (3, 19);
INSERT INTO story_issue VALUES (3, 20);
INSERT INTO story_issue VALUES (4, 21);
INSERT INTO story_issue VALUES (5, 22);

-- PLUSY I MINUSY
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (1, 'Cos tam 1', 1, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (2, 'Cos tam 2', 2, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (3, 'Cos tam 3', 3, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (4, 'Cos tam 4', 4, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (5, 'Cos tam 5', 5, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (6, 'Cos tam 6', 6, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (1, 'Cos tam 1', 1, 'PLUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (2, 'Cos tam 2', 2, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (3, 'Cos tam 3', 3, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (4, 'Cos tam 4', 4, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (5, 'Cos tam 5', 5, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (6, 'Cos tam 6', 6, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (1, 'Cos tam 1', 1, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (2, 'Cos tam 2', 2, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (3, 'Cos tam 3', 3, 'MINUS');
INSERT INTO retrospectiveentity_retrospectiveitemembeddables VALUES (4, 'Cos tam 4', 4, 'MINUS');

-- ROLE
INSERT INTO role VALUES (1, 'ADMIN');
INSERT INTO role VALUES (2, 'SCRUM_MASTER');
INSERT INTO role VALUES (3, 'PRODUCT_OWNER');
INSERT INTO role VALUES (4, 'DEVELOPER');

-- ROLE UZYTKOWNIKOW
INSERT INTO role_developer VALUES (1, 5);
INSERT INTO role_developer VALUES (1, 7);
INSERT INTO role_developer VALUES (2, 6);
INSERT INTO role_developer VALUES (2, 14);
INSERT INTO role_developer VALUES (3, 10);
INSERT INTO role_developer VALUES (3, 15);
INSERT INTO role_developer VALUES (4, 1);
INSERT INTO role_developer VALUES (4, 2);
INSERT INTO role_developer VALUES (4, 3);
INSERT INTO role_developer VALUES (4, 4);
INSERT INTO role_developer VALUES (4, 5);
INSERT INTO role_developer VALUES (4, 6);
INSERT INTO role_developer VALUES (4, 7);
INSERT INTO role_developer VALUES (4, 8);
INSERT INTO role_developer VALUES (4, 9);
INSERT INTO role_developer VALUES (4, 10);
INSERT INTO role_developer VALUES (4, 11);
INSERT INTO role_developer VALUES (4, 12);
INSERT INTO role_developer VALUES (4, 13);
INSERT INTO role_developer VALUES (4, 14);
INSERT INTO role_developer VALUES (4, 15);