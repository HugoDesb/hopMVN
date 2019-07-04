package semantic;

import common.Pair;

import java.io.File;
import java.util.ArrayList;

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
                Pair<ArrayList<FrameNetTag>, ArrayList<FrameNetTag>> hop = sentences.getSentences().get(i).matches(pattern);
                if(!hop.getValue1().isEmpty() && !hop.getValue2().isEmpty()){
                    r.addMatch(hop.getValue1() , hop.getValue1(), pattern);
                }
            }
        }
        return rules;

        /**
        for (int i = 0; i < sentences.getSentences().size();i++) {
            ArrayList<ArrayList<Chunk>> hop = sentences.getChunksForSentence(i);
            //list frames and their roles
            for (ArrayList<Chunk> frameChunks: hop) {
                for (Chunk chunk: frameChunks) {
                    FrameNetPattern fnp = patterns.matches(chunk);
                    if(fnp!=null){
                        rules.add(patterns.createRule(chunk, fnp));
                    }
                }
            }
        }
        return rules;
        */
    }

    public static void main(String [] args){
        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File("/home/sesstim/Telechargements/open-sesame/open-sesame/logs/myargid/predicted-args.conll"));
        FrameNetPatterns fnp = new FrameNetPatterns(new File("./files/SemanticAnalysis/FrameNetPatterns"));

        RulesGenerator rg = new RulesGenerator(sost, fnp);

        ArrayList<Rule> hop = rg.generateRules();
        for (Rule rule : hop) {
            System.out.println(rule.toString());
        }
    }


}
