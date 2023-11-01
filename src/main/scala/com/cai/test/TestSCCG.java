package com.cai.test;


import com.cai.compress.SCCG.SCCG;
import com.cai.compress.SCCG.methods.ORGC;
import com.cai.tools.Read;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author cai584770
 * @date 2023/11/1 下午5:29
 * @Version
 */
public class TestSCCG {

    @Test
    public void Test01() {
        System.out.println("aa");
    }

    @Test
    public void Test02() {
        String filepath = ".././data/test";
        try {
            ORGC.use7zip(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test // 测试压缩
    public void Test03() throws IOException, InterruptedException {
        String ref_file_path = "/home/caiorg/program/Seq/data/data/pseudo88_chr1.fa";
        String tar_file_path = "/home/caiorg/program/Seq/data/data/pseudo108_chr1.fa";
        String ref = Read.readsequence(ref_file_path);
        String tar = Read.readsequence(tar_file_path);
        SCCG sccg = new SCCG();
        System.out.println("1");

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
