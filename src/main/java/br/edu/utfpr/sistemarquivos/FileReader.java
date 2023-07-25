package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {

    public static void read(String fileName) {
        try {
            final var path = Paths.get(fileName);
            final var showFile = Files.readString(path);
            System.out.println(showFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
