package main;

import document.TextDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import pretreatement.ExtractorHAS;
import pretreatement.ExtractorPDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Initialize {

    private String recoPath;



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
    }



    /**
     * Get all reco files
     * @return
     */
    private Map<String, String> listFilesPathAndTypes(){
        Map<String, String> map = new HashMap<>();
        File f = new File(recoPath);
        if(!f.isDirectory()){
            throw new IllegalArgumentException("The path given does not refers to a directory as it should. Current value : " + recoPath);
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
