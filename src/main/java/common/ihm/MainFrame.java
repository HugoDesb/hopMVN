package common.ihm;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        setBounds(200, 200, 900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        TabbedPanel p = new TabbedPanel();
        add(p, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}
