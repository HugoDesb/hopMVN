package pretreatement.Filter;

import document.Sentence;

import java.util.ArrayList;

public class Filter {

    public String regexSelect;
    public String regexDelete;

    public Filter(String regexSelect, String regexDelete) {
        this.regexSelect = regexSelect;
        this.regexDelete = regexDelete;
    }

    public ArrayList<Sentence> filter(ArrayList<Sentence> lines) {
        ArrayList<Sentence> ret = new ArrayList<>();
        for (Sentence line : lines) {
            if(line.getText().toLowerCase().matches(regexSelect) && !line.getText().toLowerCase().matches(regexDelete)){
                ret.add(line);
            }
        }
        return ret;
    }
}

