package pretreatement.Filter;

import config.Config;
import document.Sentence;

import java.io.*;
import java.util.ArrayList;

public class Filter {

    public String regexSelect;
    public String regexDelete;

    public Filter(){
        File f = new File(Config.TERMES_DECLENCHEURS_PATH);
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String regexSelect = "";
            while(line != null){
                regexSelect += "(.*\\s"+line+".*)|";
                line = br.readLine();
            }
            this.regexSelect = regexSelect.substring(0,regexSelect.length());
            this.regexDelete = "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

