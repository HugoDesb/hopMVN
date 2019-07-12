package semantic;

import common.config.Config;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class MainSemantic {

    private Map<String, String> configSet;
    private Config config;

    public MainSemantic(Map<String, String> configSet) {
        this.configSet = configSet;
        this.config = Config.getInstance(configSet.get("config_file"));
    }

    public void run(boolean opensesame, boolean ruleGenerator, boolean combineMWE){
        if(opensesame){
            runOpenSesame();
        }
        if(ruleGenerator){
            runAnalysis();
        }
        if(combineMWE){
            runCombinator();
        }
    }

    private void runCombinator() {
        System.out.println("[runCombinator] : Not Yet Implemented");
        throw new NotImplementedException();
    }

    public void runOpenSesame(){
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

            //PARA WINDOWS
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
        }
    }

    public void runAnalysis(){
        String output = config.getProp("os_analysis.output_folder")+configSet.get("name")+ File.separator+"rules.txt";
        String annotated_file = config.getProp("open-sesame.absolute_path")+"logs/"+config.getProp("open-sesame.model-arg_id")+"/predicted-args.conll";
        String patterns = config.getProp("open-sesame.patterns");

        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File(annotated_file));
        FrameNetPatterns fnp = new FrameNetPatterns(new File(patterns));

        RulesGenerator rg = new RulesGenerator(sost, fnp, "type 2 diabetes");

        ArrayList<Rule> hop = rg.generateRules();

        rg.writeResults(hop, output);
    }
}
