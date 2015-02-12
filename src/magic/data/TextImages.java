package magic.data;

import magic.ui.IconImages;
import magic.model.MagicCounterType;

import javax.swing.ImageIcon;

import java.util.HashMap;
import java.util.Map;

public class TextImages {

    private static final Map<String,ImageIcon> TEXT_ICONS = new HashMap<>();

    static {
        TEXT_ICONS.put("{0}",IconImages.COST_ZERO);
        TEXT_ICONS.put("{1}",IconImages.COST_ONE);
        TEXT_ICONS.put("{2}",IconImages.COST_TWO);
        TEXT_ICONS.put("{3}",IconImages.COST_THREE);
        TEXT_ICONS.put("{4}",IconImages.COST_FOUR);
        TEXT_ICONS.put("{5}",IconImages.COST_FIVE);
        TEXT_ICONS.put("{6}",IconImages.COST_SIX);
        TEXT_ICONS.put("{7}",IconImages.COST_SEVEN);
        TEXT_ICONS.put("{8}",IconImages.COST_EIGHT);
        TEXT_ICONS.put("{9}",IconImages.COST_NINE);
        TEXT_ICONS.put("{10}",IconImages.COST_TEN);
        TEXT_ICONS.put("{11}",IconImages.COST_ELEVEN);
        TEXT_ICONS.put("{12}",IconImages.COST_TWELVE);
        TEXT_ICONS.put("{13}",IconImages.COST_THIRTEEN);
        TEXT_ICONS.put("{14}",IconImages.COST_FOURTEEN);
        TEXT_ICONS.put("{15}",IconImages.COST_FIFTEEN);
        TEXT_ICONS.put("{16}",IconImages.COST_SIXTEEN);
        TEXT_ICONS.put("{X}",IconImages.COST_X);

        TEXT_ICONS.put("{W}",IconImages.COST_WHITE);
        TEXT_ICONS.put("{U}",IconImages.COST_BLUE);
        TEXT_ICONS.put("{B}",IconImages.COST_BLACK);
        TEXT_ICONS.put("{R}",IconImages.COST_RED);
        TEXT_ICONS.put("{G}",IconImages.COST_GREEN);
        TEXT_ICONS.put("{W/P}",IconImages.COST_PHYREXIAN_WHITE);
        TEXT_ICONS.put("{U/P}",IconImages.COST_PHYREXIAN_BLUE);
        TEXT_ICONS.put("{B/P}",IconImages.COST_PHYREXIAN_BLACK);
        TEXT_ICONS.put("{R/P}",IconImages.COST_PHYREXIAN_RED);
        TEXT_ICONS.put("{G/P}",IconImages.COST_PHYREXIAN_GREEN);
        TEXT_ICONS.put("{2/W}",IconImages.COST_HYBRID_WHITE);
        TEXT_ICONS.put("{2/U}",IconImages.COST_HYBRID_BLUE);
        TEXT_ICONS.put("{2/B}",IconImages.COST_HYBRID_BLACK);
        TEXT_ICONS.put("{2/R}",IconImages.COST_HYBRID_RED);
        TEXT_ICONS.put("{2/G}",IconImages.COST_HYBRID_GREEN);
        TEXT_ICONS.put("{B/G}",IconImages.COST_BLACK_GREEN);
        TEXT_ICONS.put("{B/R}",IconImages.COST_BLACK_RED);
        TEXT_ICONS.put("{U/B}",IconImages.COST_BLUE_BLACK);
        TEXT_ICONS.put("{U/R}",IconImages.COST_BLUE_RED);
        TEXT_ICONS.put("{G/U}",IconImages.COST_GREEN_BLUE);
        TEXT_ICONS.put("{G/W}",IconImages.COST_GREEN_WHITE);
        TEXT_ICONS.put("{R/G}",IconImages.COST_RED_GREEN);
        TEXT_ICONS.put("{R/W}",IconImages.COST_RED_WHITE);
        TEXT_ICONS.put("{W/B}",IconImages.COST_WHITE_BLACK);
        TEXT_ICONS.put("{W/U}",IconImages.COST_WHITE_BLUE);

        TEXT_ICONS.put("{f}",IconImages.getIcon(MagicIcon.FORWARD2));
        TEXT_ICONS.put("{O}",IconImages.getIcon(MagicIcon.TARGET));
        TEXT_ICONS.put("{T}",IconImages.TAPPED);
        TEXT_ICONS.put("{S}",IconImages.getIcon(MagicIcon.CANNOTTAP));
        TEXT_ICONS.put("{r}",IconImages.getIcon(MagicIcon.REGENERATED));
        TEXT_ICONS.put("{s}",IconImages.getIcon(MagicIcon.SLEEP));
        TEXT_ICONS.put("{c}",IconImages.getIcon(MagicIcon.COMBAT));
        TEXT_ICONS.put("{b}",IconImages.getIcon(MagicIcon.BLOCKED));
        TEXT_ICONS.put("{D}",IconImages.getIcon(MagicIcon.DAMAGE));
        TEXT_ICONS.put("{P}",IconImages.getIcon(MagicIcon.PREVENT));
        TEXT_ICONS.put("{t}",IconImages.getIcon(MagicIcon.TOKEN));
        TEXT_ICONS.put(MagicCounterType.PlusOne.getText(), IconImages.getIcon(MagicIcon.PLUS));
        TEXT_ICONS.put(MagicCounterType.MinusOne.getText(),IconImages.getIcon(MagicIcon.MINUS));
        TEXT_ICONS.put(MagicCounterType.Charge.getText(),IconImages.getIcon(MagicIcon.CHARGE));
        TEXT_ICONS.put("{F}",IconImages.getIcon(MagicIcon.FEATHER));
        TEXT_ICONS.put("{g}",IconImages.getIcon(MagicIcon.GOLDCOUNTER));
        TEXT_ICONS.put("{br}",IconImages.getIcon(MagicIcon.BRIBECOUNTER));
        TEXT_ICONS.put("{L}",IconImages.getIcon(MagicIcon.LOSE));
        TEXT_ICONS.put(MagicCounterType.Spore.getText(),IconImages.getIcon(MagicIcon.SPORECOUNTER));
    }

    public static ImageIcon getIcon(final String text) {
        if (!TEXT_ICONS.containsKey(text)) {
            throw new RuntimeException("No corresponding icon for " + text);
        }
        return TEXT_ICONS.get(text);
    }
}
