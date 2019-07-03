package semantic;

import java.io.File;
import java.util.ArrayList;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns matcher;

    private RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns matcher) {
        this.sentences = sentences;
        this.matcher = matcher;
    }

    private ArrayList<Rule> generateRules(){
        ArrayList<Rule> rules = new ArrayList<>();


        for (int i = 0; i < sentences.getSentences().size();i++) {
            ArrayList<ArrayList<Chunk>> hop = sentences.getChunksForSentence(i);

            //list frames and their roles

            for (ArrayList<Chunk> frameChunks: hop) {
                for (Chunk chunk: frameChunks) {
                    FrameNetPattern fnp = matcher.matches(chunk);
                    if(fnp!=null){
                        rules.add(matcher.createRule(chunk, fnp));
                    }
                }
            }

        }
        return rules;
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
