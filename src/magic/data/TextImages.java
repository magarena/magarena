package magic.data;

import magic.model.MagicCounterType;
import java.util.HashMap;
import java.util.Map;

public class TextImages {

    private static final Map<String, MagicIcon> TEXT_ICONS = new HashMap<>();

    static {
        TEXT_ICONS.put("{0}", MagicIcon.MANA_0);
        TEXT_ICONS.put("{1}", MagicIcon.MANA_1);
        TEXT_ICONS.put("{2}", MagicIcon.MANA_2);
        TEXT_ICONS.put("{3}", MagicIcon.MANA_3);
        TEXT_ICONS.put("{4}", MagicIcon.MANA_4);
        TEXT_ICONS.put("{5}", MagicIcon.MANA_5);
        TEXT_ICONS.put("{6}", MagicIcon.MANA_6);
        TEXT_ICONS.put("{7}", MagicIcon.MANA_7);
        TEXT_ICONS.put("{8}", MagicIcon.MANA_8);
        TEXT_ICONS.put("{9}", MagicIcon.MANA_9);
        TEXT_ICONS.put("{10}", MagicIcon.MANA_10);
        TEXT_ICONS.put("{11}", MagicIcon.MANA_11);
        TEXT_ICONS.put("{12}", MagicIcon.MANA_12);
        TEXT_ICONS.put("{13}", MagicIcon.MANA_13);
        TEXT_ICONS.put("{14}", MagicIcon.MANA_14);
        TEXT_ICONS.put("{15}", MagicIcon.MANA_15);
        TEXT_ICONS.put("{16}", MagicIcon.MANA_16);
        TEXT_ICONS.put("{X}", MagicIcon.MANA_X);

        TEXT_ICONS.put("{W}", MagicIcon.MANA_WHITE);
        TEXT_ICONS.put("{U}", MagicIcon.MANA_BLUE);
        TEXT_ICONS.put("{B}", MagicIcon.MANA_BLACK);
        TEXT_ICONS.put("{R}", MagicIcon.MANA_RED);
        TEXT_ICONS.put("{G}", MagicIcon.MANA_GREEN);
        TEXT_ICONS.put("{W/P}", MagicIcon.MANA_PHYREXIAN_WHITE);
        TEXT_ICONS.put("{U/P}", MagicIcon.MANA_PHYREXIAN_BLUE);
        TEXT_ICONS.put("{B/P}", MagicIcon.MANA_PHYREXIAN_BLACK);
        TEXT_ICONS.put("{R/P}", MagicIcon.MANA_PHYREXIAN_RED);
        TEXT_ICONS.put("{G/P}", MagicIcon.MANA_PHYREXIAN_GREEN);
        TEXT_ICONS.put("{2/W}", MagicIcon.MANA_HYBRID_WHITE);
        TEXT_ICONS.put("{2/U}", MagicIcon.MANA_HYBRID_BLUE);
        TEXT_ICONS.put("{2/B}", MagicIcon.MANA_HYBRID_BLACK);
        TEXT_ICONS.put("{2/R}", MagicIcon.MANA_HYBRID_RED);
        TEXT_ICONS.put("{2/G}", MagicIcon.MANA_HYBRID_GREEN);
        TEXT_ICONS.put("{B/G}", MagicIcon.MANA_BLACK_GREEN);
        TEXT_ICONS.put("{B/R}", MagicIcon.MANA_BLACK_RED);
        TEXT_ICONS.put("{U/B}", MagicIcon.MANA_BLUE_BLACK);
        TEXT_ICONS.put("{U/R}", MagicIcon.MANA_BLUE_RED);
        TEXT_ICONS.put("{G/U}", MagicIcon.MANA_GREEN_BLUE);
        TEXT_ICONS.put("{G/W}", MagicIcon.MANA_GREEN_WHITE);
        TEXT_ICONS.put("{R/G}", MagicIcon.MANA_RED_GREEN);
        TEXT_ICONS.put("{R/W}", MagicIcon.MANA_RED_WHITE);
        TEXT_ICONS.put("{W/B}", MagicIcon.MANA_WHITE_BLACK);
        TEXT_ICONS.put("{W/U}", MagicIcon.MANA_WHITE_BLUE);

        TEXT_ICONS.put("{f}", MagicIcon.FORWARD2);
        TEXT_ICONS.put("{O}", MagicIcon.TARGET);
        TEXT_ICONS.put("{T}", MagicIcon.MANA_TAPPED);
        TEXT_ICONS.put("{S}", MagicIcon.CANNOTTAP);
        TEXT_ICONS.put("{r}", MagicIcon.REGENERATED);
        TEXT_ICONS.put("{s}", MagicIcon.SLEEP);
        TEXT_ICONS.put("{c}", MagicIcon.COMBAT);
        TEXT_ICONS.put("{b}", MagicIcon.BLOCKED);
        TEXT_ICONS.put("{D}", MagicIcon.DAMAGE);
        TEXT_ICONS.put("{P}", MagicIcon.PREVENT);
        TEXT_ICONS.put("{t}", MagicIcon.TOKEN);
        TEXT_ICONS.put(MagicCounterType.PlusOne.getText(), MagicIcon.PLUS);
        TEXT_ICONS.put(MagicCounterType.MinusOne.getText(), MagicIcon.MINUS);
        TEXT_ICONS.put(MagicCounterType.Charge.getText(), MagicIcon.CHARGE);
        TEXT_ICONS.put("{F}", MagicIcon.FEATHER);
        TEXT_ICONS.put("{g}", MagicIcon.GOLDCOUNTER);
        TEXT_ICONS.put("{br}", MagicIcon.BRIBECOUNTER);
        TEXT_ICONS.put("{L}", MagicIcon.LOSE);
        TEXT_ICONS.put(MagicCounterType.Spore.getText(), MagicIcon.SPORECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Loyalty.getText(), MagicIcon.LOYALTYCOUNTER);
    }

    public static MagicIcon getIcon(final String text) {
        if (!TEXT_ICONS.containsKey(text)) {
            throw new RuntimeException("No corresponding icon for " + text);
        }
        return TEXT_ICONS.get(text);
    }
}
