DROP DATABASE paper_review;
CREATE DATABASE paper_review;
USE paper_review;

-- Tabella Utente
CREATE TABLE Utente (
                        id_utente INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        cognome VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
);

-- Tabella Conferenza
CREATE TABLE Conferenza (
                            id_conferenza INT AUTO_INCREMENT PRIMARY KEY,
                            nome VARCHAR(100) NOT NULL,
                            descrizione TEXT NOT NULL,
                            data_conferenza DATETIME NOT NULL,
                            location TEXT NOT NULL,
                            metodo_assegnazione ENUM('Broadcast', 'Topic'),
                            metodo_valutazione ENUM('2', '3', '4'),
                            rate_accettazione INT NOT NULL,
                            paper_previsti INT,
                            giorni_preavviso INT NOT NULL DEFAULT(3),
                            scadenza_sottomissione DATETIME NOT NULL,
                            scadenza_revisione DATETIME NOT NULL,
                            scadenza_sottomissione_2 DATETIME NOT NULL,
                            scadenza_editing DATETIME NOT NULL,
                            scadenza_sottomissione_3 DATETIME NOT NULL,
                            scadenza_impaginazione DATETIME NOT NULL
);

-- Tabella Notifica
CREATE TABLE Notifica (
                          id_notifica INT AUTO_INCREMENT PRIMARY KEY,
                          data DATETIME NOT NULL,
                          testo TEXT NOT NULL,
                          isLetta BOOLEAN NOT NULL,
                          ref_utente INT NOT NULL,
                          ref_conferenza INT NOT NULL,
                          FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente),
                          FOREIGN KEY (ref_conferenza) REFERENCES Conferenza(id_conferenza)
);

-- Tabella Invito
CREATE TABLE Invito (
                        id_invito INT AUTO_INCREMENT PRIMARY KEY,
                        data DATETIME NOT NULL,
                        testo TEXT NOT NULL,
                        status ENUM('Inviato', 'Accettato', 'Rifiutato') NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        codice VARCHAR(100) NOT NULL UNIQUE,
                        ref_conferenza INT NOT NULL,
                        ref_mittente INT NOT NULL,
                        ref_destinatario INT,
                        FOREIGN KEY (ref_conferenza) REFERENCES Conferenza(id_conferenza),
                        FOREIGN KEY (ref_mittente) REFERENCES Utente(id_utente),
                        FOREIGN KEY (ref_destinatario) REFERENCES Utente(id_utente)
);

-- Tabella Topic
CREATE TABLE Topic (
                       id_topic INT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL
);

-- Tabella Paper
CREATE TABLE Paper (
                       id_paper INT AUTO_INCREMENT PRIMARY KEY,
                       titolo VARCHAR(200) NOT NULL,
                       abstract TEXT,
                       file LONGBLOB,
                       data_sottomissione DATETIME,
                       ref_utente INT NOT NULL,
                       ref_conferenza INT NOT NULL,
                       FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente),
                       FOREIGN KEY (ref_conferenza) REFERENCES Conferenza(id_conferenza)
);

-- Tabella Revisione
CREATE TABLE Revisione (
                           id_revisione INT AUTO_INCREMENT PRIMARY KEY,
                           testo TEXT,
                           valutazione INT,
                           data_sottomissione DATETIME,
                           punti_forza TEXT,
                           punti_debolezza TEXT,
                           commento_chair TEXT,
                           ref_utente INT NOT NULL,
                           ref_paper INT NOT NULL,
                           FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente),
                           FOREIGN KEY (ref_paper) REFERENCES Paper(id_paper)
);

-- Tabella Proceeding
CREATE TABLE Proceeding (
                            id_proceeding INT AUTO_INCREMENT PRIMARY KEY,
                            titolo VARCHAR(200) NOT NULL,
                            data_sottomissione DATETIME,
                            ref_utente INT NOT NULL,
                            ref_conferenza INT NOT NULL,
                            FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente),
                            FOREIGN KEY (ref_conferenza) REFERENCES Conferenza(id_conferenza)
);

-- Tabella Ruolo_conferenza
CREATE TABLE Ruolo_conferenza (
                                  id_ruolo INT AUTO_INCREMENT PRIMARY KEY,
                                  ruolo ENUM('Chair', 'Revisore', 'Sottorevisore', 'Autore', 'Editor') NOT NULL,
                                  ref_utente INT NOT NULL,
                                  ref_conferenza INT NOT NULL,
                                  FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente),
                                  FOREIGN KEY (ref_conferenza) REFERENCES Conferenza(id_conferenza)
);

-- Tabella TopicUtente
CREATE TABLE TopicUtente (
                             id_topic_utente INT AUTO_INCREMENT PRIMARY KEY,
                             ref_topic INT NOT NULL,
                             ref_utente INT NOT NULL,
                             FOREIGN KEY (ref_topic) REFERENCES Topic(id_topic),
                             FOREIGN KEY (ref_utente) REFERENCES Utente(id_utente)
);

-- Tabella TopicPaper
CREATE TABLE TopicPaper (
                            id_topic_paper INT AUTO_INCREMENT PRIMARY KEY,
                            ref_topic INT NOT NULL,
                            ref_paper INT NOT NULL,
                            FOREIGN KEY (ref_topic) REFERENCES Topic(id_topic),
                            FOREIGN KEY (ref_paper) REFERENCES Paper(id_paper)
);

-- Tabella CoAutoriPaper
CREATE TABLE CoAutoriPaper (
                               id_CoAutoriPaper INT AUTO_INCREMENT PRIMARY KEY,
                               email VARCHAR(100) NOT NULL,
                               ref_paper INT NOT NULL,
                               FOREIGN KEY (ref_paper) REFERENCES Paper(id_paper)
);


INSERT INTO Utente (nome, cognome, email, password) VALUES
                                                        ('Diego', 'Corona', 'diecoro2003@gmail.com', 'IdS30!'),
                                                        ('Leonardo Giovanni', 'Caiezza', 'leonardo.giovanni.caiezza@community.unipa.it', 'IdS30!'),
                                                        ('Luca', 'Gaetani', 'luca.gaetani@community.unipa.it', 'IdS30!'),
                                                        ('Daniele Orazio', 'Susino', 'susino.daniele@outlook.com', 'GayStup1d0!'),
                                                        ('Francesco', 'Russo', 'francesco.russo@community.unipa.it', 'IdS30!'),
                                                        ('Alessandro', 'Marini', 'alessandro.marini@community.unipa.it', 'IdS30!'),
                                                        ('Martina', 'Costa', 'martina.costa@community.unipa.it', 'IdS30!'),
                                                        ('Giovanni', 'Verdi', 'giovanni.verdi@community.unipa.it', 'IdS30!'),
                                                        ('Lorenzo', 'Bianchi', 'lorenzo.bianchi@community.unipa.it', 'IdS30!'),
                                                        ('Simone', 'De Luca', 'simone.deluca@community.unipa.it', 'IdS30!');

INSERT INTO Topic (nome) VALUES
                             ('Machine Learning'),
                             ('Cybersecurity'),
                             ('Data Mining'),
                             ('Natural Language Processing'),
                             ('Computer Vision'),
                             ('Big Data Analytics'),
                             ('Banane della Moldovia'),
                             ('Sostenibilità Agricola'),
                             ('Intelligenza Artificiale'),
                             ('Apprendimento Automatico'),
                             ('Crittografia'),
                             ('Genetica Molecolare'),
                             ('Neuroscienze'),
                             ('Psicologia Cognitiva'),
                             ('Psicoanalisi'),
                             ('Chimica Organica'),
                             ('Chimica Analitica'),
                             ('Fisica Quantistica'),
                             ('Astrofisica'),
                             ('Teoria degli Algoritmi'),
                             ('Sistemi Operativi'),
                             ('Linguistica Computazionale'),
                             ('Filologia Romanza'),
                             ('Letteratura Italiana'),
                             ('Comparatistica'),
                             ('Filosofia della Scienza'),
                             ('Filosofia Morale'),
                             ('Antropologia Culturale'),
                             ('Sociologia Digitale'),
                             ('Storia Medievale'),
                             ('Storia Contemporanea'),
                             ('Diritto Costituzionale'),
                             ('Diritto dell’Informatica'),
                             ('Economia Comportamentale'),
                             ('Finanza Quantitativa'),
                             ('Architettura Sostenibile'),
                             ('Urbanistica'),
                             ('Biodiversità Vegetale'),
                             ('Fisiologia delle Piante'),
                             ('Zoologia Evolutiva'),
                             ('Etologia'),
                             ('Ecologia Marina'),
                             ('Climatologia'),
                             ('Robotica Biomedica'),
                             ('Protesi Intelligenti'),
                             ('Ingegneria Meccanica'),
                             ('Ingegneria Elettronica'),
                             ('Musica Elettroacustica'),
                             ('Composizione Musicale'),
                             ('Cinema Digitale'),
                             ('Teoria della Narrazione'),
                             ('Educazione Inclusiva'),
                             ('Didattica Digitale'),
                             ('Big Data'),
                             ('Etica Applicata'),
                             ('Bioetica');


INSERT INTO Conferenza (nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione, paper_previsti, rate_accettazione, scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2, scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione) VALUES
                                                                                                                                                                                                                                                                             ('AI Summit 2025', 'Conferenza internazionale su intelligenza artificiale e machine learning', '2025-09-15 09:00:00', 'Milano', 'Broadcast', '3', 25, 12, '2025-06-01 23:59:59', '2025-06-15 23:59:59', '2025-07-01 23:59:59', '2025-07-15 23:59:59', '2025-08-01 23:59:59', '2025-08-15 23:59:59'),
                                                                                                                                                                                                                                                                             ('Cybersecurity Conf 2025', 'Conferenza sulla sicurezza informatica con esperti del settore', '2025-10-10 10:00:00', 'Roma', 'Topic', '4', 100,25, '2025-07-01 23:59:59', '2025-07-20 23:59:59', '2025-08-01 23:59:59', '2025-08-15 23:59:59', '2025-09-01 23:59:59', '2025-09-10 23:59:59'),
                                                                                                                                                                                                                                                                             ('Banane della Moldovia 2025', 'Conferenza dedicata allo studio delle banane della Moldovia, un caso di eccellenza agricola', '2025-11-01 09:00:00', 'Chisinău', 'Topic', '2', 80, 25, '2025-08-01 23:59:59', '2025-08-15 23:59:59', '2025-09-01 23:59:59', '2025-09-15 23:59:59', '2025-10-01 23:59:59', '2025-10-10 23:59:59');
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
                                                                                               ('Deep Learning per immagini mediche', 'Uso del deep learning per la diagnosi medica tramite immagini', NULL, '2025-05-15 18:00:00', 1, 1),
                                                                                               ('Sicurezza in reti IoT', 'Studio sulle vulnerabilità nelle reti IoT', NULL, '2025-06-01 12:00:00', 2, 2),
                                                                                               ('Analisi dati social media', 'Analisi dei dati dei social media per identificare trend', NULL, '2025-05-20 09:00:00', 3, 3),
                                                                                               ('Robotica per veicoli autonomi', 'Applicazioni della robotica per il controllo di veicoli autonomi', NULL, '2025-06-05 15:30:00', 4, 1),
                                                                                               ('Apprendimento rinforzato per robot', 'Tecniche di apprendimento rinforzato applicate alla robotica', NULL, '2025-06-10 14:00:00', 5, 1),
                                                                                               ('Banane della Moldovia: Studio di caso', 'Studio sulle banane della Moldovia, una risorsa agricola di eccellenza', NULL, '2025-06-15 13:00:00', 6, 3),
                                                                                               ('Sostenibilità agricola e banane', 'Studi sulla sostenibilità nella produzione di banane in Moldovia', NULL, '2025-06-20 14:00:00', 7, 3),
                                                                                               ('Blockchain per la sicurezza alimentare', 'Applicazioni della blockchain nella tracciabilità alimentare', NULL, '2025-07-01 10:30:00', 8, 2),
                                                                                               ('Analisi predittiva per la manutenzione', 'Analisi predittiva nella manutenzione industriale', NULL, '2025-07-05 11:00:00', 9, 1),
                                                                                               ('Cybersecurity nelle applicazioni cloud', 'Approcci avanzati alla sicurezza nelle applicazioni cloud', NULL, '2025-07-10 12:30:00', 10, 2),
                                                                                               ('Visione artificiale per veicoli autonomi', 'Uso della visione artificiale per il miglioramento della sicurezza dei veicoli autonomi', NULL, '2025-07-15 13:30:00', 1, 1),
                                                                                               ('Data Mining per social media', 'Tecniche di data mining applicate ai social media per l\'analisi dei trend', NULL, '2025-07-20 14:00:00', 2, 2),
                                                                                               ('Natural Language Processing per chatbot', 'Tecniche di NLP applicate ai chatbot per il customer service', NULL, '2025-07-25 16:30:00', 3, 1),
                                                                                               ('Big Data per la salute pubblica', 'Utilizzo dei Big Data per migliorare le politiche di salute pubblica', NULL, '2025-08-01 17:00:00', 4, 2),
                                                                                               ('Machine Learning per la sanità', 'Tecniche avanzate di Machine Learning nel settore sanitario', NULL, '2025-08-05 18:00:00', 5, 1),
                                                                                               ('Banane della Moldovia: Approccio agronomico', 'Approccio scientifico alla produzione di banane in Moldovia', NULL, '2025-08-10 09:30:00', 6, 3),
                                                                                               ('Cybersecurity per l\'industria IoT', 'Strategie di sicurezza per il settore IoT industriale', NULL, '2025-08-15 11:00:00', 7, 2),
                                                                                               ('Apprendimento automatico per il marketing', 'Applicazioni di machine learning nel marketing digitale', NULL, '2025-08-20 14:00:00', 8, 1),
                                                                                               ('Robotics in the workplace', 'Implementazione della robotica nei processi industriali', NULL, '2025-08-25 15:00:00', 9, 2),
                                                                                               ('AI per la gestione delle risorse agricole', 'Applicazione dell\'AI nella gestione delle risorse agricole', NULL, '2025-09-01 13:30:00', 10, 3),
                                                                                               ('Sostenibilità nelle banane', 'Progetti di sostenibilità nella produzione di banane in Moldovia', NULL, '2025-09-05 10:00:00', 1, 3);
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
                                                                                               ('Deep Learning per immagini mediche', 'Uso del deep learning per la diagnosi medica tramite immagini', NULL, '2025-05-15 18:00:00', 1, 1),
                                                                                               ('Sicurezza in reti IoT', 'Studio sulle vulnerabilità nelle reti IoT', NULL, '2025-06-01 12:00:00', 2, 2),
                                                                                               ('Analisi dati social media', 'Analisi dei dati dei social media per identificare trend', NULL, '2025-05-20 09:00:00', 3, 3),
                                                                                               ('Robotica per veicoli autonomi', 'Applicazioni della robotica per il controllo di veicoli autonomi', NULL, '2025-06-05 15:30:00', 4, 1),
                                                                                               ('Apprendimento rinforzato per robot', 'Tecniche di apprendimento rinforzato applicate alla robotica', NULL, '2025-06-10 14:00:00', 5, 1),
                                                                                               ('Banane della Moldovia: Studio di caso', 'Studio sulle banane della Moldovia, una risorsa agricola di eccellenza', NULL, '2025-06-15 13:00:00', 6, 3),
                                                                                               ('Sostenibilità agricola e banane', 'Studi sulla sostenibilità nella produzione di banane in Moldovia', NULL, '2025-06-20 14:00:00', 7, 3),
                                                                                               ('Blockchain per la sicurezza alimentare', 'Applicazioni della blockchain nella tracciabilità alimentare', NULL, '2025-07-01 10:30:00', 8, 2),
                                                                                               ('Analisi predittiva per la manutenzione', 'Analisi predittiva nella manutenzione industriale', NULL, '2025-07-05 11:00:00', 9, 1),
                                                                                               ('Cybersecurity nelle applicazioni cloud', 'Approcci avanzati alla sicurezza nelle applicazioni cloud', NULL, '2025-07-10 12:30:00', 10, 2),
                                                                                               ('Visione artificiale per veicoli autonomi', 'Uso della visione artificiale per il miglioramento della sicurezza dei veicoli autonomi', NULL, '2025-07-15 13:30:00', 1, 1),
                                                                                               ('Data Mining per social media', 'Tecniche di data mining applicate ai social media per l\'analisi dei trend', NULL, '2025-07-20 14:00:00', 2, 2),
                                                                                               ('Natural Language Processing per chatbot', 'Tecniche di NLP applicate ai chatbot per il customer service', NULL, '2025-07-25 16:30:00', 3, 1),
                                                                                               ('Big Data per la salute pubblica', 'Utilizzo dei Big Data per migliorare le politiche di salute pubblica', NULL, '2025-08-01 17:00:00', 4, 2),
                                                                                               ('Machine Learning per la sanità', 'Tecniche avanzate di Machine Learning nel settore sanitario', NULL, '2025-08-05 18:00:00', 5, 1),
                                                                                               ('Banane della Moldovia: Approccio agronomico', 'Approccio scientifico alla produzione di banane in Moldovia', NULL, '2025-08-10 09:30:00', 6, 3),
                                                                                               ('Cybersecurity per l\'industria IoT', 'Strategie di sicurezza per il settore IoT industriale', NULL, '2025-08-15 11:00:00', 7, 2),
                                                                                               ('Apprendimento automatico per il marketing', 'Applicazioni di machine learning nel marketing digitale', NULL, '2025-08-20 14:00:00', 8, 1),
                                                                                               ('Robotics in the workplace', 'Implementazione della robotica nei processi industriali', NULL, '2025-08-25 15:00:00', 9, 2),
                                                                                               ('AI per la gestione delle risorse agricole', 'Applicazione dell\'AI nella gestione delle risorse agricole', NULL, '2025-09-01 13:30:00', 10, 3),
                                                                                               ('Sostenibilità nelle banane', 'Progetti di sostenibilità nella produzione di banane in Moldovia', NULL, '2025-09-05 10:00:00', 1, 3);
-- Ogni paper ha 2 revisioni
INSERT INTO Revisione (testo, valutazione, data_sottomissione, ref_utente, ref_paper) VALUES
                                                                                          ('Ottimo lavoro, chiaro e completo.', 3, '2025-06-20 10:00:00', 2, 1),
                                                                                          ('Migliorare sezione dati.', 2, '2025-06-22 14:00:00', 3, 1),
                                                                                          ('Buona analisi, serve più dettaglio.', 2, '2025-06-25 12:30:00', 4, 2),
                                                                                          ('Interessante, ma metodologia poco chiara.', 1, '2025-06-27 09:00:00', 5, 2),
                                                                                          ('Ottimo lavoro!', 3, '2025-06-30 11:00:00', 6, 3),
                                                                                          ('Chiarezza nella parte finale.', 2, '2025-07-02 12:00:00', 7, 3);
-- (Continuare con altre revisioni, aggiungi per tutti i paper)
INSERT INTO Proceeding (titolo, data_sottomissione, ref_utente, ref_conferenza) VALUES
                                                                                    ('Atti AI Summit 2025', '2025-08-01 12:00:00', 1, 1),
                                                                                    ('Atti Cybersecurity Conf 2025', '2025-09-01 15:00:00', 2, 2),
                                                                                    ('Atti Data Science Expo 2025', '2025-09-20 16:30:00', 3, 3),
                                                                                    ('Proceeding Banane della Moldovia', '2025-10-01 10:00:00', 4, 3),
                                                                                    ('Supplemento AI Summit', '2025-08-10 10:00:00', 5, 1),
                                                                                    ('Workshop Cybersecurity', '2025-09-05 11:00:00', 6, 2),
                                                                                    ('Poster Data Science', '2025-09-25 09:30:00', 7, 3),
                                                                                    ('Sessione Robotica', '2025-10-10 13:00:00', 8, 3);
INSERT INTO Ruolo_conferenza (ruolo, ref_utente, ref_conferenza) VALUES
                                                                     ('Chair', 1, 1),
                                                                     ('Revisore', 2, 2),
                                                                     ('Autore', 3, 3),
                                                                     ('Revisore', 4, 1),
                                                                     ('Editor', 5, 2),
                                                                     ('Chair', 6, 3),
                                                                     ('Autore', 7, 3),
                                                                     ('Revisore', 8, 3),
                                                                     ('Autore', 1,3),
                                                                     ('Revisore', 1,3),
                                                                     ('Editor', 1,3);
