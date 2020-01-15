package magic.ui.screen.keywords;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.ui.FontsAndBorders;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class KeywordPanelA extends JPanel {

    private static final Color NAME_COLOR = MagicStyle.getTheme().getColor(Theme.COLOR_NAME_FOREGROUND);
    private static final Color TEXT_COLOR = MagicStyle.getTheme().getTextColor();

    KeywordPanelA(Keyword keyword) {

        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        setOpaque(false);

        final JLabel nameLabel = new JLabel(keyword.getName());
        nameLabel.setForeground(NAME_COLOR);
        nameLabel.setFont(FontsAndBorders.FONT2);
        add(nameLabel, "w 100%");

        final JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText(keyword.getDescriptionAsHtml());
        descriptionLabel.setForeground(TEXT_COLOR);
        add(descriptionLabel, "w 10:100%");        
    }

}
