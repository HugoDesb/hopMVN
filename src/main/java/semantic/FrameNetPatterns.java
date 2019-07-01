package semantic;

import java.io.*;
import java.util.ArrayList;

public class FrameNetPatterns {

    private ArrayList<FrameNetPattern> frameNetPatterns;

    public FrameNetPatterns(File file) {
        frameNetPatterns = loadPatterns(file);
    }

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
}
