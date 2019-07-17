package pretreatement.Extractor.Summaries;

import java.util.LinkedHashMap;
import java.util.Map;

public class SummaryHAS extends Summary {

    private Map<String, Integer> summary;

    public SummaryHAS(String text) {
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
     * Get first and last usable page's number (ie without anything above summary, and any annexes and below).
     * @return first and last usable pages
     */
    @Override
    public int [] getContentBoundaries(){
        int [] hop = {3,3};
        for (String key : summary.keySet()) {
            if(key.matches(".*Annexe.*")){
                hop[1] = summary.get(key)-1;

                for (int j:hop){
                    System.out.println("MAGUEULE : "+j);
                }

                return hop;
            }
        }
        return hop;
    }

    public int getAbbrevPageNumber(){
        for (String key : summary.keySet()) {
            if(key.matches(".*Abréviations.*")){
                return summary.get(key)-1;
            }
        }
        return -1;
    }

    /**
     * Given a typical summary line for HAS, extract title and page number
     * @param line
     */
    private void addEntry(String line){
        int startPage = super.getPage(line);
        String title = line.split("\\.\\.\\.")[0];
        summary.put(title, startPage);
    }

    @Override
    public String toString() {
        return "SummaryHAS{" +
                "summary=" + summary +
                '}';
    }
}
