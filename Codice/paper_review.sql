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
                                                        ('Leonardo Giovanni', 'Caiezza', 'leonardocaiezza20@gmail.com', 'IdS30!'),
                                                        ('Luca', 'Gaetani', 'luca.gae03@gmail.com', 'IdS30!'),
                                                        ('Daniele Orazio', 'Susino', 'susino.daniele@outlook.com', 'GayStup1d0!');

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

INSERT INTO Conferenza (
    nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione,
    paper_previsti, rate_accettazione, giorni_preavviso,
    scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2,
    scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione
) VALUES

-- 1: scadenza_sottomissione = 17 giugno 2025
('Data Science Expo 2025', 'Conferenza su tecniche avanzate di data science', '2026-07-01 09:00:00', 'Torino', 'Broadcast', '3',
 30, 20, 5,
 '2025-06-17 23:59:59', '2025-06-20 23:59:59', '2025-06-23 23:59:59', '2025-06-26 23:59:59', '2025-06-29 23:59:59', '2025-07-02 23:59:59'),

-- 2: scadenza_revisione = 17 giugno 2025
('Quantum Computing Congress', 'Calcolo quantistico e algoritmi innovativi', '2026-07-15 10:00:00', 'Zurigo', 'Topic', '4',
 40, 10, 5,
 '2025-06-14 23:59:59', '2025-06-17 23:59:59', '2025-06-20 23:59:59', '2025-06-23 23:59:59', '2025-06-26 23:59:59', '2025-06-29 23:59:59'),

-- 3: scadenza_sottomissione_2 = 17 giugno 2025
('Sustainable Tech Forum', 'Tecnologie sostenibili e innovazione ambientale', '2026-07-20 09:30:00', 'Oslo', 'Broadcast', '2',
 50, 7, 5,
 '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-17 23:59:59', '2025-06-20 23:59:59', '2025-06-23 23:59:59', '2025-06-26 23:59:59'),

-- 4: scadenza_editing = 17 giugno 2025
('Ethics and AI 2025', 'Etica, filosofia e intelligenza artificiale', '2026-07-25 11:00:00', 'Oxford', 'Topic', '3',
 60, 10, 5,
 '2025-06-08 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-17 23:59:59', '2025-06-20 23:59:59', '2025-06-23 23:59:59'),

-- 5: scadenza_sottomissione_3 = 17 giugno 2025
('Bioinformatics World Congress', 'Bioinformatica e analisi genetica computazionale', '2026-07-30 08:30:00', 'Boston', 'Broadcast', '4',
 35, 14, 5,
 '2025-06-05 23:59:59', '2025-06-08 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-17 23:59:59', '2025-06-20 23:59:59'),-- Paper per AI Summit 2025
('Future of Education Forum', 'Conferenza internazionale sull\'innovazione nella didattica e tecnologie educative', '2026-08-10 10:00:00', 'Helsinki', 'Topic', '3',
 28, 18, 5,
'2025-06-05 23:59:59', '2025-06-07 23:59:59', '2025-06-09 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-17 23:59:59');

INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Deep Learning Approaches for AI', 'Questo paper esplora l\'applicazione dei metodi di deep learning per migliorare l\'intelligenza artificiale.', NULL, '2025-05-20 14:30:00', 4, 1),
('AI in Healthcare: Current Trends and Future Prospects', 'Analizza i progressi recenti nell\'uso dell\'IA in ambito sanitario e le potenzialità future.', NULL, '2025-05-25 10:00:00', 2, 1),
('Reinforcement Learning in Autonomous Systems', 'Un\'analisi approfondita dell\'uso del reinforcement learning nei sistemi autonomi.', NULL, '2025-05-30 16:00:00', 3, 1);

-- Paper per Cybersecurity Conf 2025
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Modern Cryptographic Techniques for Secure Communication', 'Il paper esplora le più recenti tecniche crittografiche per garantire comunicazioni sicure in ambienti complessi.', NULL, '2025-06-10 09:30:00', 1, 2),
('Zero Trust Architectures in Cloud Security', 'Approfondimento sulle architetture Zero Trust come soluzione alla sicurezza nel cloud.', NULL, '2025-06-12 11:00:00', 2, 2),
('AI for Threat Detection and Prevention in Cybersecurity', 'Analizza come intelligenza artificiale può essere utilizzata per la rilevazione e prevenzione delle minacce nel campo della sicurezza informatica.', NULL, '2025-06-15 13:00:00', 3, 2);

-- Paper per Banane della Moldavia 2025
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Studying Banana Growth in Moldova: A Comparative Analysis', 'Analisi comparativa sulla crescita delle banane in Moldavia e altri paesi tropicali.', NULL, '2025-07-01 08:00:00', 4, 3),
('The Role of Climate in Banana Cultivation in Moldova', 'Il clima della Moldavia e il suo impatto sulla produzione delle banane: uno studio dettagliato.', NULL, '2025-07-05 09:00:00', 2, 3),
('Innovative Farming Techniques for Banana Production in Moldova', 'Analisi delle tecniche agricole innovative utilizzate per migliorare la produzione di banane in Moldavia.', NULL, '2025-07-10 10:30:00', 3, 3);

-- Revisioni per il Paper "Deep Learning Approaches for AI"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
('Buon approccio, ma manca un analisi approfondita sugli algoritmi specifici di deep learning.', 7, '2025-05-21 09:00:00', 'Buona comprensione delle tecniche di base.', 'Manca un confronto con i metodi piu recenti.', NULL, 2, 1),
('Interessante proposta, ma servirebbero piu dettagli sugli esperimenti condotti.', 6, '2025-05-22 10:00:00', 'Approccio ben strutturato e chiaro.', 'Dettagli insufficienti sui risultati degli esperimenti.', 'Il paper presenta sezioni simili a un lavoro già pubblicato, possibile caso di plagio.', 3, 1);

-- Revisioni per il Paper "AI in Healthcare: Current Trends and Future Prospects"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
('Ottimo lavoro nel delineare le tendenze emergenti, ma manca un approfondimento su come l IA possa influenzare il trattamento medico personalizzato.', 8, '2025-06-10 10:00:00', 'Molto interessante l approccio al miglioramento delle diagnosi.', 'Manca un esempio pratico di implementazione nei sistemi sanitari.', NULL, 1, 2),
('L analisi e completa, ma non vengono considerati i rischi e le problematiche etiche nell uso dell IA in sanita.', 7, '2025-06-12 15:00:00', 'Buon approfondimento sul tema dell accesso ai dati sanitari.', 'Poca attenzione agli aspetti di privacy e sicurezza.', 'Il paper sembra riprendere intere sezioni da un altro articolo senza citarlo correttamente, possibile plagio.', 2, 2);

-- Revisioni per il Paper "Reinforcement Learning in Autonomous Systems"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
('Il lavoro e buono ma necessita di maggiore chiarezza nell interpretazione dei risultati.', 6, '2025-05-30 11:00:00', 'Buona descrizione delle teorie di base.', 'Troppi dettagli tecnici, potrebbe risultare difficile per i non esperti.', NULL, 1, 3),
('L approccio e interessante, ma le conclusioni sembrano essere poco supportate dai dati.', 5, '2025-06-02 12:30:00', 'Descrizione interessante dei sistemi autonomi.', 'Mancano test pratici a supporto delle affermazioni.', NULL, 3, 3);

-- Revisioni per il Paper "Modern Cryptographic Techniques for Secure Communication"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(NULL, NULL, NULL, NULL, NULL, NULL, 1, 1),
('L analisi delle tecniche crittografiche e chiara, ma occorre approfondire l implementazione pratica nei sistemi reali.', 7, '2025-06-13 11:00:00', 'Descrizione chiara dei metodi crittografici.', 'Troppo generico nell analisi delle implementazioni reali.', NULL, 2, 4);

-- Revisioni per il Paper "Zero Trust Architectures in Cloud Security"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
('L approccio Zero Trust e trattato in modo corretto, ma manca un confronto con soluzioni alternative.', 8, '2025-06-14 13:30:00', 'Approfondimento dettagliato sull architettura Zero Trust.', 'Non vengono considerati i costi di implementazione.', NULL, 2, 5),
('Buon lavoro nel descrivere i benefici, ma manca una valutazione dei rischi.', 7, '2025-06-15 10:30:00', 'Chiarezza nell analisi dei benefici.', 'Non sono stati presi in considerazione gli svantaggi o le difficoltà di implementazione.', 'Il paper sembra troppo simile ad altri lavori pubblicati precedentemente, possibile plagio.', 3, 5);

-- Revisioni per il Paper "AI for Threat Detection and Prevention in Cybersecurity"
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
('Molto buono, ma occorre piu lavoro sulle soluzioni concrete.', 8, '2025-06-16 10:00:00', 'Buona analisi del ruolo dell AI nella rilevazione delle minacce.', 'Manca l analisi delle soluzioni gia implementate.', NULL, 1, 6),
('Il lavoro e valido ma deve essere piu dettagliato sull applicazione dell AI nei sistemi reali.', 7, '2025-06-17 11:30:00', 'Ottima descrizione dei metodi di rilevamento delle minacce.', 'Troppo generico nell implementazione pratica.', NULL, 2, 6);



-- Proceeding per AI Summit 2025
INSERT INTO Proceeding (titolo, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Ci sto lavorando', NULL, 1, 1);

-- Proceeding per Cybersecurity Conf 2025
INSERT INTO Proceeding (titolo, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Advanced Cryptography in Cloud Security', '2025-06-18 10:00:00', 2, 2);

-- Proceeding per Banane della Moldavia 2025
INSERT INTO Proceeding (titolo, data_sottomissione, ref_utente, ref_conferenza) VALUES
('Innovative Banana Cultivation Techniques in Moldova', '2025-07-01 11:00:00', 3, 3);

-- Inserimento dei ruoli per AI Summit 2025
INSERT INTO Ruolo_conferenza (ruolo, ref_utente, ref_conferenza) VALUES
('Chair', 1, 1),  -- Diego Corona come Chair per AI Summit 2025
('Revisore', 1, 1),  -- Diego Corona come Revisore per AI Summit 2025
('Autore', 1, 1),  -- Diego Corona come Autore per AI Summit 2025
('Sottorevisore', 1, 1), -- Diego Corona come Sottorevisore per AI Summit 2025
('Revisore', 2, 1),  -- Leonardo Giovanni Caiezza come Revisore per AI Summit 2025
('Autore', 3, 1),  -- Luca Gaetani come Autore per AI Summit 2025
('Sottorevisore', 4, 1);  -- Daniele Orazio Susino come Sottorevisore per AI Summit 2025

-- Inserimento dei ruoli per Cybersecurity Conf 2025
INSERT INTO Ruolo_conferenza (ruolo, ref_utente, ref_conferenza) VALUES
('Chair', 2, 2),  -- Leonardo Giovanni Caiezza come Chair per Cybersecurity Conf 2025
('Revisore', 3, 2),  -- Luca Gaetani come Revisore per Cybersecurity Conf 2025
('Autore', 4, 2),  -- Daniele Orazio Susino come Autore per Cybersecurity Conf 2025
('Editor', 1, 2);  -- Diego Corona come Editor per Cybersecurity Conf 2025

-- Inserimento dei ruoli per Banane della Moldavia 2025
INSERT INTO Ruolo_conferenza (ruolo, ref_utente, ref_conferenza) VALUES
('Chair', 3, 3),  -- Luca Gaetani come Chair per Banane della Moldavia 2025
('Revisore', 4, 3),  -- Daniele Orazio Susino come Revisore per Banane della Moldavia 2025
('Autore', 1, 3),  -- Diego Corona come Autore per Banane della Moldavia 2025
('Sottorevisore', 2, 3);  -- Leonardo Giovanni Caiezza come Sottorevisore per Banane della Moldavia 2025


-- Notifiche per la conferenza AI Summit 2025
INSERT INTO Notifica (data, testo, isLetta, ref_utente, ref_conferenza) VALUES
('2025-05-10 12:00:00', 'Scadenza per la sottomissione degli articoli per AI Summit 2025.', false, 1, 1),  -- Diego Corona
('2025-05-11 14:00:00', 'Revisione scadenza per AI Summit 2025.', false, 2, 1),  -- Leonardo Giovanni Caiezza
('2025-05-12 09:00:00', 'Scadenza di editing per AI Summit 2025.', false, 3, 1),  -- Luca Gaetani
('2025-05-13 10:00:00', 'Ultima opportunità per inviare la documentazione per AI Summit 2025.', false, 4, 1);  -- Daniele Orazio Susino

-- Notifiche per la conferenza Cybersecurity Conf 2025
INSERT INTO Notifica (data, testo, isLetta, ref_utente, ref_conferenza) VALUES
('2025-06-10 10:00:00', 'Scadenza per la sottomissione degli articoli per Cybersecurity Conf 2025.', false, 1, 2),  -- Diego Corona
('2025-06-11 14:30:00', 'Revisione scadenza per Cybersecurity Conf 2025.', false, 2, 2),  -- Leonardo Giovanni Caiezza
('2025-06-12 12:00:00', 'Scadenza di editing per Cybersecurity Conf 2025.', false, 3, 2),  -- Luca Gaetani
('2025-06-13 11:00:00', 'Ultima opportunità per inviare la documentazione per Cybersecurity Conf 2025.', false, 4, 2);  -- Daniele Orazio Susino

-- Notifiche per la conferenza Banane della Moldavia 2025
INSERT INTO Notifica (data, testo, isLetta, ref_utente, ref_conferenza) VALUES
('2025-06-25 09:00:00', 'Scadenza per la sottomissione degli articoli per Banane della Moldavia 2025.', false, 1, 3),  -- Diego Corona
('2025-06-26 15:00:00', 'Revisione scadenza per Banane della Moldavia 2025.', false, 2, 3),  -- Leonardo Giovanni Caiezza
('2025-06-27 10:00:00', 'Scadenza di editing per Banane della Moldavia 2025.', false, 3, 3),  -- Luca Gaetani
('2025-06-28 12:30:00', 'Ultima opportunità per inviare la documentazione per Banane della Moldavia 2025.', false, 4, 3);  -- Daniele Orazio Susino


-- Inviti per AI Summit 2025
INSERT INTO Invito (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) VALUES
('2025-05-10 08:00:00', 'Invito per partecipare come revisore per AI Summit 2025.', 'Inviato', 'leonardocaiezza20@gmail.com', 'AI_Summit_2025_Rev_001', 1, 1, 2),  -- Diego invia a Leonardo Giovanni Caiezza
('2025-05-10 09:00:00', 'Invito per partecipare come autore per AI Summit 2025.', 'Inviato', 'luca.gae03@gmail.com', 'AI_Summit_2025_Author_001', 1, 1, 3),  -- Diego invia a Luca Gaetani
('2025-05-10 10:00:00', 'Invito per partecipare come autore per AI Summit 2025.', 'Inviato', 'susino.daniele@outlook.com', 'AI_Summit_2025_Author_002', 1, 1, 4);  -- Diego invia a Daniele Orazio Susino

-- Inviti per Cybersecurity Conf 2025
INSERT INTO Invito (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) VALUES
('2025-06-10 08:00:00', 'Invito per partecipare come revisore per Cybersecurity Conf 2025.', 'Inviato', 'luca.gae03@gmail.com', 'Cybersecurity_Conf_2025_Rev_001', 2, 1, 3),  -- Diego invia a Luca Gaetani
('2025-06-10 09:00:00', 'Invito per partecipare come autore per Cybersecurity Conf 2025.', 'Inviato', 'susino.daniele@outlook.com', 'Cybersecurity_Conf_2025_Author_001', 2, 1, 4);  -- Diego invia a Daniele Orazio Susino

-- Inviti per Banane della Moldavia 2025
INSERT INTO Invito (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) VALUES
('2025-06-25 08:00:00', 'Invito per partecipare come revisore per Banane della Moldavia 2025.', 'Inviato', 'leonardocaiezza20@gmail.com', 'Banane_Moldavia_2025_Rev_001', 3, 1, 2),  -- Diego invia a Leonardo Giovanni Caiezza
('2025-06-25 09:00:00', 'Invito per partecipare come autore per Banane della Moldavia 2025.', 'Inviato', 'luca.gae03@gmail.com', 'Banane_Moldavia_2025_Author_001', 3, 1, 3);  -- Diego invia a Luca Gaetani

-- TopicUtente per AI Summit 2025
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(1, 1),  -- Diego Corona partecipa al Topic 1 per AI Summit 2025
(1, 2),  -- Leonardo Giovanni Caiezza partecipa al Topic 1 per AI Summit 2025
(1, 3);  -- Luca Gaetani partecipa al Topic 1 per AI Summit 2025

-- TopicUtente per Cybersecurity Conf 2025
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(2, 1),  -- Diego Corona partecipa al Topic 2 per Cybersecurity Conf 2025
(2, 3),  -- Luca Gaetani partecipa al Topic 2 per Cybersecurity Conf 2025
(2, 4);  -- Daniele Orazio Susino partecipa al Topic 2 per Cybersecurity Conf 2025

-- TopicUtente per Banane della Moldavia 2025
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(3, 1),  -- Diego Corona partecipa al Topic 3 per Banane della Moldavia 2025
(3, 2),  -- Leonardo Giovanni Caiezza partecipa al Topic 3 per Banane della Moldavia 2025
(3, 4);  -- Daniele Orazio Susino partecipa al Topic 3 per Banane della Moldavia 2025


-- TopicPaper per AI Summit 2025
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
(1, 1),  -- Deep Learning Approaches for AI associato al Topic 1 per AI Summit 2025
(1, 2),  -- AI in Healthcare: Current Trends and Future Prospects associato al Topic 1 per AI Summit 2025
(1, 3);  -- Reinforcement Learning in Autonomous Systems associato al Topic 1 per AI Summit 2025

-- TopicPaper per Cybersecurity Conf 2025
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
(2, 4),  -- Modern Cryptographic Techniques for Secure Communication associato al Topic 2 per Cybersecurity Conf 2025
(2, 5),  -- Zero Trust Architectures in Cloud Security associato al Topic 2 per Cybersecurity Conf 2025
(2, 6);  -- AI for Threat Detection and Prevention in Cybersecurity associato al Topic 2 per Cybersecurity Conf 2025

-- TopicPaper per Banane della Moldavia 2025
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
(3, 7),  -- Studying Banana Growth in Moldova: A Comparative Analysis associato al Topic 3 per Banane della Moldavia 2025
(3, 8),  -- The Role of Climate in Banana Cultivation in Moldova associato al Topic 3 per Banane della Moldavia 2025
(3, 9);  -- Innovative Farming Techniques for Banana Production in Moldova associato al Topic 3 per Banane della Moldavia 2025

-- CoAutoriPaper per AI Summit 2025
INSERT INTO CoAutoriPaper (email, ref_paper) VALUES
('leonardocaiezza20@gmail.com', 1),  -- Leonardo Giovanni Caiezza come co-autore di "Deep Learning Approaches for AI"
('luca.gae03@gmail.com', 1),  -- Luca Gaetani come co-autore di "Deep Learning Approaches for AI"
('susino.daniele@outlook.com', 1),  -- Daniele Orazio Susino come co-autore di "Deep Learning Approaches for AI"
('leonardocaiezza20@gmail.com', 2),  -- Leonardo Giovanni Caiezza come co-autore di "AI in Healthcare: Current Trends and Future Prospects"
('luca.gae03@gmail.com', 2),  -- Luca Gaetani come co-autore di "AI in Healthcare: Current Trends and Future Prospects"
('susino.daniele@outlook.com', 2),  -- Daniele Orazio Susino come co-autore di "AI in Healthcare: Current Trends and Future Prospects"
('leonardocaiezza20@gmail.com', 3),  -- Leonardo Giovanni Caiezza come co-autore di "Reinforcement Learning in Autonomous Systems"
('luca.gae03@gmail.com', 3),  -- Luca Gaetani come co-autore di "Reinforcement Learning in Autonomous Systems"
('susino.daniele@outlook.com', 3);  -- Daniele Orazio Susino come co-autore di "Reinforcement Learning in Autonomous Systems"

-- CoAutoriPaper per Cybersecurity Conf 2025
INSERT INTO CoAutoriPaper (email, ref_paper) VALUES
('leonardocaiezza20@gmail.com', 4),  -- Leonardo Giovanni Caiezza come co-autore di "Modern Cryptographic Techniques for Secure Communication"
('luca.gae03@gmail.com', 4),  -- Luca Gaetani come co-autore di "Modern Cryptographic Techniques for Secure Communication"
('susino.daniele@outlook.com', 4),  -- Daniele Orazio Susino come co-autore di "Modern Cryptographic Techniques for Secure Communication"
('leonardocaiezza20@gmail.com', 5),  -- Leonardo Giovanni Caiezza come co-autore di "Zero Trust Architectures in Cloud Security"
('luca.gae03@gmail.com', 5),  -- Luca Gaetani come co-autore di "Zero Trust Architectures in Cloud Security"
('susino.daniele@outlook.com', 5),  -- Daniele Orazio Susino come co-autore di "Zero Trust Architectures in Cloud Security"
('leonardocaiezza20@gmail.com', 6),  -- Leonardo Giovanni Caiezza come co-autore di "AI for Threat Detection and Prevention in Cybersecurity"
('luca.gae03@gmail.com', 6),  -- Luca Gaetani come co-autore di "AI for Threat Detection and Prevention in Cybersecurity"
('susino.daniele@outlook.com', 6);  -- Daniele Orazio Susino come co-autore di "AI for Threat Detection and Prevention in Cybersecurity"

-- CoAutoriPaper per Banane della Moldavia 2025
INSERT INTO CoAutoriPaper (email, ref_paper) VALUES
('leonardocaiezza20@gmail.com', 7),  -- Leonardo Giovanni Caiezza come co-autore di "Studying Banana Growth in Moldova: A Comparative Analysis"
('luca.gae03@gmail.com', 7),  -- Luca Gaetani come co-autore di "Studying Banana Growth in Moldova: A Comparative Analysis"
('susino.daniele@outlook.com', 7),  -- Daniele Orazio Susino come co-autore di "Studying Banana Growth in Moldova: A Comparative Analysis"
('leonardocaiezza20@gmail.com', 8),  -- Leonardo Giovanni Caiezza come co-autore di "The Role of Climate in Banana Cultivation in Moldova"
('luca.gae03@gmail.com', 8),  -- Luca Gaetani come co-autore di "The Role of Climate in Banana Cultivation in Moldova"
('susino.daniele@outlook.com', 8),  -- Daniele Orazio Susino come co-autore di "The Role of Climate in Banana Cultivation in Moldova"
('leonardocaiezza20@gmail.com', 9),  -- Leonardo Giovanni Caiezza come co-autore di "Innovative Farming Techniques for Banana Production in Moldova"
('luca.gae03@gmail.com', 9),  -- Luca Gaetani come co-autore di "Innovative Farming Techniques for Banana Production in Moldova"
('susino.daniele@outlook.com', 9);  -- Daniele Orazio Susino come co-autore di "Innovative Farming Techniques for Banana Production in Moldova"