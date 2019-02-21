package com.filk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AppUtils {
    public static String getFileContent(String filePath) throws IOException {
        String file_line;
        StringBuilder file_content = new StringBuilder();
        FileReader fr = new FileReader(new File(filePath));
        BufferedReader br = new BufferedReader(fr);
        while ((file_line = br.readLine()) != null) {
            file_content.append(file_line);
        }
        br.close();
        return file_content.toString();
    }
}
