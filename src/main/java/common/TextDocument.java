package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a .txt document
 */
public class TextDocument {

    private File file;
    private List<String> lines;


    public TextDocument(File file, List<String> lines) {
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
    public List<String> getLines() {
        return lines;
    }


    /**
     *
     * @param line
     * @return
     */
    public boolean removeLine(String line){
        return lines.remove(line);
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


    public static class Builder{

        private File file;
        private List<String> lines;

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
