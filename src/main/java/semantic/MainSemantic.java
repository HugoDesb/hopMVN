package semantic;

import common.config.Config;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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

    private void writeOutput(ArrayList<Rule> rules, String ruleFormat) throws FileNotFoundException {

        String outputFolder = config.getProp("os_analysis.output_folder")+configSet.get("name")+File.separator;
        String output = outputFolder;

        String toPrint;
        PrintStream ps;

        switch(ruleFormat){
            case Rule.HUMAN_VALIDATION_FORMAT:
                output += "rulesForValidation.xls";
                ExcelWriter ew = new ExcelWriter(rules);
                ew.write(output);
                break;
            case Rule.DEV_FORMAT:
                output += "rules.txt";
                ps = new PrintStream(new File(output));
                for (Rule r : rules) {
                    if(r.getConclusionsToStrings().size()>0) {
                        toPrint = r.toStringOutput();
                        ps.println(toPrint);
                    }
                }
                ps.close();
                break;
            case Rule.DEV_PRETTY_FORMAT:
            default:
                output += "rules.txt";
                ps = new PrintStream(new File(output));
                for (Rule r : rules) {
                    if(r.getConclusionsToStrings().size()>0) {
                        toPrint = r.toString();
                        ps.println(toPrint);
                    }
                }
                ps.close();
        }

    }

    private void runCombinator(ArrayList<Rule> rules) {
        System.out.println("[runCombinator] : Not Yet Implemented");



        //throw new NotImplementedException();
    }

    public void runOpenSesame() {
        String opensesameFolder = config.getProp("open-sesame.absolute_path");

        String file_to_annotate_local = config.getProp("pretreatment.output_folder")+configSet.get("name")+ File.separator+configSet.get("name")+".txt";
        String file_to_annotate_distant = opensesameFolder+"sentences.txt";

        String outputFolder = config.getProp("os_analysis.output_folder")+configSet.get("name")+File.separator;
        (new File(outputFolder)).mkdirs();
        String annotated_file_local = outputFolder + "annotatedSentences.csv";
        String annotated_file_distant = config.getProp("open-sesame.absolute_path")+"logs/"+config.getProp("open-sesame.model-arg_id")+"/predicted-args.conll";


        try {
            // WRITE INPUT
            Files.copy(Paths.get(file_to_annotate_local), Paths.get(file_to_annotate_distant), StandardCopyOption.REPLACE_EXISTING);

            // RUN COMMAND TO TAG
            String launch = "/bin/sh ./files/scripts/open-sesame-"+configSet.get("language")+".sh "
                    +Config.getInstance().getProp("open-sesame.absolute_path")+" "
                    +config.getProp("open-sesame.model-target_id")+" "
                    +config.getProp("open-sesame.model-frame_id")+" "
                    +config.getProp("open-sesame.model-arg_id")+" ";
            CommandLine cmdLine = CommandLine.parse(launch);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine);

            // COPY DISTANT RESULT TO LOCAL
            Files.copy(Paths.get(annotated_file_distant), Paths.get(annotated_file_local), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
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

        try {
            writeOutput(rg.getGeneratedRules(), Rule.HUMAN_VALIDATION_FORMAT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //rg.writeResults(rg.getGeneratedRules(), output, Rule.HUMAN_VALIDATION_FORMAT);
    }
}
