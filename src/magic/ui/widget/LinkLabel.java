package magic.ui.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import magic.ui.helpers.UrlHelper;

@SuppressWarnings("serial")
public class LinkLabel extends JLabel {

    public LinkLabel(final String text, final String url) {
        super(text);
        setForeground(Color.BLUE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                UrlHelper.openURL(url);
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setText("<html><u>" + text + "</u></html>");
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                setText(text);
            }
        });
    }

}
