package magic.ui.widget.about;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class TitlePanel extends JPanel {

    TitlePanel(String screenTitle) {

        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        add(new VersionLabel());
        add(new ScreenLabel(screenTitle));

        setOpaque(false);
        setFocusable(false);
    }
}
