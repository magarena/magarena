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
        TEXT_ICONS.put("{17}", MagicIcon.MANA_17);
        TEXT_ICONS.put("{18}", MagicIcon.MANA_18);
        TEXT_ICONS.put("{19}", MagicIcon.MANA_19);
        TEXT_ICONS.put("{20}", MagicIcon.MANA_20);
        TEXT_ICONS.put("{X}", MagicIcon.MANA_X);
        TEXT_ICONS.put("{S}", MagicIcon.MANA_SNOW);

        TEXT_ICONS.put("{C}", MagicIcon.MANA_COLORLESS);
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
        TEXT_ICONS.put("{SS}", MagicIcon.CANNOTTAP);
        TEXT_ICONS.put("{r}", MagicIcon.REGENERATED);
        TEXT_ICONS.put("{s}", MagicIcon.SLEEP);
        TEXT_ICONS.put("{c}", MagicIcon.COMBAT);
        TEXT_ICONS.put("{b}", MagicIcon.BLOCKED);
        TEXT_ICONS.put("{D}", MagicIcon.DAMAGE);
        TEXT_ICONS.put("{P}", MagicIcon.PREVENT);
        TEXT_ICONS.put("{t}", MagicIcon.TOKEN);
        TEXT_ICONS.put("{L}", MagicIcon.LOSE);
        TEXT_ICONS.put(MagicCounterType.PlusOne.getText(), MagicIcon.PLUS);
        TEXT_ICONS.put(MagicCounterType.MinusOne.getText(), MagicIcon.MINUS);
        TEXT_ICONS.put(MagicCounterType.Charge.getText(), MagicIcon.CHARGE);
        TEXT_ICONS.put(MagicCounterType.PlusTwo.getText(), MagicIcon.PLUSTWO);
        TEXT_ICONS.put(MagicCounterType.MinusTwo.getText(), MagicIcon.MINUSTWO);
        TEXT_ICONS.put(MagicCounterType.PlusZeroPlusOne.getText(), MagicIcon.PLUSZEROPLUSONE);
        TEXT_ICONS.put(MagicCounterType.MinusZeroMinusOne.getText(), MagicIcon.MINUSZEROMINUSONE);
        TEXT_ICONS.put(MagicCounterType.PlusZeroPlusTwo.getText(), MagicIcon.PLUSZEROPLUSTWO);
        TEXT_ICONS.put(MagicCounterType.MinusZeroMinusTwo.getText(), MagicIcon.MINUSZEROMINUSTWO);
        TEXT_ICONS.put(MagicCounterType.PlusOnePlusZero.getText(), MagicIcon.PLUSONEPLUSZERO);
        TEXT_ICONS.put(MagicCounterType.MinusOneMinusZero.getText(), MagicIcon.MINUSONEMINUSZERO);
        TEXT_ICONS.put(MagicCounterType.PlusOnePlusTwo.getText(), MagicIcon.PLUSONEPLUSTWO);
        TEXT_ICONS.put(MagicCounterType.PlusTwoPlusZero.getText(), MagicIcon.PLUSTWOPLUSZERO);
        TEXT_ICONS.put(MagicCounterType.MinusTwoMinusOne.getText(), MagicIcon.MINUSTWOMINUSONE);
        TEXT_ICONS.put(MagicCounterType.Feather.getText(), MagicIcon.FEATHER);
        TEXT_ICONS.put(MagicCounterType.Gold.getText(), MagicIcon.GOLDCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Bribery.getText(), MagicIcon.BRIBECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Fade.getText(), MagicIcon.FADECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Time.getText(), MagicIcon.TIMECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Quest.getText(), MagicIcon.QUESTCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Level.getText(), MagicIcon.LEVELCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Hoofprint.getText(), MagicIcon.HOOFPRINTCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Age.getText(), MagicIcon.AGECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Ice.getText(), MagicIcon.ICECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Spore.getText(), MagicIcon.SPORECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Arrowhead.getText(), MagicIcon.ARROWHEADCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Loyalty.getText(), MagicIcon.LOYALTYCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Ki.getText(), MagicIcon.KICOUNTER);
        TEXT_ICONS.put(MagicCounterType.Depletion.getText(), MagicIcon.DEPLETIONCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Mining.getText(), MagicIcon.MININGCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Muster.getText(), MagicIcon.MUSTERCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Treasure.getText(), MagicIcon.TREASURECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Strife.getText(), MagicIcon.STRIFECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Study.getText(), MagicIcon.STUDYCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Trap.getText(), MagicIcon.TRAPCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Shield.getText(), MagicIcon.SHIELDCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Wish.getText(), MagicIcon.WISHCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Shell.getText(), MagicIcon.SHELLCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Hatchling.getText(), MagicIcon.SHELLCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Blaze.getText(), MagicIcon.BLAZECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Tide.getText(), MagicIcon.TIDECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Gem.getText(), MagicIcon.GEMCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Credit.getText(), MagicIcon.BRIBECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Pressure.getText(), MagicIcon.PRESSURECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Verse.getText(), MagicIcon.VERSECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Music.getText(), MagicIcon.MUSICCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Rust.getText(), MagicIcon.RUSTCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Lore.getText(), MagicIcon.STUDYCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Blood.getText(), MagicIcon.BLOODCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Growth.getText(), MagicIcon.GROWTHCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Wage.getText(), MagicIcon.BRIBECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Plague.getText(), MagicIcon.PLAGUECOUNTER);
        TEXT_ICONS.put(MagicCounterType.Pin.getText(), MagicIcon.PINCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Healing.getText(), MagicIcon.HEALINGCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Scream.getText(), MagicIcon.SCREAMCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Devotion.getText(), MagicIcon.DEVOTIONCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Divinity.getText(), MagicIcon.DIVINITYCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Death.getText(), MagicIcon.DEATHCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Wind.getText(), MagicIcon.WINDCOUNTER);
        TEXT_ICONS.put(MagicCounterType.Tower.getText(), MagicIcon.TOWERCOUNTER);
    }

    public static MagicIcon getIcon(final String text) {
        if (TEXT_ICONS.containsKey(text) == false) {
            throw new RuntimeException("No corresponding icon for " + text);
        }
        return TEXT_ICONS.get(text);
    }
    
    public static boolean contains(final String text) {
        return TEXT_ICONS.containsKey(text);
    }
}
