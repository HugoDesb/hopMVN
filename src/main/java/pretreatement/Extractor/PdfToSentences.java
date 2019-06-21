package pretreatement.Extractor;

import common.config.Config;
import common.document.Sentence;
import common.document.TextDocument;
import pretreatement.Filter.Filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfToSentences {

    /**
     * Extract sentences from source file to default target txt file (same name, same folder, with .txt extension)
     *
     * @param config_file config
     * @param source path to the source file
     * @param name the name of the file
     * @param type type
     * @param isExpertFile if the goal is to extract highlighted text
     * @return the TextDocument containing the sentences
     */
    public static TextDocument extract(String config_file, String source, String name, String type, boolean isExpertFile) throws IOException {
        Config config = Config.getInstance("config_file");
        return extract(source, getDefaultTargetFile(config.getProp("pretreatment.output_folder"), source), isExpertFile);
    }

    /**
     * Extract sentences from source file to the target txt file
     * @param source path to the source file
     * @param target path to the target file
     * @param isExpertFile if the point is to extract highlighted text
     * @return the TextDocument containing the sentences
     */
    public static TextDocument extract(String source, Path target, boolean isExpertFile) throws IOException {
        TextDocument ret;
        Path sourcePath = Paths.get(source);
        File sourceFile = sourcePath.toFile();
        if(!sourceFile.isFile()){
            throw new IllegalArgumentException("The source path isn't a file");
        }
        ArrayList<String> blocks = ExtractorPDF.extract(sourceFile, isExpertFile);
        ArrayList<Sentence> sentences = textToSentences(blocks);
        ret = new TextDocument(target.toFile(), sentences);

        if(!isExpertFile){
            ret.setLines((new Filter()).filter(ret.getLines()));
        }
        return ret;
    }

    /**
     * Transform text to sentences, then select it or not.
     * @param lines detected lines from the pdf file
     * @return all selected sentences
     */
    private static ArrayList<Sentence> textToSentences(ArrayList<String> lines) {
        ArrayList<Sentence> sentencesToReturn = new ArrayList<>();
        Iterator<String> it = lines.iterator();
        String block;
        StringBuilder toAdd = new StringBuilder();
        String [] textLines;


        int i = 0;
        //pour chaque bloc de texte
        while(it.hasNext()){
            block = it.next();
            //System.out.println("############");
            //System.out.println(block);
            //System.out.println("############");
            textLines = block.split("\n");
            //pour toutes les lignes du bloc

            for(String line : textLines){
                if(line.trim().matches("AE\\s.*")){
                    line = line.substring(3).trim();
                }else if(!line.trim().matches(".*\\.\\.\\.\\..*")){
                    String [] sentences = line.split("\\.\\s");
                    //pour toutes les "phrases"
                    for (String sentence: sentences) {
                        //System.out.println("Line : "+sentence);
                        //ligne non vide
                        if(!sentence.trim().equals("")){
                            // First character is a UPPERCASE
                            //TODO : [OPT] add empty or meaningless characters first ? (dots, spaces, ...)
                            if(sentence.matches("^[ABCDEFGHIJKLMNOPQRSTUVWXYZÉÈÊÔŒÎÏËÇÆÂÀÙŸ].*")){
                                // We can consider it's a new sentence
                                // add previous line
                                //System.out.println("Sentence : "+toAdd+".");
                                sentencesToReturn.add(new Sentence(toAdd.toString().trim()+"."));
                                //init new line
                                toAdd = new StringBuilder(sentence);
                            } else {
                                // first character is NOT an UPPERCASE
                                // continue new line
                                toAdd.append(sentence);
                            }
                        }
                    }
                }
            }
            sentencesToReturn.add(new Sentence(toAdd+"."));
        }
        return sentencesToReturn;
    }

    /**
     * Get the default target file path (same file, with .txt extension)
     *
     * @param tmpfolder
     * @param source path to the source file
     * @return returns the path
     */
    private static Path getDefaultTargetFile(String tmpfolder, String source){
        Path sourcePath = Paths.get(source);
        if(!sourcePath.toFile().isFile()){
            throw new IllegalArgumentException("The source path isn't a file");
        }

        String filenameWithoutExtension = sourcePath.getFileName().toString().split("\\.")[0];
        String basefolder = tmpfolder+"/"+filenameWithoutExtension+"/";
        (new File(basefolder)).mkdirs();
        return Paths.get(basefolder+filenameWithoutExtension+".txt");
    }
}
