package magic.ui.screen.keywords;

import java.io.IOException;
import magic.ui.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanelA extends ScreenContentPanel {

    ContentPanelA() {
        try {
            setLayout(new MigLayout("insets 0, gap 0, fill"));
            add(new KeywordsScrollPane(), "grow");
        } catch (IOException ex) {
            showKeywordsFileError(ex);
        }
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
    }
}
