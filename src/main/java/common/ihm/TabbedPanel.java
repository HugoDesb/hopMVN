package common.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TabbedPanel extends JPanel {

    public TabbedPanel() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent panel1 = new ChainOperatorPanel();
        tabbedPane.addTab("Chain Operator", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new TermsComparatorPanel();
        tabbedPane.addTab("Comparaison Extraction/Expert", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
