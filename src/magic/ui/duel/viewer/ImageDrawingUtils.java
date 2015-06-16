package magic.ui.duel.viewer;

import magic.ui.IconImages;
import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.event.MagicManaActivation;
import magic.ui.widget.FontsAndBorders;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import magic.data.MagicIcon;

public class ImageDrawingUtils {
    public static void drawCostInfo(
        final Graphics g,
        final ImageObserver observer,
        final MagicManaCost cost,
        final int x1,
        final int x2,
        final int y
    ) {
        final List<MagicIcon> icons = cost.getIcons();
        int x=x2-icons.size()*16;
        for (final MagicIcon icon : icons) {
            if (x>=x1) {
                g.drawImage(IconImages.getIcon(icon).getImage(),x,y,observer);
            }
            x+=16;
        }
    }

    public static int drawManaInfo(
        final Graphics g,
        final ImageObserver observer,
        final Collection<MagicManaActivation> acts,
        int ax,
        final int ay
    ) {
        final Set<MagicManaType> types = new HashSet<MagicManaType>();
        boolean snowMana = false;
        for (final MagicManaActivation manaAct : acts) {
            for (final MagicManaType manaType : manaAct.getManaTypes()) {
                if (manaType == MagicManaType.Snow) {
                    snowMana = true;
                }
                if (manaType != MagicManaType.Colorless || manaType != MagicManaType.Snow) {
                    types.add(manaType);
                }
            }
        }
        final List<ImageIcon> icons = new ArrayList<ImageIcon>();
        if (types.size()==MagicColor.NR_COLORS) {
            icons.add(IconImages.getIcon(MagicIcon.MANA_ANY));
        } else if (types.isEmpty() && !acts.isEmpty()) {
            icons.add(IconImages.getIcon(MagicIcon.MANA_1));
            if (snowMana) {
                icons.add(IconImages.getIcon(MagicIcon.MANA_SNOW));
            }
        } else {
            for (final MagicColor color : MagicColor.values()) {
                if (types.contains(color.getManaType())) {
                    icons.add(IconImages.getIcon(color.getManaType()));
                }
            }
            if (snowMana) {
                icons.add(IconImages.getIcon(MagicIcon.MANA_SNOW));
            }
        }
        for (final ImageIcon icon : icons) {
            g.drawImage(icon.getImage(),ax,ay,observer);
            ax+=16;
        }
        return ax;
    }

    public static int drawAbilityInfo(
        final Graphics g,
        final ImageObserver observer,
        final Set<MagicAbility> abilityFlags,
        int ax,
        final int ay
    ) {
        if (abilityFlags.contains(MagicAbility.Flying)) {
            g.drawImage(IconImages.getIcon(MagicIcon.FLYING).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.FirstStrike)) {
            g.drawImage(IconImages.getIcon(MagicIcon.STRIKE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Trample)) {
            g.drawImage(IconImages.getIcon(MagicIcon.TRAMPLE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Infect)) {
            g.drawImage(IconImages.getIcon(MagicIcon.INFECT).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Wither)) {
            g.drawImage(IconImages.getIcon(MagicIcon.WITHER).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromBlack)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTBLACK).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromBlue)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTBLUE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromGreen)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTGREEN).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromRed)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTRED).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromWhite)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTWHITE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromAllColors)) {
            g.drawImage(IconImages.getIcon(MagicIcon.PROTALLCOLORS).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Defender)) {
            g.drawImage(IconImages.getIcon(MagicIcon.DEFENDER).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Vigilance)) {
            g.drawImage(IconImages.getIcon(MagicIcon.VIGILANCE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.DoubleStrike)) {
            g.drawImage(IconImages.getIcon(MagicIcon.DOUBLESTRIKE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Deathtouch)) {
            g.drawImage(IconImages.getIcon(MagicIcon.DEATHTOUCH).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Lifelink)) {
            g.drawImage(IconImages.getIcon(MagicIcon.LIFELINK).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Reach)) {
            g.drawImage(IconImages.getIcon(MagicIcon.REACH).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Shroud)) {
            g.drawImage(IconImages.getIcon(MagicIcon.SHROUD).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Hexproof)) {
            g.drawImage(IconImages.getIcon(MagicIcon.HEXPROOF).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Fear)) {
            g.drawImage(IconImages.getIcon(MagicIcon.FEAR).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Intimidate)) {
            g.drawImage(IconImages.getIcon(MagicIcon.INTIMIDATE).getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Indestructible)) {
            g.drawImage(IconImages.getIcon(MagicIcon.INDESTRUCTIBLE).getImage(),ax,ay,observer);
            ax+=16;
        }
        return ax;
    }

    public static int drawCountersInfo(
        final Graphics g,
        final ImageObserver observer,
        final MagicPermanent permanent,
        int ax,
        final int ay
    ) {
        for (final MagicCounterType counterType : MagicCounterType.values()) {
            int amount = permanent.getCounters(counterType);
            if (amount > 0) {
                final String str = Integer.toString(amount);
                final int inc = 16 + 8 * (str.length() - 1);
                if (counterType == MagicCounterType.Charge) {
                    g.drawImage(IconImages.getIcon(MagicIcon.CHARGE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOne) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUS).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOne) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUS).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusTwo) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusTwo) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusZeroPlusOne) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSZEROPLUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusZeroMinusOne) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUSZEROMINUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusZeroPlusTwo) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSZEROPLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusZeroMinusTwo) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUSZEROMINUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOnePlusZero) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSONEPLUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOneMinusZero) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUSONEMINUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOnePlusTwo) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSONEPLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusTwoPlusZero) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLUSTWOPLUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusTwoMinusOne) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MINUSTWOMINUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Feather) {
                    g.drawImage(IconImages.getIcon(MagicIcon.FEATHER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gold) {
                    g.drawImage(IconImages.getIcon(MagicIcon.GOLDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Bribery) {
                    g.drawImage(IconImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Fade) {
                    g.drawImage(IconImages.getIcon(MagicIcon.FADECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Time) {
                    g.drawImage(IconImages.getIcon(MagicIcon.TIMECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Quest) {
                    g.drawImage(IconImages.getIcon(MagicIcon.QUESTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Level) {
                    g.drawImage(IconImages.getIcon(MagicIcon.LEVELCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hoofprint) {
                    g.drawImage(IconImages.getIcon(MagicIcon.HOOFPRINTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Age) {
                    g.drawImage(IconImages.getIcon(MagicIcon.AGECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ice) {
                    g.drawImage(IconImages.getIcon(MagicIcon.ICECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Spore) {
                    g.drawImage(IconImages.getIcon(MagicIcon.SPORECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Arrowhead) {
                    g.drawImage(IconImages.getIcon(MagicIcon.ARROWHEADCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Loyalty) {
                    g.drawImage(IconImages.getIcon(MagicIcon.LOYALTYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ki) {
                    g.drawImage(IconImages.getIcon(MagicIcon.KICOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Depletion) {
                    g.drawImage(IconImages.getIcon(MagicIcon.DEPLETIONCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Mining) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MININGCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Muster) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MUSTERCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Treasure) {
                    g.drawImage(IconImages.getIcon(MagicIcon.TREASURECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Strife) {
                    g.drawImage(IconImages.getIcon(MagicIcon.STRIFECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Study) {
                    g.drawImage(IconImages.getIcon(MagicIcon.STUDYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Trap) {
                    g.drawImage(IconImages.getIcon(MagicIcon.TRAPCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shield) {
                    g.drawImage(IconImages.getIcon(MagicIcon.SHIELDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wish) {
                    g.drawImage(IconImages.getIcon(MagicIcon.WISHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shell) {
                    g.drawImage(IconImages.getIcon(MagicIcon.SHELLCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hatchling) {
                    g.drawImage(IconImages.getIcon(MagicIcon.SHELLCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Blaze) {
                    g.drawImage(IconImages.getIcon(MagicIcon.BLAZECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Tide) {
                    g.drawImage(IconImages.getIcon(MagicIcon.TIDECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gem) {
                    g.drawImage(IconImages.getIcon(MagicIcon.GEMCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Credit) {
                    g.drawImage(IconImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Pressure) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PRESSURECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Verse) {
                    g.drawImage(IconImages.getIcon(MagicIcon.VERSECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Music) {
                    g.drawImage(IconImages.getIcon(MagicIcon.MUSICCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Rust) {
                    g.drawImage(IconImages.getIcon(MagicIcon.RUSTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Lore) {
                    g.drawImage(IconImages.getIcon(MagicIcon.STUDYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Blood) {
                    g.drawImage(IconImages.getIcon(MagicIcon.BLOODCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Growth) {
                    g.drawImage(IconImages.getIcon(MagicIcon.GROWTHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wage) {
                    g.drawImage(IconImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Plague) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PLAGUECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Pin) {
                    g.drawImage(IconImages.getIcon(MagicIcon.PINCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Healing) {
                    g.drawImage(IconImages.getIcon(MagicIcon.HEALINGCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Scream) {
                    g.drawImage(IconImages.getIcon(MagicIcon.SCREAMCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Devotion) {
                    g.drawImage(IconImages.getIcon(MagicIcon.DEVOTIONCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Divinity) {
                    g.drawImage(IconImages.getIcon(MagicIcon.DIVINITYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Death) {
                    g.drawImage(IconImages.getIcon(MagicIcon.DEATHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wind) {
                    g.drawImage(IconImages.getIcon(MagicIcon.WINDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                }
            }
        }
        return ax;
    }

    private static void drawStringWithOutline(final Graphics g, final String str, int x, int y, final ImageObserver observer) {
        g.setColor(FontsAndBorders.GRAY2);
        g.drawString(str,x+1,y);
        g.drawString(str,x-1,y);
        g.drawString(str,x,y+1);
        g.drawString(str,x,y-1);
        g.setColor(Color.DARK_GRAY);
        g.drawString(str,x,y);
    }

    public static void drawCreatureInfo(
        final Graphics g,
        final FontMetrics metrics,
        final String pt,
        final int ptWidth,
        final String damage,
        final int x,
        final int y,
        final boolean flip
    ) {
        final boolean isDamage = damage.length() > 0;
        g.setColor(FontsAndBorders.GRAY2);
        g.fillRect(x,y,ptWidth+4,isDamage?32:18);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x,y,ptWidth+4,isDamage?32:18);
        g.drawString(pt,x+3,isDamage&&flip?y+28:y+14);
        if (isDamage) {
            final int damageWidth=metrics.stringWidth(damage);
            final int dx=x+3+(ptWidth-damageWidth)/2;
            g.setColor(Color.RED);
            g.drawString(damage,dx,flip?y+14:y+28);
        }
    }
}
