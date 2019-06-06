package Format;

import java.util.ArrayList;
import Object.CandidatTerm;

public class Formatter {

    /**
     * Adds to the candidate terms, all sentences containing them
     * @param list_candidat_terms_validated terms validated
     * @param all_phrases all sentences, cleaned (cf Cleaning class)
     * @return
     */
    public static ArrayList<CandidatTerm> extend(ArrayList<CandidatTerm> list_candidat_terms_validated, ArrayList<String> all_phrases) {
        ArrayList<CandidatTerm> out = new ArrayList<CandidatTerm>();
        for (CandidatTerm candidatTerm : list_candidat_terms_validated) {
            for (String sentence : all_phrases) {
                if(sentence.trim().toLowerCase().contains(candidatTerm.getTerm().trim().toLowerCase())){
                    candidatTerm.addSentence(sentence);
                }
            }
            out.add(candidatTerm);
        }
        return out;
    }
}
