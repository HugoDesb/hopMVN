package main;

import document.TextDocument;
import pretreatement.ExtractorHAS;
import pretreatement.ExtractorPDF;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Initialize {

    private String recoBasePath;
    private ArrayList<TextDocument> textDocuments;

    /**
     * Constructor for Initialize
     * @param recoBasePath The path where the pdf files are stored
     */
    public Initialize(String recoBasePath) {
        this.recoBasePath = recoBasePath;
        textDocuments = new ArrayList<>();
    }

    /**
     * Main access to run the initialization for the process.
     * @return this
     */
    public Initialize run(){
        textDocuments = extractAndFilter(listFilesPathAndTypes());
        return this;
    }

    /**
     * Sauvegarde dans des fichiers txt les phrases retournées par l'extraction et séléction de phrases
     * @return this
     */
    public Initialize save(){
        for (TextDocument textDocument : textDocuments) {
            textDocument.writeFile();
        }
        return this;
    }

    /**
     * Getter for the textDocuments
     * @return ArrayList<TextDocuments>
     */
    public ArrayList<TextDocument> getTextDocuments(){
        return textDocuments;
    }

    /**
     * Extract the text from a map of reco files according to their type.
     * @param map of path,type
     * @return An ArrayList of TextDocuments
     */
    private ArrayList<TextDocument> extractAndFilter(Map<String, String> map){
        ArrayList<TextDocument> textDocuments = new ArrayList<>();
        File f;
        ExtractorHAS extHAS = new ExtractorHAS();
        ExtractorPDF extPDF = new ExtractorPDF();

        for (String path : map.keySet()) {
            switch(map.get(path)){
                case "HAS":
                    f = new File(path);
                    textDocuments.add(extHAS.extract(f));
                    break;
                default:
                    f = new File(path);
                    textDocuments.add(extPDF.extract(f));
                    break;
            }
        }
        return textDocuments;
    }

    /**
     * Given a base folder, get the path of all reco files and their type into a HashMap
     * @return HashMap<String, String>
     */
    private Map<String, String> listFilesPathAndTypes(){
        Map<String, String> map = new HashMap<>();
        File f = new File(recoBasePath);
        if(!f.isDirectory()){
            throw new IllegalArgumentException("The path given does not refers to a directory as it should. Current value : " + recoBasePath);
        }
        //scan folder
        for (File reco : f.listFiles()) {
            if(reco.getName().matches("^HAS.*")){
                map.put(reco.getPath(), "HAS");
            }else{
                map.put(reco.getPath(), "STANDARD");
            }
        }
        return map;
    }
}
