package semantic;

import java.io.*;
import java.util.ArrayList;

public class FrameNetPatterns {

    private ArrayList<FrameNetPattern> frameNetPatterns;

    public FrameNetPatterns(File file) {
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
                if(!line.trim().isEmpty()){
                    hop.add(new FrameNetPattern(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hop;
    }

    public Rule createRule(Chunk chunk, FrameNetPattern fnp) {
        ArrayList<String> condition = new ArrayList<>();
        ArrayList<String> objet = new ArrayList<>();
        if(fnp.getDestination().equals("Object")){
            objet.add(chunk.getText());
        }else{
            condition.add(chunk.getText());
        }
        return new Rule(condition, objet, fnp.toString(), chunk.getSentenceFull());
    }

    public FrameNetPattern matches(Chunk chunk) {
        for (FrameNetPattern fnp : frameNetPatterns) {
            if(fnp.matches(chunk))
                return fnp;
        }
        return null;
    }
}
