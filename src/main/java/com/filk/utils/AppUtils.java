package com.filk.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    public static String getFileContent(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
