package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
        List<List<String>> tmpOneSentenceOneFrame = new ArrayList<>();

        Iterator<List<String>> it = hop.iterator();
        List<String> line;
        int lastSentenceNumber=0, currentSentenceNumber=0;
        while(it.hasNext()){
             line = it.next();
             if(line.size() == 1){
                 if(lastSentenceNumber != currentSentenceNumber) {
                     sentences.add(sentence);
                     sentence = new Sentence();
                     lastSentenceNumber = currentSentenceNumber;
                 }
                 sentence.addFrameIdentification(tmpOneSentenceOneFrame);
                 tmpOneSentenceOneFrame = new ArrayList<>();
             }else{
                 currentSentenceNumber = Integer.parseInt(line.get(6));
                 tmpOneSentenceOneFrame.add(line);
             }
        }

        if(lastSentenceNumber != currentSentenceNumber) {
            sentences.add(sentence);
            sentence = new Sentence();
        }
        sentence.addFrameIdentification(tmpOneSentenceOneFrame);
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
