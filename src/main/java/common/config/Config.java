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
            instance = new Config();
        }
        return instance;
    }

    public static Config getInstance(String config_file){
        if( null == instance || !instance.filepath.equals(config_file)){
            instance = new Config(config_file);
        }
        return instance;
    }

    private Config(){
        this.filepath = "default.properties";
    }

    private Config(String config_file){
        this.filepath = config_file;
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

    private void readProperties(File configFile){
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
