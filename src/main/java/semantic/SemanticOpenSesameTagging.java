package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;

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
            if(s.getSentenceNumber() < lines.size() && s.toString().contains("unk ")){

                //RNNTagger tagger = new RNNTagger();
                //ArrayList<RNNTag> hop = tagger.tag(new common.document.Sentence(lines.get(s.getSentenceNumber())));

                String cleanSentence = lines.get(s.getSentenceNumber());
                cleanSentence = clean(cleanSentence);

                String unkSentence = "";
                for (Word w: s.getWords()) {
                    unkSentence += " "+w.getText();
                }
                unkSentence = clean(unkSentence);

                String [] sentenceFromOS = unkSentence.toLowerCase().split("\\s");
                String [] sentenceOriginal = cleanSentence.toLowerCase().split("\\s");


                if(sentenceFromOS.length == sentenceOriginal.length){

                    //Store temporarily words to replace in the order of apparition
                    ArrayList<String> replacements = new ArrayList<>();
                    for (int i = 0; i<sentenceFromOS.length; i++) {
                        if(sentenceFromOS[i].equals("unk")){
                            replacements.add(sentenceOriginal[i]);
                        }
                    }

                    //Replace by order of apparition
                    ArrayList<Word> newWords = new ArrayList<>();
                    int i = 0;
                    for (Word w : s.getWords()) {
                        if(w.getText().toLowerCase().equals("unk")){
                            w.setText(replacements.get(i));
                            i++;
                        }
                        newWords.add(w);
                    }
                    //set new Word list
                    s.setWords(newWords);
                }else{
                    System.out.println(lines.get(s.getSentenceNumber()));
                    System.out.println(cleanSentence.split("\\s").length);
                    System.out.println(cleanSentence.trim());
                    System.out.println(unkSentence.split("\\s").length);
                    System.out.println(unkSentence.trim());
                }




                cleanSentence = cleanSentence.trim().replaceAll("\\s+", " ");

                //ArrayList<RNNTag> cleaned = new ArrayList<>();
                //s.correctWords(cleaned);
            }else{

            }
        }

    }

    private String clean(String s){
        s = s.replaceAll("'", " ");
        s = s.replaceAll("#", " ");
        s = s.replaceAll("%", " % ");
        s = s.replaceAll("\\(|\\)"," ");
        s = s.replaceAll("\\[|\\]"," ");
        //s = s.replaceAll("([0-9a-zA-Z])\\s*–\\s([0-9a-zA-Z])", "$0 – $1");
        s = s.replaceAll("[^a-zA-Z0-9\\s-%–]","");
        s = s.replaceAll("\\s+", " ");
        s = s.trim();
        return s;
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
}
