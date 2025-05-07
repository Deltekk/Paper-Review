# LLM di Paper Review

L'**LLM di Paper Review** è basata su **LLAMA** e viene utilizzata per **generare tag** in base ai documenti che gli vengono proposti.

## Come Installarla 

Per utilizzare l'LLM bisogna **prima installare LLAMA e CUDA** nel sistema, per poi instalalre i **moduli di python** tramite il file requirements.

### Installare CUDA

Seguire la **guida ufficiale di CUDA** per l'installazione, in modo tale da poter usare la GPU per runnare l'LLM [Installazione CUDA](https://docs.nvidia.com/cuda/cuda-installation-guide-microsoft-windows/index.html).

### Installare LLAMA

L'installazione è automatica, basta **scaricare ed eseguire il file relativo al proprio OS** dal sito di [LLAMA](https://ollama.com/download).

### Installare i moduli python

L'installazione anche qui è automatica. Si **consiglia** di utilizzare un **ambiente virtuale** per installare i moduli di **python**.

Per installare i moduli, basterà **eseguire il comando**:
```bash
    pip install -r requirements.txt
```

## Come eseguire la LLM

Per **mettere in ascolto l'API** dell'LLM bisogna **eseguire il comando**:
```bash
    uvicorn main:app
```

