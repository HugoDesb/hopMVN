package pretreatement.Filter;

import document.Sentence;
import document.TextDocument;

import java.util.ArrayList;

public class InitialFilter{

    private static final String PATTERN_STRING_DECLENCHEUR = "(.*\\s+mise\\s+en\\s+place.*)" +
            "|(.*\\s+auto[a-z]*.*)" +
            "|(.*\\s+recommand.*)" +
            "|(.*\\s+privil[ée]gi.*)" +
            "|(.*\\s+d+(oi[st(vent)]|ev[(ons)(ez)(ions)(iez)(ais)(ait)(aient)r[(ais)(ait)(aient)(a)(as)(ons)(ez)(ont)]]).*)" +
            "|(.*indiqu.*)|(.*indication.*)" +
            "|(.*\\s+prot[éèe]g.*)" +
            "|(.*\\s+pr[eo]scri.*)" +
            "|(.*\\s+grossesse.*)|(.*\\s+enceinte.*)" +
            "|(.*non-pharmacologique.*)" +
            "|(.*\\s+ag[ée].*)" +
            "|(.*\\s+limit.*)" +
            "|(.*\\s+envisag[ée].*)" +
            "|(.*\\s+pr[ée]v).*";
    private static final String PATTERN_STRING_EXCLUSION = "";


    public static void filter(TextDocument td){
        TextDocument.Builder newTD = new TextDocument.Builder();
        newTD.setFile(td.getFile());

        ArrayList<Sentence> sentencesOUT = new ArrayList<>();

        for (Sentence sentence : td.getLines()) {
            if(select(sentence.getText())){
                if(!unselect(sentence.getText())){
                    newTD.addLine(sentence);
                }

            }
        }

        td.setLines(sentencesOUT);
    }

    private static boolean unselect(String line) {
        return line.matches(PATTERN_STRING_EXCLUSION);
    }

    public static boolean select(String line) {
        return line.matches(PATTERN_STRING_DECLENCHEUR);
    }
}
