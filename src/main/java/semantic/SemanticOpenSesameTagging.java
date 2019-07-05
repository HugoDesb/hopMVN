package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SemanticOpenSesameTagging {

    private File file;

    private ArrayList<Sentence> sentences;

    public SemanticOpenSesameTagging(File file) {
        this.file = file;
        this.sentences = readCONLL();
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Read the conll file and extract its content
     * @return All sentences with their taggings
     */
    private ArrayList<Sentence> readCONLL(){
        List<List<String>> hop = MultiColumnCSVSort.readCsv(file.getAbsolutePath());

        ArrayList<Sentence> sentences = new ArrayList<>();

        Sentence sentence = new Sentence();

        List<List<String>> tmpOneSentenceOneFrame = new ArrayList<>();
        int currentSentenceNumber = 0;
        for (List<String> line : hop)
            if (line.get(0).isEmpty()) {
                if (Integer.parseInt(tmpOneSentenceOneFrame.get(0).get(6)) == currentSentenceNumber) {
                    sentence.addFrameIdentification(tmpOneSentenceOneFrame);
                    sentences.add(sentence);
                    sentence = new Sentence();
                    currentSentenceNumber++;
                }
                sentence.addFrameIdentification(tmpOneSentenceOneFrame);
                tmpOneSentenceOneFrame = new ArrayList<>();
            } else {
                currentSentenceNumber = Integer.parseInt(line.get(6));
                tmpOneSentenceOneFrame.add(line);
            }
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
