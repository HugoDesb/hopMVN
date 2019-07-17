package pretreatement.Extractor.Summaries;

import java.util.Map;

public abstract class Summary {

    private Map<String, Integer> summary;

    protected int getPage(String line){
        int i = line.lastIndexOf('.');
        return Integer.parseInt(line.substring(i+1).trim());
    }

    protected String getTitle(String text){
        return "";
    }

    public abstract int [] getContentBoundaries();


    @Override
    public String toString() {
        return "Summary{" +
                "summary=" + summary +
                '}';
    }
}
