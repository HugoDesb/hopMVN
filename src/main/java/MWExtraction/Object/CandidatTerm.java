/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MWExtraction.Object;

import java.util.ArrayList;

/**
 *
 * @author juanlossio
 */
public class CandidatTerm {
    private String term;
    private double importance;
    private int isTrueTerm;
    private String sourceDictionary;
    private ArrayList<String> sentences;

    /**
     *
     * @param term
     * @param importance
     */
    public CandidatTerm(String term, double importance) {
        setTerm(term);
        setImportance(importance);
        setSourceDictionary("");
        sentences = new ArrayList<>();
    }

    /**
     * Print a line for CSV representation of the term
     * Separator is \t
     * @return the CSV entry
     */
    public String toCSVLine(){
        String sentencesInLine = "";
        for (String s : sentences) {
            sentencesInLine += s+"\t";
        }
        return term +"\t"+isTrueTerm+"\t"+importance + "\t" + sentences.size() + "\t" + sentencesInLine;
    }

    /**
     * Adds a sentence to the object
     * The sentence should contain this term
     * @param s sentence
     */
    public void addSentence(String s){
        sentences.add(s);
    }

    /**
     * @return the term
     */
    
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the importance
     */
    public double getImportance() {
        return importance;
    }

    /**
     * @param importance the importance to set
     */
    public void setImportance(double importance) {
        this.importance = importance;
    }

    /**
     * @param isTrueTerm the isTrueTerm to set
     */
    public void setIsTrueTerm(boolean isTrueTerm) {
        this.setIsTrueTerm(isTrueTerm);
    }

    /**
     * @return the isTrueTerm
     */
    public int getIsTrueTerm() {
        return isTrueTerm;
    }

    /**
     * @param isTrueTerm the isTrueTerm to set
     */
    public void setIsTrueTerm(int isTrueTerm) {
        this.isTrueTerm = isTrueTerm;
    }

    /**
     * @return the sourceDictionary
     */
    public String getSourceDictionary() {
        return sourceDictionary;
    }

    /**
     * @param sourceDictionary the sourceDictionary to set
     */
    public void setSourceDictionary(String sourceDictionary) {
        this.sourceDictionary = sourceDictionary;
    }
    
}
