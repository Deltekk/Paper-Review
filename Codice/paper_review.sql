INSERT INTO Utente (nome, cognome, email, password) VALUES
                                                        ('Diego', 'Corona', 'diecoro2003@gmail.com', '$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm'),
                                                        ('Leonardo Giovanni', 'Caiezza', 'leonardocaiezza20@gmail.com', '$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm'),
                                                        ('Luca', 'Gaetani', 'luca.gae03@gmail.com', '$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm'),
                                                        ('Daniele Orazio', 'Susino', 'susino.daniele@outlook.com', '$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm'),
                                                        ('Giovanni','Castelli','caste283289@gmail.com','$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm'),
                                                        ('Giulia', 'Greco', 'greco.giulia1105@gmail.com', '$2a$12$uWNDYFYwlDv2y4qIul0mQOfpS5vFPauouB1HW10iZrbpyMik7BFvm');

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

-- Diego Corona
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(1, 1),   -- Machine Learning
(7, 1),   -- Banane della Moldovia
(10, 1);  -- Apprendimento Automatico

-- Leonardo Giovanni Caiezza
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(2, 2),   -- Cybersecurity
(11, 2),  -- Crittografia
(27, 2);  -- Filosofia Morale

-- Luca Gaetani
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(9, 3),   -- Intelligenza Artificiale
(21, 3),  -- Sistemi Operativi
(44, 3);  -- Ingegneria Meccanica

-- Daniele Orazio Susino
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(25, 4),  -- Filosofia della Scienza
(26, 4),  -- Filosofia Morale
(54, 4);  -- Etica Applicata

-- Giovanni Castelli
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(18, 5),   -- Astrofisica
(20, 5),   -- Teoria degli Algoritmi
(41, 5);   -- Ecologia Marina

-- Giulia Greco
INSERT INTO TopicUtente (ref_topic, ref_utente) VALUES
(13, 6),   -- Neuroscienze
(14, 6),   -- Psicologia Cognitiva
(55, 6);   -- Bioetica

INSERT INTO Conferenza (
    nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione,
    paper_previsti, rate_accettazione, giorni_preavviso,
    scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2,
    scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione
) VALUES
-- 1: scadenza_sottomissione = 17 giugno 2025
('Le Banane in Moldavia', 'Conferenza internazionale sulla coltivazione, distribuzione e innovazione nella filiera delle banane moldave', '2026-08-10 10:00:00', 'Chișinău', 'Topic', '3',
 3, 20, 5,
 '2025-06-20 23:59:59', '2025-07-20 23:59:59', '2025-08-23 23:59:59', '2025-09-26 23:59:59', '2025-10-29 23:59:59', '2025-11-02 23:59:59'),
-- 2: scadenza_revisione = 17 giugno 2025
('Quantum Computing Congress', 'Calcolo quantistico e algoritmi innovativi', '2026-07-15 10:00:00', 'Zurigo', 'Broadcast', '4',
 4, 10, 5,
 '2025-06-19 23:59:59', '2025-06-25 23:59:59', '2025-07-20 23:59:59', '2025-08-23 23:59:59', '2025-09-26 23:59:59', '2025-10-29 23:59:59'),
-- 3: scadenza_sottomissione_2 = 17 giugno 2025
('Sustainable Tech Forum', 'Tecnologie sostenibili e innovazione ambientale', '2026-07-20 09:30:00', 'Oslo', 'Broadcast', '3',
 5, 7, 5,
 '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-20 23:59:59', '2025-07-20 23:59:59', '2025-08-23 23:59:59', '2025-09-26 23:59:59'),
-- 4: scadenza_editing = 17 giugno 2025
('Ethics and AI 2025', 'Etica, filosofia e intelligenza artificiale', '2026-07-25 11:00:00', 'Oxford', 'Topic', '3',
 6, 10, 5,
 '2025-06-08 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-20 23:59:59', '2025-07-20 23:59:59', '2025-08-23 23:59:59'),
-- 5: scadenza_sottomissione_3 = 17 giugno 2025
('Bioinformatics World Congress', 'Bioinformatica e analisi genetica computazionale', '2026-07-30 08:30:00', 'Boston', 'Broadcast', '4',
 3, 14, 5,
 '2025-06-05 23:59:59', '2025-06-08 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-20 23:59:59', '2025-07-20 23:59:59'),
-- 6: scadenza_impaginazione = 17 giugno 2025
('Data Science Expo 2025', 'Conferenza su tecniche avanzate di data science', '2026-07-01 09:00:00', 'Torino', 'Broadcast', '3',
 2, 18, 5,
'2025-06-05 23:59:59', '2025-06-07 23:59:59', '2025-06-09 23:59:59', '2025-06-11 23:59:59', '2025-06-14 23:59:59', '2025-06-20 23:59:59');

INSERT INTO Ruolo_conferenza (ruolo, ref_utente, ref_conferenza) VALUES
-- Diego Corona (id_utente = 1)
('Chair', 1, 1), ('Revisore', 1, 1), ('Sottorevisore', 1, 1), ('Autore', 1, 1), ('Editor', 1, 1),
('Chair', 1, 2), ('Revisore', 1, 2), ('Sottorevisore', 1, 2), ('Autore', 1, 2), ('Editor', 1, 2),
('Chair', 1, 3), ('Revisore', 1, 3), ('Sottorevisore', 1, 3), ('Autore', 1, 3), ('Editor', 1, 3),
('Chair', 1, 4), ('Revisore', 1, 4), ('Sottorevisore', 1, 4), ('Autore', 1, 4), ('Editor', 1, 4),
('Chair', 1, 5), ('Revisore', 1, 5), ('Sottorevisore', 1, 5), ('Autore', 1, 5), ('Editor', 1, 5),
('Chair', 1, 6), ('Revisore', 1, 6), ('Sottorevisore', 1, 6), ('Autore', 1, 6), ('Editor', 1, 6),

-- Leonardo Giovanni Caiezza (id_utente = 2)
('Chair', 2, 1), ('Revisore', 2, 1), ('Sottorevisore', 2, 1), ('Autore', 2, 1), ('Editor', 2, 1),
('Chair', 2, 2), ('Revisore', 2, 2), ('Sottorevisore', 2, 2), ('Autore', 2, 2), ('Editor', 2, 2),
('Chair', 2, 3), ('Revisore', 2, 3), ('Sottorevisore', 2, 3), ('Autore', 2, 3), ('Editor', 2, 3),
('Chair', 2, 4), ('Revisore', 2, 4), ('Sottorevisore', 2, 4), ('Autore', 2, 4), ('Editor', 2, 4),
('Chair', 2, 5), ('Revisore', 2, 5), ('Sottorevisore', 2, 5), ('Autore', 2, 5), ('Editor', 2, 5),
('Chair', 2, 6), ('Revisore', 2, 6), ('Sottorevisore', 2, 6), ('Autore', 2, 6), ('Editor', 2, 6),

-- Luca Gaetani (id_utente = 3)
('Chair', 3, 1), ('Revisore', 3, 1), ('Sottorevisore', 3, 1), ('Autore', 3, 1), ('Editor', 3, 1),
('Chair', 3, 2), ('Revisore', 3, 2), ('Sottorevisore', 3, 2), ('Autore', 3, 2), ('Editor', 3, 2),
('Chair', 3, 3), ('Revisore', 3, 3), ('Sottorevisore', 3, 3), ('Autore', 3, 3), ('Editor', 3, 3),
('Chair', 3, 4), ('Revisore', 3, 4), ('Sottorevisore', 3, 4), ('Autore', 3, 4), ('Editor', 3, 4),
('Chair', 3, 5), ('Revisore', 3, 5), ('Sottorevisore', 3, 5), ('Autore', 3, 5), ('Editor', 3, 5),
('Chair', 3, 6), ('Revisore', 3, 6), ('Sottorevisore', 3, 6), ('Autore', 3, 6), ('Editor', 3, 6),

-- Daniele Orazio Susino (id_utente = 4)
('Chair', 4, 1), ('Revisore', 4, 1), ('Sottorevisore', 4, 1), ('Autore', 4, 1), ('Editor', 4, 1),
('Chair', 4, 2), ('Revisore', 4, 2), ('Sottorevisore', 4, 2), ('Autore', 4, 2), ('Editor', 4, 2),
('Chair', 4, 3), ('Revisore', 4, 3), ('Sottorevisore', 4, 3), ('Autore', 4, 3), ('Editor', 4, 3),
('Chair', 4, 4), ('Revisore', 4, 4), ('Sottorevisore', 4, 4), ('Autore', 4, 4), ('Editor', 4, 4),
('Chair', 4, 5), ('Revisore', 4, 5), ('Sottorevisore', 4, 5), ('Autore', 4, 5), ('Editor', 4, 5),
('Chair', 4, 6), ('Revisore', 4, 6), ('Sottorevisore', 4, 6), ('Autore', 4, 6), ('Editor', 4, 6),

-- Giovanni Castelli (id_utente = 5)
('Chair', 5, 1), ('Revisore', 5, 1), ('Sottorevisore', 5, 1), ('Autore', 5, 1), ('Editor', 5, 1),
('Chair', 5, 2), ('Revisore', 5, 2), ('Sottorevisore', 5, 2), ('Autore', 5, 2), ('Editor', 5, 2),
('Chair', 5, 3), ('Revisore', 5, 3), ('Sottorevisore', 5, 3), ('Autore', 5, 3), ('Editor', 5, 3),
('Chair', 5, 4), ('Revisore', 5, 4), ('Sottorevisore', 5, 4), ('Autore', 5, 4), ('Editor', 5, 4),
('Chair', 5, 5), ('Revisore', 5, 5), ('Sottorevisore', 5, 5), ('Autore', 5, 5), ('Editor', 5, 5),
('Chair', 5, 6), ('Revisore', 5, 6), ('Sottorevisore', 5, 6), ('Autore', 5, 6), ('Editor', 5, 6),

-- Giulia Greco (id_utente = 6)
('Chair', 6, 1), ('Revisore', 6, 1), ('Sottorevisore', 6, 1), ('Autore', 6, 1), ('Editor', 6, 1),
('Chair', 6, 2), ('Revisore', 6, 2), ('Sottorevisore', 6, 2), ('Autore', 6, 2), ('Editor', 6, 2),
('Chair', 6, 3), ('Revisore', 6, 3), ('Sottorevisore', 6, 3), ('Autore', 6, 3), ('Editor', 6, 3),
('Chair', 6, 4), ('Revisore', 6, 4), ('Sottorevisore', 6, 4), ('Autore', 6, 4), ('Editor', 6, 4),
('Chair', 6, 5), ('Revisore', 6, 5), ('Sottorevisore', 6, 5), ('Autore', 6, 5), ('Editor', 6, 5),
('Chair', 6, 6), ('Revisore', 6, 6), ('Sottorevisore', 6, 6), ('Autore', 6, 6), ('Editor', 6, 6);





-- Paper 1
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Modellazione Predittiva del Clima per la Coltivazione della Banana in Moldavia',
  'Questo lavoro propone un approccio predittivo per valutare l’impatto dei cambiamenti climatici sulla resa delle banane in Moldavia. Utilizzando reti neurali ricorrenti addestrate su dati climatici storici, si forniscono scenari di adattamento per la selezione delle varietà e delle pratiche agronomiche sostenibili.',
  NULL,
  '2025-06-17 15:30:00',
  5,
  1
);
INSERT INTO CoAutoriPaper (email, ref_paper) VALUES
('maria.popescu@meteo.md', 1),
('andrei.rusu@agridata.md', 1);

-- Paper 2
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Qubit Entanglement Optimization in Noisy Intermediate-Scale Quantum Devices',
  'Questo studio propone una strategia per ottimizzare il grado di entanglement tra qubit nei dispositivi NISQ, riducendo l’influenza della decoerenza attraverso algoritmi ibridi quantistici-classici. L’approccio viene validato su IBM Q e simulazioni con Qiskit.',
  NULL,
  '2025-06-14 11:30:00',
  3,
  2
);

-- Paper 3
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Benchmarking of Quantum Algorithms for Combinatorial Optimization',
  'Il lavoro confronta la performance di algoritmi come QAOA e Grover su problemi di ottimizzazione combinatoria, analizzando scalabilità, accuratezza e sensibilità ai parametri. Viene inoltre presentata un’analisi metrica della qualità della soluzione su simulatori e hardware reali.',
  NULL,
  '2025-06-14 13:15:00',
  3,
  2
);

-- Paper 4
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Analisi Comparativa di Materiali Sostenibili per l’Edilizia Urbana',
  'Il paper presenta uno studio comparato su tre tipologie di materiali da costruzione sostenibili: legno trattato, calcestruzzo riciclato e biomattoni. I criteri di valutazione includono impatto ambientale, costi e performance strutturali.',
  NULL,
  '2025-06-14 11:45:00',
  2,
  3
);

-- Giulia Greco
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'Il confronto è interessante, ma mancano informazioni dettagliate su costi e reperibilità.',
  -1,
  '2025-06-14 19:00:00',
  'Ben organizzato, chiara esposizione.',
  'Limitata comparazione sulla durabilità.',
  '',
  6,
  4
);

-- Paper 5
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Bias Etici nei Sistemi di Raccomandazione Basati su AI',
  'Il paper analizza i bias algoritmici nei sistemi di raccomandazione e propone un framework etico per valutarne l’impatto sociale. Lo studio include casi reali e metriche di equità.',
  NULL,
  '2025-06-14 23:59:59',
  3,
  4
);
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'Argomento di grande attualità, presentato con rigore. La parte sperimentale potrebbe essere ampliata.',
  2,
  '2025-06-11 15:30:00',
  'Buona struttura logica, casi reali interessanti.',
  'Assenza di una sezione quantitativa.',
  '',
  1,
  5
);
-- Paper 6
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'L’Illusione del Libero Arbitrio nelle Decisioni Automatizzate',
  'Un’analisi filosofica sull’impatto delle decisioni automatizzate nelle società moderne. Il paper esplora il concetto di agency in presenza di sistemi intelligenti.',
  NULL,
  '2025-06-14 23:59:59',
  3,
  4
);
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'Discussione filosofica ben articolata. Qualche concetto richiede maggiore definizione.',
  -2,
  '2025-06-11 16:40:00',
  'Prospettiva originale, riferimenti ben scelti.',
  'Terminologia talvolta ambigua.',
  '',
  6,
  6
);

-- Paper 7
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Metodi di Normalizzazione nei Dataset Genomici ad Alta Variabilità',
  'Il lavoro analizza diverse tecniche di normalizzazione per dataset genomici con alta variabilità interindividuale. Sono valutati metodi Z-score, quantile normalization e scaling robusto, con benchmark su 3 dataset pubblici.',
  NULL,
  '2025-06-17 23:59:59',
  1,
  5
);
-- Giovanni Castelli (Revisore)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'Studio ben articolato sulle tecniche di normalizzazione. Ottima analisi comparativa, ma alcune sezioni risultano troppo sintetiche.',
  2,
  '2025-06-08 10:00:00',
  'Approccio sistematico, buoni grafici di supporto.',
  'Poco approfondita la parte sulla compatibilità con RNA-seq.',
  '',
  5,
  7
);

-- Paper 8
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Applicazione di Reti Neurali Convoluzionali per la Classificazione di Mutazioni Patogene',
  'Il paper presenta un modello CNN addestrato su sequenze genomiche codificate, in grado di classificare mutazioni con un’accuratezza del 93%. È fornito un confronto con modelli classici e una discussione sulle interpretazioni biologiche.',
  NULL,
  '2025-06-17 23:59:59',
  6,
  5
);

-- Daniele Susino
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'La trattazione è solida. Mancano però alcuni test di generalizzazione su popolazioni diverse.',
  2,
  '2025-06-08 13:10:00',
  'Codifica interessante, discussione accurata.',
  'Assenza di confronto su altri dataset clinici.',
  '',
  4,
  8
);

-- Paper 9
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Analisi Predittiva su Flussi di Dati in Tempo Reale tramite Apache Flink',
  'Questo studio presenta un’architettura per l’analisi predittiva su stream di dati ad alta frequenza utilizzando Apache Flink. L’efficienza è valutata su scenari industriali simulati con benchmark su latenze ed errori.',
  NULL,
  '2025-06-14 23:59:59',
  1,
  6
);
-- Leonardo Caiezza (Revisore)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'Studio robusto e ben implementato, ma la sezione sui risultati necessita maggiore dettaglio.',
  1,
  '2025-06-07 10:45:00',
  'Buona integrazione con sistemi real-time, benchmark accurato.',
  'Grafici poco leggibili, mancano intervalli di confidenza.',
  '',
  2,
  9
);

-- Paper 10
INSERT INTO Paper (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) VALUES
(
  'Feature Selection Dinamica in Modelli di Machine Learning su Dataset Eterogenei',
  'Il paper propone una strategia adattiva di selezione delle feature in ambienti data-driven con variabilità temporale, testata su dataset sanitari e finanziari.',
  NULL,
  '2025-06-14 23:59:59',
  6,
  6
);
-- Leonardo Caiezza (Revisore)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper) VALUES
(
  'La metodologia è interessante e utile. Sarebbe utile confrontarla con tecniche classiche di riduzione dimensionale.',
  0,
  '2025-06-07 12:00:00',
  'Strategia dinamica ben formulata, testata su scenari realistici.',
  'Analisi dei risultati un po’ limitata.',
  '',
  2,
  10
);


-- Paper 1
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (1, 1);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (2, 1);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (3, 1);

-- Paper 2
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (2, 2);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (4, 2);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (6, 2);

-- Paper 3
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (5, 3);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (3, 3);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (6, 3);

-- Paper 4
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (4, 4);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (1, 4);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (7, 4);

-- Paper 5
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (3, 5);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (5, 5);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (6, 5);

-- Paper 6
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (2, 6);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (4, 6);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (7, 6);

-- Paper 7
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (1, 7);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (2, 7);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (6, 7);

-- Paper 8
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (2, 8);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (3, 8);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (5, 8);

-- Paper 9
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (1, 9);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (4, 9);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (6, 9);

-- Paper 10
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (3, 10);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (5, 10);
INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (7, 10);


INSERT INTO Conferenza (
    nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione,
    paper_previsti, rate_accettazione, giorni_preavviso,
    scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2,
    scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione
) VALUES (
             'CyberSecurity & Privacy Summit',
             'Discussione sulle sfide emergenti in ambito sicurezza informatica, crittografia e privacy digitale',
             '2026-08-05 09:30:00',
             'Tallinn',
             'Topic',
             '4',
             4, 15, 5,
             '2025-06-15 23:59:59',  -- scadenza_sottomissione
             '2025-06-22 23:59:59',  -- scadenza_revisione (richiesta)
             '2025-07-20 23:59:59',
             '2025-08-23 23:59:59',
             '2025-09-26 23:59:59',
             '2025-10-29 23:59:59'
         );

-- Diego Corona
INSERT INTO Ruolo_conferenza (ref_utente, ref_conferenza, ruolo) VALUES
                                                                     (1, 7, 'Chair'),
                                                                     (1, 7, 'Revisore'),
                                                                     (1, 7, 'Autore');

-- Leonardo Giovanni Caiezza
INSERT INTO Ruolo_conferenza (ref_utente, ref_conferenza, ruolo) VALUES
                                                                     (2, 7, 'Chair'),
                                                                     (2, 7, 'Revisore'),
                                                                     (2, 7, 'Autore'),
                                                                     (2, 7, 'Editor');  -- solo lui come Editor

-- Luca Gaetani
INSERT INTO Ruolo_conferenza (ref_utente, ref_conferenza, ruolo) VALUES
                                                                     (3, 7, 'Chair'),
                                                                     (3, 7, 'Revisore'),
                                                                     (3, 7, 'Autore');

-- Daniele Orazio Susino
INSERT INTO Ruolo_conferenza (ref_utente, ref_conferenza, ruolo) VALUES
                                                                     (4, 7, 'Chair'),
                                                                     (4, 7, 'Revisore'),
                                                                     (4, 7, 'Autore');

-- Paper 11: Autore Diego (id_utente = 1)
INSERT INTO Paper (titolo, abstract, data_sottomissione, ref_utente, ref_conferenza)
VALUES ('Quantum Security Analysis', 'Studio sull\'applicazione della sicurezza quantistica nei sistemi distribuiti.', NOW(), 1, 7);

-- Paper 12: Autore Leonardo (id_utente = 2)
INSERT INTO Paper (titolo, abstract, data_sottomissione, ref_utente, ref_conferenza)
VALUES ('AI Ethics in Practice', 'Analisi dei dilemmi etici nell\'uso dell\'intelligenza artificiale nella società moderna.', NOW(), 2, 7);

-- Paper 13: Autore Luca (id_utente = 3)
INSERT INTO Paper (titolo, abstract, data_sottomissione, ref_utente, ref_conferenza)
VALUES ('Data Privacy Frameworks', 'Confronto tra i principali framework normativi per la protezione dei dati personali.', NOW(), 3, 7);

-- Paper 14: Autore Daniele (id_utente = 4)
INSERT INTO Paper (titolo, abstract, data_sottomissione, ref_utente, ref_conferenza)
VALUES ('Biometric Authentication Risks', 'Valutazione delle vulnerabilità nei sistemi di autenticazione biometrica.', NOW(), 4, 7);

-- Revisione 1: Paper di Diego (id_paper = 1), revisore Leonardo (id_utente = 2)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper, ref_sottorevisore)
VALUES (NULL, NULL, null, NULL, NULL, NULL, 2, 11, NULL);

-- Revisione 2: Paper di Leonardo (id_paper = 2), revisore Luca (id_utente = 3)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper, ref_sottorevisore)
VALUES (NULL, NULL, null, NULL, NULL, NULL, 3, 12, NULL);

-- Revisione 3: Paper di Luca (id_paper = 3), revisore Daniele (id_utente = 4)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper, ref_sottorevisore)
VALUES (NULL, NULL, null, NULL, NULL, NULL, 4, 13, NULL);

-- Revisione 4: Paper di Daniele (id_paper = 4), revisore Diego (id_utente = 1)
INSERT INTO Revisione (testo, valutazione, data_sottomissione, punti_forza, punti_debolezza, commento_chair, ref_utente, ref_paper, ref_sottorevisore)
VALUES (NULL, NULL, null, NULL, NULL, NULL, 1, 14, NULL);

INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
                                                  (18, 11),
                                                  (11, 11),
                                                  (2, 11);

INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
                                                  (9, 12),
                                                  (27, 12),
                                                  (54, 12);

INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
                                                  (32, 13),
                                                  (2, 13),
                                                  (29, 13);

INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES
                                                  (9, 14),
                                                  (55, 14),
                                                  (14, 14);
