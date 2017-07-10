package magic.ui.widget.cards.table;

import java.awt.Color;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;

public enum CardsTableStyle {

    LIGHT(Color.WHITE),
    DARK(Color.BLACK),
    THEME(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));

    private static CardsTableStyle style = LIGHT;

    public static void setStyle(int ordinal) {
        style = CardsTableStyle.values()[ordinal];
    }

    public static CardsTableStyle getStyle() {
        return style;
    }

    /*
     Instance fields and methods.
    */

    private final Color emptyBackgroundColor;

    private CardsTableStyle(Color emptyBackgroundColor) {
        this.emptyBackgroundColor = MagicStyle.getTranslucentColor(emptyBackgroundColor, 230);
    }

    public Color getEmptyBackgroundColor() {
        return this.emptyBackgroundColor;
    }
}
