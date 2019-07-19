package semantic;

import java.util.*;

public class Rule {

    private Sentence sentence;
    private Set<Word> premise;
    private Set<Word> conclusion;
    private ArrayList<FrameNetPattern> correspondingPatterns;
    private String topic;

    private Set<String> premisesString;
    private Set<String> conclusionsString;
    private ArrayList<String> mwe;

    Rule(Sentence sentence, String topic){
        this.sentence = sentence;
        this.premise = new HashSet<>();
        this.conclusion = new HashSet<>();
        this.correspondingPatterns = new ArrayList<>();
        this.topic = topic;
        this.mwe = new ArrayList<>();
    }

    public void addMWE(String m){
        mwe.add(m);
    }

    public void addMatchPremise(List<Word> words){
        premise.addAll(words);
    }
    public void addMatchConclusion(List<Word> words){
        conclusion.addAll(words);
    }
    public void addMatchPattern(FrameNetPattern fnp){
        correspondingPatterns.add(fnp);
    }

    public boolean isEmpty(){
        return correspondingPatterns.size() == 0;
    }


    public Set<Word> getPremise() {
        return premise;
    }

    public Set<Word> getConclusion() {
        return conclusion;
    }

    public ArrayList<FrameNetPattern> getCorrespondingPatterns() {
        return correspondingPatterns;
    }

    public Set<String> getPremisesToStrings(){
        if(premisesString == null){
            premisesString = simplifyFrameNetTagList(this.premise);
        }
        return premisesString;
    }

    public Set<String> getConclusionsToStrings(){
        if(conclusionsString == null){
            conclusionsString = simplifyFrameNetTagList(this.conclusion);
        }
        return simplifyFrameNetTagList(this.conclusion);
    }

    private Set<String> simplifyFrameNetTagList(Set<Word> tokens){
        Set<String> out = new HashSet<>();
        ArrayList<Word> sorted = new ArrayList<>(tokens);
        Collections.sort(sorted, Word.indexComparator);
        StringBuilder builder = new StringBuilder();
        int last = -2;
        for (int i = 0; i < sorted.size(); i++) {
            if(last == -2){
                last = sorted.get(i).getIndex()-1;
            }
            if(last == sorted.get(i).getIndex()-1){
                if(builder.length() == 0){
                    builder.append(sorted.get(i).getText());
                }else{
                    builder.append(" "+sorted.get(i).getText());
                }
                last++;
            }else{
                out.add(builder.toString());
                builder = new StringBuilder();
                last = sorted.get(i).getIndex();
            }
        }
        if(builder.length() != 0){
            out.add(builder.toString());
        }
        return out;
    }

    public void setPremisesString(Set<String> premisesString) {
        this.premisesString = premisesString;
    }

    public void setConclusionsString(Set<String> conclusionsString) {
        this.conclusionsString = conclusionsString;
    }

    private String setToString(Set<String> set){
        StringBuilder sb = new StringBuilder();
        if(set.size() != 0){
            for (String string: set) {
                if(string.length()!=0){

                    if(sb.length() == 0){
                        sb.append("['" + string);
                    }else{
                        sb.append("', '"+string);
                    }
                }
            }
            sb.append("']");
            return sb.toString();
        }else{
            return "[]";
        }
    }

    private String preparePremiseToString(){
        Set<String> out = getPremisesToStrings();
        out.add(topic);
        return setToString(out);
    }

    private String prepareConclusionToString(){
        return setToString(getConclusionsToStrings());
    }

    public String toStringOutput(){
        return "##############################################################################" +
                "\nSentence :" + sentence +
                "\nIF :" + preparePremiseToString() +
                "\nTHEN :" + prepareConclusionToString() +
                "\nCorrespondingPatterns :" + correspondingPatterns +
                "\nMWE : "+ mwe;

    }

    @Override
    public String toString() {
        return "Rule{\n" +
                "\tSentence : "+sentence+
                "\n\tIF : " + premise +
                "\n\t, THEN : " + conclusion +
                "\n\t, correspondingPatterns= '" + correspondingPatterns + '\'' +
                "\n}";
    }

    public Sentence getSentence() {
        return sentence;
    }
}
