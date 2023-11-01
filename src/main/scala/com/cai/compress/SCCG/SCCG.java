package com.cai.compress.SCCG;

import com.cai.compress.SCCG.methods.SCCGD;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SCCG {

    public List<String> compress(String reference_sequence, String target_sequence)
            throws IOException, InterruptedException {
        processsequence(reference_sequence, true);
        processsequence(target_sequence, false);

        String ref_path = "./reference.fa";
        String tar_path = "./target.fa";

        Runtime.getRuntime().exec("mkdir -p ./temp"); // 临时文件夹

        Runtime.getRuntime().exec("java -Xmx12G SCCGC " + ref_path + " " + tar_path + " ./temp "); // 进行压缩

        String filePath = "./temp/target.fa";

        File file = new File(filePath);
        while (!file.exists()) { // 此处需要注意 因不明原因
                                 // 解压的文件过段时间会消失（使用Runtime.getRuntime().exec来执行命令时，需要格外小心，这种方法可能会导致潜在的安全风险和跨平台兼容性问题
            // sudo apt-get install p7zip 解压文件
            Runtime.getRuntime().exec("7z x ./temp/target.fa.7z -o./temp");
        }

        Stream<String> lines = Files.lines(Paths.get(filePath));
        List<String> filteredLines = lines
                .skip(2)
                .collect(Collectors.toList());

        lines.close();
        System.out.println(filteredLines.size());

        return filteredLines;
    }

    public String uncompress(String reference_sequence, List<String> result_of_compress)
            throws IOException, InterruptedException {
        processsequence(reference_sequence, true);
        String ref_path = "./reference.fa";
        String result_of_compress_path = "./temp/target.fa";
        String target_sequence = "";
        SCCGD sccgd = new SCCGD();

        Runtime.getRuntime().exec("mkdir -p ./temp"); // 临时文件夹
        write(result_of_compress, result_of_compress_path);

        sccgd.uncompress(ref_path, result_of_compress_path, "./temp");

        target_sequence = read(result_of_compress_path);

        return target_sequence;
    }

    /**
     * 将序列写入临时文件
     * 
     * @param sequence 序列
     * @param flag     True代表写入的为参考序列 False代表写入的为目标序列
     * 
     * 
     */
    public static void processsequence(String sequence, Boolean flag) {
        String filePath = "";
        if (flag) {
            filePath = "./reference.fa";
        } else {
            filePath = "./target.fa";
        }

        try {
            FileWriter fileWriter = new FileWriter(filePath);

            if (flag) {
                fileWriter.write(">reference\n");
            } else {
                fileWriter.write(">target\n");
            }

            int length = sequence.length();
            for (int i = 0; i < length; i += 50) {
                if (i + 50 < length) {
                    fileWriter.write(sequence.substring(i, i + 50) + "\n");
                } else {
                    fileWriter.write(sequence.substring(i) + "\n");
                }
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 
     * 解压缩时需要将压缩的结果写入文件
     */
    public static void write(List<String> filteredLines, String filePath) {
        filteredLines.add(0, ">target");
        filteredLines.add(1, "50");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (String line : filteredLines) {
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 从临时文件中读取序列
     */
    public static String read(String filePath) {
        String data = "";
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(">")) {
                    content.append(line);
                }
            }

            reader.close();

            data = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
