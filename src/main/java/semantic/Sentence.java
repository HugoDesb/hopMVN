package semantic;

import common.Pair;

import java.util.*;

//ALL FRAMES, ONE SENTENCE
public class Sentence {

    //private String sentence;
    private int sentenceNumber;
    private ArrayList<Frame> frames; //same for all frames
    private ArrayList<FrameNetTag> tokens;
    private Map<FrameNetTag, Set<String>> deconstructedFramesAndRolesPerToken;

    Sentence() {
        this.tokens = new ArrayList<>();
        frames = new ArrayList<>();
        deconstructedFramesAndRolesPerToken = deconstructIntoTokens();
    }

    /**
     *
     * @return
     */
    private Map<FrameNetTag, Set<String>> deconstructIntoTokens(){
        Map<FrameNetTag, Set<String>> framesPerToken = new HashMap<>();
        for (FrameNetTag token: tokens) {
            Set<String> tmp = new HashSet<>();
            for (Frame frame : frames) {
                if(frame.getRange().contains(token.getIndex())){
                    for (FrameElement fe : frame.getFrameElements()) {
                        if(fe.getRange().contains(token.getIndex())){
                            tmp.add(frame.getFrameName()+"["+fe.getName()+"]");
                        }
                    }
                }
            }
            framesPerToken.put(token, tmp);
        }
        return framesPerToken;
    }

    /**
     *
     * @param fnp
     * @return
     */
    public Pair<ArrayList<FrameNetTag>, ArrayList<FrameNetTag>> matches(FrameNetPattern fnp){
        ArrayList<FrameNetTag> premises = new ArrayList<>();
        ArrayList<FrameNetTag> conslusion = new ArrayList<>();
        boolean matchesPremise = true, matchesConclusion = true;

        for (FrameNetTag token: deconstructedFramesAndRolesPerToken.keySet()) {
            int objective = fnp.getPremises().size();
            matchesPremise = tokenMatches(fnp, token, objective);


            objective = fnp.getConslusions().size();
            matchesConclusion = tokenMatches(fnp, token, objective);

            if(matchesPremise && matchesConclusion){
                premises.add(token);
                conslusion.add(token);
            }
        }

        return new Pair<>(premises, conslusion);
    }

    /**
     *
     * @param fnp
     * @param token
     * @param objective
     * @return
     */
    private boolean tokenMatches(FrameNetPattern fnp, FrameNetTag token, int objective) {
        Set<String> frameAndRole = deconstructedFramesAndRolesPerToken.get(token);
        int current = 0;
        for (String pattern : fnp.getPremises()) {
            if(frameAndRole.contains(pattern)){
                current++;
            }
        }
        return objective == current;
    }

    /**
     *
     * @param fs
     * @param sentenceNumber
     */
    void addFrameIdentification(Frame fs, int sentenceNumber){
        frames.add(fs);
        this.sentenceNumber = sentenceNumber;
    }

    /**
     *
     * @param lines
     */
    void addFrameIdentification(List<List<String>> lines){
        for (List<String> line :lines) {
            tokens.add(new FrameNetTag(line));
        }
        Frame m = findFrame(tokens);

        frames.add(findFrameElements(tokens, m));
    }

    /**
     *
     * @param tokens
     * @return
     */
    private Frame findFrame(ArrayList<FrameNetTag> tokens) {
        for (FrameNetTag token: tokens) {
            if(!token.getFrame().equals("_")){
                return new Frame(token);
            }
        }
        return null;
    }

    /**
     *
     * @param tokens
     * @param frame
     * @return
     */
    private Frame findFrameElements(ArrayList<FrameNetTag> tokens, Frame frame) {
        ArrayList<FrameElement> frameElements = new ArrayList<>();
        for (FrameNetTag token: tokens) {
            if(!token.getFrameElement().equals("O")){
                frame.addToFrame(token);
            }
        }
        return frame;
    }

    /**
     * Gets the full sentence
     * @return the sentence
     */
    public String getSentence(){
        StringBuilder sentence = new StringBuilder();
        for (FrameNetTag tags : tokens) {
            sentence.append(tags.getWord()).append(" ");
        }
        return sentence.toString().trim();
    }

    /**
     *
     * @return
     */
    public int getSentenceNumber(){
        return tokens.get(0).getSentenceNumber();
    }

    /**
     *
     * @return
     */
    ArrayList<Frame> getFrames() {
        return frames;
    }
}
