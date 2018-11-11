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

    /**
     * Gets the String of a file from the resources folder
     * @param fileName name of the file
     * @return content of the file
     */
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

    /**
     * gets the bytes form a file in the resources folder
     * @param fileName name of the file
     * @return byte content of the file
     */
    public byte[] bytesFromResources(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();
        try{
            return  IOUtils.toByteArray(classLoader.getResourceAsStream(fileName));
        } catch (IOException e){
            e.printStackTrace();
        }
        return new byte[0];
    }
}
