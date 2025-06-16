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