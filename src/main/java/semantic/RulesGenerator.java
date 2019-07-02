package semantic;

import common.config.Config;

import java.io.File;
import java.util.ArrayList;

public class RulesGenerator {

    private ArrayList<SemanticSentence> sentences;
    private FrameNetPatterns matcher;

    public RulesGenerator(ArrayList<SemanticSentence> sentences, FrameNetPatterns matcher) {
        this.sentences = sentences;
        this.matcher = matcher;
    }

    public ArrayList<Rule> generateRules(){
        ArrayList<Rule> rules = new ArrayList<>();

        for (SemanticSentence semanticSentence : sentences) {
            Rule rule = matcher.createRule(semanticSentence);
            if(rule != null){
                rules.add(rule);
            }
        }

        return rules;
    }

    public static void main(String [] args){
        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File("/home/hugo/AAAA/AAAAAA.conll"));
        FrameNetPatterns fnp = new FrameNetPatterns(new File("./files/SemanticAnalysis/FrameNetPatterns"));

        RulesGenerator rg = new RulesGenerator(sost.getSentences(), fnp);
        ArrayList<Rule> hop = rg.generateRules();
        for (Rule rule : hop) {
            System.out.println(rule.toString());
        }
    }


}
