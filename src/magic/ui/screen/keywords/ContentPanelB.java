package magic.ui.screen.keywords;

import java.awt.Color;
import java.io.IOException;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanelB extends ScreenContentPanel {

    ContentPanelB() {

        // TODO
        // This is required to show images of missing cards but is not desirable
        // due the lengthy load time of missing scripts. It would be much more
        // efficient in this particular case to check for the image directly
        // since we already have the card name.
//        CardDefinitions.getMissingCards();

        try {
            KeywordPanelB keywordPanel = new KeywordPanelB();
            setLayout(new MigLayout("", "[grow, fill][500!, grow, fill]", "[grow, fill]"));
            add(new KeywordsListPane(keywordPanel));
            add(keywordPanel);
        } catch (IOException ex) {
            showKeywordsFileError(ex);
        }

        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG.darker());
    }
}
