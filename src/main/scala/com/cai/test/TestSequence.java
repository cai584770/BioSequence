package com.cai.test;

import com.cai.compress.SCCG.SCCG;
import com.cai.tools.Read;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author cai584770
 * @date 2023/11/1 下午7:56
 * @Version
 */
public class TestSequence {
    @Test
    public void Test01() throws IOException, InterruptedException {
        String ref_file_path = "/home/caiorg/program/Seq/data/data/pseudo88_chr1.fa";
        String tar_file_path = "/home/caiorg/program/Seq/data/data/pseudo108_chr1.fa";
        String ref = Read.readsequence(ref_file_path);
        String tar = Read.readsequence(tar_file_path);
        SCCG sccg = new SCCG();

        List<String> s = sccg.compress(ref, tar);
        int count = 0;
        for (String line : s) {
            System.out.println(line);
            count++;
            if (count >= 20) {
                break;
            }
        }

    }

}
