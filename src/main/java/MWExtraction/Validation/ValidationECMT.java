package Validation;

import Object.CandidatTerm;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

public class ValidationECMT {

    public static ArrayList<CandidatTerm> Validate_All_Terms(ArrayList<CandidatTerm> list_candidat_terms){

        File tempFile = new File("tmp.xml");

        ArrayList<CandidatTerm> list_candidat_terms_validated = new ArrayList<CandidatTerm>();
        /*
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("hugo.desbiolles@etu.univ-amu.fr", "AconerIMumPk");
        provider.setCredentials(AuthScope.ANY, credentials);
        */

        DefaultHttpClient client = new DefaultHttpClient();

        int count = 0;
        String source = "ECMTv3";

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

    private static boolean existsInHeTOP(String term, DefaultHttpClient client, File tempFile){

        if(term.contains("##########end##########")){
            return false;
        }
        String termURL = term.replace(" ", "+").trim();
        int statusCode;

        try {

            //Send request
            HttpResponse response = client.execute(
                    new HttpGet("https://cispro.chu-rouen.fr/CISMeFecmtservice/REST/getAutomaticIndexingWithOptions/e=&f=/"+termURL));
            statusCode = response.getStatusLine().getStatusCode();

            if(statusCode != 200){
                throw new HttpException("Error while connecting to ecmte");
            }

            //Getting the response body
            InputStream is = response.getEntity().getContent();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            //choose utf-8 as charset
            //write xml to temp file
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

            NodeList nList = doc.getElementsByTagName("cis:indexation");

            return nList.getLength() != 0;



        } catch (IOException | ParserConfigurationException | SAXException | HttpException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        return false;
    }
}
