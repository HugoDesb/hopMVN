package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SemanticOpenSesameTagging {

    private File file;

    private ArrayList<Sentence> sentences;

    public SemanticOpenSesameTagging(File file) {
        this.file = file;
        try {
            this.sentences = readCONLL();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Read the conll file and extract its content
     * @return All sentences with their taggings
     */
    private ArrayList<Sentence> readCONLL() throws IOException {
        List<List<String>> hop = MultiColumnCSVSort.readCsv(file.getAbsolutePath());

        ArrayList<Sentence> sentences = new ArrayList<>();

        Sentence sentence = new Sentence();
        PrintStream ps = new PrintStream(new File("./files/tmp.txt"));
        //PrintWriter pw = new PrintWriter(new File("./files/tmp.txt"));
        //pw.println("hop");
        List<List<String>> tmpOneSentenceOneFrame = new ArrayList<>();
        for (List<String> line : hop) {
            //System.out.println(line.size());
            if (line.isEmpty() || line.get(0).isEmpty()) {
                //System.out.println(tmpOneSentenceOneFrame);

                int thisEndedSentenceNumber = Integer.parseInt(tmpOneSentenceOneFrame.get(0).get(6));

                if (sentences.size() != thisEndedSentenceNumber-1) {
                    //this sentence is not the same (next)
                    sentences.add(sentence);
                    sentence = new Sentence();
                }
                sentence.addFrameIdentification(tmpOneSentenceOneFrame);
                tmpOneSentenceOneFrame = new ArrayList<>();
            } else {
                //System.out.println(line);
                ps.println(line);
                //System.out.println(line);
                tmpOneSentenceOneFrame.add(line);
            }
        }
        //pw.close();
        //sentence.addFrameIdentification(tmpOneSentenceOneFrame);
        sentences.add(sentence);

        return sentences;
    }

    /**
     * Gets the Chunks for each frames of the sentence
     * @param index sentence number
     * @return list of list of Chunk
     */
    public ArrayList<ArrayList<Chunk>> getChunksForSentence(int index){
        if(index<0 || index>=sentences.size()){
            throw new IndexOutOfBoundsException(""+index);
        }

        ArrayList<ArrayList<Chunk>> hop = new ArrayList<>();

        for (Frame frame : sentences.get(index).getFrames()) {
            //hop.add(frame.getChunks());
        }
        return hop;
    }




}
