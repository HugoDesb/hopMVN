package common.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private Properties properties;

    private static Config instance;

    private String filepath;

    public static Config getInstance(){
        if(instance == null){
            return new Config();
        }else{
            return instance;
        }
    }

    private Config(){

    }

    public static void main (String [] args){
        System.out.println(Config.getInstance().getProp("pretreatment.rnntagger_path"));
    }

    public String getProp(String propertyName){
        if(properties == null){
            readProperties(new File("default.properties"));
        }
        return properties.getProperty(propertyName);
    }

    public void readProperties(File configFile){
        try {
            properties = new Properties();

            String propFileName = "default.properties";

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
