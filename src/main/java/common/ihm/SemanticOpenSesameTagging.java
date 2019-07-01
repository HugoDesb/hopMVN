package common.ihm;

import MWExtraction.Wrapper.MultiColumnCSVSort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SemanticOpenSesameTagging {

    private File file;

    private ArrayList<SemanticSentence> sentences;

    public SemanticOpenSesameTagging(File file) {
        this.file = file;
        this.sentences = readCSV();
    }

    public ArrayList<SemanticSentence> getSentences() {
        return sentences;
    }

    public ArrayList<SemanticSentence> readCSV(){
        List<List<String>> hop = MultiColumnCSVSort.readCsv(file.getAbsolutePath());

        ArrayList<SemanticSentence> sentences = new ArrayList<>();

        SemanticSentence semanticSentence = new SemanticSentence();
        List<List<String>> tmpOneSentenceOneFrame = new ArrayList<>();
        for (List<String> line : hop) {

            if(line.get(0).isEmpty()){
                //new Block
                FrameSentence fs = new FrameSentence(tmpOneSentenceOneFrame);
                if(!semanticSentence.isSameSentence(fs)){
                    sentences.add(semanticSentence);
                    semanticSentence = new SemanticSentence();
                }
                semanticSentence.addFrameIdentification(fs);
                tmpOneSentenceOneFrame = new ArrayList<>();
            }else{
                tmpOneSentenceOneFrame.add(line);
            }
        }
        sentences.add(semanticSentence);

        return sentences;
    }

    public ArrayList<ArrayList<Chunk>> getChunksForSentence(int index ){
        if(index<0 || index>=sentences.size()){
            throw new IndexOutOfBoundsException(""+index);
        }

        ArrayList<ArrayList<Chunk>> hop = new ArrayList<>();

        for (FrameSentence frameSentence : sentences.get(index).getFrames()) {
            hop.add(frameSentence.)
        }
        return hop;
    }




}
