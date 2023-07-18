package com.github.sszuev.textfileutils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class MergeSortExamplesTest {

    /**
     * Example of source file (pattern: {@code  UUID_prefix:number_lines_in_block:line_position_after_sorting}):
     * <pre>{@code
     * 35a4db10-c14a-4929-a776-45ae2711a8f4:3:2384
     * bdbc9c9b-9fc7-4cfe-9629-1595ead80fd0:4:8225
     * 1a595ab6-b671-4f9a-974a-591b349855ea:1:1167
     * 03e4fa06-a6b3-422d-864a-d0a9d56873b4:3:145
     * 028e1725-d38e-4be4-94dc-6dfdc9a429ae:2:90
     * }</pre>
     */
    @Test
    public void testSort(@TempDir Path dir) throws Exception {
        Charset charset = StandardCharsets.UTF_8;
        String givenContent = TestsUtils.readTextFile("/shuffled.csv", charset);
        String expectedContent = TestsUtils.readTextFile("/sorted.csv", charset);

        Path source = Files.createTempFile(dir, "xxx-merge-sort-source-", ".xxx");
        Files.writeString(source, givenContent, charset);
        Path expected = Files.createTempFile(dir, "xxx-merge-sort-expected-", ".xxx");
        Files.writeString(expected, expectedContent, charset);

        Path target = Paths.get(source.toString().replace("-source-", "-target-"));

        long fileSize = Files.size(source);
        int allocatedMemory = (int) (fileSize / 10);
        Comparator<String> comparator = Comparator.comparing((String it) -> TestsUtils.substringBefore(it, ":"))
                .thenComparing(it -> TestsUtils.substringAfter(it, ":"));

        cc.datafabric.textfileutils.files.MergeSortKt.sort(
                /* source file */ source,
                /* result file (nonexistent physically) */ target,
                /* comparator to sort lines */ comparator,
                /* delimiter to be used to split content on lines */ "\n",
                /* allowed memory consumption (approximately) */ allocatedMemory,
                /* control diskspace, if true no additional diskspace will be used */ false,
                /* by default UTF-8 */ charset,
                /* use kotlin, in java we have to use thread-pools */ kotlinx.coroutines.Dispatchers.getIO()
        );

        boolean res = cc.datafabric.textfileutils.files.FilesKt.contentEquals(expected, target);
        Assertions.assertTrue(res);
    }
}
