package common;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing a .txt document
 */
public class TextDocument {

    private File file;
    private List<String> lines;


    public TextDocument(@NotNull File file) throws IOException {
        this.file = file;
        lines = Files.readAllLines(Paths.get(file.getPath()));
    }

    /**
     * Get the current file
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     *
     * @return
     */
    public List<String> getLines() {
        return lines;
    }


    /**
     *
     * @return
     * @throws FileNotFoundException
     */
    public void writeFile() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(file);
        for (String line :
                lines) {
            pw.println(line);
        }
    }

    /**
     * SETTER for lines
     * @param list
     */
    private void setLines(List<String> list){
        lines = list;
    }

    /**
     *
     * @return
     */
    public TextDocument oneSentencePerLine(){
        Iterator<String> it = lines.iterator();
        List<String> sentences = new ArrayList<>();
        String currentSentence = "";
        String currentLine;
        while(it.hasNext()){
            currentLine = it.next();
            // Si pas de phrase en cours
            if(currentSentence == ""){
                for (Character ch :currentLine.toCharArray()){
                    currentSentence += ch;
                    if(ch.equals(".")){
                        sentences.add(currentSentence+"\n");
                        currentSentence = "";
                    }
                }
            }else{
                //Si MAJUSCULE en début de ligne.
                if(currentLine.matches("^[A-Z]")){
                    //ON AJOUTE TOUTES LES PHRASES
                    String [] splitted = currentLine.split("\\.+\\s");

                    if(currentLine.matches("\\.+\\s+$") ||currentLine.matches("\\.+$") ){
                        for (String s : splitted) {
                            sentences.add(s);
                        }
                    }

                    for (int i = 0; i<currentLine.split("\\.+\\s").length; i++ ){
                        //sentences.add(s);
                    }
                }
            }
        }
        setLines(sentences);
        return this;
    }




}
