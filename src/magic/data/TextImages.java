package magic.data;

import magic.ui.IconImages;
import magic.model.MagicCounterType;

import javax.swing.ImageIcon;

import java.util.HashMap;
import java.util.Map;

public class TextImages {

    private static final Map<String,ImageIcon> TEXT_ICONS = new HashMap<>();

    static {
        TEXT_ICONS.put("{0}", IconImages.getSmallManaIcon(MagicIcon.MANA_0));
        TEXT_ICONS.put("{1}", IconImages.getSmallManaIcon(MagicIcon.MANA_1));
        TEXT_ICONS.put("{2}", IconImages.getSmallManaIcon(MagicIcon.MANA_2));
        TEXT_ICONS.put("{3}", IconImages.getSmallManaIcon(MagicIcon.MANA_3));
        TEXT_ICONS.put("{4}", IconImages.getSmallManaIcon(MagicIcon.MANA_4));
        TEXT_ICONS.put("{5}", IconImages.getSmallManaIcon(MagicIcon.MANA_5));
        TEXT_ICONS.put("{6}", IconImages.getSmallManaIcon(MagicIcon.MANA_6));
        TEXT_ICONS.put("{7}", IconImages.getSmallManaIcon(MagicIcon.MANA_7));
        TEXT_ICONS.put("{8}", IconImages.getSmallManaIcon(MagicIcon.MANA_8));
        TEXT_ICONS.put("{9}", IconImages.getSmallManaIcon(MagicIcon.MANA_9));
        TEXT_ICONS.put("{10}", IconImages.getSmallManaIcon(MagicIcon.MANA_10));
        TEXT_ICONS.put("{11}", IconImages.getSmallManaIcon(MagicIcon.MANA_11));
        TEXT_ICONS.put("{12}", IconImages.getSmallManaIcon(MagicIcon.MANA_12));
        TEXT_ICONS.put("{13}", IconImages.getSmallManaIcon(MagicIcon.MANA_13));
        TEXT_ICONS.put("{14}", IconImages.getSmallManaIcon(MagicIcon.MANA_14));
        TEXT_ICONS.put("{15}", IconImages.getSmallManaIcon(MagicIcon.MANA_15));
        TEXT_ICONS.put("{16}", IconImages.getSmallManaIcon(MagicIcon.MANA_16));
        TEXT_ICONS.put("{X}", IconImages.getSmallManaIcon(MagicIcon.MANA_X));

        TEXT_ICONS.put("{W}", IconImages.getSmallManaIcon(MagicIcon.MANA_WHITE));
        TEXT_ICONS.put("{U}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLUE));
        TEXT_ICONS.put("{B}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLACK));
        TEXT_ICONS.put("{R}", IconImages.getSmallManaIcon(MagicIcon.MANA_RED));
        TEXT_ICONS.put("{G}", IconImages.getSmallManaIcon(MagicIcon.MANA_GREEN));
        TEXT_ICONS.put("{W/P}", IconImages.getSmallManaIcon(MagicIcon.MANA_PHYREXIAN_WHITE));
        TEXT_ICONS.put("{U/P}", IconImages.getSmallManaIcon(MagicIcon.MANA_PHYREXIAN_BLUE));
        TEXT_ICONS.put("{B/P}", IconImages.getSmallManaIcon(MagicIcon.MANA_PHYREXIAN_BLACK));
        TEXT_ICONS.put("{R/P}", IconImages.getSmallManaIcon(MagicIcon.MANA_PHYREXIAN_RED));
        TEXT_ICONS.put("{G/P}", IconImages.getSmallManaIcon(MagicIcon.MANA_PHYREXIAN_GREEN));
        TEXT_ICONS.put("{2/W}", IconImages.getSmallManaIcon(MagicIcon.MANA_HYBRID_WHITE));
        TEXT_ICONS.put("{2/U}", IconImages.getSmallManaIcon(MagicIcon.MANA_HYBRID_BLUE));
        TEXT_ICONS.put("{2/B}", IconImages.getSmallManaIcon(MagicIcon.MANA_HYBRID_BLACK));
        TEXT_ICONS.put("{2/R}", IconImages.getSmallManaIcon(MagicIcon.MANA_HYBRID_RED));
        TEXT_ICONS.put("{2/G}", IconImages.getSmallManaIcon(MagicIcon.MANA_HYBRID_GREEN));
        TEXT_ICONS.put("{B/G}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLACK_GREEN));
        TEXT_ICONS.put("{B/R}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLACK_RED));
        TEXT_ICONS.put("{U/B}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLUE_BLACK));
        TEXT_ICONS.put("{U/R}", IconImages.getSmallManaIcon(MagicIcon.MANA_BLUE_RED));
        TEXT_ICONS.put("{G/U}", IconImages.getSmallManaIcon(MagicIcon.MANA_GREEN_BLUE));
        TEXT_ICONS.put("{G/W}", IconImages.getSmallManaIcon(MagicIcon.MANA_GREEN_WHITE));
        TEXT_ICONS.put("{R/G}", IconImages.getSmallManaIcon(MagicIcon.MANA_RED_GREEN));
        TEXT_ICONS.put("{R/W}", IconImages.getSmallManaIcon(MagicIcon.MANA_RED_WHITE));
        TEXT_ICONS.put("{W/B}", IconImages.getSmallManaIcon(MagicIcon.MANA_WHITE_BLACK));
        TEXT_ICONS.put("{W/U}", IconImages.getSmallManaIcon(MagicIcon.MANA_WHITE_BLUE));

        TEXT_ICONS.put("{f}",IconImages.getIcon(MagicIcon.FORWARD2));
        TEXT_ICONS.put("{O}",IconImages.getIcon(MagicIcon.TARGET));
        TEXT_ICONS.put("{T}",IconImages.getSmallManaIcon(MagicIcon.MANA_TAPPED));
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
