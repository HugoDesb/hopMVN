package semantic;

import java.util.*;

public class Rule {

    public static final String HUMAN_VALIDATION_FORMAT = "human_validation_format";
    public static final String DEV_FORMAT = "dev_format";
    public static final String DEV_PRETTY_FORMAT = "dev_pretty_format";


    private Sentence sentence;
    private Set<Word> premise;
    private Set<Word> conclusion;
    private ArrayList<FrameNetPattern> correspondingPatterns;
    private String topic;

    private Set<String> premisesString;
    private Set<String> conclusionsString;
    private ArrayList<String> mwe;

    private Map<String, List<Word>> patternsAndWords;

    Rule(Sentence sentence, String topic){
        this.sentence = sentence;
        this.premise = new HashSet<>();
        this.conclusion = new HashSet<>();
        this.correspondingPatterns = new ArrayList<>();
        this.topic = topic;
        this.mwe = new ArrayList<>();
        this.premisesString = new HashSet<>();
        this.conclusionsString = new HashSet<>();
        this.patternsAndWords = new HashMap<>();
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

    public void addMatchPattern(FrameNetPattern fnp, List<Word> words){
        patternsAndWords.put(fnp.toString(), words);
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
        if(premisesString.size()==0){
            premisesString = simplifyFrameNetTagList(this.premise);
        }
        return premisesString;
    }

    public Set<String> getConclusionsToStrings(){
        if(conclusionsString.size()==0){
            conclusionsString = simplifyFrameNetTagList(this.conclusion);
        }
        return conclusionsString;
    }

    private Set<String> simplifyFrameNetTagList(Set<Word> tokens){
        Set<String> out = new HashSet<>();
        ArrayList<Word> sorted = new ArrayList<>(tokens);
        Collections.sort(sorted, Word.indexComparator);
        StringBuilder builder = new StringBuilder();
        int last = -2;
        boolean first = true;
        for (int i = 0; i < sorted.size(); i++) {
            if(first){
                builder.append(sorted.get(i).getText());
                last = sorted.get(i).getIndex();
                first = false;
            }else{
                if(last == sorted.get(i).getIndex()-1){
                    builder.append(" "+sorted.get(i).getText());
                    last = sorted.get(i).getIndex();
                }else{
                    out.add(builder.toString());
                    builder = new StringBuilder();
                    builder.append(sorted.get(i).getText());
                    last = sorted.get(i).getIndex();
                }
            }
            /*
            if(last == sorted.get(i).getIndex()){
                if(builder.length() == 0){
                    builder.append(sorted.get(i).getText());
                }else{
                    builder.append(" "+sorted.get(i).getText());
                }

            }else{
                out.add(builder.toString());
                builder = new StringBuilder();
                last = sorted.get(i).getIndex();
            }
            */
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

    public ArrayList<String> getMwe() {
        return mwe;
    }

    private String preparePremiseToString(){
        Set<String> out = getPremisesToStrings();
        out.add(topic);
        return setToString(out);
    }

    private String prepareConclusionToString(){
        return setToString(getConclusionsToStrings());
    }

    public Sentence getSentence() {
        return sentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(sentence, rule.sentence) &&
                Objects.equals(premise, rule.premise) &&
                Objects.equals(conclusion, rule.conclusion) &&
                Objects.equals(correspondingPatterns, rule.correspondingPatterns) &&
                Objects.equals(topic, rule.topic) &&
                Objects.equals(mwe, rule.mwe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentence, premise, conclusion, correspondingPatterns, topic, mwe);
    }

    public String patternsAndWordsToString(){
        String ret = "\n";
        for (String fnp : patternsAndWords.keySet()) {
            Set<String> words = simplifyFrameNetTagList(new HashSet(patternsAndWords.get(fnp)));
            String strings = "";
            for (String s : words) {
                strings+= "["+s+"] ";
            }
            ret += "\t"+fnp+" :: "+strings+"\n";
        }
        return ret;
    }

    public String toStringOutput(){
        return "##############################################################################" +
                "\nSentence :" + sentence +
                "\nIF :" + preparePremiseToString() +
                "\nTHEN :" + prepareConclusionToString() +
                "\nCorrespondingPatterns :" + patternsAndWordsToString() +
                "\nMWE : "+ mwe +
                "\n";
    }

    @Override
    public String toString() {
        return "Rule{\n" +
                "\tSentence : "+sentence+
                "\n\tIF : " + preparePremiseToString() +
                "\n\t, THEN : " + prepareConclusionToString()+"}";
    }

    public String toValidationOutput() {
        String ret = sentence.toString()+"\n";
        ret += "IF\tPremise\tYes\tNo\tNeutral\tmHealth\n";
        for (String s : getPremisesToStrings()) {
            ret += "\t"+s+"\n";
        }
        ret += "THEN\tConclusion\tYes\tNo\tNeutral\tmHealth\n";
        for (String s : getConclusionsToStrings()) {
            ret += "\t"+s+"\n";
        }
        return ret;
    }

    public void computeStrings() {
        premisesString = new HashSet<>();
        premisesString = simplifyFrameNetTagList(this.premise);
        premisesString.add(topic);

        conclusionsString = new HashSet<>();
        conclusionsString = simplifyFrameNetTagList(this.conclusion);
    }
}
