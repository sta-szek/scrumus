------------------------------------------------- TABELE UŻYTKOWNIKÓW -------------------------------------------------

-- DEVELOPER TABLE
INSERT INTO developer VALUES (1, 'jano@email.pl', 'Jan', 'Nowak', 'jano', NULL);
INSERT INTO developer VALUES (2, 'szgo@email.pl', 'Szymon', 'Górnik', 'szgo', NULL);
INSERT INTO developer VALUES (3, 'alto@email.pl', 'Alfred', 'Tomczak', 'alto', NULL);
INSERT INTO developer VALUES (4, 'domo@email.pl', 'Donata', 'Momot', 'domo', NULL);
INSERT INTO developer VALUES (5, 'arfa@email.pl', 'Arseniusz', 'Fabisiak', 'arfa', NULL);
INSERT INTO developer VALUES (6, 'kaja@email.pl', 'Kacper', 'Janas', 'kaja', NULL);
INSERT INTO developer VALUES (7, 'wafr@email.pl', 'Warcisław', 'Frydrych', 'wafr', NULL);
INSERT INTO developer VALUES (8, 'miwi@email.pl', 'Miłosz', 'Wielgus', 'miwi', NULL);
INSERT INTO developer VALUES (9, 'trba@email.pl', 'Trzebowit', 'Bartosiak', 'trba', NULL);
INSERT INTO developer VALUES (10, 'miwa@email.pl', 'Mirella', 'Wachowiak', 'miwa', NULL);
INSERT INTO developer VALUES (11, 'grjo@email.pl', 'Grzesiak', 'Job', 'grjo', NULL);
INSERT INTO developer VALUES (12, 'anra@email.pl', 'Angelus', 'Rączka', 'anra', NULL);
INSERT INTO developer VALUES (13, 'cyru@email.pl', 'Cyryl', 'Rusin', 'cyru', NULL);
INSERT INTO developer VALUES (14, 'jafr@email.pl', 'Jazon', 'Frąckowiak', 'jafr', NULL);
INSERT INTO developer VALUES (15, 'grli@email.pl', 'Grażyna', 'Lipowski', 'grli', NULL);

-- ADMINISTRATORZY
INSERT INTO admin VALUES (1, 5);
INSERT INTO admin VALUES (2, 7);

-- SCRUM MASTERZY
INSERT INTO scrummaster VALUES (1, 6);
INSERT INTO scrummaster VALUES (2, 14);

-- HASŁA
INSERT INTO password VALUES (1, 'GH34FGH', 1);
INSERT INTO password VALUES (2, 'GH34FGH', 2);
INSERT INTO password VALUES (3, 'GH34FGH', 3);
INSERT INTO password VALUES (4, 'GH34FGH', 4);
INSERT INTO password VALUES (5, 'GH34FGH', 5);
INSERT INTO password VALUES (6, 'GH34FGH', 6);
INSERT INTO password VALUES (7, 'GH34FGH', 7);
INSERT INTO password VALUES (8, 'GH34FGH', 8);
INSERT INTO password VALUES (9, 'GH34FGH', 9);
INSERT INTO password VALUES (10, 'GH34FGH', 10);
INSERT INTO password VALUES (11, 'GH34FGH', 11);
INSERT INTO password VALUES (12, 'GH34FGH', 12);
INSERT INTO password VALUES (13, 'GH34FGH', 13);
INSERT INTO password VALUES (14, 'GH34FGH', 14);
INSERT INTO password VALUES (15, 'GH34FGH', 15);

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
INSERT INTO project VALUES (1, '2015-11-17 12:21:13 ', 'Aplikacja umożliwaiając zdalne połączenie się z komputerem',
                            'Opis aplikacji umożliwiającej łączenie się z komputerem', 'reco', 'Remote connector', 1);
INSERT INTO project VALUES (2, '2016-01-15 12:21:13 ', 'Framework do testowania obiektów POJO',
                            'Opis frameworku do testowania obiektów POJO', 'pova', 'Pojo validator', 2);

-- PRODUCT OWNERZY
INSERT INTO productowner VALUES (1, 10, 1);
INSERT INTO productowner VALUES (2, 15, 2);

--RETROSPEKTYWY
INSERT INTO retrospective VALUES (1, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (2, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (3, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (4, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (5, 'Opis retrospektywy 1');
INSERT INTO retrospective VALUES (6, 'Opis retrospektywy 1');

-- SPRINTY
INSERT INTO sprint VALUES (1, 'Implementacja story 1-5', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 1);
INSERT INTO sprint VALUES (2, 'Testy systemowe', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 2);
INSERT INTO sprint VALUES (3, 'Release wersji 1.0', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 3);
INSERT INTO sprint VALUES (4, 'Rozpoznanie środowisk', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 4);
INSERT INTO sprint VALUES (5, 'Implementacja', 'Sprint 2', '2015-12-1 12:00:00', '2015-11-24 12:00:00', 5);
INSERT INTO sprint VALUES (6, 'Testy systemowe', 'Sprint 3', '2015-12-8 12:00:00', '2015-12-1 12:00:00', 6);

-- SPRINTY DO PROJEKTU
INSERT INTO project_sprint VALUES (1, 1);
INSERT INTO project_sprint VALUES (1, 2);
INSERT INTO project_sprint VALUES (1, 3);
INSERT INTO project_sprint VALUES (2, 4);
INSERT INTO project_sprint VALUES (2, 5);
INSERT INTO project_sprint VALUES (2, 6);

-- TEAMY DO PROJEKTU
INSERT INTO project_team VALUES (1, 1);
INSERT INTO project_team VALUES (2, 2);

-- SCRUM MASTERZY DO TEAMU
INSERT INTO scrummaster_team VALUES (1, 2);
INSERT INTO scrummaster_team VALUES (2, 1);

-- STORY
INSERT INTO story VALUES (1, 'Definition of Done dla story', 'Optional name1', '1');
INSERT INTO story VALUES (2, 'Definition of Done dla story', 'Optional name2', '2');
INSERT INTO story VALUES (3, 'Definition of Done dla story', 'Optional name3', '3');
INSERT INTO story VALUES (4, 'Definition of Done dla story', 'Optional name4', '5');
INSERT INTO story VALUES (5, 'Definition of Done dla story', 'Optional name5', '8');
INSERT INTO story VALUES (6, 'Definition of Done dla story', 'Optional name6', '13');
INSERT INTO story VALUES (7, 'Definition of Done dla story', 'Optional name7', '1');
INSERT INTO story VALUES (8, 'Definition of Done dla story', 'Optional name8', '2');
INSERT INTO story VALUES (9, 'Definition of Done dla story', 'Optional name9', '3');

-- STORY DO SPRINTU
INSERT INTO sprint_story VALUES (1, 1);
INSERT INTO sprint_story VALUES (2, 2);
INSERT INTO sprint_story VALUES (3, 3);
INSERT INTO sprint_story VALUES (4, 4);
INSERT INTO sprint_story VALUES (5, 5);
INSERT INTO sprint_story VALUES (6, 6);
INSERT INTO sprint_story VALUES (1, 7);
INSERT INTO sprint_story VALUES (2, 8);
INSERT INTO sprint_story VALUES (3, 9);

-- RODZAJE ISSUE
INSERT INTO issuetype VALUES (1, 'Task');
INSERT INTO issuetype VALUES (2, 'Feature');
INSERT INTO issuetype VALUES (3, 'Bug');
INSERT INTO issuetype VALUES (4, 'Improvement');

-- ISSUES
INSERT INTO issue VALUES (1, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-1', 'nazwa', NULL, 1, 1);
INSERT INTO issue VALUES (2, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-2', 'nazwa', 1, 2, 1);
INSERT INTO issue VALUES (3, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-3', 'nazwa', NULL, 3, 11);
INSERT INTO issue VALUES (4, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-4', 'nazwa', 2, 4, 1);
INSERT INTO issue VALUES (5, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-5', 'nazwa', 3, 1, 10);
INSERT INTO issue VALUES (6, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-6', 'nazwa', 4, 2, 1);
INSERT INTO issue VALUES (7, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-7', 'nazwa', 5, 3, 12);
INSERT INTO issue VALUES (8, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-8', 'nazwa', 6, 4, 13);
INSERT INTO issue VALUES (9, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-9', 'nazwa', 7, 1, 14);
INSERT INTO issue VALUES (10, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-10', 'nazwa', 8, 2, 1);
INSERT INTO issue VALUES (11, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-11', 'nazwa', 9, 3, 8);
INSERT INTO issue VALUES (12, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-12', 'nazwa', 10, 4, 7);
INSERT INTO issue VALUES (13, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-13', 'nazwa', 11, 1, 6);
INSERT INTO issue VALUES (14, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-14', 'nazwa', 2, 2, 5);
INSERT INTO issue VALUES (15, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-15', 'nazwa', 11, 3, 4);
INSERT INTO issue VALUES (16, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-16', 'nazwa', 3, 4, 3);
INSERT INTO issue VALUES (17, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-17', 'nazwa', 1, 1, 2);
INSERT INTO issue VALUES (18, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-18', 'nazwa', 12, 2, 1);
INSERT INTO issue VALUES (19, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-19', 'nazwa', NULL, 3, 1);
INSERT INTO issue VALUES (20, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-20', 'nazwa', 13, 4, 1);
INSERT INTO issue VALUES (21, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-21', 'nazwa', NULL, 1, 15);
INSERT INTO issue VALUES (22, '2015-01-01 12:00:00', 'DoD', 'Description', 'pova-22', 'nazwa', 13, 2, 1);

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
INSERT INTO retrospective_retrospectiveitems VALUES (1, 'Cos tam 1', 1, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (2, 'Cos tam 2', 2, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (3, 'Cos tam 3', 3, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (4, 'Cos tam 4', 4, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (5, 'Cos tam 5', 5, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (6, 'Cos tam 6', 6, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (1, 'Cos tam 1', 1, 'PLUS');
INSERT INTO retrospective_retrospectiveitems VALUES (2, 'Cos tam 2', 2, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (3, 'Cos tam 3', 3, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (4, 'Cos tam 4', 4, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (5, 'Cos tam 5', 5, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (6, 'Cos tam 6', 6, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (1, 'Cos tam 1', 1, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (2, 'Cos tam 2', 2, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (3, 'Cos tam 3', 3, 'MINUS');
INSERT INTO retrospective_retrospectiveitems VALUES (4, 'Cos tam 4', 4, 'MINUS');

