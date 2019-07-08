package semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Rule {

    private Sentence sentence;
    private Set<FrameNetTag> premise;
    private Set<FrameNetTag> conclusion;
    private ArrayList<FrameNetPattern> correspondingPatterns;
    private String topic;


    Rule(Set<FrameNetTag> premise, Set<FrameNetTag> conclusion, ArrayList<FrameNetPattern> correspondingPattern, Sentence sentence) {
        this.sentence = sentence;
        this.premise = premise;
        this.conclusion = conclusion;
        this.correspondingPatterns = correspondingPattern;
    }

    Rule(Sentence sentence, String topic){
        this.sentence = sentence;
        this.premise = new HashSet<>();
        this.conclusion = new HashSet<>();
        this.correspondingPatterns = new ArrayList<>();
        this.topic = topic;
    }

    public void addMatch(FrameNetTag premise, FrameNetTag conclusion, FrameNetPattern fnp){
        this.premise.add(premise);
        this.conclusion.add(conclusion);
        correspondingPatterns.add(fnp);
    }

    public void addMatch(Set<FrameNetTag> premise, Set<FrameNetTag> conclusion, FrameNetPattern fnp){
        this.premise.addAll(premise);
        this.conclusion.addAll(conclusion);
        correspondingPatterns.add(fnp);
    }

    public boolean isEmpty(){
        return correspondingPatterns.size() == 0;
    }


    public Set<FrameNetTag> getPremise() {
        return premise;
    }

    public Set<FrameNetTag> getConclusion() {
        return conclusion;
    }

    public ArrayList<FrameNetPattern> getCorrespondingPatterns() {
        return correspondingPatterns;
    }

    private Set<String> simplifyFrameNetTagList(Set<FrameNetTag> tokens){
        Set<String> out = new HashSet<>();
        ArrayList<FrameNetTag> sorted = new ArrayList<>(tokens);
        Collections.sort(sorted, FrameNetTag.indexComparator);
        StringBuilder builder = new StringBuilder();
        int last = -2;
        for (int i = 0; i < sorted.size(); i++) {
            if(last == -2){
                last = sorted.get(i).getIndex()-1;
            }
            if(last == sorted.get(i).getIndex()-1){
                if(builder.length() == 0){
                    builder.append(sorted.get(i).getWord());
                }else{
                    builder.append(" "+sorted.get(i).getWord());
                }
                last++;
            }else{
                out.add(builder.toString());
                builder = new StringBuilder();
                last = sorted.get(i).getIndex()-1;
            }
        }
        if(builder.length() != 0){
            out.add(builder.toString());
        }
        return out;
    }

    private String setToString(Set<String> set){
        StringBuilder sb = new StringBuilder();
        if(set.size() != 0){
            for (String string: set) {
                if(string.length()!=0){
                    //System.out.println(set);
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
        Set<String> out = simplifyFrameNetTagList(this.premise);
        out.add(topic);
        return setToString(out);
    }

    private String prepareConclusionToString(){
        return setToString(simplifyFrameNetTagList(this.conclusion));
    }

    public String toStringOutput(){
        return "##############################################################################" +
                "\nSentence :" + sentence +
                "\nIF :" + preparePremiseToString() +
                "\nTHEN :" + prepareConclusionToString() +
                "\nCorrespondingPatterns :" + correspondingPatterns;
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
}
