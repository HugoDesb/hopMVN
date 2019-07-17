package pretreatement.Extractor.Summaries;

import java.util.LinkedHashMap;
import java.util.Map;

public class SummaryNICE extends Summary {

    private Map<String, Integer> summary;

    public SummaryNICE(String text) {
        this.summary = new LinkedHashMap<>();
        boolean hasStarted = false;
        String toAdd = "";
        String [] l = text.split("\n");
        for (String line : l) {
            //first summary line
            if(line.matches(".*\\.\\.\\..*") && !hasStarted){
                hasStarted = true;
                addEntry(line);
                // any line without ... --> continues next line
            }else if(!line.matches(".*\\.\\.\\..*")&&hasStarted){
                toAdd += line;
                // any line with ... --> add to summary
            }else if(line.matches(".*\\.\\.\\..*")&&hasStarted){
                toAdd += line;
                addEntry(toAdd);
                toAdd = "";
            }
        }
    }

    /**
     * Get first and last usable page's number (ie covers the recommandation part).
     * @return first and last usable pages
     */
    public int [] getContentBoundaries(){
        int [] hop = {3,3};
        for (String key : summary.keySet()) {
            if(key.toLowerCase().matches(".*recommendation.*") && key.toLowerCase().matches(".*research.*")){
                hop[1] = summary.get(key)-2;
                return hop;
            }else if(key.matches(".*Recommendation.*")){
                hop[0] = summary.get(key)-1;
            }
        }
        return hop;
    }

    /**
     * Given a typical summary line for NICE guidelines, extract title and page number
     * @param line
     */
    private void addEntry(String line){
        int startPage = super.getPage(line);
        String title = line.split("\\.\\.\\.")[0];
        title = title.replaceAll("^([0-9](\\.[0-9])*)\\s", "");
        title = title.replaceAll("\\.", "").trim();
        summary.put(title, startPage);
    }
}