package document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Class representing a .txt document
 */
public class TextDocument {

    private File file;
    private ArrayList<Sentence> sentences;

    /**
     * Constructeur
     * @param file
     * @param sentences
     */
    public TextDocument(File file, ArrayList<Sentence> sentences) {
        this.file = file;
        this.sentences = sentences;

    }

    /**
     * Get the current file
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * Get All sentences
     * @return
     */
    public ArrayList<Sentence> getLines() {
        return sentences;
    }

    /**
     * SETTER for lines
     * @param list
     */
    public void setLines(ArrayList<Sentence> list){
        sentences = list;
    }

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
    public void writeFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            System.out.println("NO OUTPUT FILE FOUND -- MAKE SURE THE FILE EXISTS");
            e.printStackTrace();
        }
        for (Sentence sentence : sentences) {
            pw.println(sentence.getText());
        }
    }

    public static class Builder{

        private File file;
        private ArrayList<Sentence> sentences;

        public Builder() {
            this.sentences = new ArrayList<>();
        }

        public void setFile(File file){
            this.file = file;
        }

        public void addLine(Sentence sentence){
            sentences.add(sentence);
        }

        public TextDocument build(){
            if(file == null){
                throw new InvalidParameterException("No file was specified");
            }
            return new TextDocument(file, sentences);
        }
    }


}
