package com.github.sszuev.textfileutils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class BinarySearchExamplesTest {

    /**
     * Example of source file (pattern: {@code  UUID_prefix:number_lines_in_block:line_position}):
     * <pre>{@code
     * 006f4154-eb9e-4b5e-b388-4898d2176cf4:1:16
     * 0072252c-24cf-4977-b834-e115002a9126:1:17
     * 00823f7d-5e06-4547-848b-3e10952c4fa6:3:18
     * 00823f7d-5e06-4547-848b-3e10952c4fa6:3:19
     * 00823f7d-5e06-4547-848b-3e10952c4fa6:3:20
     * 009eb89a-5f87-48a3-a8fc-39372206b510:1:21
     * 00ad11fc-fc28-4835-b71c-9464d81bc121:4:22
     * 00ad11fc-fc28-4835-b71c-9464d81bc121:4:23
     * 00ad11fc-fc28-4835-b71c-9464d81bc121:4:24
     * 00ad11fc-fc28-4835-b71c-9464d81bc121:4:25
     * }</pre>
     */
    @Test
    public void testSearch(@TempDir Path dir) throws Exception {
        Charset charset = StandardCharsets.UTF_8;
        String content = TestsUtils.readTextFile("/sorted.csv", charset);

        Path source = Files.createTempFile(dir, "xxx-binary-search-", ".xxx");
        Files.writeString(source, content, charset);

        Comparator<String> comparator = Comparator.comparing(it -> TestsUtils.substringBefore(it, ":"));

        try (Stream<String> lines = Files.lines(source, charset)) {
            lines.forEach(line -> {
                String searchString = TestsUtils.substringBefore(line, ":");
                int expectedBlockSize = Integer.parseInt(TestsUtils.substringBefore(TestsUtils.substringAfter(line, ":"), ":"));

                kotlin.Pair<Long, List<String>> found = cc.datafabric.textfileutils.files.BinarySearchKt.binarySearch(
                        /* source (sorted) file */ source,
                        /* pattern to search (here it is the prefix before ":")*/ searchString,
                        /* to use while reading data from file */ ByteBuffer.allocateDirect(8912 * 2),
                        /* by default UTF-8 */ charset,
                        /* delimiter to be used to split content on lines */ "\n",
                        /* comparator to sort lines */ comparator,
                        /* line restriction, to avoid memory lack e.g. when there is no delimiter */ 1024,
                        /* maximum number of lines in a paragraph */ 8912
                );
                Assertions.assertEquals(expectedBlockSize, found.getSecond().size());
                found.getSecond().forEach(it -> {
                    Assertions.assertTrue(it.length() > searchString.length());
                    Assertions.assertTrue(it.startsWith(searchString + ":"));
                });
            });
        }
    }
}
