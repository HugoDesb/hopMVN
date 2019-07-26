package semantic;

import java.io.*;
import java.util.ArrayList;

public class FrameNetPatterns {

    private ArrayList<FrameNetPattern> frameNetPatterns;
    private ArrayList<Pattern> blacklist;

    FrameNetPatterns(File file) {
        blacklist = new ArrayList<>();
        frameNetPatterns = loadPatterns(file);
    }

    /**
     * Load patterns from patterns file
     * @param file
     * @return
     */
    private ArrayList<FrameNetPattern> loadPatterns(File file) {
        ArrayList<FrameNetPattern> hop = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));

            String line;
            while((line = bf.readLine())!=null){
                if(!line.trim().isEmpty() && line.charAt(0) != '#'){
                    if(line.trim().contains("--")){
                        blacklist.add(FrameNetPattern.createPatternForOneframe(line.replaceAll("--", "").replaceAll(";","")));
                    }else{
                        hop.add(new FrameNetPattern(line));
                    }

                    //System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hop;
    }


    public boolean isInBlacklist(Pattern p){
        return blacklist.contains(p);
    }

    public ArrayList<Pattern> getBlackList(){
        return blacklist;
    }

    public ArrayList<FrameNetPattern> getFrameNetPatterns() {
        return frameNetPatterns;
    }
}
