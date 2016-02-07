CREATE SEQUENCE admin_id_seq START WITH 1;

CREATE SEQUENCE backlog_id_seq START WITH 1;

CREATE SEQUENCE comment_id_seq START WITH 1;

CREATE SEQUENCE developer_id_seq START WITH 1;

CREATE SEQUENCE issue_id_seq START WITH 1;

CREATE SEQUENCE issue_type_id_seq START WITH 1;

CREATE SEQUENCE password_id_seq START WITH 1;

CREATE SEQUENCE picture_id_seq START WITH 1;

CREATE SEQUENCE priority_id_seq START WITH 1;

CREATE SEQUENCE product_owner_id_seq START WITH 1;

CREATE SEQUENCE retrospective_id_seq START WITH 1;

CREATE SEQUENCE role_id_seq START WITH 1;

CREATE SEQUENCE scrum_master_id_seq START WITH 1;

CREATE SEQUENCE sprint_id_seq START WITH 1;

CREATE SEQUENCE state_id_seq START WITH 1;

CREATE SEQUENCE story_id_seq START WITH 1;

CREATE SEQUENCE team_id_seq START WITH 1;

CREATE TABLE backlog ( 
	id                   serial  NOT NULL,
	CONSTRAINT backlog_pkey PRIMARY KEY ( id )
 );

CREATE TABLE issue_type ( 
	id                   serial  NOT NULL,
	name                 varchar(32)  NOT NULL,
	CONSTRAINT issue_type_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_ff22b26a31m6rkn70y5h208ju UNIQUE ( name ) 
 );

CREATE TABLE picture ( 
	id                   serial  NOT NULL,
	data                 bytea,
	name                 varchar(255)  NOT NULL,
	CONSTRAINT picture_pkey PRIMARY KEY ( id )
 );

CREATE TABLE priority ( 
	id                   serial  NOT NULL,
	name                 varchar(32)  NOT NULL,
	CONSTRAINT priority_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_ebq5ppp7ts6rnb1e2rf152am2 UNIQUE ( name ) 
 );

CREATE TABLE retrospective ( 
	id                   serial  NOT NULL,
	description          varchar(4096)  ,
	CONSTRAINT retrospective_pkey PRIMARY KEY ( id )
 );

CREATE TABLE retrospectiveentity_retrospectiveitemembeddables ( 
	retrospectiveentity_id integer  NOT NULL,
	description          varchar(256)  NOT NULL,
	rate                 integer  NOT NULL,
	retrospectiveitemtypeenum varchar(255)  
 );

CREATE TABLE "role" ( 
	id                   serial  NOT NULL,
	roletype             varchar(255)  NOT NULL,
	CONSTRAINT role_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_oaoxefkufyo39d7uxxhiahri0 UNIQUE ( roletype ) 
 );

CREATE TABLE "state" ( 
	id                   serial  NOT NULL,
	name                 varchar(32)  NOT NULL,
	CONSTRAINT state_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_2g0hi7w44i4sjkffh61pusaav UNIQUE ( name ) 
 );

CREATE TABLE team ( 
	id                   serial  NOT NULL,
	name                 varchar(30)  NOT NULL,
	CONSTRAINT team_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_g2l9qqsoeuynt4r5ofdt1x2td UNIQUE ( name ) 
 );

CREATE TABLE developer ( 
	id                   serial  NOT NULL,
	email                varchar(40)  NOT NULL,
	firstname            varchar(30)  NOT NULL,
	surname              varchar(30)  NOT NULL,
	username             varchar(20)  NOT NULL,
	pictureentity_id     integer  ,
	CONSTRAINT developer_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_7he3e2kpo85x20u5oefpkddt8 UNIQUE ( username ) ,
	CONSTRAINT uk_chmq1wdfnhjthxo65affwdnky UNIQUE ( email ) 
 );

CREATE TABLE issue ( 
	id                   serial  NOT NULL,
	creationdate         timestamp  NOT NULL,
	definitionofdone     varchar(4096)  ,
	description          varchar(4096)  ,
	projectkey           varchar(8)  NOT NULL,
	summary              varchar(255)  NOT NULL,
	assignee_id          integer  ,
	issuetypeentity_id   integer  NOT NULL,
	priorityentity_id    integer  NOT NULL,
	reporter_id          integer  ,
	stateentity_id       integer  NOT NULL,
	CONSTRAINT issue_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_kkydtsoyud1ive53mp74cbmis UNIQUE ( summary ) 
 );

CREATE TABLE "password" ( 
	id                   serial  NOT NULL,
	"password"           varchar(1024)  NOT NULL,
	developerentity_id   integer  NOT NULL,
	CONSTRAINT password_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_rn663kuircghjxn0di5y2qswb UNIQUE ( developerentity_id ) 
 );

CREATE TABLE role_developer ( 
	role_id              integer  NOT NULL,
	developerentities_id integer  NOT NULL
 );

CREATE TABLE scrum_master ( 
	id                   serial  NOT NULL,
	developerentity_id   integer  NOT NULL,
	CONSTRAINT scrum_master_pkey PRIMARY KEY ( id )
 );

CREATE TABLE scrum_master_team ( 
	scrum_master_id      integer  NOT NULL,
	teamentities_id      integer  NOT NULL,
	CONSTRAINT uk_11b854q9daaddoj34jw85kbmn UNIQUE ( teamentities_id ) 
 );

CREATE TABLE team_developer ( 
	team_id              integer  NOT NULL,
	developerentities_id integer  NOT NULL
 );

CREATE TABLE "admin" ( 
	id                   serial  NOT NULL,
	developerentity_id   integer  NOT NULL,
	CONSTRAINT admin_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_sf4xhbwfg9cqdhv17arl86vkt UNIQUE ( developerentity_id ) 
 );

CREATE TABLE backlog_issue ( 
	backlog_id           integer  NOT NULL,
	issueentities_id     integer  NOT NULL,
	CONSTRAINT uk_m1rg8dgd2ask48kwv9r7t5cco UNIQUE ( issueentities_id ) 
 );

CREATE TABLE "comment" ( 
	id                   serial  NOT NULL,
	commentbody          varchar(4096)  NOT NULL,
	creationdate         timestamp  NOT NULL,
	developerentity_id   integer  ,
	CONSTRAINT comment_pkey PRIMARY KEY ( id )
 );

CREATE TABLE issue_comment ( 
	issue_id             integer  NOT NULL,
	commententities_id   integer  NOT NULL,
	CONSTRAINT uk_tjjtrd2o9idt4c6cw69hsa9g3 UNIQUE ( commententities_id ) 
 );

CREATE TABLE retrospective_comment ( 
	retrospective_id     integer  NOT NULL,
	commententities_id   integer  NOT NULL,
	CONSTRAINT uk_chhr1pxq3xue4gp597c29qf94 UNIQUE ( commententities_id ) 
 );

CREATE TABLE product_owner ( 
	id                   serial  NOT NULL,
	developerentity_id   integer  NOT NULL,
	projectentity_key    varchar(8)  ,
	CONSTRAINT product_owner_pkey PRIMARY KEY ( id ),
	CONSTRAINT uk_r0yrt2inlmcglvq8l1j0j2de3 UNIQUE ( developerentity_id ) 
 );

CREATE TABLE project ( 
	"key"                varchar(8)  NOT NULL,
	creationdate         timestamp  NOT NULL,
	definitionofdone     varchar(4096)  ,
	description          varchar(4096)  ,
	name                 varchar(255)  NOT NULL,
	backlogentity_id     integer  ,
	currentsprint_id     integer  ,
	CONSTRAINT project_pkey PRIMARY KEY ( "key" )
 );

CREATE TABLE sprint ( 
	id                   serial  NOT NULL,
	definitionofdone     varchar(4096)  ,
	name                 varchar(64)  NOT NULL,
	enddate              timestamp  NOT NULL,
	startdate            timestamp  NOT NULL,
	project_key          varchar(8)  NOT NULL,
	retrospectiveentity_id integer  ,
	CONSTRAINT sprint_pkey PRIMARY KEY ( id )
 );

CREATE TABLE story ( 
	id                   serial  NOT NULL,
	definitionofdone     varchar(4096)  ,
	name                 varchar(255)  NOT NULL,
	points               integer  ,
	sprint_id            integer  NOT NULL,
	CONSTRAINT story_pkey PRIMARY KEY ( id )
 );

CREATE TABLE story_issue ( 
	story_id             integer  NOT NULL,
	issueentities_id     integer  NOT NULL,
	CONSTRAINT uk_8oh6on6ryhctr5ikb8qsw3njf UNIQUE ( issueentities_id ) 
 );

CREATE TABLE team_project ( 
	team_id              integer  NOT NULL,
	projectentities_key  varchar(8)  NOT NULL
 );

ALTER TABLE "admin" ADD CONSTRAINT fk_sf4xhbwfg9cqdhv17arl86vkt FOREIGN KEY ( developerentity_id ) REFERENCES developer( id );

ALTER TABLE backlog_issue ADD CONSTRAINT fk_bqcllrs8od5qqnfel335g0xor FOREIGN KEY ( backlog_id ) REFERENCES backlog( id );

ALTER TABLE backlog_issue ADD CONSTRAINT fk_m1rg8dgd2ask48kwv9r7t5cco FOREIGN KEY ( issueentities_id ) REFERENCES issue( id );

ALTER TABLE "comment" ADD CONSTRAINT fk_m8c4ulwdkyrwtdgkrr6i1tq1x FOREIGN KEY ( developerentity_id ) REFERENCES developer( id );

ALTER TABLE developer ADD CONSTRAINT fk_qgidu3c7e9agkc8wejjpbigt1 FOREIGN KEY ( pictureentity_id ) REFERENCES picture( id );

ALTER TABLE issue ADD CONSTRAINT fk_13ifiqjxslqe7ess5di7ytpv0 FOREIGN KEY ( assignee_id ) REFERENCES developer( id );

ALTER TABLE issue ADD CONSTRAINT fk_2olhj8ayt8u4uld98d6hvbq3o FOREIGN KEY ( reporter_id ) REFERENCES developer( id );

ALTER TABLE issue ADD CONSTRAINT fk_4vov6e416tnd5q3d0lebqxhkb FOREIGN KEY ( issuetypeentity_id ) REFERENCES issue_type( id );

ALTER TABLE issue ADD CONSTRAINT fk_h8w0r074e5wpuat1vmk2oqqg FOREIGN KEY ( priorityentity_id ) REFERENCES priority( id );

ALTER TABLE issue ADD CONSTRAINT fk_9y0gkt0sg3tt7caofsy786n71 FOREIGN KEY ( stateentity_id ) REFERENCES "state"( id );

ALTER TABLE issue_comment ADD CONSTRAINT fk_tjjtrd2o9idt4c6cw69hsa9g3 FOREIGN KEY ( commententities_id ) REFERENCES "comment"( id );

ALTER TABLE issue_comment ADD CONSTRAINT fk_nsfsbf6ndamn60heict6xr1ne FOREIGN KEY ( issue_id ) REFERENCES issue( id );

ALTER TABLE "password" ADD CONSTRAINT fk_rn663kuircghjxn0di5y2qswb FOREIGN KEY ( developerentity_id ) REFERENCES developer( id );

ALTER TABLE product_owner ADD CONSTRAINT fk_r0yrt2inlmcglvq8l1j0j2de3 FOREIGN KEY ( developerentity_id ) REFERENCES developer( id );

ALTER TABLE product_owner ADD CONSTRAINT fk_40boib6k58ncc3si0ern7sfup FOREIGN KEY ( projectentity_key ) REFERENCES project( "key" );

ALTER TABLE project ADD CONSTRAINT fk_flh640v2f1pqyu17voqe7hfdw FOREIGN KEY ( backlogentity_id ) REFERENCES backlog( id );

ALTER TABLE project ADD CONSTRAINT fk_e138ehwmpv7yqoa7stolq8j6d FOREIGN KEY ( currentsprint_id ) REFERENCES sprint( id );

ALTER TABLE retrospective_comment ADD CONSTRAINT fk_chhr1pxq3xue4gp597c29qf94 FOREIGN KEY ( commententities_id ) REFERENCES "comment"( id );

ALTER TABLE retrospective_comment ADD CONSTRAINT fk_oxvkp3ptek4p50esvfwcn65 FOREIGN KEY ( retrospective_id ) REFERENCES retrospective( id );

ALTER TABLE retrospectiveentity_retrospectiveitemembeddables ADD CONSTRAINT fk_f5xfw9dvq6odgb0ftsxll7cs3 FOREIGN KEY ( retrospectiveentity_id ) REFERENCES retrospective( id );

ALTER TABLE role_developer ADD CONSTRAINT fk_g98he8ukvp5kg0uojcuxnwnw5 FOREIGN KEY ( developerentities_id ) REFERENCES developer( id );

ALTER TABLE role_developer ADD CONSTRAINT fk_t0is9mum0dc806510k8a1kc0a FOREIGN KEY ( role_id ) REFERENCES "role"( id );

ALTER TABLE scrum_master ADD CONSTRAINT fk_22od8ts9n7koubgce483thrvp FOREIGN KEY ( developerentity_id ) REFERENCES developer( id );

ALTER TABLE scrum_master_team ADD CONSTRAINT fk_poci3yjn2w39j4r1nm2j4bdm5 FOREIGN KEY ( scrum_master_id ) REFERENCES scrum_master( id );

ALTER TABLE scrum_master_team ADD CONSTRAINT fk_11b854q9daaddoj34jw85kbmn FOREIGN KEY ( teamentities_id ) REFERENCES team( id );

ALTER TABLE sprint ADD CONSTRAINT fk_lrgu1v003v3gxsmc9par6rkd5 FOREIGN KEY ( project_key ) REFERENCES project( "key" );

ALTER TABLE sprint ADD CONSTRAINT fk_hd1les9ljoagg8cepb9tjiuq6 FOREIGN KEY ( retrospectiveentity_id ) REFERENCES retrospective( id );

ALTER TABLE story ADD CONSTRAINT fk_gsomcx84uhavse5vnt8dvdv6a FOREIGN KEY ( sprint_id ) REFERENCES sprint( id );

ALTER TABLE story_issue ADD CONSTRAINT fk_8oh6on6ryhctr5ikb8qsw3njf FOREIGN KEY ( issueentities_id ) REFERENCES issue( id );

ALTER TABLE story_issue ADD CONSTRAINT fk_ky07vcpr862jax0wpxtilda4x FOREIGN KEY ( story_id ) REFERENCES story( id );

ALTER TABLE team_developer ADD CONSTRAINT fk_fwrphpx6hfynsee1qqkx8gl49 FOREIGN KEY ( developerentities_id ) REFERENCES developer( id );

ALTER TABLE team_developer ADD CONSTRAINT fk_b9yip5ju0p0wwml67qfdebcyi FOREIGN KEY ( team_id ) REFERENCES team( id );

ALTER TABLE team_project ADD CONSTRAINT fk_ib4fc2sgusb1ahsbbwfpgq7fy FOREIGN KEY ( projectentities_key ) REFERENCES project( "key" );

ALTER TABLE team_project ADD CONSTRAINT fk_plf3x56hac017x6dsx8dxkpdg FOREIGN KEY ( team_id ) REFERENCES team( id );


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
select setval('DEVELOPER_ID_SEQ', 16, true);

-- ADMINISTRATORZY
INSERT INTO admin VALUES (1, 5);
INSERT INTO admin VALUES (2, 7);
select setval('admin_ID_SEQ', 3, true);

-- SCRUM MASTERZY
INSERT INTO scrum_master VALUES (1, 6);
INSERT INTO scrum_master VALUES (2, 14);
select setval('scrum_master_ID_SEQ', 3, true);

-- HASŁA
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
INSERT INTO project VALUES ('reco', '2015-11-17 12:21:13 ', 'Aplikacja umożliwaiając zdalne połączenie się z komputerem', 'Opis aplikacji umożliwiającej łączenie się z komputerem', 'Remote connector', 1);
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
INSERT INTO sprint VALUES (4, 'Rozpoznanie środowisk', 'Sprint 1', '2015-11-24 12:00:00', '2015-11-17 12:00:00', 'pote', 4);
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
INSERT INTO priority VALUES (2, 'Średni');
INSERT INTO priority VALUES (3, 'Niski');
INSERT INTO priority VALUES (4, 'Mały');
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