package pretreatement.Filter;

import java.util.ArrayList;

public class Filter {

    public String regexSelect;
    public String regexDelete;

    public Filter(String regexSelect, String regexDelete) {
        this.regexSelect = regexSelect;
        this.regexDelete = regexDelete;
    }

    public ArrayList<String> filter(ArrayList<String> lines) {
        ArrayList<String> ret = new ArrayList<>();
        for (String line : lines) {
            if(line.toLowerCase().matches(regexSelect) && !line.toLowerCase().matches(regexDelete)){
                ret.add(line);
            }
        }
        return ret;
    }
}

