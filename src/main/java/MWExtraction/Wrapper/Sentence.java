package MWExtraction.Wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sentence {

    private String text;
    private Map<Integer, ArrayList<MWE>> allMWE;
    private int number;

    public Sentence(String text, int number) {
        this.text = text;
        this.number = number;
        allMWE = new HashMap<>();
    }

    public void addMWE(int length, String text, boolean validated, double measure){
        if(!allMWE.containsKey(length)){
            allMWE.put(length, new ArrayList<>());
        }

        allMWE.get(length).add(new MWE(text, validated, measure));
    }

    @Override
    public String toString() {
        String ret = number+"\n"+text+"\n";


        for (int i = 4; i > 0 ; i--) {
            if(!allMWE.containsKey(i)){
                allMWE.put(i, new ArrayList<>());
            }

            ArrayList<MWE> forOneSizeMWE = allMWE.get(i);
            ret += "T"+i+"\n";
            for (MWE mwe :forOneSizeMWE) {
                ret += mwe.toString() + "\n";
            }
        }
        ret += "##########END##########\n";

        return ret;
    }

    public int getMWECountFor(int length) {
        if(!allMWE.containsKey(length)){
            return 0;
        }else{
            return allMWE.get(length).size();
        }
    }

    public ArrayList<MWE> getAllMWEFor(int length) {
        if(!allMWE.containsKey(length)){
            return new ArrayList<>();
        }else{
            return allMWE.get(length);
        }
    }

    public String getOnlyMWEof(int length) {
        if(!allMWE.containsKey(length)){
            return null;
        }else{
            return allMWE.get(length).get(0).getText();
        }
    }

    public String getText() {
        return text;
    }
}
