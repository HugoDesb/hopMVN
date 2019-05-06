package pretreatement.Filter;

public class DefaultFilter extends Filter{

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
            "|(.*\\s+pr[ée]v.*)" +
            "|(.*\\s+[eé]vit.*)" +
            "|(.*\\s+dia).*";
    private static final String PATTERN_STRING_EXCLUSION = "";

    public DefaultFilter() {
        super(PATTERN_STRING_DECLENCHEUR, PATTERN_STRING_EXCLUSION);
    }
}
