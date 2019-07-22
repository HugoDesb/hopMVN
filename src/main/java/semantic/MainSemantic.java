package semantic;

import common.config.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Map;

public class MainSemantic {

    private Map<String, String> configSet;
    private Config config;

    public MainSemantic(Map<String, String> configSet) {
        this.configSet = configSet;
        this.config = Config.getInstance(configSet.get("config_file"));
    }

    public void run(boolean opensesame, boolean ruleGenerator){
        if(opensesame){
            runOpenSesame();
        }
        if(ruleGenerator){
            runAnalysis();
        }
    }

    private void runCombinator(ArrayList<Rule> rules) {
        System.out.println("[runCombinator] : Not Yet Implemented");



        //throw new NotImplementedException();
    }

    public void runOpenSesame() {
        String file_to_annotate = config.getProp("pretreatment.output_folder")+configSet.get("name")+ File.separator+configSet.get("name")+".txt";
        String opensesameFolder = config.getProp("open-sesame.absolute_path");

        try{

            //Local Machine :
            String cmd = "cd "+opensesameFolder + " && " +
                    "python2.7 -m sesame.targetid --mode=predict --model "+config.getProp("open-sesame.model-target_id")+" --raw_input="+file_to_annotate+" && " +
                    "python2.7 -m sesame.frameid --mode predict --model "+config.getProp("open-sesame.model-frame_id")+" --raw_input=logs/"+config.getProp("open-sesame.model-target_id")+"/predicted-targets.conll && " +
                    "python2.7 -m sesame.argid --mode predict --model "+config.getProp("open-sesame.model-arg_id")+" --raw_input=logs/"+config.getProp("open-sesame.model-frame_id")+"/predicted-frames.conll";


            /////////////////////////////////////////////////////////
            ///////////////////// C A M B I O S /////////////////////
            /////////////////////////////////////////////////////////
            //PARA UNIX
            Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec(cmd);

            //POUR WINDOWS
        	/*Runtime runtime = Runtime.getRuntime();
        	final Process process = runtime.exec(cmd + " " + file);
        	*/

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try{
                while((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }finally {
                reader.close();
            }
        }catch (Exception e){
            System.err.println(e.toString());
        }finally {
            String annotated_file = config.getProp("open-sesame.absolute_path")+"logs/"+config.getProp("open-sesame.model-arg_id")+"/predicted-args.conll";
            String outputFolder = config.getProp("os_analysis.output_folder")+configSet.get("name")+File.separator;
            String annotated_file_local = outputFolder + "annotatedSentences.csv";
            try {
                Files.copy(Paths.get(annotated_file), Paths.get(annotated_file_local), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void runAnalysis(){
        String file_to_annotate = config.getProp("pretreatment.output_folder")+configSet.get("name")+ File.separator+configSet.get("name")+".txt";
        String outputFolder = config.getProp("os_analysis.output_folder")+configSet.get("name")+File.separator;
        String mweFile = config.getProp("mwe.output_folder")+configSet.get("name")+File.separator+"outSentences.txt";
        String annotated_file_local = outputFolder + "annotatedSentences.csv";

        String output = outputFolder + "rules.txt";
        String patterns = config.getProp("open-sesame.patterns");

        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File(annotated_file_local));
        sost.correctSentences(file_to_annotate);
        FrameNetPatterns fnp = new FrameNetPatterns(new File(patterns));
        RulesGenerator rg = new RulesGenerator(sost, fnp, configSet.get("topic"));

        rg.generateRules();
        rg.combineMultiWordsExpression(mweFile);

        rg.writeResults(rg.getGeneratedRules(), output);
    }
}
