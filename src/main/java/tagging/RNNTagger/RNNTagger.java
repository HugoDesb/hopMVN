package tagging.RNNTagger;

import common.config.Config;
import common.document.Sentence;
import common.document.TextDocument;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import tagging.Tagger;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class RNNTagger implements Tagger {

    public void tag(TextDocument textDocument) {
        for (Sentence sentence : textDocument.getLines()) {
            ArrayList<RNNTag> tokens = tag(sentence);
            sentence.setTokens(tokens);
        }
    }

    public ArrayList<RNNTag> tag(Sentence sentence){
        String line, launch;
        File in, out;
        FileWriter fw;
        BufferedReader br;
        ArrayList<RNNTag> ret = new ArrayList<>();

        try {
            // WRITE INPUT
            in = new File(Config.getInstance().getProp("pretreatment.rnntagger_path")+"tmp.txt");
            fw = new FileWriter(in);
            fw.write(sentence.getText());
            fw.close();

            // RUN COMMAND TO TAG
            launch = "/bin/sh ./files/scripts/tag-english.sh "+Config.getInstance().getProp("pretreatment.rnntagger_path");
            CommandLine cmdLine = CommandLine.parse(launch);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine);

            //READ OUTPUT
            out = new File(Config.getInstance().getProp("pretreatment.rnntagger_path")+"tmp_tagged.txt");
            br = new BufferedReader(new FileReader(out));

            while ( (line = br.readLine()) != null){
                if(line.equals("") || Objects.equals(line, "DONE")){
                    break;
                }else{
                    ret.add(new RNNTag(line));
                }

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
