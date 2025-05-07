from fastapi import FastAPI, UploadFile, File # Librerie per la gestione delle API
from fastapi.responses import JSONResponse # Librerie per la risposta delle API
import fitz  # Libreria per estrarre il testo dal PDF 
import ollama # Libreria per utilizzare LLAMA, l'LLM che analizzerà il testo
import tempfile # Libreria per gestire i file temporanei (file che si creano quando si riceve il file da analizzare)
import os # Libreria per poter cancellare il file temporaneo
import json # Libreria per formattare la risposta dell' LLM in JSON

# Avviamo l'applicazione
app = FastAPI()

def estrai_array_json_grezzo(testo):
    """
    Cerca di estrarre un array JSON valido da una stringa anche se preceduto da testo extra.
    """
    try:
        inizio = testo.index('[') # L'inizio del testo sarà dove troviamo la prima parentesi quadra aperta
        sottojson = testo[inizio:] # prendiamo tutto ciò che c'è dopo, perché è effettivamente il testo necessario
        return json.loads(sottojson) # ritorniamo il testo trovato come json
    except (ValueError, json.JSONDecodeError) as e: # se c'è stato un errore di parsing
        print("Errore di parsing JSON:", e)
        print("Contenuto grezzo:", testo)
        return None # non ritorniamo nulla 

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

def genera_tag_con_ollama(testo, max_parole=1000, modello='llama3'):
    """
    Genera i tag in base al testo passato come parametro
    """
    testo_troncato = testo[:max_parole * 6]  # Limitiamo il testo per evitare di eccedere il limite del modello
    prompt = (
        "Analizza il seguente testo e restituisci ESCLUSIVAMENTE una lista JSON e nient'altro. Prendi 4-7 argomenti principali, "
        "scritti in italiano, adatti come tag generici. NON scrivere nient'altro se non il JSON con i tag. "
        "Formato atteso, in italiano: [\"Argomento1\", \"Argomento2\", ...]\n\n"
        f"Testo:\n{testo_troncato}\n\n"
    )

    # effetuiamo la richiesta al modello LLM 
    response = ollama.chat(
        model=modello,
        messages=[{"role": "user", "content": prompt}]
    )

    # Ritorniamo la risposta dell' LLM
    return response['message']['content']

@app.post("/tag-pdf")
async def tag_pdf(file: UploadFile = File(...)):
    """
    Esponiamo la rotta /tag-pdf e facciamo in modo che accetti dei file come parametro, questa poi restituirà i tag in formato json del documento
    """

    # Salva il file temporaneamente per prenderne la path
    with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as tmp:
        tmp.write(await file.read())
        tmp_path = tmp.name

    # Estrai testo e genera tag
    try:
        testo = estrai_testo_da_pdf(tmp_path)
        raw_tags = genera_tag_con_ollama(testo)
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