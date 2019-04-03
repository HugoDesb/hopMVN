package tagging.RNNTagger;

import config.Config;
import document.TextDocument;
import tagging.Tagger;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class RNNTagger implements Tagger {


    private final String tmpFolderRelativePath = "files/tmp/";

    @Override
    public ArrayList<TaggedSentence> tag(TextDocument textDocument) {
        ArrayList<TaggedSentence> taggedFile = new ArrayList<>();
        for (String sentence : textDocument.getLines()) {
            System.out.println("LINE : " + sentence);
            Process pr = null;

            TaggedSentence taggedSentence = new TaggedSentence();
            try {
                pr = Runtime.getRuntime().exec("/bin/bash cd "+ Config.RNNTAGGER_ABSOLUTE_PATH+" " +
                        "&& echo \""+sentence+"\" > tmp.txt " +
                        "&& cmd/rnn-tagger-french.sh tmp.txt > tmp_tagged.txt " +
                        "&& echo \"DONE\"");
                int exitVal = pr.waitFor();
                System.out.println("Processus exited with value : "+ exitVal);

                //READ OUTPUT

                File out = new File(Config.RNNTAGGER_ABSOLUTE_PATH+"tmp_tagged.txt");
                BufferedReader br = new BufferedReader(new FileReader(out));

                String line;
                System.out.println("<OUTPUT>");
                while ( (line = br.readLine()) != null){
                    if(line.equals("") || Objects.equals(line, "DONE")){
                        break;
                    }else{
                        System.out.println(line);
                        taggedSentence.addTaggedToken(new RNNTag(line));
                    }

                }
                System.out.println("</OUTPUT>");

                br.close();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            taggedFile.add(taggedSentence);
        }

        return taggedFile;
    }



    private File createTempFile(String sentence){
        File tmpSourceFile = new File(tmpFolderRelativePath + "");
        return  tmpSourceFile;
    }
}
