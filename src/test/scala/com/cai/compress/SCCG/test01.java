package com.cai.compress.SCCG;

import com.cai.compress.SCCG.SCCG;
import com.cai.tools.Read;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.lang.management.*;

public class test01 {
    public static void main(String[] args) throws IOException, InterruptedException {
        String ref_file_path = "./data/pseudo88_chr1.fa";
        String tar_file_path = "./data/pseudo108_chr1.fa";
        String ref = Read.readsequence(ref_file_path);
        String tar = Read.readsequence(tar_file_path);
        SCCG sccg = new SCCG();

        List<String> s = sccg.compress(ref, tar);

        // System.out.println(s.get(0));
        // System.err.println();
        // System.out.println(s);
        String result = sccg.uncompress(ref, s);
        System.out.println(result);

        // test processTargetSequence
        // ProcessInformation result = processTargetSequence(ref);
        // String targetSequence = result.getTargetSequence();
        // String Nlist = result.getNlist();
        // String Llist = result.getLlist();
        // System.out.println(result);
        // System.out.println(targetSequence);
        // System.out.println(Nlist);
        // System.out.println(Llist);

        // String temp01 = result01.processSequence(ref);
        // System.out.println(temp01);

        // String temp02 = result01.LreadSeq(file_path);
        // System.out.println(temp02);

        // String temp03 = sccgc.GreadrefSeq(file_path);
        // System.out.println(temp03);

        // String temp04 = sccgc.processSequence(ref);
        // System.out.println(temp04);

        // String temp05 = sccgc.GreadtarSeq(file_path, result_path);
        // System.out.println(temp05);
        // System.out.println("-----");

        // ProcessInformation temp06 = sccgc.processTargetSequence(ref);
        // String targets = temp06.getTargetSequence();
        // String Nlist = temp06.getNlist();
        // String Llist = temp06.getLlist();
        // System.out.println(Nlist);
        // System.out.println(Llist);
        // System.out.println("-----");
        // System.out.println(targets);
        // System.out.println("-----");

        // List<Position> temp07 = sccgc.lowercase_position(tar);
        // for (Position iterable_element : temp07) {
        // System.out.println();
        // System.out.println("-----");
        // }

        // List<Position> temp08 = sccgc.Lmatch(ref, tar, kmer_length);
        // for (Position iterable_element : temp08) {
        // System.out.println();
        // System.out.println("-----");
        // }

    }



}
