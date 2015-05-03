package magic.ui;

import java.awt.Color;
import java.awt.Dimension;
import magic.ui.theme.ThemeFactory;


public class CardFilterPanel {

    public static final String[] COST_VALUES = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
    public static final String[] FILTER_CHOICES = {"Match any selected", "Match all selected", "Exclude selected"};
    public static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    public static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);
    public static final Dimension BUTTON_HOLDER_PANEL_SIZE = new Dimension(100, 36);

}
