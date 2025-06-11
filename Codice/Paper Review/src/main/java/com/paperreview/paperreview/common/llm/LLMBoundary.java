package com.paperreview.paperreview.common.llm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperreview.paperreview.common.DotenvUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;


public class LLMBoundary {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<String> inviaPdf(Path pdfPath) throws IOException, InterruptedException {
        DotenvUtil.init(); // inizializza .env
        String apiUrl = DotenvUtil.getTagPDFApi();

        String boundary = "----CMSBoundary";
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArrays(
                MultipartBodyPublisher.create(pdfPath, boundary)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(bodyPublisher)
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
