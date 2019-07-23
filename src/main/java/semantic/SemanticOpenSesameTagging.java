package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;
import tagging.RNNTagger.RNNTag;
import tagging.RNNTagger.RNNTagger;

import java.io.*;
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

    public void correctSentences(String originalTextFile){
        File f = new File(originalTextFile);

        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = bf.readLine();
            while(line!= null){
                lines.add(line);
                line = bf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Sentence s : sentences) {
            if(s.getSentenceNumber() < lines.size()){

                RNNTagger tagger = new RNNTagger();
                ArrayList<RNNTag> hop = tagger.tag(new common.document.Sentence(lines.get(s.getSentenceNumber())));

                String sentence = lines.get(s.getSentenceNumber());
                //sentence = sentence.replaceAll("#", " ");
                sentence = sentence.replaceAll("'", " ");
                sentence = sentence.replaceAll("[^a-zA-Z0-9\\s-]","");
                sentence = sentence.replaceAll("\\s+", " ");
                sentence = sentence.trim();
                String ret = "";
                for (Word w: s.getWords()) {
                    ret += " "+w.getText();
                }

                //ret = ret.replaceAll("#", " ");
                ret = ret.replaceAll("'", " ");
                ret = ret.replaceAll("[^a-zA-Z0-9\\s-]", "");
                ret = ret.replaceAll("\\s+", " ");
                ret = ret.trim();

                String [] sentenceFromOS = sentence.toLowerCase().split("\\s");
                String [] sentenceOriginal = ret.toLowerCase().split("\\s");


                if(sentenceFromOS.length == sentenceOriginal.length){
                    int i=0, j=0;
                    while(i<sentenceFromOS.length && j<sentenceOriginal.length){
                        if(sentenceFromOS[i].equals("unk")){
                            String replacement = sentenceOriginal[j];
                            ArrayList<Word> wordsToReplace = new ArrayList<>();
                            for (Word w: s.getWords()) {
                                if()
                            }
                        }
                    }
                }


                System.out.println(sentence.split("\\s").length);
                System.out.println(sentence.trim());
                System.out.println(ret.split("\\s").length);
                System.out.println(ret.trim());



                /*
                sentence = sentence.replaceAll("'", " ");
                sentence = sentence.replaceAll("\"", " ");
                sentence = sentence.replaceAll(":", " ");
                sentence = sentence.replaceAll("\\.", " ");
                sentence = sentence.replaceAll("\\?", " ");
                sentence = sentence.replaceAll("!", " ");
                sentence = sentence.replaceAll(",", " ");
                sentence = sentence.replaceAll(";", " ");
                sentence = sentence.replaceAll("", " ");
                sentence = sentence.replaceAll("?", " ");
                sentence = sentence.replaceAll("?", " ");
                sentence = sentence.replaceAll("?", " ");
                */

                sentence = sentence.trim().replaceAll("\\s+", " ");

                ArrayList<RNNTag> cleaned = new ArrayList<>();
                s.correctWords(cleaned);
            }
        }

    }

    /**
     * Read the conll file and extract its content
     * @return All sentences with their taggings
     */
    private ArrayList<Sentence> readCONLL() throws IOException {
        List<List<String>> hop = MultiColumnCSVSort.readCsv(file.getAbsolutePath());

        ArrayList<Sentence> sentences = new ArrayList<>();

        Sentence.Builder sentence = new Sentence.Builder();
        List<List<String>> tmpOneSentenceOneFrame = new ArrayList<>();

        Iterator<List<String>> it = hop.iterator();
        List<String> line;
        int lastSentenceNumber=0, currentSentenceNumber=0;
        while(it.hasNext()){
             line = it.next();
             if(line.size() == 1){
                 if(!it.hasNext()){
                     break;
                 }
                 if(lastSentenceNumber != currentSentenceNumber) {
                     sentences.add(sentence.build());
                     sentence = new Sentence.Builder();
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
            sentences.add(sentence.build());
            sentence = new Sentence.Builder();
        }
        sentence.addFrameIdentification(tmpOneSentenceOneFrame);
        sentences.add(sentence.build());

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
