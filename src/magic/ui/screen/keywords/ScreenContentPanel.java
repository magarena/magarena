package magic.ui.screen.keywords;

import javax.swing.JLabel;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class ScreenContentPanel extends TexturedPanel {

    protected final void showKeywordsFileError(Exception ex) {
        JLabel lbl = new JLabel(String.format(
            "<html><h2><font color=\"red\"><b>%s</b></font></h1></html>",
            ex.toString().replaceAll("\n", "<br>"))
        );
        setLayout(new MigLayout());
        add(lbl);
    }
}
