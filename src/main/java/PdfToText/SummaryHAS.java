package PdfToText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryHAS {

    private List<String> lines;
    private Map<String, Integer> summary;

    public SummaryHAS(String text) {
        this.lines = new ArrayList<>();
        this.summary = new HashMap<>();
        boolean hasStarted = false;
        String [] l =text.split("\n");
        for (String line : l) {
            if(line.matches("\\.\\.\\.")){
                hasStarted = true;
                lines.add(line);
            }
        }
    }

    public int [] getUsablePages(){
        int [] hop = {};
        return hop;
    }

    /**
     *
     * @param line
     */
    private void addEntry(String line){
        int i = line.lastIndexOf('.');
        Integer startPage = Integer.parseInt(line.substring(i+1));
        System.out.println("startPage: "+startPage);
        String title = line.split("...")[0];
        summary.put(title, startPage);
    }
}
