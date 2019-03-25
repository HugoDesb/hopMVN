package tagging.RNNTagger;

import config.Config;
import document.TextDocument;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tagging.TaggedSentence;
import tagging.Tagger;

import java.io.*;

public class RNNTagger implements Tagger {

    private final String absolutePath;
    private final String tmpFolderRelativePath = "files/tmp/";

    public RNNTagger(String absolutePath) {
        this.absolutePath = Config.RNNTAGGER_ABSOLUTE_PATH;
    }

    @Override
    public TaggedSentence tag(TextDocument textDocument) {
        for (String sentence : textDocument.getLines()) {
            Runtime rt = Runtime.getRuntime();
            Process pr = null;
            try {
                pr = rt.exec("cd "+ absolutePath + " && echo "+sentence+" > test.txt && sh cmd/rnn-tagger-french.sh test.txt");
                InputStream stdin = pr.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);

                String line = null;
                System.out.println("<OUTPUT>");

                while (br.ready()){
                    line = br.readLine();
                    System.out.println(line);
                }

                System.out.println("</OUTPUT>");
                int exitVal = pr.waitFor();
                System.out.println("Process exitValue: " + exitVal);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        throw new NotImplementedException();
    }



    private File createTempFile(String sentence){
        File tmpSourceFile = new File(tmpFolderRelativePath + "");
        return  tmpSourceFile;
    }
}
