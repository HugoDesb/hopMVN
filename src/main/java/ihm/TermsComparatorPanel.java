package ihm;

import document.Sentence;
import main.ChainHandler;
import stemmer.Stemmer;
import stemmer.snowball.SnowballStemmer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TermsComparatorPanel extends JPanel {

    private JLabel lbl_upload;

    private JButton btn_upLoadRegular;
    private JLabel lbl_uploadRegularValidation;

    private JButton btn_upLoadExpert;
    private JLabel lbl_uploadExpertValidation;

    private JButton btn_compare;

    private JScrollPane scr_left;
    private JTextArea sentencesArea;
    private JButton btn_addTerm;

    private JScrollPane scr_right;
    private JTextArea txt_termsDisplay;
    private JButton btn_saveTerms;


    private JFileChooser fc_regular;

    private File regularFile;
    private File expertFile;

    public TermsComparatorPanel() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout( gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = this.getWidth()/2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);


        // Main Label
        lbl_upload = new JLabel("Files (regular and expert): ");
        add(lbl_upload);

        // Listener for upload buttons
        UploadListener uploadListener = new UploadListener();

        //Button for regular file
        btn_upLoadRegular = new JButton("Upload Regular File");
        btn_upLoadRegular.addActionListener(uploadListener);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(btn_upLoadRegular, gbc);
        lbl_uploadRegularValidation = new JLabel("no file");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lbl_uploadRegularValidation, gbc);

        // Button for expert file
        btn_upLoadExpert = new JButton("Upload Expert File");
        btn_upLoadExpert.addActionListener(uploadListener);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(btn_upLoadExpert, gbc);
        lbl_uploadExpertValidation = new JLabel("no file");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(lbl_uploadExpertValidation, gbc);

        // Button for comparing
        btn_compare = new JButton("Compare");
        btn_compare.addActionListener(new CompareListener());
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(btn_compare, gbc);


        // first half : sentences
        sentencesArea = new JTextArea();
        sentencesArea.setEditable(true);
        scr_left = new JScrollPane(sentencesArea);

        gbc.ipady = 300;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(scr_left, gbc);

        btn_addTerm = new JButton("Add Selected Term");
        btn_addTerm.addActionListener(new AddTermListener());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(btn_addTerm, gbc);


        //second half
        txt_termsDisplay = new JTextArea();
        txt_termsDisplay.setEditable(false);
        scr_right = new JScrollPane(txt_termsDisplay);
        gbc.ipady = 300;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(txt_termsDisplay, gbc);

        btn_saveTerms = new JButton("Save");
        btn_saveTerms.addActionListener(new SaveTermsListener());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 0;
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(btn_saveTerms, gbc);

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
                //Get regular sentences
                ArrayList<Sentence> regularSentences = ChainHandler.until_selectedSentences(regularFile, false);
                //Get expert sentences
                ArrayList<Sentence> expertSentences = ChainHandler.until_selectedSentences(expertFile, true);

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
                    System.out.println("Sentence Out : "+s);
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
                File termsFile = new File("./files/conf/patterns");
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
