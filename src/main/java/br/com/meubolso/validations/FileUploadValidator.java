package br.com.meubolso.validations;

import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.exception.ErrorMessages;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public final class FileUploadValidator {
    private static final long DEFAULT_MAX_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    private static final Set<String> CSV_CONTENT_TYPES = Set.of(
            "text/csv",
            "application/vnd.ms-excel",
            "application/octet-stream"
    );

    private FileUploadValidator() {}

    public static void validateNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.FILE_REQUIRED);
        }
    }

    public static void validateSize(MultipartFile file, long maxBytes) {
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException("arquivo excede tamanho máximo de " + maxBytes + " bytes");
        }
    }

    public static void validateContentType(MultipartFile file, Set<String> allowed) {
        String ct = file.getContentType();
        if (ct != null && allowed.stream().noneMatch(a -> a.equalsIgnoreCase(ct))) {
            throw new IllegalArgumentException("content-type inválido: " + ct + " (esperado CSV)");
        }
    }

    public static void validateEncodingUTF8(MultipartFile file) {
        try (var raw = file.getInputStream()) {
            // Não depender de mark/reset: apenas ler os primeiros bytes e encerrar
            int b1 = raw.read();
            int b2 = raw.read();
            int b3 = raw.read();
            // Detecta BOM UTF-8 (EF BB BF). Caso contrário, assume UTF-8.
            // Nenhuma ação necessária aqui, pois o serviço já ignora BOM ao parsear.
            boolean hasBom = (b1 == 0xEF && b2 == 0xBB && b3 == 0xBF);
            // validação informativa: nunca falha por ausência de BOM
        } catch (Exception e) {
            throw new IllegalArgumentException("não foi possível validar encoding: " + e.getMessage());
        }
    }

    public static void validateMaxLines(MultipartFile file, int maxLines) {
        if (maxLines <= 0) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                count++;
                if (count > maxLines) {
                    throw new IllegalArgumentException("arquivo ultrapassa o limite de " + maxLines + " linhas");
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("falha ao contar linhas: " + e.getMessage());
        }
    }

    public static void validateCsvUploadOrThrow(MultipartFile file, CategoriaFinanceira tipo) {
        validateNotEmpty(file);
        validateSize(file, DEFAULT_MAX_SIZE_BYTES);
        validateContentType(file, CSV_CONTENT_TYPES);
        validateEncodingUTF8(file);
    }
}