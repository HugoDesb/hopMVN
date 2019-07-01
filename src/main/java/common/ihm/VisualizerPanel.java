package common.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class VisualizerPanel extends JComponent {

    private JLabel lbl_uploadMessage;
    private JButton btn_uploadCONLL;

    private JFileChooser fc_regular;

    private JLabel lbl_sentence;
    private VisionPanel jp_annotations;

    private File conllFile;

    public VisualizerPanel() {
        setLayout(new BorderLayout());

        // add message
        lbl_uploadMessage = new JLabel("Upload CONLL file");
        add(lbl_uploadMessage, BorderLayout.NORTH);

        //Button for regular file
        UploadListener uploadListener = new UploadListener();
        btn_uploadCONLL = new JButton("Upload Regular File");
        btn_uploadCONLL.addActionListener(uploadListener);
        add(btn_uploadCONLL, BorderLayout.NORTH);

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
            int returnVal = fc_regular.showOpenDialog(VisualizerPanel.this);
            //Check if regular or expert
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                conllFile= fc_regular.getSelectedFile();
                jp_annotations = new VisionPanel(conllFile);
                add(jp_annotations, BorderLayout.CENTER);
            }
        }
    }


}
