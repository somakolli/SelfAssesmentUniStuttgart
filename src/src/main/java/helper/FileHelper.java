package helper;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileHelper {
    public FileHelper() {

    }

    public List<String> getFileMapFromResourceDirectory(File directory) throws IOException {
        ArrayList<String> filenames = new ArrayList<>();
        for (File file :
                directory.listFiles()) {

            if(file.isDirectory()) {
                filenames.addAll(getFileMapFromResourceDirectory(file));
            } else {
                String path = file.getPath().replaceAll(".*website", "");
                if(path.charAt(0)=='\\'){
                    path = path.substring(1);
                }
                System.out.println(path);
                filenames.add(path);
            }
        }
        return filenames;
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

    public byte[] bytesFromResources(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();
        try{
            return  IOUtils.toByteArray(classLoader.getResourceAsStream(fileName));
        } catch (IOException e){
            e.printStackTrace();
        }
        return new byte[0];
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
