package MWExtraction.Validation;

import MWExtraction.Object.CandidatTerm;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class ValidationHetop {

    public static ArrayList<CandidatTerm> Validate_All_Terms(ArrayList<CandidatTerm> list_candidat_terms){

        File tempFile = new File("tmp.xml");

        ArrayList<CandidatTerm> list_candidat_terms_validated = new ArrayList<CandidatTerm>();


        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("hugo.desbiolles@etu.univ-amu.fr", "AconerIMumPk");
        provider.setCredentials(AuthScope.ANY, credentials);


        HttpClient client = HttpClientBuilder.create()
                //.setDefaultCredentialsProvider(provider)
                .build();

        int count = 0;
        String source = "HeTOP";

        while(count<list_candidat_terms.size()){
            if(existsInHeTOP(list_candidat_terms.get(count).getTerm().trim(), client, tempFile)){
                list_candidat_terms.get(count).setIsTrueTerm(1);
                list_candidat_terms.get(count).setSourceDictionary(source);
            }
            list_candidat_terms_validated.add(list_candidat_terms.get(count));
            count++;
        }
        tempFile.delete();

        return list_candidat_terms_validated;
    }

    private static boolean existsInHeTOP(String term, HttpClient client, File tempFile){

        if(term.toLowerCase().contains("##########end##########")){
            return false;
        }
        String termURL = term.replace(" ", "+").trim();
        int statusCode;

        try {

            //Send request
            HttpGet b = new HttpGet("http://www.hetop.fr/CISMeFhetopservice/REST/search/"+termURL+"/fr/def=false&f=T_DESC_MESH_DESCRIPTEUR");

            String encoding = Base64.getEncoder().encodeToString("hugo.desbiolles@etu.univ-amu.fr:AconerIMumPk".getBytes());
            b.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

            System.out.println("[ MWE ] Search hétop for :: " + term);

            HttpResponse response = client.execute(b);
            //http://www.hetop.eu/CISMeFhetopservice/REST/search/avant bras/fr/def=false&f=T_DESC_MESH_DESCRIPTEUR
            statusCode = response.getStatusLine().getStatusCode();




            //Getting the response body
            InputStream is = response.getEntity().getContent();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            response.getEntity().consumeContent();
            //EntityUtils.consume(response.getEntity());

            if(statusCode != 200){
                return false;
            }

            //choose utf-8 as charset
            //buildOutput xml to temp file
            String outText = result.toString("UTF-8");
            FileWriter fw = new FileWriter(tempFile);
            fw.write(outText);
            fw.close();

            // Parse file to read xml
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(tempFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cis:dbo");

            return nList.getLength() != 0;



        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        return false;
    }
}
