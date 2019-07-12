package MWExtraction.Wrapper;

import java.io.*;
import java.util.ArrayList;

public class Preparation {

    /**
     * Change the File to BioTex format
     * Each extracted sentences is considered as ONE Document.
     * @param filePath the path to .txt file
     * @return the unique .txt file containing all sentences from the file
     */
    public static String makeEachLineADocument(String filePath) {
        File temporaryFile = getTemporaryFile();

        try{
            FileWriter fw = new FileWriter(temporaryFile);

            File sourceFile = getSourceFile(filePath);
            BufferedReader bf = new BufferedReader(new FileReader(sourceFile));

            String line = bf.readLine();

            while(line != null){
                fw.write("##########END##########\n");
                fw.write(line+"\n");
                line = bf.readLine();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temporaryFile.getPath();
    }

    /**
     * Change the File to BioTex format
     * Adds a line a the bottom to mark the end of the document
     * @param filePath the path to .txt file
     * @return the unique .txt file containing all sentences from the file
     */
    public static String makeADocument(String filePath) {
        File temporaryFile = getTemporaryFile();

        try{
            FileWriter fw = new FileWriter(temporaryFile, true);

            File sourceFile = getSourceFile(filePath);
            BufferedReader bf = new BufferedReader(new FileReader(sourceFile));

            String line = bf.readLine();
            while(line != null){
                fw.write(line+"\n");
                line = bf.readLine();
            }
            fw.write("##########END##########\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temporaryFile.getPath();
    }

    /**
     * Change the Files to BioTex format
     * All extracted sentences from each pdf file, are considered as ONE Document.
     * @param filesPaths all paths to .txt files
     * @return the unique .txt file containing all sentences from all files
     */
    public static String makeADocument(ArrayList<String> filesPaths) {
        if(filesPaths.size() == 0){
            throw new IllegalArgumentException("The array must have at least one file in it");
        }
        String ret = "";
        for (String filePath : filesPaths) {
            ret = makeADocument(filePath);
        }
        return ret;
    }

    /**
     * Gets the source File Object and checks if it exists as a file
     * @param filePath the path to the file
     * @return the source File Object
     * @throws FileNotFoundException if the file doesn't exists
     */
    private static File getSourceFile(String filePath) throws FileNotFoundException {
        File sourceFile = new File(filePath);
        if(!sourceFile.isFile()){
            throw new FileNotFoundException(sourceFile.getAbsolutePath());
        }
        return sourceFile;
    }

    /**
     * Gets the temporary File Object
     * @return the temporary File Object
     */
    private static File getTemporaryFile(){
        (new File("./tmp/")).mkdirs();
        File temporaryFile = new File("./tmp/tmp.txt");;

        return temporaryFile;
    }
}
