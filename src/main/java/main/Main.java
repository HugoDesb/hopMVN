package main;

import pretreatement.InitialFilter;
import document.TextDocument;

import java.io.*;

import static pretreatement.PdfToText.convert;

public class Main {

    public static final String pathReco = "./files/recos_txt/";
    public static final String pathRecoAdjusted = "./files/recos_txt_adjusted/";
    public static final String pathRecoTagged = "./files/recos_txt_adjusted/";

    public static void main(String [] args){
        //System.out.println("The only argument should be a PATH to the root folder containing the pdf files");
        String hopela = "hopela. ";
        System.out.println(hopela.split("z")[0]);
        //System.out.println(hopela.split("z")[1]);

        try {
            TextDocument td = convert(new File("./files/10irp04_reco_diabete_type_2.pdf"));
            td = InitialFilter.filter(td);
            td.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove '?' at the beginning of a line && put lists back into one line (end-of-line == ';')
     * @param in original recos_txt file
     * @param out modified recos_txt file
     */
    public static void adjustFile(File in, File out){
        try{
            PrintWriter printWriter = new PrintWriter(out);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(in));

            String lineRead = bufferedReader.readLine();
            boolean precedentHasTwoPoints = false;
            while(lineRead != null){
                if(precedentHasTwoPoints){

                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void showHelp(){
        System.out.println("The only argument should be a PATH to the root folder containing the pdf files");
        String hopela = "hopela";
        System.out.println(hopela.split("e")[0]);
        System.out.println(hopela.split("e")[1]);
    }
}
