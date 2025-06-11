package com.paperreview.paperreview.common.llm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MultipartBodyPublisher {

    public static Iterable<byte[]> create(Path filePath, String boundary) throws IOException {
        String fileName = filePath.getFileName().toString();
        String CRLF = "\r\n";

        List<byte[]> byteArrays = new ArrayList<>();

        String partHeader = "--" + boundary + CRLF +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + CRLF +
                "Content-Type: application/pdf" + CRLF + CRLF;

        byteArrays.add(partHeader.getBytes());
        byteArrays.add(Files.readAllBytes(filePath));
        byteArrays.add(CRLF.getBytes());

        String end = "--" + boundary + "--" + CRLF;
        byteArrays.add(end.getBytes());

        return byteArrays;
    }
}
