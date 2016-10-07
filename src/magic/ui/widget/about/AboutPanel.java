package magic.ui.widget.about;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

    public AboutPanel(String screenTitle) {

        setLayout(new MigLayout("insets 0, gap 4, flowx, aligny center"));
        add(new AboutButton());
        add(new TitlePanel(screenTitle));

        setOpaque(false);
        setFocusable(false);
    }
}
