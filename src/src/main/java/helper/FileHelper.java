package helper;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileHelper {
    public FileHelper() {

    }

    public Map<String, byte[]> getFileMapFromResourceDirectory(File directory) throws IOException {
        Map<String, byte[]> fileMap = new LinkedHashMap<>();
        for (File file :
                directory.listFiles()) {

            if(file.isDirectory()) {
                fileMap.putAll(getFileMapFromResourceDirectory(file));
            } else {
                String path = file.getPath().replaceAll(".*website", "");
                if(path.charAt(0)=='\\'){
                    path = path.substring(1);
                }
                System.out.println(path);
                fileMap.put(path, Files.readAllBytes(file.toPath()));
            }
        }
        return fileMap;
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
