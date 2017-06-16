package magic.ui.mwidgets;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import magic.ui.widget.scrollbar.MScrollBarUI;

public class MScrollPane extends MWidget {

    private final JScrollPane scrollPane = new JScrollPane();

    public MScrollPane() {
        setCornerComponent();
    }

    /**
     * This fills the gap in the bottom right corner of a JScrollPane by adding
     * a panel which is approximately the same color as {@link MScrollBarUI}.
     */
    private void setCornerComponent() {
        final JPanel p = new JPanel();
        p.setBackground(MScrollBarUI.COLOR_A);
        scrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, p);
    }

    @Override
    public JComponent component() {
        return scrollPane;
    };

    //
    // swing component delegates
    //    
    public void setViewportView(Component c) {
        scrollPane.setViewportView(c);
    }

    public void setBorder(Border b) {
        scrollPane.setBorder(b);
    }

    public void setOpaque(boolean b) {
        scrollPane.setOpaque(b);
        scrollPane.getViewport().setOpaque(b);
    }

    public void setVScrollBarIncrement(int i) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(i);
    }

    public void setVScrollBarBlockIncrement(int i) {
        scrollPane.getVerticalScrollBar().setBlockIncrement(i);
    }

    public void setVScrollBarValue(int i) {
        scrollPane.getVerticalScrollBar().setValue(0);
    }

    void setHScrollBarAsNeeded() {
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public void setVisible(boolean b) {
        scrollPane.setVisible(b);
    }

    public void setBackground(Color c) {
        scrollPane.setBackground(c);
        scrollPane.getViewport().setBackground(c);
    }
}
