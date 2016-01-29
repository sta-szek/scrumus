------------------------------------------------- TABELE U¯YTKOWNIKÓW -------------------------------------------------

-- DEVELOPER TABLE                                                                                ADMIN   SM    TEAM    PO
INSERT INTO developer VALUES (1, 'jano@email.pl', 'Jan', 'Nowak', 'jano', NULL);              --    -     -      1      -
INSERT INTO developer VALUES (2, 'szgo@email.pl', 'Szymon', 'Górnik', 'szgo', NULL);          --    -     -      2      -
INSERT INTO developer VALUES (3, 'alto@email.pl', 'Alfred', 'Tomczak', 'alto', NULL);         --    -     -      1      -
INSERT INTO developer VALUES (4, 'domo@email.pl', 'Donata', 'Momot', 'domo', NULL);           --    -     -      2      -
INSERT INTO developer VALUES (5, 'arfa@email.pl', 'Arseniusz', 'Fabisiak', 'arfa', NULL);     --    Y     -      3      -
INSERT INTO developer VALUES (6, 'kaja@email.pl', 'Kacper', 'Janas', 'kaja', NULL);           --    -     2      2      -
INSERT INTO developer VALUES (7, 'wafr@email.pl', 'Warcis³aw', 'Frydrych', 'wafr', NULL);     --    Y     -      1,3    -
INSERT INTO developer VALUES (8, 'miwi@email.pl', 'Mi³osz', 'Wielgus', 'miwi', NULL);         --    -     -      2      -
INSERT INTO developer VALUES (9, 'trba@email.pl', 'Trzebowit', 'Bartosiak', 'trba', NULL);    --    -     -      1      -
INSERT INTO developer VALUES (10, 'miwa@email.pl', 'Mirella', 'Wachowiak', 'miwa', NULL);     --    -     -      2      1
INSERT INTO developer VALUES (11, 'grjo@email.pl', 'Grzesiak', 'Job', 'grjo', NULL);          --    -     -      1      -
INSERT INTO developer VALUES (12, 'anra@email.pl', 'Angelus', 'R¹czka', 'anra', NULL);        --    -     -      2      -
INSERT INTO developer VALUES (13, 'cyru@email.pl', 'Cyryl', 'Rusin', 'cyru', NULL);           --    -     -      1      -
INSERT INTO developer VALUES (14, 'jafr@email.pl', 'Jazon', 'Fr¹ckowiak', 'jafr', NULL);      --    -     1      1      -
INSERT INTO developer VALUES (15, 'grli@email.pl', 'Gra¿yna', 'Lipowski', 'grli', NULL);      --    -     -      1      2
select setval('DEVELOPER_ID_SEQ', 16, true);

-- ADMINISTRATORZY
INSERT INTO admin VALUES (1, 5);
INSERT INTO admin VALUES (2, 7);
select setval('admin_ID_SEQ', 3, true);

-- SCRUM MASTERZY
INSERT INTO scrum_master VALUES (1, 6);
INSERT INTO scrum_master VALUES (2, 14);
select setval('scrum_master_ID_SEQ', 3, true);

-- HAS£A
INSERT INTO password VALUES (1, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 1);
INSERT INTO password VALUES (2, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 2);
INSERT INTO password VALUES (3, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 3);
INSERT INTO password VALUES (4, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 4);
INSERT INTO password VALUES (5, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 5);
INSERT INTO password VALUES (6, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 6);
INSERT INTO password VALUES (7, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 7);
INSERT INTO password VALUES (8, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 8);
INSERT INTO password VALUES (9, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 9);
INSERT INTO password VALUES (10, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 10);
INSERT INTO password VALUES (11, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 11);
INSERT INTO password VALUES (12, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 12);
INSERT INTO password VALUES (13, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 13);
INSERT INTO password VALUES (14, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 14);
INSERT INTO password VALUES (15, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 15);
select setval('password_ID_SEQ', 16, true);

-- TEAMY
INSERT INTO team VALUES (1, 'Fumfle');
INSERT INTO team VALUES (2, 'Nikifory');
INSERT INTO team VALUES (3, 'Administratorzy');
select setval('team_ID_SEQ', 4, true);

-- DEVELOPER TEAM
INSERT INTO team_developer VALUES (1, 1);
INSERT INTO team_developer VALUES (2, 2);
INSERT INTO team_developer VALUES (1, 3);
INSERT INTO team_developer VALUES (2, 4);
INSERT INTO team_developer VALUES (3, 5);
INSERT INTO team_developer VALUES (2, 6);
INSERT INTO team_developer VALUES (1, 7);
INSERT INTO team_developer VALUES (2, 7);
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
select setval('backlog_ID_SEQ', 3, true);

-- PROJEKTY
INSERT INTO project VALUES ('reco', '2015-11-17 12:21:13 ', 'Aplikacja umo¿liwaiaj¹c zdalne po³¹czenie siê z komputerem', 'Opis aplikacji umo¿liwiaj¹cej ³¹czenie siê z komputerem', 'Remote connector', 1);
INSERT INTO project VALUES ('pote', '2016-01-15 12:21:13 ', 'Framework do testowania obiektów POJO', 'Opis frameworku do testowania obiektów POJO', 'Pojo tester', 2);

-- PRODUCT OWNERZY
INSERT INTO product_owner VALUES (1, 10, 'reco');
INSERT INTO product_owner VALUES (2, 15, 'pote');
select setval('product_owner_ID_SEQ', 3, true);

--RETROSPEKTYWY
INSERT INTO retrospective VALUES (1, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (2, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (3, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (4, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (5, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (6, 'Opis retrospektywy 1');
select setval('retrospective_ID_SEQ', 7, true);

-- SPRINTY
INSERT INTO sprint VALUES (1, 'Implementacja story 1-5', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 'reco', 1);
INSERT INTO sprint VALUES (2, 'Testy systemowe', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 'reco', 2);
INSERT INTO sprint VALUES (3, 'Release wersji 1.0', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 'reco', 3);
INSERT INTO sprint VALUES (4, 'Rozpoznanie œrodowisk', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 'pote', 4);
INSERT INTO sprint VALUES (5, 'Implementacja', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 'pote', 5);
INSERT INTO sprint VALUES (6, 'Testy systemowe', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 'pote', 6);
select setval('sprint_ID_SEQ', 7, true);

UPDATE project SET currentsprint_id=1 WHERE project.key = 'reco';
UPDATE project SET currentsprint_id=4 WHERE project.key = 'pote';


-- TEAMY DO PROJEKTU
INSERT INTO team_project VALUES (1, 'reco');
INSERT INTO team_project VALUES (2, 'pote');

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
INSERT INTO story VALUES (8, 'Definition of Done dla story', 'Optional name8', '2', 4);
INSERT INTO story VALUES (9, 'Definition of Done dla story', 'Optional name9', '3', 3);
select setval('story_ID_SEQ', 10, true);

-- RODZAJE ISSUE
INSERT INTO issue_type VALUES (1, 'Task');
INSERT INTO issue_type VALUES (2, 'Feature');
INSERT INTO issue_type VALUES (3, 'Bug');
INSERT INTO issue_type VALUES (4, 'Improvement');
select setval('issue_Type_ID_SEQ', 5, true);

-- PRIORYTETY
INSERT INTO priority VALUES (1, 'Wysoki');
INSERT INTO priority VALUES (2, 'Œredni');
INSERT INTO priority VALUES (3, 'Niski');
INSERT INTO priority VALUES (4, 'Ma³y');
select setval('priority_ID_SEQ', 5, true);

-- STANY
INSERT INTO state VALUES (1, 'TO DO');
INSERT INTO state VALUES (2, 'IN PROGRES');
INSERT INTO state VALUES (3, 'DONE');
select setval('state_ID_SEQ', 4, true);

-- ISSUES
INSERT INTO issue VALUES (1, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote1', NULL, 1, 1, 1, 1);
INSERT INTO issue VALUES (2, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote2', 1, 2, 1, 1, 1);
INSERT INTO issue VALUES (3, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote3', NULL, 3, 1, 11, 1);
INSERT INTO issue VALUES (4, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote4', 2, 4, 1, 1, 1);
INSERT INTO issue VALUES (5, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote5', 3, 1, 1, 10, 1);
INSERT INTO issue VALUES (6, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote6-todo', 4, 2, 1, 1, 1);
INSERT INTO issue VALUES (7, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote7-todo', 5, 3, 1, 12, 1);
INSERT INTO issue VALUES (13, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote13-todo', 11, 1, 1, 6, 1);
INSERT INTO issue VALUES (14, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote14-todo', 2, 2, 1, 5, 1);
INSERT INTO issue VALUES (15, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote15-done', 11, 3, 1, 4, 3);
INSERT INTO issue VALUES (16, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote16-done', 3, 4, 1, 3, 3);
INSERT INTO issue VALUES (17, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote17-done', 1, 1, 1, 2, 3);
INSERT INTO issue VALUES (21, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote21-inprogres', NULL, 1, 1, 15, 2);
INSERT INTO issue VALUES (22, '2015-01-01 12:00:00', 'DoD', 'Description', 'pote', 'pote22-inprogres', 13, 2, 1, 1, 2);

INSERT INTO issue VALUES (18, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco18-todo', 12, 2, 1, 1, 1);
INSERT INTO issue VALUES (19, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco19-inprogres', NULL, 3, 1, 1, 2);
INSERT INTO issue VALUES (20, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco20-done', 13, 4, 1, 1, 3);

INSERT INTO issue VALUES (8, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco8-todo', 6, 4, 1, 13, 1);
INSERT INTO issue VALUES (9, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco9-todo', 7, 1, 1, 14, 1);
INSERT INTO issue VALUES (10, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco10-inprogres', 8, 2, 1, 1, 2);
INSERT INTO issue VALUES (11, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco11-inprogres', 9, 3, 1, 8, 2);
INSERT INTO issue VALUES (12, '2015-01-01 12:00:00', 'DoD', 'Description', 'reco', 'reco12-done', 10, 4, 1, 7, 3);
select setval('issue_ID_SEQ', 23, true);

-- KOMENTARZE
INSERT INTO comment VALUES (1,  'Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 Komentarz 1 ', '2015-02-02 12:00:00', 1);
INSERT INTO comment VALUES (2,  'Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 Komentarz 2 ', '2015-02-02 12:00:00', 2);
INSERT INTO comment VALUES (3,  'Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 Komentarz 3 ', '2015-02-02 12:00:00', 3);
INSERT INTO comment VALUES (4,  'Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 Komentarz 4 ', '2015-02-02 12:00:00', 4);
INSERT INTO comment VALUES (5,  'Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 Komentarz 5 ', '2015-02-02 12:00:00', 5);
INSERT INTO comment VALUES (6,  'Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 Komentarz 6 ', '2015-02-02 12:00:00', 6);
INSERT INTO comment VALUES (7,  'Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 Komentarz 7 ', '2015-02-02 12:00:00', 7);
INSERT INTO comment VALUES (8,  'Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 Komentarz 8 ', '2015-02-02 12:00:00', 8);
INSERT INTO comment VALUES (9,  'Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 Komentarz 9 ', '2015-02-02 12:00:00', 9);
INSERT INTO comment VALUES (10, 'Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10Komentarz 10', '2015-02-02 12:00:00', 10);
INSERT INTO comment VALUES (11, 'Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11Komentarz 11', '2015-02-02 12:00:00', 11);
INSERT INTO comment VALUES (12, 'Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12Komentarz 12', '2015-02-02 12:00:00', 12);
INSERT INTO comment VALUES (13, 'Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13Komentarz 13', '2015-02-02 12:00:00', 13);
INSERT INTO comment VALUES (14, 'Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14Komentarz 14', '2015-02-02 12:00:00', 14);
select setval('comment_ID_SEQ', 30, true);

-- KOMENTARZE DO ISSE
INSERT INTO issue_comment VALUES (9, 1);
INSERT INTO issue_comment VALUES (9, 2);
INSERT INTO issue_comment VALUES (9, 3);
INSERT INTO issue_comment VALUES (9, 4);
INSERT INTO issue_comment VALUES (9, 5);
INSERT INTO issue_comment VALUES (9, 6);
INSERT INTO issue_comment VALUES (9, 7);
INSERT INTO issue_comment VALUES (9, 8);
INSERT INTO issue_comment VALUES (9, 9);

-- KOMENTARZE DO RETROSPEKTYWY
INSERT INTO retrospective_comment VALUES (1, 10);
INSERT INTO retrospective_comment VALUES (1, 11);
INSERT INTO retrospective_comment VALUES (1, 12);
INSERT INTO retrospective_comment VALUES (2, 13);
INSERT INTO retrospective_comment VALUES (2, 14);

-- ISSUE DO BACKLOGA
INSERT INTO backlog_issue VALUES (2, 4);
INSERT INTO backlog_issue VALUES (2, 5);
INSERT INTO backlog_issue VALUES (2, 6);
INSERT INTO backlog_issue VALUES (2, 7);

-- ISSUE DO STORY
INSERT INTO story_issue VALUES (1, 8);
INSERT INTO story_issue VALUES (1, 9);
INSERT INTO story_issue VALUES (1, 10);
INSERT INTO story_issue VALUES (1, 11);
INSERT INTO story_issue VALUES (1, 12);
INSERT INTO story_issue VALUES (4, 13);
INSERT INTO story_issue VALUES (4, 14);
INSERT INTO story_issue VALUES (4, 15);
INSERT INTO story_issue VALUES (4, 16);
INSERT INTO story_issue VALUES (4, 17);
INSERT INTO story_issue VALUES (7, 18);
INSERT INTO story_issue VALUES (7, 19);
INSERT INTO story_issue VALUES (7, 20);
INSERT INTO story_issue VALUES (8, 21);
INSERT INTO story_issue VALUES (8, 22);

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
select setval('role_ID_SEQ', 5, true);

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