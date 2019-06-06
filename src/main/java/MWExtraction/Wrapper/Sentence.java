package Wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sentence {

    private String text;
    private Map<Integer, ArrayList<MWE>> allMWE;

    public Sentence(String text) {
        this.text = text;
        allMWE = new HashMap<>();
    }

    /*public addMWE(int length, String text, boolean validated, double measure){
        if(allMWE.get())
    }*/

    @Override
    public String toString() {
        String ret = text+"\n";

        for (int i = 4; i > 0 ; i--) {
            ArrayList<MWE> forOneSizeMWE = allMWE.get(Integer.valueOf(i));
            if(forOneSizeMWE.size() != 0){
                ret += "T"+i+"\n";
                for (MWE mwe :forOneSizeMWE) {
                    ret += mwe.toString() + "\n";
                }
            }
        }

        return ret;
    }
}
