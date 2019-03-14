package Pretraitement;

import common.TextDocument;

import java.util.ArrayList;

/**
 * Class enabling a clean up of txt files, in order to obtain 1 sentences per line.
 */
public class Normalize {

    private TextDocument file;


    public Normalize(TextDocument file) {
        this.file = file;
    }


    /**
     * Each paragraph is separated by a blank line
     * @return
     */
    public ArrayList<String> separateIntoParagraphs(){
        ArrayList<String> paragraphes = new ArrayList<>();
        StringBuilder para = new StringBuilder();
        for (String line :
                file.getLines()) {
            if(line.matches("\\s") && !para.toString().equals("")){
                paragraphes.add(para.toString().replace("\n", ""));
                para = new StringBuilder();
            }else{
                para.append(line);
            }
        }
        return paragraphes;
    }

    private boolean startByUPPERCASE(String line){
        return line.matches("^[A-Z]");
    }

    public boolean hasPoint(String line){
        return line.matches("\\.");
    }
}
