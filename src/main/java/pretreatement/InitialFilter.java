package pretreatement;

import document.TextDocument;

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
            "|(.*\\s+ag[ée].*)";;
    private static final String PATTERN_STRING_EXCLUSION = "";


    public static TextDocument filter(TextDocument td){
        TextDocument.Builder newTD = new TextDocument.Builder();
        newTD.setFile(td.getFile());


        for (String line : td.getLines()) {
            if(select(line)){
                if(!unselect(line)){
                    newTD.addLine(line);
                }

            }
        }
        return newTD.build();
    }

    private static boolean unselect(String line) {
        return line.matches(PATTERN_STRING_EXCLUSION);
    }

    public static boolean select(String line) {
        return line.matches(PATTERN_STRING_DECLENCHEUR);
    }
}
