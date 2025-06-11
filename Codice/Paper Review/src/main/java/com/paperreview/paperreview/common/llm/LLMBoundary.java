package com.paperreview.paperreview.common.llm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperreview.paperreview.common.DotenvUtil;
import com.paperreview.paperreview.entities.TopicEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LLMBoundary {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<String> assegnaParoleChiave(Path pdfPath, List<TopicEntity> listaParoleChiave) throws IOException, InterruptedException {
        DotenvUtil.init(); // inizializza .env
        String apiUrl = DotenvUtil.getTagPDFApi();
        String boundary = "----CMSBoundary";
        String CRLF = "\r\n";

        // Estrai nomi dei topic e convertili in JSON stringificato
        List<String> nomi = new ArrayList<>();
        for (TopicEntity topic : listaParoleChiave) {
            nomi.add(topic.getNome());
        }
        String jsonKeywords = mapper.writeValueAsString(nomi); // es: ["AI", "Robotica"]

        List<byte[]> byteArrays = new ArrayList<>();

        // Parte 1: file PDF
        String fileHeader = "--" + boundary + CRLF +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + pdfPath.getFileName() + "\"" + CRLF +
                "Content-Type: application/pdf" + CRLF + CRLF;
        byteArrays.add(fileHeader.getBytes());
        byteArrays.add(Files.readAllBytes(pdfPath));
        byteArrays.add(CRLF.getBytes());

        // Parte 2: parole_chiave come JSON in text/plain
        String keywordHeader = "--" + boundary + CRLF +
                "Content-Disposition: form-data; name=\"parole_chiave\"" + CRLF +
                "Content-Type: text/plain" + CRLF + CRLF;
        byteArrays.add(keywordHeader.getBytes());
        byteArrays.add(jsonKeywords.getBytes());
        byteArrays.add(CRLF.getBytes());

        // Fine del multipart
        byteArrays.add(("--" + boundary + "--" + CRLF).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parsing JSON: {"tags": ["tag1", "tag2", ...]}
        String responseBody = response.body();
        return mapper.readTree(responseBody)
                .get("tags")
                .traverse(mapper)
                .readValueAs(new TypeReference<>() {});
    }
}
