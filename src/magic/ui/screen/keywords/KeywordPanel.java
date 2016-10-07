package magic.ui.screen.keywords;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.KeywordDefinitions.KeywordDefinition;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class KeywordPanel extends JPanel {

    private static final Color NAME_COLOR = MagicStyle.getTheme().getColor(Theme.COLOR_NAME_FOREGROUND);
    private static final Color TEXT_COLOR = MagicStyle.getTheme().getTextColor();

    KeywordPanel(KeywordDefinition keyword) {

        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        setOpaque(false);

        final JLabel nameLabel = new JLabel(keyword.name);
        nameLabel.setForeground(NAME_COLOR);
        nameLabel.setFont(FontsAndBorders.FONT2);
        add(nameLabel, "w 100%");

        final JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("<html>" + keyword.description.replace("<br>", " ") + "</html>");
        descriptionLabel.setForeground(TEXT_COLOR);
        add(descriptionLabel, "w 10:100%");        
    }

}
