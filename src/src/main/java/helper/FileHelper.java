package helper;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public FileHelper() {

    }

    public String getFileFromResources(String fileName){
        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try{
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public void writeFileToResources(String fileName, String content){
        try {
            Files.write(Paths.get(fileName), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPathFormResources(String fileName){
        return Paths.get(fileName).toString();
    }
}
