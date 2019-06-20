package common.ihm;

import common.config.Config;
import common.document.Sentence;
import main.ChainHandler;
import pretreatement.stemmer.Stemmer;
import pretreatement.stemmer.snowball.SnowballStemmer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TermsComparatorPanel extends JPanel {

    //NORTH
    private JLabel title;

    //CENTER
    private JPanel jp_center;
    private JLabel lbl_upload;

    private JButton btn_upLoadRegular;
    private JLabel lbl_uploadRegularValidation;

    private JButton btn_upLoadExpert;
    private JLabel lbl_uploadExpertValidation;

    private JButton btn_compare;

    //SOUTH
    private JPanel jp_south;
    private JScrollPane scr_sentencesArea;
    private JTextArea sentencesArea;
    private JPanel jp_btn_addTerm;
    private JButton btn_addTerm;

    private JScrollPane scr_terms;
    private JTextArea txt_termsDisplay;
    private JPanel jp_btn_saveTerms;
    private JButton btn_saveTerms;


    private JFileChooser fc_regular;

    private File regularFile;
    private File expertFile;

    public TermsComparatorPanel() {
        setLayout(new BorderLayout());

        //NORTH
        title = new JLabel("Veuillez choisir 2 fichiers puis comparez les.");
        add(title, BorderLayout.NORTH);

        //CENTER
        jp_center = new JPanel(new GridLayout(3,3));

        //Main Label
        lbl_upload = new JLabel("Files (regular and expert): ");
        jp_center.add(lbl_upload);

        // Listener for upload buttons
        UploadListener uploadListener = new UploadListener();

        //Button for regular file
        btn_upLoadRegular = new JButton("Upload Regular File");
        btn_upLoadRegular.addActionListener(uploadListener);
        jp_center.add(btn_upLoadRegular);
        lbl_uploadRegularValidation = new JLabel("no file");
        jp_center.add(lbl_uploadRegularValidation);
        jp_center.add(new JPanel());

        // Button for expert file
        btn_upLoadExpert = new JButton("Upload Expert File");
        btn_upLoadExpert.addActionListener(uploadListener);
        jp_center.add(btn_upLoadExpert);
        lbl_uploadExpertValidation = new JLabel("no file");
        jp_center.add(lbl_uploadExpertValidation);
        jp_center.add(new JPanel());
        jp_center.add(new JPanel());

        // Button for comparing
        btn_compare = new JButton("Compare");
        btn_compare.addActionListener(new CompareListener());
        jp_center.add(btn_compare);

        add(jp_center, BorderLayout.CENTER);

        // SOUTH
        //panel
        jp_south = new JPanel();
        jp_south.setLayout(new BoxLayout(jp_south, BoxLayout.PAGE_AXIS));
        jp_south.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // sentences area
        sentencesArea = new JTextArea();
        sentencesArea.setEditable(true);
        sentencesArea.setRows(40);
        sentencesArea.setWrapStyleWord(true);
        sentencesArea.setLineWrap(true);
        scr_sentencesArea = new JScrollPane(sentencesArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        jp_south.add(scr_sentencesArea);

        // button in jpanel
        jp_btn_addTerm = new JPanel();
        jp_btn_addTerm.setLayout(new BoxLayout(jp_btn_addTerm, BoxLayout.LINE_AXIS));
        jp_btn_addTerm.add(Box.createHorizontalGlue());
        btn_addTerm = new JButton("Add Selected Term");
        btn_addTerm.addActionListener(new AddTermListener());
        jp_btn_addTerm.add(btn_addTerm);
        jp_south.add(jp_btn_addTerm);


        // terms area
        txt_termsDisplay = new JTextArea();
        txt_termsDisplay.setEditable(false);
        scr_terms = new JScrollPane(txt_termsDisplay);
        jp_south.add(scr_terms);

        jp_btn_saveTerms = new JPanel();
        jp_btn_saveTerms.setLayout(new BoxLayout(jp_btn_saveTerms, BoxLayout.LINE_AXIS));
        jp_btn_saveTerms.add(Box.createHorizontalGlue());
        btn_saveTerms = new JButton("Save");
        btn_saveTerms.addActionListener(new SaveTermsListener());
        jp_btn_saveTerms.add(btn_saveTerms);
        jp_south.add(jp_btn_saveTerms);

        add(jp_south, BorderLayout.SOUTH);


        fc_regular = new JFileChooser();
    }


    /**
     * Listener for file uploads
     */
    private class UploadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //Handle open button action.

            // Open Dialog (file chooser)
            int returnVal = fc_regular.showOpenDialog(TermsComparatorPanel.this);
            //Check if regular or expert
            if(actionEvent.getSource() == btn_upLoadRegular){
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    regularFile = fc_regular.getSelectedFile();
                    lbl_uploadRegularValidation.setText(regularFile.getName());
                }
            }else{
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    expertFile= fc_regular.getSelectedFile();
                    lbl_uploadExpertValidation.setText(expertFile.getName());
                }
            }
        }
    }

    /**
     * Listener for launching sentences selection for both files, and compare them.
     * Then display all sentences in a JTable
     */
    private class CompareListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            boolean found = false;

            ArrayList<String> sentencesOut = new ArrayList<>();

            if(regularFile == null || expertFile == null){
                // TODO : gérer l'erreur ?
            }else{
                sentencesArea.setText("");


                //Get regular sentences
                ArrayList<Sentence> regularSentences = ChainHandler.pretreatmentModule("default.properties",
                        regularFile.getAbsolutePath(),
                        "test",
                        "test",
                        false,
                        true).getLines();
                //Get expert sentences
                ArrayList<Sentence> expertSentences = ChainHandler.pretreatmentModule("default.properties",
                        expertFile.getAbsolutePath(),
                        "test",
                        "test",
                        false,
                        true).getLines();
                File f = new File("./files/resultat_mon_algo_expert.txt");
                try {
                    FileWriter writer = new FileWriter(f);
                    for (Sentence s :
                            expertSentences) {
                        writer.write(s.getText()+"\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File f2 = new File("./files/resultat_mon_algo.txt");
                try {
                    FileWriter writer2 = new FileWriter(f2);
                    for (Sentence s :
                            regularSentences) {
                        writer2.write(s.getText()+"\n");
                    }
                    writer2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Get sentences in expert file but not in regular -- use contains + (levenshtein ?)
                for (Sentence sExpert: expertSentences) {
                    if(!sExpert.getText().contains(" ")){
                        continue;
                    }else{
                        found = false;
                        for(Sentence sRegular : regularSentences){
                            if(sExpert.getText().contains(sRegular.getText()) || sRegular.getText().contains(sExpert.getText())){
                                found = true;
                            }
                        }
                        if(!found){
                            sentencesOut.add(sExpert.getText());
                            sentencesArea.append(sExpert.getText()+"\n");
                        }
                    }
                }

                for (String s : sentencesOut) {
                    //System.out.println("Sentence Out : "+s);
                }
            }


        }
    }

    private class AddTermListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String selectedText = sentencesArea.getSelectedText();
            if(selectedText!=null) {
                // TODO : stem text
                Stemmer s = new SnowballStemmer(SnowballStemmer.ALGORITHM.FRENCH);
                selectedText = String.valueOf(s.stem(selectedText));
                //TODO: update selected sentences with the new term ?
                txt_termsDisplay.append(selectedText + "\n");
            }
        }
    }

    private class SaveTermsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String terms = txt_termsDisplay.getText();
            if(!terms.isEmpty()){
                File termsFile = new File(Config.getInstance().getProp("pretreatment.termes_declencheurs_path"));
                try {
                    FileWriter fw = new FileWriter(termsFile, true);
                    fw.write(terms);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                txt_termsDisplay.setText("");
            }
        }
    }
}
