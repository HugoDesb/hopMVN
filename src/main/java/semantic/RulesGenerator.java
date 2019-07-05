package semantic;

import common.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns patterns;

    private RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns patterns) {
        this.sentences = sentences;
        this.patterns = patterns;
    }

    private ArrayList<Rule> generateRules(){
        ArrayList<Rule> rules = new ArrayList<>();
        for(int i = 0; i<sentences.getSentences().size(); i++){
            Rule r = new Rule(sentences.getSentences().get(i));
            for (FrameNetPattern pattern : patterns.getFrameNetPatterns()) {
                Pair<Set<FrameNetTag>, Set<FrameNetTag>> hop = sentences.getSentences().get(i).matches(pattern);

                if(!hop.getValue2().isEmpty()){
                    r.addMatch(hop.getValue1() , hop.getValue2(), pattern);
                }
            }
            if(!r.isEmpty()) {
                rules.add(r);
            }
        }
        return rules;
    }

    public static void main(String [] args){
        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File(
                "/home/sesstim/Telechargements/open-sesame/open-sesame/logs/myargid/predicted-args.conll"
        ));
        FrameNetPatterns fnp = new FrameNetPatterns(new File("./files/SemanticAnalysis/framenetpatternV2"));

        RulesGenerator rg = new RulesGenerator(sost, fnp);

        ArrayList<Rule> hop = rg.generateRules();
        for (Rule rule : hop) {
            System.out.println(rule.toStringBeautifulOutput());
        }
    }


}
