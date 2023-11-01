package com.cai.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author cai584770
 * @date 2023/11/1 下午5:40
 * @Version
 */
public class Read {

    public static String readsequence(String filepath) {
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith(">")) {
                    data = data + line;
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
