package com.easymarkersapp.easymarkersapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Определяет тип изображения по "магическим" байтам начала файла.
 * Поддерживает JPEG, WebP, AVIF.
 */
public final class ImageTypeDetector {

    private ImageTypeDetector() {}

    public static final String JPG_EXTENSION  = "jpg";
    public static final String WEBP_EXTENSION = "webp";
    public static final String AVIF_EXTENSION = "avif";

    private static final int HEADER_SIZE = 12;

    /**
     * Читает первые байты потока и возвращает каноническое расширение.
     * Поток должен поддерживать mark/reset (оборачивай в BufferedInputStream).
     */
    public static Optional<String> detectExtension(InputStream in) throws IOException {
        if (!in.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark/reset");
        }
        in.mark(HEADER_SIZE);
        byte[] header = new byte[HEADER_SIZE];
        int read = in.readNBytes(header, 0, HEADER_SIZE);
        in.reset();

        if (read < HEADER_SIZE) {
            return Optional.empty();
        }

        // JPEG: FF D8 FF
        if ((header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF) {
            return Optional.of(JPG_EXTENSION);
        }

        // WebP: "RIFF" .... "WEBP"
        if (matches(header, 0, "RIFF") && matches(header, 8, "WEBP")) {
            return Optional.of(WEBP_EXTENSION);
        }

        // AVIF: [4 bytes size] "ftyp" [brand: "avif"|"avis"]
        if (matches(header, 4, "ftyp")
                && (matches(header, 8, "avif") || matches(header, 8, "avis"))) {
            return Optional.of(AVIF_EXTENSION);
        }

        return Optional.empty();
    }

    private static boolean matches(byte[] data, int offset, String ascii) {
        byte[] expected = ascii.getBytes(StandardCharsets.US_ASCII);
        if (offset + expected.length > data.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (data[offset + i] != expected[i]) return false;
        }
        return true;
    }
}