package magic.ui.screen.keywords;

import java.awt.Color;
import magic.data.CardDefinitions;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanelB extends TexturedPanel {

    ContentPanelB() {

        // TODO
        // This is requird to show images of missing cards but is not desirable
        // due the lengthy load time of missing scripts. It would be much more
        // efficient in this particular case to check for the image directly
        // since we already have the card name.
        CardDefinitions.getMissingCards();

        KeywordPanelB keywordPanel = new KeywordPanelB();

        setLayout(new MigLayout("", "[grow, fill][500!, grow, fill]", "[grow, fill]"));
        add(new KeywordsListPane(keywordPanel));
        add(keywordPanel);

//        setBackground(ActionBar.getBackgroundColor().darker());
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG.darker());

    }
}
