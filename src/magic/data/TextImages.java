package magic.data;

import java.util.HashMap;
import java.util.Map;

import magic.model.MagicCostManaType;
import magic.model.MagicCounterType;
import magic.model.MagicPermanentState;

public class TextImages {

    private static final Map<String, MagicIcon> TEXT_ICONS = new HashMap<>();

    static {
        add("{f}", MagicIcon.FORWARD2);
        add("{O}", MagicIcon.TARGET);
        add("{SS}", MagicIcon.CANNOTTAP);
        add("{D}", MagicIcon.DAMAGE);
        add("{PD}", MagicIcon.PREVENT);
        add("{t}", MagicIcon.TOKEN);
        add("{L}", MagicIcon.LOSE);
        add("{c}", MagicIcon.COMBAT);
        add(MagicPermanentState.Regenerated, MagicIcon.REGENERATED);
        add(MagicPermanentState.DoesNotUntapDuringNext, MagicIcon.SLEEP);
        add(MagicPermanentState.Blocked, MagicIcon.BLOCKED);

        add("{0}", MagicIcon.MANA_0);
        add("{1}", MagicIcon.MANA_1);
        add("{2}", MagicIcon.MANA_2);
        add("{3}", MagicIcon.MANA_3);
        add("{4}", MagicIcon.MANA_4);
        add("{5}", MagicIcon.MANA_5);
        add("{6}", MagicIcon.MANA_6);
        add("{7}", MagicIcon.MANA_7);
        add("{8}", MagicIcon.MANA_8);
        add("{9}", MagicIcon.MANA_9);
        add("{10}", MagicIcon.MANA_10);
        add("{11}", MagicIcon.MANA_11);
        add("{12}", MagicIcon.MANA_12);
        add("{13}", MagicIcon.MANA_13);
        add("{14}", MagicIcon.MANA_14);
        add("{15}", MagicIcon.MANA_15);
        add("{16}", MagicIcon.MANA_16);
        add("{17}", MagicIcon.MANA_17);
        add("{18}", MagicIcon.MANA_18);
        add("{19}", MagicIcon.MANA_19);
        add("{20}", MagicIcon.MANA_20);
        add("{X}", MagicIcon.MANA_X);
        add("{T}", MagicIcon.MANA_TAP);
        add("{Q}", MagicIcon.MANA_UNTAP);
        add("{P}", MagicIcon.MANA_PHYREXIAN_NULL);

        add(MagicCostManaType.Colorless, MagicIcon.MANA_COLORLESS);
        add(MagicCostManaType.Snow, MagicIcon.MANA_SNOW);
        add(MagicCostManaType.White, MagicIcon.MANA_WHITE);
        add(MagicCostManaType.Blue, MagicIcon.MANA_BLUE);
        add(MagicCostManaType.Black, MagicIcon.MANA_BLACK);
        add(MagicCostManaType.Red, MagicIcon.MANA_RED);
        add(MagicCostManaType.Green, MagicIcon.MANA_GREEN);

        add(MagicCostManaType.PhyrexianWhite, MagicIcon.MANA_PHYREXIAN_WHITE);
        add(MagicCostManaType.PhyrexianBlue, MagicIcon.MANA_PHYREXIAN_BLUE);
        add(MagicCostManaType.PhyrexianBlack, MagicIcon.MANA_PHYREXIAN_BLACK);
        add(MagicCostManaType.PhyrexianRed, MagicIcon.MANA_PHYREXIAN_RED);
        add(MagicCostManaType.PhyrexianGreen, MagicIcon.MANA_PHYREXIAN_GREEN);

        add(MagicCostManaType.HybridWhite, MagicIcon.MANA_HYBRID_WHITE);
        add(MagicCostManaType.HybridBlue, MagicIcon.MANA_HYBRID_BLUE);
        add(MagicCostManaType.HybridBlack, MagicIcon.MANA_HYBRID_BLACK);
        add(MagicCostManaType.HybridRed, MagicIcon.MANA_HYBRID_RED);
        add(MagicCostManaType.HybridGreen, MagicIcon.MANA_HYBRID_GREEN);

        add(MagicCostManaType.BlackGreen, MagicIcon.MANA_BLACK_GREEN);
        add(MagicCostManaType.BlackRed, MagicIcon.MANA_BLACK_RED);
        add(MagicCostManaType.BlueBlack, MagicIcon.MANA_BLUE_BLACK);
        add(MagicCostManaType.BlueRed, MagicIcon.MANA_BLUE_RED);
        add(MagicCostManaType.GreenBlue, MagicIcon.MANA_GREEN_BLUE);
        add(MagicCostManaType.GreenWhite, MagicIcon.MANA_GREEN_WHITE);
        add(MagicCostManaType.RedGreen, MagicIcon.MANA_RED_GREEN);
        add(MagicCostManaType.RedWhite, MagicIcon.MANA_RED_WHITE);
        add(MagicCostManaType.WhiteBlack, MagicIcon.MANA_WHITE_BLACK);
        add(MagicCostManaType.WhiteBlue, MagicIcon.MANA_WHITE_BLUE);

        add(MagicCounterType.PlusOne, MagicIcon.PLUS);
        add(MagicCounterType.MinusOne, MagicIcon.MINUS);
        add(MagicCounterType.Charge, MagicIcon.CHARGE);
        add(MagicCounterType.PlusTwo, MagicIcon.PLUSTWO);
        add(MagicCounterType.MinusTwo, MagicIcon.MINUSTWO);
        add(MagicCounterType.PlusZeroPlusOne, MagicIcon.PLUSZEROPLUSONE);
        add(MagicCounterType.MinusZeroMinusOne, MagicIcon.MINUSZEROMINUSONE);
        add(MagicCounterType.PlusZeroPlusTwo, MagicIcon.PLUSZEROPLUSTWO);
        add(MagicCounterType.MinusZeroMinusTwo, MagicIcon.MINUSZEROMINUSTWO);
        add(MagicCounterType.PlusOnePlusZero, MagicIcon.PLUSONEPLUSZERO);
        add(MagicCounterType.MinusOneMinusZero, MagicIcon.MINUSONEMINUSZERO);
        add(MagicCounterType.PlusOnePlusTwo, MagicIcon.PLUSONEPLUSTWO);
        add(MagicCounterType.PlusTwoPlusZero, MagicIcon.PLUSTWOPLUSZERO);
        add(MagicCounterType.MinusTwoMinusOne, MagicIcon.MINUSTWOMINUSONE);
        add(MagicCounterType.Feather, MagicIcon.FEATHER);
        add(MagicCounterType.Gold, MagicIcon.GOLDCOUNTER);
        add(MagicCounterType.Bribery, MagicIcon.BRIBECOUNTER);
        add(MagicCounterType.Fade, MagicIcon.FADECOUNTER);
        add(MagicCounterType.Time, MagicIcon.TIMECOUNTER);
        add(MagicCounterType.Quest, MagicIcon.QUESTCOUNTER);
        add(MagicCounterType.Level, MagicIcon.LEVELCOUNTER);
        add(MagicCounterType.Hoofprint, MagicIcon.HOOFPRINTCOUNTER);
        add(MagicCounterType.Age, MagicIcon.AGECOUNTER);
        add(MagicCounterType.Ice, MagicIcon.ICECOUNTER);
        add(MagicCounterType.Spore, MagicIcon.SPORECOUNTER);
        add(MagicCounterType.Arrowhead, MagicIcon.ARROWHEADCOUNTER);
        add(MagicCounterType.Loyalty, MagicIcon.LOYALTYCOUNTER);
        add(MagicCounterType.Ki, MagicIcon.KICOUNTER);
        add(MagicCounterType.Depletion, MagicIcon.DEPLETIONCOUNTER);
        add(MagicCounterType.Mining, MagicIcon.MININGCOUNTER);
        add(MagicCounterType.Muster, MagicIcon.MUSTERCOUNTER);
        add(MagicCounterType.Treasure, MagicIcon.TREASURECOUNTER);
        add(MagicCounterType.Strife, MagicIcon.STRIFECOUNTER);
        add(MagicCounterType.Study, MagicIcon.STUDYCOUNTER);
        add(MagicCounterType.Trap, MagicIcon.TRAPCOUNTER);
        add(MagicCounterType.Shield, MagicIcon.SHIELDCOUNTER);
        add(MagicCounterType.Wish, MagicIcon.WISHCOUNTER);
        add(MagicCounterType.Shell, MagicIcon.SHELLCOUNTER);
        add(MagicCounterType.Hatchling, MagicIcon.SHELLCOUNTER);
        add(MagicCounterType.Blaze, MagicIcon.BLAZECOUNTER);
        add(MagicCounterType.Tide, MagicIcon.TIDECOUNTER);
        add(MagicCounterType.Gem, MagicIcon.GEMCOUNTER);
        add(MagicCounterType.Credit, MagicIcon.BRIBECOUNTER);
        add(MagicCounterType.Pressure, MagicIcon.PRESSURECOUNTER);
        add(MagicCounterType.Verse, MagicIcon.VERSECOUNTER);
        add(MagicCounterType.Music, MagicIcon.MUSICCOUNTER);
        add(MagicCounterType.Rust, MagicIcon.RUSTCOUNTER);
        add(MagicCounterType.Lore, MagicIcon.STUDYCOUNTER);
        add(MagicCounterType.Blood, MagicIcon.BLOODCOUNTER);
        add(MagicCounterType.Growth, MagicIcon.GROWTHCOUNTER);
        add(MagicCounterType.Wage, MagicIcon.BRIBECOUNTER);
        add(MagicCounterType.Plague, MagicIcon.PLAGUECOUNTER);
        add(MagicCounterType.Pin, MagicIcon.PINCOUNTER);
        add(MagicCounterType.Healing, MagicIcon.HEALINGCOUNTER);
        add(MagicCounterType.Scream, MagicIcon.SCREAMCOUNTER);
        add(MagicCounterType.Devotion, MagicIcon.DEVOTIONCOUNTER);
        add(MagicCounterType.Divinity, MagicIcon.DIVINITYCOUNTER);
        add(MagicCounterType.Death, MagicIcon.DEATHCOUNTER);
        add(MagicCounterType.Wind, MagicIcon.WINDCOUNTER);
        add(MagicCounterType.Tower, MagicIcon.TOWERCOUNTER);
        add(MagicCounterType.Infection, MagicIcon.INFECTIONCOUNTER);
        add(MagicCounterType.Fuse, MagicIcon.FUSECOUNTER);
        add(MagicCounterType.Page, MagicIcon.PAGECOUNTER);
        add(MagicCounterType.Sleep, MagicIcon.SLEEPCOUNTER);
    }

    private static void add(final MagicCounterType key, final MagicIcon icon) {
        add(key.getText(), icon);
    }

    private static void add(final MagicCostManaType key, final MagicIcon icon) {
        add(key.getText(), icon);
    }

    private static void add(final MagicPermanentState key, final MagicIcon icon) {
        add(key.getText(), icon);
    }

    private static void add(final String text, final MagicIcon icon) {
        assert TEXT_ICONS.containsKey(text) == false : "Duplicate key in TextImages: " + text;
        TEXT_ICONS.put(text, icon);
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
