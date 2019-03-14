package PdfToText;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class BasicPipelineExample {

    public static void main(String[] args) throws IOException {
        List<String> sentences = Files.readAllLines(Paths.get("./files/10irp04_reco_diabete_type_2.txt"));

        StringBuilder sb= new StringBuilder();

        for(String tempString:sentences){
            sb.append(" ").append(tempString);
        }
        String text = "\n" +
                "Messages clés \n" +
                " \n" +
                "L’objectif glycémique doit être individualisé en fonction du profil des patients et peut donc \n" +
                "évoluer au cours du temps. \n" +
                " \n" +
                "Pour la plupart des patients diabétiques de type 2, une cible d’HbA1c inférieure ou égale à \n" +
                "7 % est recommandée. Le traitement médicamenteux doit être instauré ou réévalué si \n" +
                "l’HbA1c est supérieure à 7 %.  \n" +
                " \n" +
                "Le diabète est évolutif et le traitement doit être réévalué régulièrement dans toutes ses \n" +
                "composantes : mesures hygiéno-diététiques, éducation thérapeutique et traitement \n" +
                "médicamenteux. \n" +
                " \n" +
                "La mise en place de mesures hygiéno-diététiques efficaces est un préalable nécessaire au \n" +
                "traitement médicamenteux du contrôle glycémique. \n" +
                " \n" +
                "La stratégie médicamenteuse repose sur l’écart par rapport à l’objectif d’HbA1c, l’efficacité \n" +
                "attendue des traitements, leur tolérance, leur sécurité et leur coût. \n" +
                " \n" +
                "La metformine est le médicament de première intention en monothérapie. \n" +
                "L’association metformine + sulfamide est la bithérapie à privilégier. \n" +
                "L’insuline est le traitement de choix lorsque les traitements oraux et non insuliniques ne \n" +
                "permettent pas d’atteindre l’objectif glycémique. ";

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object

        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);

        for (CoreSentence l :
                document.sentences()) {
            System.out.println(l.text().replaceAll("\n", ""));
        }
    }
}