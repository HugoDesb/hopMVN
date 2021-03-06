package semantic;

import tagging.RNNTagger.RNNTag;

import java.util.*;

//ALL FRAMES, ONE SENTENCE
public class Sentence {

    //private String sentence;
    private int sentenceNumber;
    private ArrayList<Frame> frames; //same for all frames
    private ArrayList<Word> words;

    public Sentence(int sentenceNumber, ArrayList<Frame> frames, ArrayList<Word> words) {
        this.sentenceNumber = sentenceNumber;
        this.frames = frames;
        this.words = words;
    }

    public void correctWords(ArrayList<RNNTag> tokens){
        int j = 0;
        int i = 0;
        String tmp = "";
        while(i<words.size() && j<tokens.size()){
            if(words.get(i).getText().equals("unk")){
                tmp += tokens.get(j).getWord();

                boolean search = true;
                while(search){
                    if(i == words.size()-1){
                        //unk dernier token de open sesame
                        j++;
                        while(j<tokens.size()){
                            tmp+= " "+tokens.get(j).getWord();
                        }
                        words.get(i).setText(tmp);
                        words.get(i).setLemma(tokens.get(j-1).getLemma());
                        search = false;
                        tmp = "";
                        //si dernier de tokens : set normal
                        // sinon ajouter tout le reste de tokens
                    }else if (j == tokens.size()-1 ){
                        System.out.println("hop");
                        // Cas particulier
                        // dernier mot
                        //set normal as usual
                        //search = false
                    }else{
                        if(words.get(i+1).getText().equals(tokens.get(j+1).getWord().toLowerCase())){
                            words.get(i).setText(tmp);
                            words.get(i).setLemma(tokens.get(j).getLemma());
                            search = false;
                            tmp = "";
                        }else{
                            if(words.get(i+1).getText().equals("unk")){
                                words.get(i).setText(tmp);
                                words.get(i).setLemma(tokens.get(j).getLemma());
                                search = false;
                                tmp = "";
                            }else{
                                tmp += " "+tokens.get(j+1).getWord();
                                j++;
                            }
                        }
                    }
                }
            }else{
                words.get(i).setText(tokens.get(j).getWord());
                words.get(i).setLemma(tokens.get(j).getLemma());
            }
            j++;
            i++;
        }

    }

    /**
     *
     * @return
     */
    private Map<Word, Set<String>> deconstructIntoTokens(){
        Map<Word, Set<String>> framesPerToken = new LinkedHashMap<>();
        for (Word word: words) {
            Set<String> tmp = new HashSet<>();
            for (Frame frame : frames) {
                if(frame.getRange().contains(word.getIndex())){
                    for (FrameElement fe : frame.getFrameElements()) {
                        if(fe.getRange().contains(word.getIndex())){
                            tmp.add(frame.getFrameName()+"["+fe.getName()+"]");
                        }
                    }
                }
            }
            framesPerToken.put(word, tmp);
        }
        return framesPerToken;
    }

    /**
     * Returns all indexes for words concerned by the specified pattern
     * @param fnp the pattern
     * @return an ArrayList of concerned words index's
     */
    public ArrayList<Integer> matches(Pattern fnp){
        ArrayList<Integer> ret = new ArrayList<>();
        for (Frame frame: frames) {
            if(frame.getFrameName().equals(fnp.getFrame())){
                for (String frameElement : fnp.getFrameElements()) {
                    ret.addAll(frame.getFrameElementIndexes(frameElement));
                }
            }
        }
        return ret;
    }

    /**
     * Gets the full sentence
     * @return the sentence
     */
    public String getSentence(){
        StringBuilder sentence = new StringBuilder();
        for (Word word: words) {
            sentence.append(word.getText()).append(" ");
        }
        return sentence.toString().trim();
    }

    @Override
    public String toString(){
        return getSentence();
    }

    /**
     *
     * @return
     */
    ArrayList<Frame> getFrames() {
        return frames;
    }

    public Word getWord(Integer index){
        for (Word word :words) {
            if(word.getIndex()==index){
                return word;
            }
        }
        return null;
    }

    public List<Word> getSentence(ArrayList<Integer> matched) {
        List<Word> words = new ArrayList<>();
        Collections.sort(matched);
        for (Integer i : matched) {
            words.add(getWord(i));
        }
        return words;
    }

    public Integer getTargetMatch(Pattern pattern) {
        for (Frame frame: frames) {
            if(frame.getFrameName().equals(pattern.getFrame())){
                return frame.getTargetIndex();
            }
        }
        return -1;
    }

    public int getSentenceNumber() {
        return sentenceNumber;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public static class Builder{
        private int sentenceNumber;
        private ArrayList<Frame> frames; //same for all frames
        private ArrayList<Word> words;

        public Builder() {
            frames = new ArrayList<>();
            words = new ArrayList<>();
        }

        /**
         * Adds Frame
         * @param lines
         */
        void addFrameIdentification(List<List<String>> lines){
            ArrayList<FrameNetTag> tokens = new ArrayList<>();

            ArrayList<FrameElement.Builder> fes = new ArrayList<>();
            Frame.Builder fr = new Frame.Builder();

            for (List<String> line :lines) {
                int index = Integer.parseInt(line.get(0));
                if(words.size() != lines.size()){
                    words.add(new Word(line));
                    sentenceNumber = Integer.parseInt(line.get(6));
                }

                if(!line.get(13).equals("_")){
                    fr.setIndexTarget(index);
                    fr.setName(line.get(13));
                }
                if(!line.get(14).equals("O")){
                    fr.addFrameElement(line.get(14).split("-")[1], index);
                }
            }
            frames.add(fr.build());
        }

        public Sentence build(){
            return new Sentence(sentenceNumber, frames, words);
        }

    }
}
