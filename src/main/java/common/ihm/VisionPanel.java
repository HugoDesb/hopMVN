package common.ihm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class VisionPanel extends JPanel {

    private SemanticOpenSesameTagging taggedSentences;

    private static final int RECT_X = 20;
    private static final int RECT_Y = RECT_X;
    private static final int RECT_WIDTH = 100;
    private static final int RECT_HEIGHT = RECT_WIDTH;

    private SemanticOpenSesameTagging sost;

    public VisionPanel(File conllFile) {
        this.sost = new SemanticOpenSesameTagging(conllFile);

        for(int i = 0; i<sost.getSentences().size(); i++){
            ArrayList<ArrayList<Chunk>> chunkedSentenceFrames = sost.getChunksForSentence(i);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw the rectangle here
        //g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
    }



}
