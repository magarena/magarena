package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
class ViewerScrollPane extends JScrollPane {

    private JPanel contentPanel;

    ViewerScrollPane() {

        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(FontsAndBorders.NO_BORDER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setUnitIncrement(80);
        getVerticalScrollBar().setBlockIncrement(80);
    }

    synchronized JPanel getContent() {
        if (contentPanel==null) {
            contentPanel=new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
            contentPanel.setBackground(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_VIEWER_BACKGROUND));
        }
        return contentPanel;
    }

    synchronized void switchContent() {
        if (contentPanel!=null) {
            final Rectangle rect=getViewport().getViewRect();
            final JPanel mainPanel=new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
            mainPanel.add(contentPanel,BorderLayout.NORTH);
            getViewport().add(mainPanel);
            getViewport().scrollRectToVisible(rect);
            contentPanel=null;
        }
    }
}
