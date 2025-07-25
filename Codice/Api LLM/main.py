from fastapi import FastAPI, UploadFile, File, Form # Librerie per la gestione delle API
from fastapi.responses import JSONResponse # Librerie per la risposta delle API
import ast # Conversione delle parole chiave
import fitz  # Libreria per estrarre il testo dal PDF 
import ollama # Libreria per utilizzare LLAMA, l'LLM che analizzerà il testo
import tempfile # Libreria per gestire i file temporanei (file che si creano quando si riceve il file da analizzare)
import os # Libreria per poter cancellare il file temporaneo
import json # Libreria per formattare la risposta dell' LLM in JSON

# Avviamo l'applicazione
app = FastAPI()

import json

def estrai_array_json_grezzo(testo):
    """
    Estrae e carica un array JSON valido da una stringa, cercando tra la prima '[' e l'ultima ']'.
    Restituisce None in caso di errore di parsing.
    """
    try:
        inizio = testo.index('[')
        fine = testo.rindex(']') + 1  # incluso
        sottojson = testo[inizio:fine]
        return json.loads(sottojson)
    except (ValueError, json.JSONDecodeError) as e:
        print("Errore di parsing JSON:", e)
        print("Contenuto grezzo:", testo)
        return None


def estrai_testo_da_pdf(percorso_pdf):
    """
    Apre ed estrae il testo dal PDF il quale percorso è stato passato come parametro
    """

    # Apriamo il percorso del file
    with fitz.open(percorso_pdf) as doc:
        testo = ""
        for pagina in doc: # per ogni pagina, salviamo il testo letto
            testo += pagina.get_text()
    return testo # ritorniamo il testo

def genera_tag_con_ollama(testo, parole_chiave, max_parole=1500, modello='llama3'):
    """
    Genera i tag in base al testo passato come parametro
    """
    testo_troncato = testo[:max_parole * 6]  # Limitiamo il testo per evitare di eccedere il limite del modello
    prompt = (
        "Analizza il seguente testo e restituisci **ESCLUSIVAMENTE** (e **SOLO SE HA SENSO**)"
        "una lista di massimo **2 parole chiave** pertinenti, **scelte unicamente tra quelle elencate** di seguito. "
        "La risposta deve essere scritta **in italiano** e deve contenere **solo** un array JSON.\n"        
        f"Parole chiave tra cui scegliere (in italiano):\n{parole_chiave}\n\n"
        f"Testo da analizzare (in italiano):\n{testo_troncato}\n\n"
        f"Adesso ritornami le parole chiave in un array di stringhe tra parentesi quadre in ITALIANO E SOLO IN ITALIANO! IL FORMATO DA SEGUIRE È QUESTO: [\"ParolaChiave1\", \"ParolaChiave2\", ...]\n\n"
    )

    # effetuiamo la richiesta al modello LLM 
    response = ollama.chat(
        model=modello,
        messages=[{"role": "user", "content": prompt}]
    )

    # Ritorniamo la risposta dell' LLM
    return response['message']['content']

@app.post("/tag-pdf")
async def tag_pdf(file: UploadFile = File(...), parole_chiave: str = Form(...)):
    """
    Esponiamo la rotta /tag-pdf e facciamo in modo che accetti dei file come parametro, questa poi restituirà i tag in formato json del documento
    """

    parole_chiave = ast.literal_eval(parole_chiave)

    # Salva il file temporaneamente per prenderne la path
    with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as tmp:
        tmp.write(await file.read())
        tmp_path = tmp.name

    # Estrai testo e genera tag
    try:
        testo = estrai_testo_da_pdf(tmp_path)
        raw_tags = genera_tag_con_ollama(testo, parole_chiave)
        tags = estrai_array_json_grezzo(raw_tags)

        # Se il json restituito è corretto
        if tags is not None: # Ritorniamo i tag
            return {"tags": tags}
        else:
            # Altrimenti ritorniamo un errore di parsing
            return JSONResponse(status_code=500, content={
                "error": "Errore di parsing JSON",
                "risposta_modello": raw_tags
            })
    except Exception as e:        
        return JSONResponse(status_code=500, content={"error": str(e)})
    finally:
        # In ogni occasione, se il file esiste ancora, lo rimuoviamo per evitare residui
        if os.path.exists(tmp_path):
            os.remove(tmp_path)