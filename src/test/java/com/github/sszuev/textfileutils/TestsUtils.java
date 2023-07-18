package com.github.sszuev.textfileutils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

class TestsUtils {
    static String readTextFile(String resource, Charset charset) throws IOException {
        try (InputStream source = Objects.requireNonNull(TestsUtils.class.getResourceAsStream(resource))) {
            return new String(source.readAllBytes(), charset);
        }
    }

    @SuppressWarnings("SameParameterValue")
    static String substringBefore(String source, String delimiter) {
        int index = source.indexOf(delimiter);
        return index == -1 ? source : source.substring(0, index);
    }

    @SuppressWarnings("SameParameterValue")
    static String substringAfter(String source, String delimiter) {
        int index = source.indexOf(delimiter);
        return index == -1 ? source : source.substring(index + delimiter.length());
    }
}
