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
    private ArrayList<String> lines;

    public TextDocument(File file, ArrayList<String> lines) {
        this.file = file;
        this.lines = lines;
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
    public ArrayList<String> getLines() {
        return lines;
    }

    /**
     * SETTER for lines
     * @param list
     */
    public void setLines(ArrayList<String> list){
        lines = list;
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
        for (String line :
                lines) {
            pw.println(line);
        }
    }

    public static class Builder{

        private File file;
        private ArrayList<String> lines;

        public Builder() {
            this.lines = new ArrayList<>();
        }

        public void setFile(File file){
            this.file = file;
        }

        public void addLine(String line){
            lines.add(line);
        }

        public TextDocument build(){
            if(file == null){
                throw new InvalidParameterException("No file was specified");
            }
            return new TextDocument(file, lines);
        }
    }


}
